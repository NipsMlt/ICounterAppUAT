package com.xac.demo.Presenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import com.xac.demo.AppData;
import com.xac.helper.Crypto;
import com.xac.helper.EMV;
import com.xac.helper.PinpadManager;
import com.xac.helper.TLV;
import com.xac.helper.Utility;
import com.xac.helper.VNG;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rtaservices.RTAFineandInquiryServices.Interfaces.CardValuesListener;
import utility.Constant;

import static com.xac.helper.Logger.LINE_OUT;
import static com.xac.helper.Logger.log;

public class EmvTransactionPresenter extends BasePresenterViewWrapper {

    private PinpadManager pinpadManager = null;
    private CardValuesListener cardValuesListenerCallBack;
    private String Track1Data,Track2Data;

    @Override
    protected void presenterImp() {

        setNaviTitle("EMV Transaction");
        putAction("Load Contact EMV Data", () -> loadContactEmvData());
        putAction("Load Test Key", () -> loadTestKey());
        putAction("Run Transaction", () -> runTransaction());
        putAction("Dump Version and Log", () -> dumpLog());

        initCommPort();
        checkSRED();

        if (!sredFlag)
            putAction("Enable SRED", () -> enableSred());

        //pinpadManager = new PinpadManager(ctx, AppData.getInstance().product);
    }


    @Override
    public void beforeInvoke(String methodName) {
        commPort.connect();
    }

    @Override
    public void afterInvoke(String methodName) {
        commPort.disconnect();
    }

    private boolean VngCmdKm3LoadDKey(String targetKeySlot, String targetKeyData, String kekSlot, String kekData, String params) {
            /*
                <STX>KM3{KEK#}{Method}{TK#}{Usage}{Alg}{Mode}{Multiplier}{Key_Data}{KCV Type}{KCV}{Optional_Data}<ETX>{LRC}

                KEK# = (1 byte) slot # of KEK which creates target key
                TK# = (1 byte) target key slot #
                Usage = key usage (2 bytes)
                Alg = Algorithm (1 byte)
                Mode = mode of use (1 byte)
                Multiplier = (1 byte) target key length multiplier (n)
                Key_Data = (n*16 bytes) target key data
                KCV Type
                ‘0’ = TDES[00 00 00…00] by target TDES key
                ‘1’ = AES[00 00 00…00] by target AES key

                KCV = 6 or 4 bytes of key check value
                Optional_Data = optional data like IKSN for DUKPT
                {RS}{LEN}{Type}{Data}

                Type = 2 binary bytes of Optional block ID
                Data = n binary bytes optional data
            */
        VNG req = new VNG();

        // KM3{KEK#}{Method}{TK#}{Usage}{Alg}{Mode}
        req.addData("KM3");
        req.addData(kekSlot);   // KEK - 0
        req.addData("0");   // Method - 0 : TDES CBC (IV=0) decryption method
        req.addData(targetKeySlot);   // Target Key - X
        req.addData(params);

        // {Multiplier}{Key_Data}{KCV Type}{KCV}
        int multiplier = (targetKeyData.length() / 16);

        req.addData(Integer.toString(multiplier));

        String kcvType = "0";
        //String kcv = "000000";
        String kcv = "33C4CB";

        String eTargetKeyData = Crypto.encrypt(Crypto.ALGORITHM.TDES, targetKeyData, kekData);
        kcv = Crypto.getKcv(Crypto.ALGORITHM.TDES, targetKeyData);

        req.addData(eTargetKeyData);
        req.addData(kcvType);
        req.addData(kcv);

        String type = "KS";  // Key Set Identifier
        // IKSN
        byte[] optionalData = Utility.hex2Byte("FFFF9876543210E00000");

        //{RS}{LEN}{Type}{Data}
        req.addRSLength(optionalData.length + 2);
        req.addData(type);
        req.addData(optionalData);

        VNG rsp = exchangeData(req);

        if (rsp != null) {
            // KM3{Status}
            if (!rsp.parseString(3).equals("KM3")) {
                addLog("Unexpected Response");
                return false;
            } else {
                String status = rsp.parseString(1);
                if (status.equals("0")) {
                    addLog("Load Key " + targetKeySlot + " success");
                } else {
                    addLog("KM3 status - " + status);
                    return false;
                }
            }
        } else
            return false;

        return true;
    }

    String bklk = "30313233343536373839414243444546";
    String pinKey = "6AC292FAA1315B4D858AB3A3D7D5933A";
    String sredKey = "50515253545556575859616263646566";

    public void loadTestKey() {

        // BKLK : 0
        // -- Value : 30313233343536373839414243444546
        // -- KCV   : 33C4CB
        // DK4PIN : X
        // -- Value : 6AC292FAA1315B4D858AB3A3D7D5933A
        // -- KCV   : AF8C07

        // Check Root Key KCV = 33C4CB
        VNG rsp = exchangeData(new VNG("KM10"));
        if (rsp == null || !(rsp.parseString(10).equals("KM1033C4CB"))) {
            addLog("Unrecognized Root Key");
            return;
        }


        // Update Root Itself to Clear all sub keys
        rsp = exchangeData(new VNG("KM300000TB2494D16FB2860C464850B3D7643861A27033C4CB"));
        if (rsp == null || !(rsp.parseString(4).equals("KM30"))) {
            addLog("Fail to Clear Key");
            return;
        }

        // Load DK4PIN
        // -- Usage - 10 : PIN encryption key for ISO PIN Block 0
        // -- Algorithm - T : Triple DEA (TDEA)
        // -- Mode - E : Encrypt only
        VngCmdKm3LoadDKey("X", pinKey, "0", bklk, "10TE");

        // Load DK2P2PE
        // -- Usage - 34 : Data Key for account data encryption
        if (sredFlag) {
            VngCmdKm3LoadDKey("Y", sredKey, "0", bklk, "34TE");
        }
    }

