package com.xac.helper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class DataSync {

    private static final String TAG = "DataSync";

    Queue<byte []> dataQueue = new LinkedList<byte []>();
    public String err = null;

    public void reset() {
        dataQueue.clear();
        err = null;
    }
    public synchronized byte [] waitData(long millis)
    {
        if (!dataQueue.isEmpty()) {
            return dataQueue.poll();
        }

        try {
            this.wait(millis);
        } catch (InterruptedException e) {
            return null;
        }
        return dataQueue.poll();
    }

    public synchronized void dataIn(byte [] data) {
        err = null;
        if (data != null) {
            dataQueue.offer(data);
        }
        notify();
        return;
    }

    public synchronized void dataIn(byte [] data, int length) {
        if (data != null && data.length >= length) {
            dataIn(Arrays.copyOf(data, length));
        } else {
            notify();
        }
        return;
    }

    public synchronized void setErr(String errMsg) {
        dataQueue.clear();
        err = errMsg;
        notify();
        return;
    }
}
