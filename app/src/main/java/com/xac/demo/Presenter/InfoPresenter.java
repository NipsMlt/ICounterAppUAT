package com.xac.demo.Presenter;

import android.os.Build;

import com.xac.helper.Utility;
import com.xac.helper.VNG;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import saioapi.base.Misc;
import saioapi.util.Sys;
import saioapi.util.Ver;

import static com.xac.helper.Logger.LINE_OUT;
import static com.xac.helper.Logger.log;

public class InfoPresenter extends BasePresenterViewWrapper {

    @Override
    protected void presenterImp() {
        setNaviTitle("Retrieve Information");
        putAction("Dump All Information", () -> dumpInfo());

        initCommPort();
    }

    public void dumpInfo() {

        AndroidInfo androidInfo = new AndroidInfo();

        clearLog();
        addLog("== Android System Information ==");

        for(Map.Entry<String, String> entry : androidInfo.map.entrySet()) {
            log(entry.getKey() + " : " + entry.getValue(), LINE_OUT());
            addLog(entry.getKey() + " : " + entry.getValue());
        }

        addLog("== CommPort Information ==");

        commPort.connect();
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

    class AndroidInfo {

        private Sys sys;
        private Misc misc;
        public Map<String, String> map = new HashMap<String, String>();

        public String getKernelVersion() {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/proc/version"), 256);
                try {
                    return reader.readLine();
                } finally {
                    reader.close();
                }

            }catch (IOException e) {
                log("IO Exception when getting kernel version for Device Info screen", LINE_OUT());
                return "Unavailable";
            }
        }

        public AndroidInfo() {
            misc = new Misc();
            sys = new Sys();
            getInfo();
        }

        public void getInfo() {

            map.clear();

            if (Sys.getCpuFamily() == sys.CPU_FAMILY_QCOM) {
                map.put("CPU", "QCOM");
            } else if (Sys.getCpuFamily() == Sys.CPU_FAMILY_IMX6) {
                map.put("CPU", "IMX6");
            } else if (Sys.getCpuFamily() == Sys.CPU_FAMILY_ALLWINNER) {
                map.put("CPU", "AW");
            } else if (Sys.getCpuFamily() == Sys.CPU_FAMILY_UNKNOWN) {
                map.put("CPU", "UNKNOWN");
            }

            byte [] buf = new byte[1024];

            if (misc.getSystemInfo(Misc.INFO_LOADER_VER, buf) == 0)
                map.put("loader version", Utility.byte2String(buf));
            else
                map.put("loader version", Build.BOOTLOADER);

            if (misc.getSystemInfo(Misc.INFO_MANUFACTURE, buf) == 0)
                map.put("manufacture", Utility.byte2String(buf));

            if (misc.getSystemInfo(Misc.INFO_PRODUCT, buf) == 0)
                map.put("product", Utility.byte2String(buf));

            if (misc.getSystemInfo(Misc.INFO_PRODUCT_VER, buf) == 0)
                map.put("product ver", Utility.byte2String(buf));

            if (misc.getSystemInfo(Misc.INFO_SECURE_STAT, buf) == 0)
            {
                if (buf[0] == 0x44)
                    map.put("OS Environment", "Non-secure");
                else if (buf[0] == 0x45)
                    map.put("OS Environment", "Secure");
                else
                    map.put("OS Environment", "Unknown");
            }

            map.put("build number", "\r\n----\r\n" + Build.DISPLAY  + "\r\n----");

            map.put("kernel version", "\r\n----\r\n" + getKernelVersion() + "\r\n----");

            if (Ver.getUBootEnvVersion(buf) != Ver.ERR_NOT_FOUND) {
                map.put("UBoot", Utility.byte2String(buf));
            }

            if (Ver.getSaioVersion(ctx, Ver.LIB_SAIO_ALL, buf) != Ver.ERR_NOT_FOUND) {
                map.put("Library", "\r\n----\r\n" + Utility.byte2String(buf) + "\r\n----");
            }
        }

    }

}
