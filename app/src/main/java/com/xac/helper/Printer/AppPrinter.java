package com.xac.helper.Printer;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AppPrinter extends Controller {

    private int printerWidth = 48;    // 48 or 72 mm

    public AppPrinter(Context context, OnEventListener listener) {
        super(context, listener);
    }

    public AppPrinter(Context context, OnEventListener listener, int width) {
        super(context, listener);
        printerWidth = width;
    }

    // 0:12x24, 1:9x24,
    private byte currentFont = 0;

    private List<byte []> buffer = new ArrayList<byte []>();

    public void clear() {
        buffer.clear();;
    }

    public void addCommand(Command cmd) {
        buffer.add(cmd.getData());
    }

    // feed nDotLines * 0.125 mm
    public void feed(int nDotLines) {
        addCommand(new Command(ESC.CMD_FEED_PAPER, (byte)nDotLines));
    }

    //region Text

    /**
     * @param config 0:12x24, 1:9x24, others:RFU
     */
    public void selectFont(byte config) {
        currentFont = config;
        addCommand(new Command(ESC.CMD_SELECT_FOND, config));
    }

    public void addText(Text text) {
        addCommand(text);
    }

    public void addText(String text) {
        addCommand(new Text(text));
    }

    //endregion

    //region Graphic

    public boolean addImage(Bitmap bitmap, Graphic.Alignment alignment) {

        Graphic g = new Graphic(printerWidth);
        Collection<byte []> cmds = g.printImage(bitmap, alignment);

        if (cmds == null)
            return false;

        buffer.addAll(cmds);

        return true;
    }

    public boolean addColumnsText(String leftText, String centerText, String rightText, int style) {

        Graphic g = new Graphic(printerWidth);
        Collection<byte []> cmds = g.printLine(leftText, centerText, rightText, style);

        if (cmds == null)
            return false;

        buffer.addAll(cmds);
        return true;
    }

    public boolean addColumnsText(String leftText, int leftStyle, String centerText, int centerStyle, String rightText, int rightStyle) {
        return true;
    }

    //endregion

    //region Barcode

    //endregion

    //region QR Code

    //endregion

    public void start() {

        new Thread(new Runnable() {
            @Override
            public void run() {

            try {

                for(int i = 0 ; i < buffer.size() ; i++) {

                    while(true) {
                        if (lastStatus.b5_Printer_Not_Ready)
                            Thread.sleep(5);
                        else if (lastStatus.NO_ERROR)
                            break;
                        else {
                            return;
                        }
                    }

                    if(!writeData(buffer.get(i))) {
                        eventListener.onEvent(OnEventListener.PrintResult.FAILED);
                        break;
                    }
                    Thread.sleep(5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            }
        }).start();
    }
}
