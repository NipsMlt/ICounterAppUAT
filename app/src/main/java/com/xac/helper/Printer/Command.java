package com.xac.helper.Printer;

import java.util.Arrays;

/**
 * Created by simon_chen on 2017/12/5.
 */

public class Command {

    public byte [] data = new byte [2048];
    public int size = 0;

    public Command() {

    }

    public Command(byte [] raw) {
        this.addData(raw);
    }

    public Command(byte [] raw, byte op) {
        this.addData(raw);
        this.addData(op);
    }

    public void addData(byte b) {
        data[size++] = b;
    }

    public void addData(byte [] raw) {
        addData(raw, raw.length);
    }

    public void addData(byte [] raw, int length) {
        System.arraycopy(raw, 0, data, size, length);
        size += length;
    }

    public byte [] getData() {
        return Arrays.copyOfRange(data, 0, size);
    }

}
