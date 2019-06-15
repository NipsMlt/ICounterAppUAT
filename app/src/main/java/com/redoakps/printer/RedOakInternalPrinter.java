package com.redoakps.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

import saioapi.comm.Com;

public class RedOakInternalPrinter extends RedOakPrinter {
    private final int TIMEOUT = 1000;

    private Com mPrinterCom;
    private int mPrinterHandle = Com.ERR_OPERATION;

    private final byte[] FEED_PAPER = {0x70, 0x00, 0x03, 0x1B, 0x4A, (byte) 0x80, (byte) 0xA2};
    private final byte[] CANCEL = {0x70, 0x00, 0x02, 0x1D, 0x18, 0x77};
    private final byte[] RESET_PRINTER = {0x70, 0x00, 0x02, 0x1B, 0x40, 0x29};
    private final byte[] CHECK_STATUS = {0x1D, 0x72};

    public PrintResult getStatus() {

        int length = mPrinterCom.write(mPrinterHandle, CHECK_STATUS, CHECK_STATUS.length, TIMEOUT);

        if (length != Com.ERR_OPERATION) {
            byte[] response = new byte[10];
            length = mPrinterCom.read(mPrinterHandle, response, response.length, TIMEOUT);

            if (length == 0x02 && response[1] == (byte)0x88) {

                /*
                    0 Head temperature too high or too low
                    1 Power supply too high or too low
                    2 Buffer Full
                    3 Paper out
                    4 Printer in printing
                    5 Cutter in using
                */
                if ((response[0] & 0x08) != (byte) 0x00)
                    return PrintResult.PAPER_OUT;

                if (response[0] == 0)
                    return PrintResult.OK;
            }
        }

        return PrintResult.FAILED;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public RedOakInternalPrinter() {
        TAG = "RedOakInternalPrinter";
        mPrintWidth = PrintWidth.TP_48;
        mInterval = 20;
        mPrintJobTimeout = 500;
        mRunnable = new PrintRunnable();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public RedOakInternalPrinter(int interval, int printJobTimeout) {
        TAG = "RedOakInternalPrinter";
        mPrintWidth = PrintWidth.TP_48;
        mInterval = interval;
        mPrintJobTimeout = printJobTimeout;
        mRunnable = new PrintRunnable();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public PrintResult open(Context context) {
        mPrinterCom = new Com();
        mPrinterHandle = mPrinterCom.open((short) Com.DEVICE_PRINTER0);

        if (mPrinterHandle != Com.ERR_OPERATION && (mPrinterHandle < Com.ERR_NOT_READY || mPrinterHandle > Com.ERR_XPD_BUSY)) {
            if (mPrinterCom.connect(mPrinterHandle, 115200, (byte) 0, (byte) 0, (byte) 0, (byte) Com.PROTOCOL_RAW_DATA, null) == 0) {
                Log.d(TAG, "200T Internal Printer Connected");

                // Reset the printer device
//                addToArrayList(RESET_PRINTER);

                Log.d(TAG, "Printer Opened - Print Job Started");
                return PrintResult.OK;
            }
            else {
                Log.d(TAG, "Error connecting to 200T Internal Printer");
            }
        }
        else {
            int error = mPrinterCom.lastError();
            Log.d(TAG, "Error opening 200T Internal Printer: " + error);
        }

        return PrintResult.FAILED;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public PrintResult printImage(Bitmap bitmap, Alignment align) {
        if (null == bitmap) {
            Log.d(TAG, "Invalid Bitmap");
            return PrintResult.FAILED;
        }

        Collection<byte[]> collection = bitmapToCommands(bitmap, align);

        if (collection.size() == 0) {
            Log.d(TAG, "Invalid Bitmap");
            return PrintResult.FAILED;
        }

        for (byte[] command : collection) {
            byte[] formattedCommand = new byte[command.length + 4];

            // Protocol Command Format
            formattedCommand[0] = 0x70;

            // Data Length
            formattedCommand[1] = (byte) (command.length >> 8);
            formattedCommand[2] = (byte) command.length;

            // Instruction Field
            System.arraycopy(command, 0, formattedCommand, 3, command.length);

            // Checksum
            byte checkSum = 0;
            for (int i = 0; i < formattedCommand.length; i++) {
                checkSum = (byte) (checkSum ^ formattedCommand[i]);
            }
            formattedCommand[formattedCommand.length - 1] = checkSum;

            addToArrayList(formattedCommand);
        }

        return PrintResult.OK;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void feed() {
        addToArrayList(FEED_PAPER);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void cut(boolean fullCut) {
        // Placeholder - 200T Internal Printer does not support cutter
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void close() {
        if (mPrinterHandle != Com.ERR_OPERATION) {
            Log.d(TAG, "Shutting down 200T Internal Printer");

            // Shutdown the printer handle
            mPrinterCom.cancel(mPrinterHandle);

            // TODO: Delay required to prevent error similar to this:
            // JNI ERROR (app bug): accessed deleted global reference 0x1d20054a
            // or this:
            // JNI ERROR (app bug): accessed stale global reference 0x1d20043a (index 270 in a table of size 270)
            Utility.delay(250);

            mPrinterCom.close(mPrinterHandle);

            mPrinterHandle = Com.ERR_OPERATION;

            clearArrayList();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private class PrintRunnable implements Runnable {
        @Override
        public void run() {
            PrintResult printResult;
            Command command;

            while (true) {
                Utility.delay(mInterval);

                command = readFromArrayList();
                // If we don't have a current command, continue the while loop
                if (command == null) {
                    Log.d(TAG, "Current command list is exhausted");
                    continue;
                }

                if (!command.isEndOfPrintJob()) {
                    if ((printResult = getPrintJobResult()) != PrintResult.OK) {
                        decrementIndex();
                        Log.d(TAG, "Print Result Error Occurred: " + printResult);
                        return;
                    }

                    if ((printResult = sendCommand(command.getCommand())) != PrintResult.OK) {
                        if (printResult != PrintResult.PAPER_OUT) {
                            cancelPrintJob();
                        }
                        setPrintJobResult(printResult);
                        return;
                    }
                }
                else {
                    Log.d(TAG, "Print Job Complete");
                    setPrintJobResult(PrintResult.COMPLETE);
                    return;
                }
            }
        }
    };

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private final byte PRINTER_COMMAND_ERROR_TAG = 0x01;
    private final byte PRINTER_COMMAND_ERROR_CHECKSUM = 0x02;
    private final byte PRINTER_COMMAND_ERROR_COMMAND = 0x03;
    private final byte PRINTER_COMMAND_ERROR_PARITY = 0x04;
    private final byte PRINTER_COMMAND_ERROR_OVERRUN = 0x05;
    private final byte PRINTER_COMMAND_ERROR_FRAME = 0x06;
    private final byte PRINTER_COMMAND_ERROR_TIMEOUT = 0x07;
    private final byte PRINTER_COMMAND_ERROR_OVERFLOW = 0x08;

    private final byte PRINTER_ERROR_HEAD_TEMP = 0x01;
    private final byte PRINTER_ERROR_POWER = 0x02;
    private final byte PRINTER_ERROR_BUFFER_FULL = 0x04;
    private final byte PRINTER_ERROR_PAPER_OUT = 0x08;
    private final byte PRINTER_ERROR_BUSY = (byte)0x80;

    private PrintResult sendCommand(byte[] command) {
        try {
            int length = mPrinterCom.write(mPrinterHandle, command, command.length, TIMEOUT);

            if (length != Com.ERR_OPERATION) {
                //Log.d(TAG, "Successfully Wrote Packet Length: " + length + " bytes");
                Log.d(TAG, Utility.binToStr(command));
                if (command[0] != 0x70) {
                    return PrintResult.OK;
                }

                byte[] response = new byte[10];
                length = mPrinterCom.read(mPrinterHandle, response, response.length, TIMEOUT);

                if (length != Com.ERR_OPERATION) {
                    if (response[0] == 0x71) {
                        //Log.d(TAG, "Successfully Read Packet Length: " + length + " bytes");
                        //Log.d(TAG, Utility.binToStr(response));

                        if ((response[4] & PRINTER_ERROR_HEAD_TEMP) == PRINTER_ERROR_HEAD_TEMP) {
                            Log.d(TAG, "PRINTER_ERROR_HEAD_TEMP");
                            return PrintResult.FAILED;
                        }
                        else if ((response[4] & PRINTER_ERROR_POWER) == PRINTER_ERROR_POWER) {
                            Log.d(TAG, "PRINTER_ERROR_POWER");
                            return PrintResult.FAILED;
                        }
                        else if ((response[4] & PRINTER_ERROR_BUFFER_FULL) == PRINTER_ERROR_BUFFER_FULL) {
//                            Log.d(TAG, "PRINTER_ERROR_BUFFER_FULL - Resending Previous Command");
                            decrementIndex();
                            return PrintResult.OK;
                        }
                        else if ((response[4] & PRINTER_ERROR_PAPER_OUT) == PRINTER_ERROR_PAPER_OUT) {
                            Log.d(TAG, "PRINTER_ERROR_PAPER_OUT");
                            decrementIndex();
                            return PrintResult.PAPER_OUT;
                        }
                        else if ((response[4] & PRINTER_ERROR_BUSY) == PRINTER_ERROR_BUSY) {
                            Log.d(TAG, "PRINTER_ERROR_BUSY");
                            return PrintResult.FAILED;
                        }

                        return PrintResult.OK;
                    }
                    else if (response[0] == 0x72) {
                        Log.d(TAG, "Successfully Read Error Packet Length: " + length + " bytes");

                        if ((response[4] & PRINTER_COMMAND_ERROR_TAG) == PRINTER_COMMAND_ERROR_TAG) {
                            Log.d(TAG, "PRINTER_COMMAND_ERROR_TAG");
                            return PrintResult.FAILED;
                        }
                        else if ((response[4] & PRINTER_COMMAND_ERROR_CHECKSUM) == PRINTER_COMMAND_ERROR_CHECKSUM) {
                            Log.d(TAG, "PRINTER_COMMAND_ERROR_CHECKSUM");
                            return PrintResult.FAILED;
                        }
                        else if ((response[4] & PRINTER_COMMAND_ERROR_COMMAND) == PRINTER_COMMAND_ERROR_COMMAND) {
                            Log.d(TAG, "PRINTER_COMMAND_ERROR_COMMAND");
                            return PrintResult.FAILED;
                        }
                        else if ((response[4] & PRINTER_COMMAND_ERROR_PARITY) == PRINTER_COMMAND_ERROR_PARITY) {
                            Log.d(TAG, "PRINTER_COMMAND_ERROR_PARITY");
                            return PrintResult.FAILED;
                        }
                        else if ((response[4] & PRINTER_COMMAND_ERROR_OVERRUN) == PRINTER_COMMAND_ERROR_OVERRUN) {
                            Log.d(TAG, "PRINTER_COMMAND_ERROR_OVERRUN");
                            return PrintResult.FAILED;
                        }
                        else if ((response[4] & PRINTER_COMMAND_ERROR_FRAME) == PRINTER_COMMAND_ERROR_FRAME) {
                            Log.d(TAG, "PRINTER_COMMAND_ERROR_FRAME");
                            return PrintResult.FAILED;
                        }
                        else if ((response[4] & PRINTER_COMMAND_ERROR_TIMEOUT) == PRINTER_COMMAND_ERROR_TIMEOUT) {
                            Log.d(TAG, "PRINTER_COMMAND_ERROR_TIMEOUT");
                            return PrintResult.FAILED;
                        }
                        else if ((response[4] & PRINTER_COMMAND_ERROR_OVERFLOW) == PRINTER_COMMAND_ERROR_OVERFLOW) {
                            Log.d(TAG, "PRINTER_COMMAND_ERROR_OVERFLOW");
                            return PrintResult.FAILED;
                        }
                    }
                }
                else {
                    Log.d(TAG, "Read Error = " + mPrinterCom.lastError());
                }
            }
            else {
                Log.d(TAG, "Write Error = " + mPrinterCom.lastError());
            }
        }
        catch (Exception e) {
            Log.d(TAG, "sendCommand Exception");
            e.printStackTrace();
        }
        return PrintResult.FAILED;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void cancelPrintJob() {
        try {
            int length = mPrinterCom.write(mPrinterHandle, CANCEL, CANCEL.length, TIMEOUT);
            if (length != Com.ERR_OPERATION) {
                Log.d(TAG, "Successfully Wrote Cancel Packet Length: " + length + " bytes");

                byte[] response = new byte[1000];
                length = mPrinterCom.read(mPrinterHandle, response, response.length, TIMEOUT);

                if (length != Com.ERR_OPERATION) {
                    Log.d(TAG, "Successfully Read Cancel Packet Length: " + length + " bytes");
                }
                else {
                    Log.d(TAG, "Read Error = " + mPrinterCom.lastError());
                }
            }
            else {
                Log.d(TAG, "Write Error = " + mPrinterCom.lastError());
            }
        }
        catch (Exception e) {
            Log.d(TAG, "cancelPrintJob Exception");
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    static int[] _pixels = null;
    private final boolean _isDebug = false;

    private Collection<byte[]> bitmapToCommands(Bitmap bmp, Alignment align)
    {
        if(null == bmp)
            return null;

        ArrayList<byte[]> cmds = null;
        long t = 0L;
        int nMaxCmdLen = 1152;//0x0480: default for TP_48 (48x24)
        if(PrintWidth.TP_72 == mPrintWidth)
            nMaxCmdLen = 1728;//0x06C0: for TP_72 (72x24)
        if(_isDebug)
            Log.d(TAG, "nMaxCmdLen=" + nMaxCmdLen);

        int height = bmp.getHeight();
//        if(height > bmp.getHeight())
//        {
//            Log.w(TAG, "The parameter height (" + height + ") is larger than the height of transfer bitmap (" + bmp.getHeight() + ")!");
//            height = bmp.getHeight();
//        }
        if(null == _pixels || (bmp.getWidth() * height > _pixels.length))
        {
            _pixels = new int[bmp.getWidth() * height];
        }
        if(_isDebug)Log.v(TAG, "bmp_size=" + bmp.getWidth() + "x" + height);

        t = System.currentTimeMillis();
        bmp.getPixels(_pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), height);
        if(_isDebug)Log.v(TAG, "Spend " + (System.currentTimeMillis() - t) + " ms to get bmp pixels");

        //ARGB pixels to Monochrome bytes
        t = System.currentTimeMillis();
        byte[] raw = _pixelsARGB2Mono(_pixels, bmp.getWidth(), height, mPrintWidth.nativeInt, align, false);
        if(_isDebug)Log.v(TAG, "Spend " + (System.currentTimeMillis() - t) + " ms to convert pixels from ARGB to Mono");

        if(null != raw)
        {
            cmds = new ArrayList<byte[]>();
            int nb = 0;
            byte[] cmd = null;
            int nCmdLen = 0;

            //split monochrome bytes to print commands
            t = System.currentTimeMillis();
            while(true)
            {
                nb = cmds.size() * nMaxCmdLen;
                if(nb >= raw.length)
                    break;

                cmd = null;
                if((raw.length - nb) >= nMaxCmdLen)
                {
                    cmd = newGraphicCommand(mPrintWidth, nMaxCmdLen);
                    nCmdLen = nMaxCmdLen;
                    System.arraycopy(raw, nb, cmd, nCommandHeader, nCmdLen);
                }
                //reminder data
                else
                {
                    int r = raw.length % nMaxCmdLen;
                    cmd = newGraphicCommand(mPrintWidth, r);
                    nCmdLen = ((r % mPrintWidth.nativeInt) == 0)? r : r - (r % mPrintWidth.nativeInt) + mPrintWidth.nativeInt;
                    System.arraycopy(raw, nb, cmd, nCommandHeader, r);
                }

                Log.i(getClass().getName(), "CMDS:\n" + Utility.binToStr(cmd));
                cmds.add(cmd);
            }
            if(_isDebug)Log.v(TAG, "Spend " + (System.currentTimeMillis() - t) + " ms to sparate " + cmds.size() + " cmd(s)");
        }
        else
            return null;

        return cmds;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static int nCommandHeader = 8;

    private static byte[] newGraphicCommand(PrintWidth printWidth, int len)
    {
        nCommandHeader = 8;
        byte[] cmd = new byte[len + nCommandHeader];

        cmd[0] = 0x1B;
        cmd[1] = 0x2A;
        cmd[2] = (byte)(len & 0xFF);
        cmd[3] = (byte)(len >> 8);
        cmd[4] = 0x00;
        cmd[5] = 0x00;
        cmd[6] = 0x00;
        cmd[7] = (byte)printWidth.nativeInt;

        return cmd;
    }
}