    public void loadContactEmvData() {

        // Set CTLS Reader Mode to EMV mode , turnoff QN
        exchangeData(new VNG(Utility.hex2Byte("51 52 1E 00 04 6C 00 01 00 ")));

        // setup configuration
        // Ap Config
        EMV.APP_CONFIG appConfig = new EMV.APP_CONFIG();

        // Virtual PinpadManager has to handle whole flow by application
        if (pinpadManager.virtualPinpad)
            appConfig.pinEntryProcess = 0x01;

        if (sredFlag) {
            appConfig.sredFlag = 0;     // note: for EMV Contact definition, 0 : Enable, 1 : disable
            appConfig.sredMaskSetting = "*640000".getBytes();
            appConfig.sredFormatSetting = "Y200".getBytes();
        }

        EMV setAppConfig = new EMV((byte) EMV.CMD_ID._B2_EmvSetApConfiguration, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0, appConfig.toByteArray());
        exchangeEmvCmd(setAppConfig);

        // load profiles
        try {
            String[] profiles = ctx.getResources().getAssets().list("profiles");
            for (int i = 0; i < profiles.length; i++) {

                log("profile : " + profiles[i], LINE_OUT());

                byte[] data = new byte[2048];
                int count = 0;
                InputStream f = ctx.getResources().getAssets().open("profiles/" + profiles[i]);
                count = f.read(data, 0, 2048);

                if (count != 0) {
                    if (profiles[i].contains("PROFILE_ICS"))
                        exchangeEmvCmd(new EMV((byte) EMV.CMD_ID._42_EmvSetProfile, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0, data, count));
                    else if (profiles[i].contains("PROFILE_TERM"))
                        exchangeEmvCmd(new EMV((byte) EMV.CMD_ID._42_EmvSetProfile, (byte) EMV.CMD_TYPE.REQUEST, (byte) 1, (byte) 0, data, count));
                    else if (profiles[i].contains("PROFILE_APPL"))
                        exchangeEmvCmd(new EMV((byte) EMV.CMD_ID._42_EmvSetProfile, (byte) EMV.CMD_TYPE.REQUEST, (byte) 2, (byte) 0, data, count));
                    else if (profiles[i].contains("PROFILE_KEY"))
                        exchangeEmvCmd(new EMV((byte) EMV.CMD_ID._42_EmvSetProfile, (byte) EMV.CMD_TYPE.REQUEST, (byte) 3, (byte) 0, data, count));
                }
            }
            addLog("Load all profiles done");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Set Call back method to get the values in CardPaymentSwipe activity
    public void setcardValuesCallBackMethod(CardValuesListener CallBack) {
        this.cardValuesListenerCallBack = CallBack;
    }

    public void runTransaction() {

        VNG req = new VNG();
        VNG rsp = new VNG();

        // Auto Report Command
        // AR<RS>{Len}{AR Data}
        // AR Data = {MSR Status}{ICC Status}{CTLS Status}{CTLS Info}{Timeout}
            /*
                { MSR Status}:1 byte, 0x00 = Disabled, 0x01 = Enabled
                { ICC Status} : 1 byte, 0x00 = Disabled, 0x01 = Enabled
                { CTLS Status} : 1 byte, 0x00 = Disabled, 0x01 = Enabled
                { CTLS Info} : Present when { CTLS Status}= 0x01.
                Please follow command 55 of CTLS command Set to set {RFID mode} and corresponding tags.
                { Timeout} : 1 byte.Present when { CTLS Status} = 0x00.
                Range 0x00(no timeout) ~0xFF(255 seconds)
            */

        req.clear();
        req.addData("AR");
        // enable all readers
        // CLTS Data = 9F 02 06 00 00 00 00 00 20 DF 15 01 01 DF 20 01 14
        // Timeout == DF20 - 0x14 : 20 second
        // Timeout == DF20 - 0x3C : 60 second
        byte[] arData = Utility.hex2Byte("01 01 01 00 9F 02 06 00 00 00 00 00 20 DF 15 01 01 DF 20 01 3C");
        req.addRSLenData(arData);

        rsp = exchangeData(req);
        if (rsp == null || !rsp.parseString(2).equals("AR")) {
            addLog("Unrecognized Respond of AR");
            return;
        }

        if (rsp.parseRSLenData()[0] != 0x00) {
            addLog("Fail to Enable Auto Report");
            return;
        }

        addLog("Present Card...");

        while (true) {
            // Wait for Present Card - 20 sec
            byte[] rspData = waitData(60000,cardValuesListenerCallBack);
            cardValuesListenerCallBack = cvl;

            if (rspData == null) {
                addLog("Auto Report No Response");
                return;
            }

            rsp.clear();
            ;
            rsp.addData(rspData);

            // Magnestripe
            if (rsp.tryToParse("81.")) {
                String track1 = rsp.parseStringToSymbol(VNG.FS);
                String track2 = rsp.parseStringToSymbol(VNG.FS);
                String track3 = rsp.parseStringToSymbol(VNG.ETX);

                addLog("Track 1 : " + track1);
                addLog("Track 2 : " + track2);
                addLog("Track 3 : " + track3);

                Log.i(Constant.TAG,track1);
                Log.i(Constant.TAG,track2);
                Log.i(Constant.TAG,track3);

                //Card Values Listener is not null get the values
                if(cardValuesListenerCallBack!=null)
                {
                    cardValuesListenerCallBack.CardValuesCallBackMethod(track1,track2);
                }

                return;
            }
            // CTLS
            else if (rsp.tryToParse("QR")) {
                return;
            }
            // Card Inserted into Chip Reader
            else if (rsp.tryToParse("QSD")) {
                String status = rsp.parseString(1);
                if (status.equals("0"))  // power on ok
                {
                    byte[] ATR = rsp.parseRSLenData();
                } else {
                    addLog("Power On Fail : " + status);
                    return;
                }
            } else if (rsp.tryToParse("AR")) {
                byte status = rsp.parseRSLenData()[0];
                if (status == 2)
                    addLog("Auto Report : Timeout");
                else if (status == 3)
                    addLog("Auto Report : Cancel");
                else
                    addLog("Auto Report : " + status);

                return;
            } else if (rsp.tryToParse("QN")) {
                // ignore QN
                continue;
            } else {
                addLog("Unrecognized Response of AR v2");
                return;
            }
            break;
        }

        // Always Sync KSN before start transaction
        exchangeData(new VNG("KM5Y00000000000000000000"));

        if (!payStart(false))
            return;

        addLog("PAN : " + AppData.getInstance().pan);

        // Change CVM
        // 0x32 : EmvSetIcsValue
        // - P1: ICS Ca talog
        // -- 0x01 : Set ICS CVM value.
        // -- 0x02 : Set ICS CDA mode.
        //
        // public byte byCVMCapability;
        // bit8: offline palin pin
        // bit7: online encrypher pin
        // bit6: signature
        // bit5: offline encipher pin
        // bit4: no cvm required
//            byte[] cvmValue = new byte[] { (byte)0xF0 };  // all but no cvm
//            byte[] cvmValue = new byte[] { (byte)0x40 };  // online pin only
//            exchangeEmvCmd(new EMV((byte)EMV.CMD_ID._32_EmvSetIcsValue, (byte)EMV.CMD_TYPE.REQUEST, (byte)0x01, (byte)0, cvmValue));

        if (!payAuth())
            return;

        if (!payFirstGenAC(false))
            return;

        TLV arpc = new TLV();
        arpc.addData(0x8A, Utility.hex2Byte("3030"));
        arpc.addData(0x89, Utility.hex2Byte("313233343536"));
        arpc.addData(0x9F01, Utility.hex2Byte("414243444546"));
        arpc.addData(0x91, Utility.hex2Byte("0102030405060708"));

        if (!paySecGenAC(true, false, arpc))
            return;

        payStop(true);

        addLog("Please Remove Card...");

        VNG qm1 = new VNG("QM1");
        while (true) {
            VNG qm1Rsp = exchangeData(qm1);
                /*
                    QM1{ Card Position Status}
                    ‘0’: No Card
                    ‘2’: ICC Card inserted
                */
            if (qm1Rsp == null || qm1Rsp.parseString(4).equals("QM10"))
                break;

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        addLog("Transaction Done");

    }

    public void enableSred() {

        checkToRunOnUiThread(() -> {
            new AlertDialog.Builder(ctx)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Enable SRED")
                    .setMessage("Are you sure you want to Enable SRED? Once enabled, it could not be disabled directly.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commPort.connect();
                            //VNG rsp = exchangeData(new VNG("QZ01FFFFFFFFFFFFFFFF"));
                            //if (rsp != null && rsp.parseString().equals("QZ00")) {
                            VNG rsp = exchangeData(new VNG(""));
                            if (rsp != null && rsp.parseString().equals("")) {
                                addLog("Enable SRED Successfully. Please Reload Config/Test Key Again");
                                sredFlag = true;
                            }
                            commPort.disconnect();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        });
    }

    boolean sredFlag = false;

    private void checkSRED() {
        commPort.connect();
        VNG rsp = exchangeData(new VNG("X13"));
        if (rsp != null && rsp.parseString(3).equals("X13")) {
            byte[] config = Utility.hex2Byte(rsp.parseString());
            if (config.length >= 64) {
                sredFlag = ((config[48] & 0x20) != 0);  // byte 48 bit 6
                addLog("SRED is " + (sredFlag ? "Enabled" : "Disabled"));
            }
        }
        commPort.disconnect();
    }

    @Override
    public void saveLog() {
        super.saveLog();
        if (debugLog.length() > 0) {
            String logPath = Utility.saveLogToFile(ctx, "emvDebugLog", debugLog.toString());
            if (logPath != null)
                addLog("Save Cmd Log to -\r\n" + logPath, true);
            else
                addLog("Save Cmd Log Failed");
        }

    }

    //region Contact EMV Flow Handling

    private boolean waitFlag = false;
    private int appSelIndex = -1;
    public StringBuilder debugLog = new StringBuilder();

    public EMV exchangeEmvCmd(EMV req) {

        if (!sendData(req.getCmdBuffer()))
            return null;

        while (true) {

            byte[] rspData = waitData();

            if (rspData == null)
                return null;

            EMV rsp = new EMV(rspData);
            if (rsp.tryToParse()) {
                if (rsp.cmdId == req.cmdId && rsp.cmdType == EMV.CMD_TYPE.RESPONSE) {
                    return rsp;
                } else if (rsp.cmdType == EMV.CMD_TYPE.REQUEST || rsp.cmdType == EMV.CMD_TYPE.NOTIFICATION) {
                    if (!handlingEmvCallback(rsp)) {
                        return null;
                    }
                } else {
                    addLog("Unrecognized EMV Response ID : " + rsp.cmdId);
                    return null;
                }
            } else {
                // non-EMV YM format response
                if (!handlingVngCallback(new VNG(rspData)))
                    return null;
            }
        }
    }

    private boolean handlingVngCallback(VNG callback) {

        if (callback.tryToParse("EP0")) {

            String len = callback.parseString(2);
            if (len.equals("E0")) {
                addLog("PIN length < minimal length");
            } else if (len.equals("E1")) {
                addLog("PIN length > maximum length");
            } else {
                pinpadManager.updatePinLength(Integer.parseInt(len));
            }
            return true;
        }

        return false;
    }

    private void selectionDialog(final CharSequence[] candidates) {

        waitFlag = true;

        checkToRunOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setCancelable(false);
            builder.setTitle("Select Application");
            builder.setItems(candidates, (dialog, which) -> {
                appSelIndex = which;
                waitFlag = false;
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.cancel();
                waitFlag = false;
            });

            final AlertDialog dialog = builder.create();
            dialog.show();

            final Handler handler = new Handler();
            final Runnable runnable = () -> {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                waitFlag = false;
            };

            dialog.setOnDismissListener(dialog1 -> handler.removeCallbacks(runnable));

            handler.postDelayed(runnable, 10000);
        });

        while (waitFlag) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private EMV startPinProcess(byte pinType, TLV pinInfo) {

        /*
                P1+P2:Result
                0x0000 : Sending Verify APDU Command OK
                0x0001 : Pinpad function returns error.
                In this case, kernel will continue to try next available CVM method.
                0x0002 : Cancel PIN(User cancel PIN entry or Force stop transaction)
                0x0003 : Bypass PIN
        */
        EMV emvCallbackRsp = new EMV((byte) EMV.CMD_ID._18_CB_PINENTRY, (byte) EMV.CMD_TYPE.RESPONSE, (byte) 0, (byte) 0);

        VNG req, rsp;

        if (pinType == EMV.CVM_PIN_TYPE.OnlinePin) {

            // EP1{PEK#}{PBF}{Min}{Max}{Timeout}{Account}
            req = new VNG();
            req.addData("EP1");
            req.addData("X00412060");           // key slot - 'X', len 4~12, timeout 060 sec

            if (AppData.getInstance().pan.contains("F"))
                req.addData("FFFFFFFFFFFFFFFF");    // Account - SRED enabled, use acount stored in PINPAD
            else
                req.addData(AppData.getInstance().pan);    // Account - SRED enabled, use acount stored in PINPAD

        } else if (pinType == EMV.CVM_PIN_TYPE.OfflinePlaintextPin || pinType == EMV.CVM_PIN_TYPE.OfflineEncryptedPin) {
            // EP4{Min}{Max}{Timeout}<RS>{LEN}{Data}
            req = new VNG();
            req.addData("EP4");
            req.addData("0412060");

            if (pinType == EMV.CVM_PIN_TYPE.OfflineEncryptedPin) {

                byte[] tagC2 = pinInfo.retrieveTag(0xC2, true);
                // <RS>{LEN}{Data} = {PK Len}{PK Modulus}{Challenge}{PK Exponent}
                req.addRSLenData(tagC2);
            }
        } else {
            emvCallbackRsp.parameter[1] = 1;
            return emvCallbackRsp;
        }
        rsp = exchangeData(req, 60 * 1000);

        while (true) {

            if (rsp.tryToParse("EP0")) {   // PIN input
                String len = rsp.parseString(2);
                if (len.equals("E0")) {
                    addLog("PIN length < minimal length");
                } else if (len.equals("E1")) {
                    addLog("PIN length > maximum length");
                } else {
                    pinpadManager.updatePinLength(Integer.parseInt(len));
                }
            } else if (rsp.tryToParse("EP1")) {    // online PIN response

                //<STX>EP1{Status}{PIN Len}{EPB}{KSN}<ETX>{LRC}
                //Status =0 ->success
                //Status =1-> invalid parameter
                //Status =2-> cancel by user
                //Status =3-> timeout
                String status = rsp.parseString(1);
                if (status.equals("0")) {

                    String pinLen = rsp.parseString(2);
                    String str_epb_ksn = rsp.parseString();
                    addLog("PIN LEN : " + pinLen + "\r\n" + "EPB/KSN : " + str_epb_ksn);

                    // {EPB|KSN} 16/26 bytes ASCII string
                    byte[] epb_ksn = Utility.hex2Byte(str_epb_ksn.substring(0, 16));
                    emvCallbackRsp.dataField = new TLV();
                    emvCallbackRsp.dataField.addData(0xDF43, epb_ksn);

                } else if (status.equals("2")) {
                    addLog("Online PIN : Cancel by User");
                    emvCallbackRsp.parameter[0] = 0x02;    // user cancel / timeout
                } else if (status.equals("3")) {
                    addLog("Online PIN : timeout");
                    emvCallbackRsp.parameter[0] = 0x02;    // user cancel / timeout
                } else {
                    addLog("Online PIN Err : " + status);
                    emvCallbackRsp.parameter[0] = 0x01;    // pinpad error
                }
                break;

            } else if (rsp.tryToParse("EP4")) {     // offline PIN response

                //<STX>EP4{Status}{RPDU}<ETX>{LRC}
                //Status: 1 ASCII Byte
                //0->ICC RPDU is followed
                //1-> invalid parameter
                //2-> cancel by user
                //3-> timeout
                //4-> bypass PIN
                String status = rsp.parseString(1);
                if (status.equals("0")) {

                    byte[] rpdu = Utility.hex2Byte(new String(rsp.parseBytes(4)));
                    emvCallbackRsp.dataField = new TLV();
                    emvCallbackRsp.dataField.addData(0xDF45, rpdu);

                } else if (status.equals("2")) {
                    addLog("Offline PIN : Cancel by User");
                    emvCallbackRsp.parameter[0] = 0x02;    // user cancel / timeout
                } else if (status.equals("3")) {
                    addLog("Offline PIN : timeout");
                    emvCallbackRsp.parameter[0] = 0x02;    // user cancel / timeout
                } else if (status.equals("4")) {
                    addLog("Offline PIN : bypass PIN");
                    emvCallbackRsp.parameter[0] = 0x03;    // bypass pin
                } else {
                    addLog("Offline PIN Err : " + status);
                    emvCallbackRsp.parameter[0] = 0x01;    // pinpad error
                }
                break;
            } else {
                // exception
                emvCallbackRsp.parameter[1] = 1;
                break;
            }

            rsp = new VNG(waitData(60 * 1000,cvl));
        }

        return emvCallbackRsp;
    }

    private void enablePinEntry() {
        runOnWindowFocusChanged(hasFocus -> {
            if (!hasFocus && pinpadManager.PIN_MODE) {

                if (pinpadManager.virtualPinpad) {
                    String layout = pinpadManager.getVirtualPinpadLayout();
                    VNG rsp = exchangeData(new VNG("EPT" + layout));

                    // fail to setup virtual pinpad layout
                    if (rsp == null || !rsp.tryToParse("EPT0"))
                        pinpadManager.disable();
                }
                synchronized (pinpadManager.waitPopupDialog) {
                    pinpadManager.waitPopupDialog.notify();
                }
            }
        });

        pinpadManager.enable();

        try {
            synchronized (pinpadManager.waitPopupDialog) {
                pinpadManager.waitPopupDialog.wait(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void disablePinEntry() {
        pinpadManager.disable();
        runOnWindowFocusChanged(null);
    }

    private EMV startEmvPinEntryProcess(byte pinType, TLV pinInfo) {

        EMV emvCallbackRsp = null;

        enablePinEntry();

        if (pinpadManager.PIN_MODE) {
            emvCallbackRsp = startPinProcess(pinType, pinInfo);
        } else {
            // pinpad error
            emvCallbackRsp = new EMV((byte) EMV.CMD_ID._18_CB_PINENTRY, (byte) EMV.CMD_TYPE.RESPONSE, (byte) 0, (byte) 1);
        }

        disablePinEntry();

        return emvCallbackRsp;
    }

    private void showPinInputInformation(byte pinType, byte pinTryCounter) {

        if (pinType == 1) {
            addLog("Online PIN");
        } else if (pinType == 2) {
            addLog("Offline Plaintext PIN");
        } else if (pinType == 3) {
            addLog("Offline Encrypted PIN");
        }

        if (pinTryCounter > 0) {
            addLog("PIN Try Counter : " + pinTryCounter);
        }
    }

    public boolean handlingEmvCallback(EMV callback) {

        EMV rsp = null;
        switch (callback.cmdId) {
            case EMV.CMD_ID._12_NF_DEBUGINFO:

                String info = new String(callback.dataField.toByteArray());
                debugLog.append(info);
                log(info, LINE_OUT());

                break;
            case EMV.CMD_ID._13_NF_DISPLAYMSG:

                String msg = EMV.EMV_MAP.MSG_TABLE.get((Integer) (callback.parameter[0] & 0xFF));
                addLog(msg);

                if (callback.parameter[0] == (byte) 0x09 ||            // PIN ENTRY
                        callback.parameter[0] == (byte) 0xA4) {            // LAST PIN TRY

                    // check P2 for more information
                    // -- only presented if Application Configuration Table - PIN Entry Process is 0x00 or 0x02
                    /*
                        P2- LBYTE : PIN Type
                            x1h : Online PIN
                            x2h : Offline Plaintext PIN
                            x3h : Offline Encrypted PIN
                        P2- HBYTE:PIN Try Counter (9F17)
                            2xh ~ Fxh : Remainder PIN try counter.
                                    If value is larger than 15, set to Fxh,
                            0xh : In case of online PIN
                    */
                    byte hb = (byte) ((callback.parameter[1] & 0xF0) >> 4);
                    byte lb = (byte) (callback.parameter[1] & 0x0F);
                    showPinInputInformation(lb, hb);

                    if (!pinpadManager.virtualPinpad)
                        enablePinEntry();

                } else if (callback.parameter[0] == (byte) 0x0A ||     // INCORRECT PIN
                        callback.parameter[0] == (byte) 0x0D ||        // PIN OK
                        callback.parameter[0] == (byte) 0xA5 ||        // ONLINE PIN OK
                        callback.parameter[0] == (byte) 0xA6) {       // CANCEL PIN

                    if (!pinpadManager.virtualPinpad)
                        disablePinEntry();
                }

                break;
            case EMV.CMD_ID._14_CB_SELECTAPP:

                List<EMV.CANDIDATE> candidateList = new ArrayList<EMV.CANDIDATE>();
                int offset = 0;

                while (offset < callback.dataField.index) {

                    TLV.ENTRY candidateRaw = callback.dataField.parseTLV(offset);
                    offset += candidateRaw.itemLen;

                    EMV.CANDIDATE candidate = new EMV.CANDIDATE(candidateRaw.value);
                    candidateList.add(candidate);
                    log(candidate.toString(), LINE_OUT());
                }

                List<String> candidates = new ArrayList<String>();

                for (EMV.CANDIDATE candidate : candidateList) {
                    candidates.add(candidate.szAppLabel);
                }

                appSelIndex = -1;

                selectionDialog(candidates.toArray(new CharSequence[candidates.size()]));

                if (appSelIndex == -1)
                    rsp = new EMV(callback.cmdId, (byte) EMV.CMD_TYPE.RESPONSE, (byte) 0xFF, (byte) 0xFF);
                else
                    rsp = new EMV(callback.cmdId, (byte) EMV.CMD_TYPE.RESPONSE, (byte) 0, candidateList.get(appSelIndex).byIndex);

                break;
            case EMV.CMD_ID._15_CB_TRANAMOUNT:
                rsp = new EMV(callback.cmdId, (byte) EMV.CMD_TYPE.RESPONSE, (byte) 0, (byte) 1);
                break;
            case EMV.CMD_ID._16_CB_EXCEPTION:
                rsp = new EMV(callback.cmdId, (byte) EMV.CMD_TYPE.RESPONSE, (byte) 0, (byte) 1);
                break;
            case EMV.CMD_ID._17_CB_VOICEREFERAL:
                rsp = new EMV(callback.cmdId, (byte) EMV.CMD_TYPE.RESPONSE, (byte) 0, (byte) 1);
                break;
            case EMV.CMD_ID._18_CB_PINENTRY:
                showPinInputInformation(callback.parameter[0], callback.parameter[1]);
                rsp = startEmvPinEntryProcess(callback.parameter[0], callback.dataField);
                break;
            default:
                addLog("Unrecognized EMV Callback ID : " + callback.cmdId);
                return false;
        }

        if (rsp != null) {
            if (!sendData(rsp.getCmdBuffer()))
                return false;
        }

        return true;
    }

    //endregion

    // region Contact EMV Transaction Commands

    public void dumpTlv(TLV tlv) {

        if (tlv.index == 0)
            return;

        int parseIndex = 0;
        while (true) {
            TLV.ENTRY item = tlv.parseTLV(parseIndex);
            if (item == null)
                return;
            addLog("<" + Integer.toHexString(item.tag) + "> " + Utility.bytes2Hex(item.value));
            parseIndex += item.itemLen;
        }
    }

    public boolean checkEmvRsp(EMV rsp) {
        if (rsp == null)
            return false;

        if (!rsp.checkError()) {
            addLog(rsp.getError());
            return false;
        }

        if (rsp.dataField != null) {
            dumpTlv(rsp.dataField);
        }
        return true;
    }

    // YM<RS>{Len}{0x51}C{P1:Reader Flag}{P2:0x00}{Len}{BER-TLV}
    public boolean payStart(boolean debugMode) {

        //        P1:Reader Flag
        //        bit1 : Force Online
        //        bit2 : Force Accept
        //        bit8 : Enable Debug Mode
        byte P1 = 0;
        byte P2 = 0;

        if (debugMode) {
            P1 |= 0x80;
            debugLog.setLength(0);
        }

        EMV req = new EMV((byte) EMV.CMD_ID._51_PayStart, (byte) EMV.CMD_TYPE.REQUEST, P1, P2);

        TLV dataField = new TLV();
        dataField.addData(0x9F02, "000000003000");    // amount
        dataField.addData(0x9C, "02");      // transaction type
        // EMV_TRANSTYPE_CASH : 0x0001
        // EMV_TRANSTYPE_GOODS : 0x0002
        // EMV_TRANSTYPE_SERVICES : 0x0004
        // EMV_TRANSTYPE_CASHBACK : 0x0008
        dataField.addData(0xDF46, "0002");                  // transaction type by XAC
        dataField.addData(0xDF41, "575AE0");            // required tags
        req.dataField = dataField;

        EMV rsp = exchangeEmvCmd(req);
        boolean ret = checkEmvRsp(rsp);
        if (ret) {
            byte[] panNumeric = rsp.dataField.retrieveTag(0x5A, true);
            AppData.getInstance().pan = Utility.numeric2string(panNumeric);
        }
        return ret;
    }

    // YM<RS>{Len}{0x52}C{P1:0x00}{P2:0x00}{Len}{BER-TLV}
    public boolean payAuth() {

        EMV req = new EMV((byte) EMV.CMD_ID._52_PayAuth, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0);

        TLV dataField = new TLV();
        dataField.addData(0xDF41, "95");            // required tags

        return checkEmvRsp(exchangeEmvCmd(req));
    }

    // YM<RS>{Len}{0x53}C{P1:ForceAAC}{P2:0x00}{Len}{BER-TLV}
    public boolean payFirstGenAC(boolean forceAAC) {

        byte P1 = (byte) (forceAAC ? 1 : 0);
        EMV req = new EMV((byte) EMV.CMD_ID._53_PayFirstGenAC, (byte) EMV.CMD_TYPE.REQUEST, P1, (byte) 0);

        TLV dataField = new TLV();
        dataField.addData(0xDF41, "5F245F2A5F348284959A9B9C9F029F039F099F109F1A9F269F279F339F349F399F359F369F379F08");            // required tags
        req.dataField = dataField;

        EMV rsp = exchangeEmvCmd(req);

        boolean ret = checkEmvRsp(rsp);

        if (ret) {
            if (rsp.parameter[0] == 0x00 && rsp.parameter[1] == 0x00) {         // TC
                addLog("PayFirstGenAC : TC");
            } else if (rsp.parameter[0] == 0x00 && rsp.parameter[1] == 0x01) {  // AAC
                addLog("PayFirstGenAC : AAC");
            } else if (rsp.parameter[0] == 0x00 && rsp.parameter[1] == 0x02) {  // ARQC
                addLog("PayFirstGenAC : ARQC");
            }
        }

        return ret;
    }

    // YM<RS>{Len}{0x54}C{P1:OnlineStatus}{P2:ForceAAC}{Len}{BER-TLV}
    public boolean paySecGenAC(boolean onlineStatus, boolean forceAAC, TLV arpc) {

        byte P1 = (byte) (onlineStatus ? 1 : 0);
        byte P2 = (byte) (forceAAC ? 1 : 0);

        EMV req = new EMV((byte) EMV.CMD_ID._54_PaySecGenAC, (byte) EMV.CMD_TYPE.REQUEST, P1, (byte) 0);

        TLV dataField = new TLV();
        dataField.addData(0xDF41, "95");        // required tags
        if (onlineStatus && arpc != null) {      // online auth data - arpc
            dataField.addData(arpc);
        }
        req.dataField = dataField;

        EMV rsp = exchangeEmvCmd(req);

        boolean ret = checkEmvRsp(rsp);

        if (ret)
            addLog("PaySecGenAC : " + ((rsp.parameter[1] % 2 == 0) ? "TC" : "AAC") + (((rsp.parameter[1] & 0xC8) != 0) ? ", Print Receipt" : ""));

        return ret;
    }

    // YM<RS>{Len}{0x55}C{P1:0x00}{P2:0x00}{Len:0x0000}
    public void payStop(boolean turnoffIcc) {

        EMV req = new EMV((byte) EMV.CMD_ID._55_PayStop, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0);
        exchangeEmvCmd(req);

        if (turnoffIcc)
            sendData("QS3".getBytes());
    }

    // endregion

    //region Contact EMV Parameters

    private boolean getSerialNumber() {

        VNG vngRsp = exchangeData(new VNG("R0"));

        if (vngRsp != null) {
            if (!vngRsp.parseString(2).equals("R0")) {
                addLog("Unexpected Response\r\n" + Utility.bytes2Hex(vngRsp.getCmdBuffer()));
                return false;
            } else {
                addLog(" modelName : " + vngRsp.parseString(16).replaceFirst("^0+(?!$)", ""));
                addLog(" firmware version : " + vngRsp.parseString(8));
                addLog("build number : " + vngRsp.parseString(8));
                AppData.getInstance().serialNumber = vngRsp.parseString(16).replaceFirst("^0+(?!$)", "").replaceAll("[^\\d.]", "");
                addLog("serial number : " + AppData.getInstance().serialNumber);
                addLog("cid : " + vngRsp.parseString(4));
            }
        } else {
            addLog("No Response");
            return false;
        }

        String sn = AppData.getInstance().serialNumber;
        AppData.getInstance().serialNumber = "00000000".substring(sn.length()) + sn;

        return true;
    }

    public boolean setParameter(int tag, byte[] value) {
        TLV tlv = new TLV();
        tlv.addData(tag, value);

        return setParameters(tlv);
    }

    public boolean setParameters(TLV params) {

        EMV req = new EMV((byte) EMV.CMD_ID._21_EmvSetParam, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0);
        req.dataField = params;

        return checkEmvRsp(exchangeEmvCmd(req));
    }

    public TLV getParameters(byte[] tags) {

        EMV req = new EMV((byte) EMV.CMD_ID._20_EmvGetParam, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0);
        req.dataField = new TLV(0xDF41, tags);

        EMV rsp = exchangeEmvCmd(req);
        if (!checkEmvRsp(rsp))
            return null;

        return rsp.dataField;
    }

    public boolean setTerminalProfiles() {

        TLV tlv = new TLV();
        // 9F1A	Terminal Country Code
        tlv.addData(0x9F1A, new byte[]{0x08, 0x40});

        // 9F1E	Interface Device(IFD) Serial Number
        if (AppData.getInstance().serialNumber == null) {
            if (!getSerialNumber())
                return false;
        }
        tlv.addData(0x9F1E, Utility.hex2Byte(AppData.getInstance().serialNumber));

        // YM<RS>{Len}{0x42}C{P1:Profile Type}{P2:Profile Index}{Len}{BER-TLV}
        EMV req = new EMV((byte) EMV.CMD_ID._42_EmvSetProfile, (byte) EMV.CMD_TYPE.REQUEST, (byte) 1, (byte) 0);
        req.dataField = tlv;

        return checkEmvRsp(exchangeEmvCmd(req));
    }

    public boolean setApplicationProfiles() {

        TLV tlv = new TLV();

        // 9F06	Application Identifier (AID) - Terminal
        tlv.addData(0x9F06, "A0000000031010");

        // 9F09	Application Version Number - Terminal
        tlv.addData("9F0902008C");

        // 9F4E	Merchant Name and Location
        tlv.addData(0x9F4E, "Target_USA".getBytes());

        // DF10	Application Selection Indicator
        tlv.addData("DF100101");                    // allow partial matching

        // 9F01 Acquirer Identifier
        tlv.addData("9F0106012345678901");

        // 9F15	Merchant Category Code
        tlv.addData("9F15021234");

        // 9F16	Merchant Identifier
        tlv.addData(0x9F16, "123456789012345".getBytes());

        // 9F1C	Terminal Identification
        if (AppData.getInstance().serialNumber == null) {
            if (!getSerialNumber())
                return false;
        }
        tlv.addData(0x9F1C, Utility.hex2Byte(AppData.getInstance().serialNumber));

        // 5F2A	Transaction Currency Code
        tlv.addData("5F2A020840");

        // 5F36	Transaction Current Exponent
        tlv.addData("5F360102");

        // 9F3C	Transaction Reference Currency Code
        tlv.addData("9F3C020002");

        // DF11	Transaction Reference Currency Conversion
        tlv.addData("DF110400000002");

        // 9F3D	Transaction Reference Currency Exponent
        tlv.addData("9F3D0102");

        // DF12	Default Transaction Certificate Data Object List (TDOL)
        tlv.addData("DF12069F02069F0306");

        // DF13	Default Dynamic Data Authentication Data Object List (DDOL)
        tlv.addData("DF13159F02069F03069F1A0295055F2A029A039C019F3704");

        //// DF14	Terminal Action Code - Default
        tlv.addData("DF1405DC4000A800");

        //// DF15	Terminal Action Code - Denial
        tlv.addData("DF15050010000000");

        //// DF16	Terminal Action Code - Online
        tlv.addData("DF1605DC4004F800");

        // 9F1B	Terminal Floor Limit
        tlv.addData("9F1B04000007D0");

        //// 9F1D	Terminal Risk Management Data
        tlv.addData("9F1D080000000000000000");

        // DF17	Maximum Target Percentage to be Used for Biased Random Selection
        tlv.addData("DF170102");

        // DF18	Target Percentage to be Used for Random Selection
        tlv.addData("DF180100");

        // DF19	Threshold Value for Biased Random Selection
        tlv.addData("DF190400002000");

        // YM<RS>{Len}{0x42}C{P1:Profile Type}{P2:Profile Index}{Len}{BER-TLV}
        EMV req = new EMV((byte) EMV.CMD_ID._42_EmvSetProfile, (byte) EMV.CMD_TYPE.REQUEST, (byte) 2, (byte) 0);
        req.dataField = tlv;

        return checkEmvRsp(exchangeEmvCmd(req));
    }

    public boolean setCapkProfiles() {

        TLV tlv = new TLV();
        EMV req = new EMV((byte) EMV.CMD_ID._42_EmvSetProfile, (byte) EMV.CMD_TYPE.REQUEST, (byte) 3, (byte) 0);

        // -------------------- VISA KEY 50 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220150");
        // DF21	CAPK Modulus
        tlv.addData(0xDF21, "D11197590057B84196C2F4D11A8F3C05408F422A35D702F90106EA5B019BB28AE607AA9CDEBCD0D81A38D48C7EBB0062D287369EC0C42124246AC30D80CD602AB7238D51084DED4698162C59D25EAC1E66255B4DB2352526EF0982C3B8AD3D1CCE85B01DB5788E75E09F44BE7361366DEF9D1E1317B05E5D0FF5290F88A0DB47");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203010001");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "B769775668CACB5D22A647D1D993141EDAB7237B");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503014455");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 51 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220151");
        // DF21	CAPK Modulus
        tlv.addData(0xDF21, "DB5FA29D1FDA8C1634B04DCCFF148ABEE63C772035C79851D3512107586E02A917F7C7E885E7C4A7D529710A145334CE67DC412CB1597B77AA2543B98D19CF2CB80C522BDBEA0F1B113FA2C86216C8C610A2D58F29CF3355CEB1BD3EF410D1EDD1F7AE0F16897979DE28C6EF293E0A19282BD1D793F1331523FC71A228800468C01A3653D14C6B4851A5C029478E757F");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203000003");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "B9D248075A3F23B522FE45573E04374DC4995D71");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 53 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220153");
        // DF21	CAPK Modulus
        tlv.addData(0xDF21, "BCD83721BE52CCCC4B6457321F22A7DC769F54EB8025913BE804D9EABBFA19B3D7C5D3CA658D768CAF57067EEC83C7E6E9F81D0586703ED9DDDADD20675D63424980B10EB364E81EB37DB40ED100344C928886FF4CCC37203EE6106D5B59D1AC102E2CD2D7AC17F4D96C398E5FD993ECB4FFDF79B17547FF9FA2AA8EEFD6CBDA124CBB17A0F8528146387135E226B005A474B9062FF264D2FF8EFA36814AA2950065B1B04C0A1AE9B2F69D4A4AA979D6CE95FEE9485ED0A03AEE9BD953E81CFD1EF6E814DFD3C2CE37AEFA38C1F9877371E91D6A5EB59FDEDF75D3325FA3CA66CDFBA0E57146CC789818FF06BE5FCC50ABD362AE4B80996D");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203000003");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "AC213A2E0D2C0CA35AD0201323536D58097E4E57");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 54 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220154");
        // DF21	CAPK Modulus

        tlv.addData(0xDF21, "C6DDC0B7645F7F16286AB7E4116655F56DD0C944766040DC68664DD973BD3BFD4C525BCBB95272B6B3AD9BA8860303AD08D9E8CC344A4070F4CFB9EEAF29C8A3460850C264CDA39BBE3A7E7D08A69C31B5C8DD9F94DDBC9265758C0E7399ADCF4362CAEE458D414C52B498274881B196DACCA7273F687F2A65FAEB809D4B2AC1D3D1EFB4F6490322318BD296D153B307A3283AB4E5BE6EBD910359A8565EB9C4360D24BAACA3DBFE393F3D6C830D603C6FC1E83409DFCD80D3A33BA243813BBB4CEAF9CBAB6B74B00116F72AB278A88A011D70071E06CAB140646438D986D48281624B85B3B2EBB9A6AB3BF2178FCC3011E7CAF24897AE7D");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203010001");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "06960618791A86D387301EDD4A3BAF2D34FEF1B4");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 57 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220157");
        // DF21	CAPK Modulus

        tlv.addData(0xDF21, "942B7F2BA5EA307312B63DF77C5243618ACC2002BD7ECB74D821FE7BDC78BF28F49F74190AD9B23B9713B140FFEC1FB429D93F56BDC7ADE4AC075D75532C1E590B21874C7952F29B8C0F0C1CE3AEEDC8DA25343123E71DCF86C6998E15F756E3");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203010001");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "251A5F5DE61CF28B5C6E2B5807C0644A01D46FF5");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 58 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220158");
        // DF21	CAPK Modulus

        tlv.addData(0xDF21, "99552C4A1ECD68A0260157FC4151B5992837445D3FC57365CA5692C87BE358CDCDF2C92FB6837522842A48EB11CDFFE2FD91770C7221E4AF6207C2DE4004C7DEE1B6276DC62D52A87D2CD01FBF2DC4065DB52824D2A2167A06D19E6A0F781071CDB2DD314CB94441D8DC0E936317B77BF06F5177F6C5ABA3A3BC6AA30209C97260B7A1AD3A192C9B8CD1D153570AFCC87C3CD681D13E997FE33B3963A0A1C79772ACF991033E1B8397AD0341500E48A24770BC4CBE19D2CCF419504FDBF0389BC2F2FDCD4D44E61F");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203010001");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "753ED0AA23E4CD5ABD69EAE7904B684A34A57C22");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 94 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220194");
        // DF21	CAPK Modulus
        tlv.addData(0xDF21, "AC D2 B1 23 02 EE 64 4F 3F 83 5A BD 1F C7 A6 F6 2C CE 48 FF EC 62 2A A8 EF 06 2B EF 6F B8 BA 8B C6 8B BF 6A B5 87 0E ED 57 9B C3 97 3E 12 13 03 D3 48 41 A7 96 D6 DC BC 41 DB F9 E5 2C 46 09 79 5C 0C CF 7E E8 6F A1 D5 CB 04 10 71 ED 2C 51 D2 20 2F 63 F1 15 6C 58 A9 2D 38 BC 60 BD F4 24 E1 77 6E 2B C9 64 80 78 A0 3B 36 FB 55 43 75 FC 53 D5 7C 73 F5 16 0E A5 9F 3A FC 53 98 EC 7B 67 75 8D 65 C9 BF F7 82 8B 6B 82 D4 BE 12 4A 41 6A B7 30 19 14 31 1E A4 62 C1 9F 77 1F 31 B3 B5 73 36 00 0D FF 73 2D 3B 83 DE 07 05 2D 73 03 54 D2 97 BE C7 28 71 DC CF 0E 19 3F 17 1A BA 27 EE 46 4C 6A 97 69 09 43 D5 9B DA BB 2A 27 EB 71 CE EB DA FA 11 76 04 64 78 FD 62 FE C4 52 D5 CA 39 32 96 53 0A A3 F4 19 27 AD FE 43 4A 2D F2 AE 30 54 F8 84 06 57 A2 6E 0F C6 17");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203000003");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "C4 A3 C4 3C CF 87 32 7D 13 6B 80 41 60 E4 7D 43 B6 0E 6E 0F");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 95 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220195");
        // DF21	CAPK Modulus
        tlv.addData(0xDF21, "BE9E1FA5E9A803852999C4AB432DB28600DCD9DAB76DFAAA47355A0FE37B1508AC6BF38860D3C6C2E5B12A3CAAF2A7005A7241EBAA7771112C74CF9A0634652FBCA0E5980C54A64761EA101A114E0F0B5572ADD57D010B7C9C887E104CA4EE1272DA66D997B9A90B5A6D624AB6C57E73C8F919000EB5F684898EF8C3DBEFB330C62660BED88EA78E909AFF05F6DA627B");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203000003");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "EE1511CEC71020A9B90443B37B1D5F6E703030F6");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 96 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220196");
        // DF21	CAPK Modulus

