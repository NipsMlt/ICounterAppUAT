package com.xac.helper.Printer;

import android.content.Context;
import android.util.Log;

import com.xac.helper.Utility;

import java.util.Arrays;

import saioapi.comm.v2.ComManager;
import saioapi.comm.v2.OnComEventListener;

public class Controller {

    protected ComManager comManager;
    protected OnEventListener eventListener;
    protected PrinterStatus lastStatus;

    private final int TIMEOUT_WRITE = 1000;
    private final int TIMEOUT_READ = 1000;

    public Controller(Context context, OnEventListener listener) {
        comManager = new ComManager(context);
        eventListener = listener;
        lastStatus = new PrinterStatus();
    }

    //region IO R/W

    public boolean open() {

        if (comManager.open(ComManager.DEVICE_PRINTER0) == 0) {
            comManager.connect(ComManager.PROTOCOL_RAW_DATA);
            comManager.setOnComEventListener(mOnComEventListener);
            writeData(new Command(ESC.CMD_AUTO_STATUS_BACK, (byte)0x01).getData());
            return true;
        }
        return false;
    }

    public void close() {
        comManager.close();
    }

    protected boolean writeData(byte [] data) {
        if (comManager.isOpened()) {
            int ret = comManager.write(data, data.length, TIMEOUT_WRITE);
            if (ret != data.length)
                Log.i(getClass().getName(),"lasterror : " + comManager.lastError());
            return ret == data.length;
        }
        return false;
    }

    protected byte [] readData() {
        if (comManager.isOpened()) {
            byte [] tmp = new byte[32];
            int nRead = comManager.read(tmp, tmp.length, TIMEOUT_READ);
            if (nRead > 0 && nRead != -1){
                Log.i(this.getClass().getName(), "DATA_READY: " + Utility.bytes2Hex(tmp, nRead));
                return Arrays.copyOfRange(tmp, 0, nRead);
            }
        }
        return null;
    }

    protected OnComEventListener mOnComEventListener = new OnComEventListener() {

        @Override
        public void onEvent(int event) {
            switch(event){
                case ComManager.EVENT_CONNECT:
                    Log.i(this.getClass().getName(), "Connected");
                    break;
                case ComManager.EVENT_DISCONNECT:
                    Log.i(this.getClass().getName(), "Disconnected");
                    eventListener.onEvent(OnEventListener.PrintResult.FAILED);
                    break;
                case ComManager.EVENT_EOT:
                    Log.i(this.getClass().getName(), "EOT");
                    break;
                case ComManager.EVENT_DATA_READY:

                    synchronized(lastStatus) {

                        lastStatus.updateStatus(readData());
                        lastStatus.notify();

                        if (lastStatus.b4_Paper_Out) {
                            eventListener.onEvent(OnEventListener.PrintResult.PAPER_OUT);
                        } else if (!lastStatus.NO_ERROR) {
                            eventListener.onEvent(OnEventListener.PrintResult.FAILED);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //endregion

    //region Global Configuration/Control

    public void cancel() {
        writeData(ESC.CMD_CANCEL);
    }

    /**
     * warning : This command will also impact the whole firmware OS env.
     * If printer and pinpad are located in the same firmware OS.
     * Reboot will make loss of session in pinpad.
     */
    public void reboot() {
        writeData(ESC.CMD_CONTROL_REBOOT);
    }

    public String getVersion() {
        if (writeData(ESC.CMD_GET_VERSION)) {
            byte[] data = readData();
            if (data != null) {
                return Utility.byte2String(data);
            }
        }
        return "UNKNOWN";
    }

    public OnEventListener.PrintResult getStatus() {

        if (writeData(ESC.CMD_GET_STATUS)) {

            synchronized(lastStatus) {
                try {
                    lastStatus.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (lastStatus.b4_Paper_Out) {
                return OnEventListener.PrintResult.PAPER_OUT;
            } else if (!lastStatus.NO_ERROR) {
                return OnEventListener.PrintResult.FAILED;
            }
            return OnEventListener.PrintResult.OK;

        }
        return OnEventListener.PrintResult.FAILED;
    }

    public boolean configIntensity(byte level) {

        // DC2 w nL nH d1 d2 …. dk - write configuration
        // d = {Tag}{Length}{Value}
        // Tag : 01
        // value : 1~10
        // Cmd -> 12 77 04 00 01 01 00 08
        Command cmd = new Command(ESC.CMD_CONTROL_WRITE_CONFIG);
        cmd.addData(new byte [] {0x04, 0x00, 0x01, 0x01, 0x00});
        cmd.addData((byte)0x08);

        return writeData(cmd.getData());
    }

    public boolean configCutterMode(boolean partial) {
        byte op = partial ? (byte)1 : (byte)0;
        return writeData(new Command(ESC.CMD_CUTTER, op).getData());
    }

    public boolean configLeftMargin(byte nColumns) {
        return writeData(new Command(ESC.CMD_MARGIN_LEFT, nColumns).getData());
    }

    public boolean configRightMargin(byte nColumns) {
        return writeData(new Command(ESC.CMD_MARGIN_RIGHT, nColumns).getData());
    }

    //endregion

    protected class PrinterStatus {

        public PrinterStatus() {}

        public String toMessage() {

            if (NO_ERROR)
                return "No Error";

            if (b4_Paper_Out)
                return "Paper Out";

            if (b5_Printer_Not_Ready)
                return "Printer Not Ready";

            return "Printer Failure";
        }

        public boolean updateStatus(byte [] statusBytes) {

            if (statusBytes != null && statusBytes.length >= 2 && statusBytes[1] == (byte)0x88) {
                NO_ERROR = (statusBytes[0] == 0) || (statusBytes[0] == 0x10);
                b1_Head_Temperaure_Abnormal = ((statusBytes[0] & 0x01) != 0);
                b2_Power_Supply_Abnormal = ((statusBytes[0] & 0x02) != 0);
                b3_Buffer_Full = ((statusBytes[0] & 0x04) != 0);
                b4_Paper_Out = ((statusBytes[0] & 0x08) != 0);
                b5_Printer_Not_Ready = ((statusBytes[0] & 0x10) != 0);
                b6_Cutter_Not_Ready = ((statusBytes[0] & 0x20) != 0);
                b7_Present_Not_Ready = ((statusBytes[0] & 0x40) != 0);
                b8_Busy = ((statusBytes[0] & 0x80) != 0);
                return true;
            }
            return false;
        }
        /*
            BIT FUNCTION BIT = 0 BIT = 1
            0 Head temperature OK Too high or too low
            1 Power supply OK Too high or too low
            2 Buffer Full No Yes
            3 Paper out No Yes
            4 Printer in printing Ready Action in progress
            5 Cutter in using Ready Action in progress
            6 Present in using Ready Action in progress
            7 Busy No Yes
         */

        public boolean NO_ERROR = true;

        public boolean b1_Head_Temperaure_Abnormal = false;
        public boolean b2_Power_Supply_Abnormal = false;
        public boolean b3_Buffer_Full = false;
        public boolean b4_Paper_Out = false;
        public boolean b5_Printer_Not_Ready = false;
        public boolean b6_Cutter_Not_Ready = false;
        public boolean b7_Present_Not_Ready = false;
        public boolean b8_Busy = false;
    }

}
