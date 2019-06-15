package com.xac.helper.Printer;

public interface OnEventListener {

    void onEvent(PrintResult result);

    public enum PrintResult {OK, PAPER_OUT, FAILED, COMPLETE, CANCELLED}
}