        tlv.addData(0xDF21, "B74586D19A207BE6627C5B0AAFBC44A2ECF5A2942D3A26CE19C4FFAEEE920521868922E893E7838225A3947A2614796FB2C0628CE8C11E3825A56D3B1BBAEF783A5C6A81F36F8625395126FA983C5216D3166D48ACDE8A431212FF763A7F79D9EDB7FED76B485DE45BEB829A3D4730848A366D3324C3027032FF8D16A1E44D8D");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203000003");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "7616E9AC8BE014AF88CA11A8FB17967B7394030E");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 97 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220197");
        // DF21	CAPK Modulus
        tlv.addData(0xDF21, "AF0754EAED977043AB6F41D6312AB1E22A6809175BEB28E70D5F99B2DF18CAE73519341BBBD327D0B8BE9D4D0E15F07D36EA3E3A05C892F5B19A3E9D3413B0D97E7AD10A5F5DE8E38860C0AD004B1E06F4040C295ACB457A788551B6127C0B29");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203000003");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "8001CA76C1203955E2C62841CD6F201087E564BF");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        req.clear();
        tlv.clear();
        // -------------------- VISA KEY 99 ------------------------
        // DF20	Registered Application Provider Identifier (RID)
        tlv.addData("DF2005A000000003");
        // 9F22	CAPK Index - Terminal (PKI)
        tlv.addData("9F220199");
        // DF21	CAPK Modulus
        tlv.addData(0xDF21, "AB79FCC9520896967E776E64444E5DCDD6E13611874F3985722520425295EEA4BD0C2781DE7F31CD3D041F565F747306EED62954B17EDABA3A6C5B85A1DE1BEB9A34141AF38FCF8279C9DEA0D5A6710D08DB4124F041945587E20359BAB47B7575AD94262D4B25F264AF33DEDCF28E09615E937DE32EDC03C54445FE7E382777");        // TLV : use 2 byte length
        // DF22	CAPK Exponent
        tlv.addData("DF2203000003");
        // DF23	CAPK Check Sum
        tlv.addData(0xDF23, "4ABFFD6B1C51212D05552E431C5B17007D2F5E6D");
        // DF24	CAPK Expiration Date (YYYYMMDD): 20150101
        tlv.addData("DF240420490101");
        // DF25	CAPK Revocation Serial Number
        tlv.addData("DF2503000000");
        // DF26	CAPK EffectiveDate (YYYYMMDD): 20080101
        tlv.addData("DF260420080101");

        req.dataField = tlv;
        if (!checkEmvRsp(exchangeEmvCmd(req)))
            return false;

        return true;
    }

    //endregion

    //region Contact EMV System Configuration

    public void dumpLog() {
        clearLog();
        dumpEmvInfo();
        dumpEmvAppConfiguration();
    }

    public void dumpEmvInfo() {

        // Kernel version
        EMV req = new EMV((byte) EMV.CMD_ID._31_EmvGetKernelVersion, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0);
        EMV rsp = exchangeEmvCmd(req);
        if (!checkEmvRsp(rsp))
            return;
        addLog("Kernel version : " + Utility.bytes2Hex(rsp.dataField.toByteArray()));

        // API version
        req = new EMV((byte) EMV.CMD_ID._30_EmvGetApiVersion, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0);
        rsp = exchangeEmvCmd(req);
        if (!checkEmvRsp(rsp))
            return;
        addLog("API version : " + Utility.bytes2Hex(rsp.dataField.toByteArray()));

        // Checksum version of profile
        // YM<RS>{Len}{0x35}C{P1:0x00}{P2:0x00}{Len:0x0000}
        //        P1:Length of Checksum
        //           0x00~0x06 : 6 bytes SHA1 checksum
        //           others : full 20 bytes SHA1 checksum
        //        P2:Checksum Item Flag
        //           bit-1 : return ICS profile checksum
        //           bit-2 : return terminal profile checksum
        //           bit-3 : return application profile checksum
        //           bit-4 : return CAPK profile checksum

        req = new EMV((byte) EMV.CMD_ID._35_EmvGetCurrentCheckSum, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0xF);
        rsp = exchangeEmvCmd(req);
        if (!checkEmvRsp(rsp))
            return;

        byte[] checksum_data = rsp.dataField.toByteArray();
        if (checksum_data.length != 24) {
            addLog("Invalid Response Data Format");
            return;
        }

        addLog("ICS checksum : " + Utility.bytes2Hex(checksum_data, 0, 6));
        addLog("Terminal checksum : " + Utility.bytes2Hex(checksum_data, 6, 6));
        addLog("Application checksum : " + Utility.bytes2Hex(checksum_data, 12, 6));
        addLog("CAPK checksum : " + Utility.bytes2Hex(checksum_data, 18, 6));
    }

    public void dumpEmvAppConfiguration() {

        EMV req = new EMV((byte) EMV.CMD_ID._B1_EmvGetApConfiguration, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0);
        EMV rsp = exchangeEmvCmd(req);
        if (!checkEmvRsp(rsp))
            return;

        EMV.APP_CONFIG appConfig = new EMV.APP_CONFIG(rsp.dataField.toByteArray());
        addLog(appConfig.toString());
    }

    public void setEmvAppConfiguration() {

        EMV.APP_CONFIG appConfig = new EMV.APP_CONFIG();

        //PDK Slot : ‘X’
        //PIN Block Format 0
        //PIN Length Range 04~12
        //Timeout : 60 seconds
        appConfig.pinEntrySetting = "X00412060".getBytes();
        //Key Slot : ‘Y’
        //Padding ‘F’
        appConfig.sredFormatSetting = "Y200".getBytes();
        //Replacing char ‘*’
        //Unmasked leading 6 and trailing 4 char
        //Do not mask “Name”, “Expiration Date”, “Service Code”
        //Do not adjust MOD10 char
        appConfig.sredMaskSetting = "*640000".getBytes();

        EMV req = new EMV((byte) EMV.CMD_ID._B2_EmvSetApConfiguration, (byte) EMV.CMD_TYPE.REQUEST, (byte) 0, (byte) 0);
        req.dataField = new TLV(appConfig.toByteArray());
        checkEmvRsp(exchangeEmvCmd(req));

    }

    //endregion


}
