package com.xac.demo.Presenter;

import com.xac.demo.AppData;
import com.xac.helper.PinpadManager;
import com.xac.helper.VNG;

public class PinpadPresenter extends BasePresenterViewWrapper {

    private PinpadManager pinpadManager;

    @Override
    protected void presenterImp() {

        setNaviTitle("Pinpad");
        putAction("PIN Entry by PINPAD", () -> startPinEntry());
        initCommPort();

        pinpadManager = new PinpadManager(ctx, AppData.getInstance().product);

    }


    private void startPinProcess(String pan) {

        int longestWaitTime = 60;   // 60 sec

        VNG req = new VNG();
        req.addData("EP1");
        req.addData("X00412" + String.format("%03d", longestWaitTime));           // key slot - 'X', len 4~12, timeout 060 sec
        req.addData(pan);    // Account - SRED enabled, use acount stored in PINPAD

        VNG rsp = exchangeData(req, longestWaitTime * 1000);

        while(true) {

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

                String status = rsp.parseString(1);
                if (status.equals("0")) {
                    String pinLen = rsp.parseString(2);
                    addLog("PIN LEN : " + pinLen + "\r\n" + "EPB/KSN : " + rsp.parseString());
                } else if (status.equals("2")) {
                    addLog("Online PIN : Cancel by User");
                } else if (status.equals("3")) {
                    addLog("Online PIN : timeout");
                } else {
                    addLog("Online PIN Err : " + status);
                }
                break;
            } else {
                // exception
            }

            rsp = new VNG(waitData(longestWaitTime * 1000,null));
        }
    }

    public void startPinEntry() {

        commPort.connect();

        runOnWindowFocusChanged(hasFocus -> {
            if(!hasFocus && pinpadManager.PIN_MODE) {

                if (pinpadManager.virtualPinpad) {
                    String layout = pinpadManager.getVirtualPinpadLayout();
                    VNG rsp = exchangeData(new VNG("EPT" + layout));

                    // fail to setup virtual pinpad layout
                    if (rsp == null || !rsp.tryToParse("EPT0"))
                        pinpadManager.disable();
                }
                synchronized(pinpadManager.waitPopupDialog) {
                    pinpadManager.waitPopupDialog.notify();
                }
            }
        });

        pinpadManager.enable();

        try {
            synchronized(pinpadManager.waitPopupDialog) {
                pinpadManager.waitPopupDialog.wait(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (pinpadManager.PIN_MODE) {
            startPinProcess("012345678912345");
            pinpadManager.disable();
        }

        runOnWindowFocusChanged(null);

        commPort.disconnect();
    }

}
