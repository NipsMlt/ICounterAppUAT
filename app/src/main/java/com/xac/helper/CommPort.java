package com.xac.helper;

import android.content.Context;
import android.util.Log;

import saioapi.comm.v2.ComManager;

public class CommPort {

   // public int READ_TIMEOUT = 3000;
    public int READ_TIMEOUT = 60000;
    public ComManager com = null;

    private final static int MAX_PACKAGE_SIZE = 2048;

    public CommPort(Context context) {

        com = new ComManager(context);
        com.setOnComEventListener(event -> {
            switch(event){
                case ComManager.EVENT_CONNECT:
                    Log.i(this.getClass().toString(),"Connected");
                    if (eventCallback != null)
                        eventCallback.onEvent(ERROR.CONNECTED, null, 0);
                    break;
                case ComManager.EVENT_DISCONNECT:
                    Log.i(this.getClass().toString(),"Disconnected");
                    if (eventCallback != null)
                        eventCallback.onEvent(ERROR.DISCONNECTED, null, 0);
                    break;
                case ComManager.EVENT_EOT:
                    Log.i(this.getClass().toString(),"EOT");
                    break;
                case ComManager.EVENT_DATA_READY:
                    byte [] tmp = new byte[MAX_PACKAGE_SIZE];
                    int nRead = readData(tmp, tmp.length);
                    if (nRead > 0 && nRead != -1){
                        if (eventCallback != null)
                            eventCallback.onEvent(ERROR.DATA_READY, tmp, nRead);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    //region connection

    public boolean connect() {
        if (com.open(com.getBuiltInEppDevId()) == 0){
            com.connect(ComManager.PROTOCOL_XAC_VNG);
            return true;
        }
        return false;
    }

    public boolean connect(int port) {
        if (com.open(port) == 0){
            com.connect(ComManager.PROTOCOL_XAC_VNG);
            return true;
        }
        return false;
    }

    public boolean connect(int port, int protocol) {
        if (com.open(port) == 0){
            com.connect(protocol);
            return true;
        }
        return false;
    }

    public void disconnect() { com.close(); }

    public boolean isConnected() { return com.isOpened(); }

    //endregion

    //region read/write

    public boolean sendData(byte [] data) {
        return sendData(data, data.length);
    }

    public boolean sendData(byte [] data, int length) {
        if (com != null && com.isOpened())
            if (com.write(data, length, 2000) != ComManager.ERR_OPERATION) {
                return true;
            }
        return false;
    }

    public int readData(byte [] data, int length) {
        int nRead = com.read(data, data.length, READ_TIMEOUT);
        return nRead;
    }

    //endregion

    //region EVENT

    public enum ERROR {
        DATA_READY, CONNECTED, DISCONNECTED, OTHERS
    }

    public interface Event {
        void onEvent(ERROR err, byte[] data, int len);
    }

    Event eventCallback = null;
    public void setOnEvent(Event onEvent) {
        eventCallback = onEvent;
    }

    //endregion

}
