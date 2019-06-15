package com.xac.demo.Presenter;

import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xac.helper.CTLS;
import com.xac.helper.Utility;
import com.xac.helper.VNG;

import example.dtc.R;
import saioapi.util.Sys;

public class ContactlessPresenter extends BasePresenterViewWrapper {

    ImageView [] led;
    Sys sys;

    @Override
    protected void presenterImp() {

        setNaviTitle("Contactless Transaction");
        putAction("Load Default Data", () -> loadDefaultData());
        putAction("Start Transaction", () -> startTransaction());
        initCommPort();

        led = new ImageView[4];
        sys = new Sys();
    }

    Dialog dialog;

    private void createPresentCardDialog() {

        checkToRunOnUiThread(() -> {
            dialog = new Dialog(ctx, R.style.AppTheme);
            dialog.setContentView(R.layout.present_card_page);

            Window dialogWindow = dialog.getWindow();
            dialogWindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialogWindow.setGravity(Gravity.CENTER);

            //The below code is EXTRA - to dim the parent view by 70%
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.dimAmount = 0.7f;
            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.cancelPresentCard).setOnClickListener(view -> dialog.cancel());

            led[0] = dialog.findViewById(R.id.led0);
            led[1] = dialog.findViewById(R.id.led1);
            led[2] = dialog.findViewById(R.id.led2);
            led[3] = dialog.findViewById(R.id.led3);

            dialog.show();
        });
    }

    private void updateLed(int ledEvent) {

        for(int i = 0 ; i < 4 ; i++) {
            if (((ledEvent >> i) & 0x01) == 1)
                led[i].setImageResource(R.drawable.green_light);
            else
                led[i].setImageResource(R.drawable.red_light);
        }
    }

    public void startTransaction() {

        checkToRunOnUiThread(() -> createPresentCardDialog());

        commPort.connect();

        VNG req = new VNG();
        VNG rsp = new VNG();

        req.clear();
        req.addData("AR");
        // enable all readers
        // CLTS Data = 9F 02 06 00 00 00 00 00 20 DF 15 01 01 DF 20 01 14
        // Timeout == DF20 - 0x14 : 20 second
        byte[] arData = Utility.hex2Byte("01 01 01 00 9F 02 06 00 00 00 00 00 20 DF 15 01 01 DF 20 01 14");
        req.addRSLenData(arData);

        rsp = exchangeData(req);
        if (rsp == null || !rsp.parseString(2).equals("AR"))
        {
            addLog("Unrecognized Respond of AR");
            return;
        }

        if (rsp.parseRSLenData()[0] != 0x00)
        {
            addLog("Fail to Enable Auto Report");
            return;
        }

        sys.startPollingCtlsLeds();
        sys.setOnEventListener((handle, event) -> {
            final int ledEvent = event;
            checkToRunOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0 ; i < 4 ; i++) {
                        if (((ledEvent >> i) & 0x01) == 1)
                            led[i].setImageResource(R.drawable.green_light);
                        else
                            led[i].setImageResource(R.drawable.red_light);
                    }
                }
            });
        });

        addLog("Present Card...");

        // Wait for Present Card - 20 sec
        byte[] rspData = waitData(20000,null);

        commPort.disconnect();

        // sleep 1 second to show-off Soft LED behavior
        // it's not necessary here.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sys.stopPollingCtlsLeds();
        checkToRunOnUiThread(() -> {
            dialog.cancel();
        });
    }

    public void loadDefaultData() {

    }

    public CTLS exchangeCtlsCmd(CTLS req) {

        if(!sendData(req.getCmdBuffer()))
            return null;

        while(true) {

            byte [] rspData = waitData();

            if (rspData == null)
                return null;

            CTLS rsp = new CTLS(rspData);
            if (!rsp.tryToParse()){
                addLog("Unrecognized CTLS response command");
                return null;
            }
        }
    }

    //region Data

    private void clearAllData() {

        CTLS req = null;
        // Reset Printer to Init Status
        req = new CTLS(CTLS.CMD_INS._F2_Reset_CTLS_Configuration);
    }


    //endregion
}
