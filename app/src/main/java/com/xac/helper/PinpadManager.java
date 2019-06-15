package com.xac.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import example.dtc.R;
import saioapi.util.Sys;

/**
 * Created by simon_chen on 2017/11/24.
 */


public class PinpadManager {

    Context activityContext = null;
    private Handler handler;
    private Dialog pinDialog = null;

    public boolean virtualPinpad = false;
    public boolean PIN_MODE = false;
    public TextView pinTextView = null;
    public Object waitPopupDialog = new Object();

    private void init(Context context) {
        activityContext = context;
        handler = new Handler(context.getMainLooper());
        createVirtualPinpadDialog();
    }
    public PinpadManager(Context context, boolean isVirtualPinpad) {
        virtualPinpad = isVirtualPinpad;
        init(context);
    }

    public PinpadManager(Context context, String productName) {
        virtualPinpad = isVirtualPinpad(productName);
        init(context);
    }

    private boolean isVirtualPinpad(String productName) {
        if ( productName.contains("AT-150") ||
                productName.contains("AT-170") ||
                productName.contains("xCL_E200CP"))
            return true;
        else
            return false;
    }

    // create dialog and assign to pinDialog
    private void createVirtualPinpadDialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pinDialog = new Dialog(activityContext, R.style.AppTheme);

                if (virtualPinpad)
                    pinDialog.setContentView(R.layout.virtual_pinpad_page);
                else
                    pinDialog.setContentView(R.layout.physical_pinpad_page);

                Window dialogWindow = pinDialog.getWindow();
                dialogWindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialogWindow.setGravity(Gravity.CENTER);

                //The below code is EXTRA - to dim the parent view by 70%
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.dimAmount = 0.7f;
                lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                pinDialog.getWindow().setAttributes(lp);

                pinTextView = pinDialog.findViewById(R.id.pinTextView);
            }
        });
    }

    // pass rendering pinpad layout information to firmware
    public String getVirtualPinpadLayout() {

        // EPT{Kx}{Ky}{Kw}{Kh}{Fx}{Fy}{Fw}{Fh}
        /*
            Kx , Ky -> left-top of button 1
            Kw , Kh -> width/height of four row pinpad
            Fx , Fy -> left-top of button "cancel" (red key)
            Fw , Fh -> width/height of "red/yellow/green" key
         */
        int [] pin1Location = new int[2];
        Button pin1 = (Button)pinDialog.findViewById(R.id.pin1);
        pin1.getLocationOnScreen(pin1Location);

        int [] pinNullRightLocation = new int[2];
        Button pinNullRight = (Button)pinDialog.findViewById(R.id.pinNullRight);
        pinNullRight.getLocationOnScreen(pinNullRightLocation);

        String kx = String.format("%04d",pin1Location[0]);
        String ky = String.format("%04d",pin1Location[1]);
        String kw = String.format("%04d",pinNullRightLocation[0] + pinNullRight.getWidth() - pin1Location[0]);
        String kh = String.format("%04d",pinNullRightLocation[1] + pinNullRight.getHeight() - pin1Location[1]);

        int [] pinRedLocation = new int[2];
        Button pinRed = (Button)pinDialog.findViewById(R.id.pinRed);
        pinRed.getLocationOnScreen(pinRedLocation);

        int [] pinGreenLocation = new int[2];
        Button pinGreen = (Button)pinDialog.findViewById(R.id.pinGreen);
        pinGreen.getLocationOnScreen(pinGreenLocation);

        String fx = String.format("%04d",pinRedLocation[0]);
        String fy = String.format("%04d",pinRedLocation[1]);
        String fw = String.format("%04d",pinGreenLocation[0] + pinGreen.getWidth() -  pinRedLocation[0]);
        String fh = String.format("%04d",pinGreenLocation[1] + pinGreen.getHeight() - pinRedLocation[1]);

        return kx + ky + kw + kh + fx + fy + fw + fh;
    }

    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    public void updatePinLength(final int len) {
        runOnUiThread (new Runnable() {
            @Override
            public void run() {
                if (len > 0)
                    pinTextView.setText(String.format("%0" + len + "d", 0).replace("0","*"));
                else
                    pinTextView.setText("");
            }
        });
    }

    // start PIN process until finish
    public void enable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PIN_MODE = true;
                pinDialog.show();

                // In PCI requirement, it requires to freeze screen during PIN INPUT
//                if(virtualPinpad)
                    Sys.setPinEntryModeEnabled(true);
            }
        });
    }

    public void disable() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // In PCI requirement, it requires to freeze screen during PIN INPUT
//                if (virtualPinpad)
                    Sys.setPinEntryModeEnabled(false);

                pinDialog.cancel();
                PIN_MODE = false;
            }
        });
    }

}