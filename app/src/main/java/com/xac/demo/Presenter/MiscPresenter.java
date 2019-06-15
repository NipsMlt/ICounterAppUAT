package com.xac.demo.Presenter;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import com.xac.helper.Utility;
import com.xac.helper.VNG;

import java.util.ArrayList;

import saioapi.comm.v2.ComManager;
import saioapi.service.SystemUIService.SystemUIService;
import saioapi.settings.SaioSettings;

public class MiscPresenter extends BasePresenterViewWrapper {

    @Override
    protected void presenterImp() {

        setNaviTitle("Misc Functions");
        putAction("Show/Hide Navigation Bar", () -> switchNavigationBar());
        putAction("Show/Hide Status Bar", () -> switchStatusBar());
        putAction("Customized Settings", () -> buildCustomSettings());
        putAction("Connect External Pinpad", () -> connectExternalPinpad());
        putAction("Communicate with CDC", () -> communicateWithCDC());

        initCommPort();
    }


    boolean showNavigationBar = true;
    boolean showStatusBar = true;

    public void switchNavigationBar() {

        if (showNavigationBar)
            SystemUIService.setNaviButtonVisibility(ctx, SystemUIService.NAVIBUTTON_NAVIBAR, View.GONE);
        else
            SystemUIService.setNaviButtonVisibility(ctx, SystemUIService.NAVIBUTTON_NAVIBAR, View.VISIBLE);

        showNavigationBar = !showNavigationBar;
    }

    public void switchStatusBar() {

        if (showStatusBar)
            SystemUIService.setStatusBarVisibility(ctx, View.GONE);
        else
            SystemUIService.setStatusBarVisibility(ctx, View.VISIBLE);

        showStatusBar = !showStatusBar;
    }

    public void buildCustomSettings() {

        // native available settings
        // https://developer.android.com/reference/android/provider/Settings.html
        // XAC custom settings option - saioapi.settings.SaioSettings

        final ArrayList<String> intentList = new ArrayList<String>();
        intentList.add(Settings.ACTION_WIFI_SETTINGS); //System settings
        intentList.add(Settings.ACTION_BLUETOOTH_SETTINGS); //System settings
        intentList.add(SaioSettings.ACTION_ETHERNET_SETTINGS); //SAIO settings

        Intent intent = new  Intent(SaioSettings.PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SaioSettings.DATA_KEY, intentList);
        ctx.startActivity(intent);
    }

    public void connectExternalPinpad() {

        int [] devices = commPort.com.getUsbDevId(ComManager.XAC_DEVICE_VID, ComManager.XAC_DEVICE_PID);
        commPort.connect(devices[devices.length-1]);
        VNG rsp = exchangeData(new VNG("R0"));
        if (rsp != null)
        {
            // R0{Model}{FWVer}{Build#}{SN}{CID}
                /*
                    Model Name: 16 bytes ASCII hexadecimal data.
                    FW Version: 8 bytes ASCII hexadecimal data
                    Build #: 8 bytes ASCII hexadecimal data
                    Serial Number = 16 bytes of ASCII data
                    CID: 4 bytes ASCII hexadecimal data
                */
            if (rsp.parseString(2).equals("R0"))
            {
                addLog("Model Name    : " + rsp.parseString(16));
                addLog("FW Version    : " + rsp.parseString(8));
                addLog("Build Number  : " + rsp.parseString(8));
                addLog("Serial Number : " + rsp.parseString(16));
                addLog("CID           : " + rsp.parseString(4));
            }
        }
        commPort.disconnect();
    }

    // This is used to test CDC communication
    // Please enable CDC and reboot device.
    // You can using PC general serial port utility like putty to see if any data is through via USB device port.
    public void communicateWithCDC() {

        // It's more like a physical serial port. It could not recognize if port is connected correctly.
        commPort.connect(ComManager.DEVICE_GS0, ComManager.PROTOCOL_RAW_DATA);

        sendData("Hello".getBytes());

        // echo any data until getting 'e' char
        while(true) {
            byte [] data = waitData();
            if (data != null) {
                String s = Utility.byte2String(data);
                // echo
                sendData(data);

                addLog(s);
                if (s.equals("e")) {
                    break;
                }
            }
        }

        commPort.disconnect();

        addLog("Close Port");
    }

}
