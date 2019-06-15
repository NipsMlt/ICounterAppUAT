package com.xac.demo.Presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.redoakps.printer.RedOakInternalPrinter;
import com.redoakps.printer.RedOakPrinter;
import com.xac.helper.Printer.OnEventListener;

import java.io.IOException;
import java.io.InputStream;

public class PrinterPresenter extends BasePresenterViewWrapper implements OnEventListener {

    // printer
    RedOakInternalPrinter printer;
    RedOakPrinter.PrintResult printResult;

    @Override
    protected void presenterImp() {
        setNaviTitle("Printer");
        putAction("Test Print", () -> testPrint());
        putAction("Get Status", () -> getStatus());
    }

    public void getStatus() {

        printer = new RedOakInternalPrinter();
        printResult = printer.open(ctx);
        if (printResult == RedOakPrinter.PrintResult.OK) {
            addLog(printer.getStatus().toString());
            printer.close();
        }
        else
            addLog("Fail to Open Printer : " + printResult.toString());
    }

    public byte [] getTextData(String msg) {

        byte [] raw = new byte[msg.length()+1];
        System.arraycopy(msg.getBytes(), 0, raw, 0, msg.length());
        raw[msg.length()-1] = 0x0A;
        return raw;
    }

    public void testPrint() {

        printer = new RedOakInternalPrinter();

        printResult = printer.open(ctx);
        if (printResult == RedOakPrinter.PrintResult.OK)
            addLog("Open Printer OK");
        else
            addLog("Fail to Open Printer : " + printResult.toString());

        printer.feed();

        printer.printLine("Item", "Quantity", "Price", RedOakPrinter.STYLE_UNDERLINE);
        printer.printLine("ABC", "3", "40", RedOakPrinter.STYLE_UNDERLINE);
        printer.printLine("def", "12", "80", RedOakPrinter.STYLE_BOLD);
        printer.printLine("Ghi", "6", "456", RedOakPrinter.STYLE_ITALIC);

        printer.printLine("Line 1\nLine 2\nLine 3", RedOakPrinter.STYLE_ITALIC);
        printer.printLine("0123456789qwertyuiopasdfghjklzxcvbnm");
        printer.printLine("Этот текст напечатанный по русски", "", "", RedOakPrinter.STYLE_NONE);
        printer.printLine("0123456789qwertyuiopasdfghjklzxcvbnm");


        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream is = ctx.getResources().getAssets().open("images/small.jpg");
            Bitmap image = BitmapFactory.decodeStream(is, null, options);
            printer.printImage(image, RedOakPrinter.Alignment.CENTER);

        } catch (IOException e) {
            e.printStackTrace();
        }

        printer.feed();

        printResult = printer.waitForResult((Activity)ctx);
        addLog("Result : " + printResult.name());

        printer.close();
        addLog("Close Printer");

    }

    @Override
    public void onEvent(PrintResult result) {
        addLog("onEvent: " + result.toString());
    }
}
