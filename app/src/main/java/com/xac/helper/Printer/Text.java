package com.xac.helper.Printer;

/**
 * Created by simon_chen on 2017/12/5.
 */

public class Text extends Command {

    public Text() {
    }

    public Text(String text) {
        addText(text, true);
    }

    public void addText(String text, boolean endLine) {
        if (text.length() > 0)
            addData(text.getBytes());
        if (endLine)
            addData(ESC.LF);
    }
    public void doubleWidth(boolean on) {
        addData(ESC.CMD_DOUBLE_WIDTH);
        if (on)
            addData((byte)0x01);
        else
            addData((byte)0x00);
    }
    public void doubleHeight(boolean on) {
        addData(ESC.CMD_DOUBLE_HEIGHT);
        if (on)
            addData((byte)0x01);
        else
            addData((byte)0x00);
    }
    public void bold(boolean on) {
        if (on)
            addData(ESC.CMD_BOLD_ON);
        else
            addData(ESC.CMD_BOLD_OFF);
    }
    public void italic(boolean on) {
        if (on)
            addData(ESC.CMD_ITALICS_ON);
        else
            addData(ESC.CMD_ITALICS_OFF);
    }


}
