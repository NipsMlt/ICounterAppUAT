package com.xac.helper;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Utility {

    public static byte[] toByteArray(List<Byte> in) {
        final int n = in.size();
        byte ret[] = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }
        return ret;
    }

    // Convert Hex String to Byte Array
    public static byte[] hex2Byte(String str)
    {
        str = str.replace(" ", "");

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte) Integer
                    .parseInt(str.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static String bytes2Hex(byte[] b, int offset, int length)
    {
        return bytes2Hex(Arrays.copyOfRange(b, offset, offset + length));
    }

    public static String bytes2Hex(byte[] b, int length)
    {
        return bytes2Hex(Arrays.copyOfRange(b, 0, length));
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String byte2Hex(byte b)
    {
        return bytes2Hex(new byte [] {b});
    }

    // Convert Byte Arrary to Hex String
    public static String bytes2Hex(byte[] bytes)
    {
        char [] hexChars = new char[bytes.length * 2];
        for(int j = 0 ; j < bytes.length ; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String byte2String(byte [] data) {
        for(int i = 0 ; i < data.length ; i++) {
            if (data[i] == 0)
                return new String(data).substring(0, i);
        }
        return new String(data);
    }

    public static String numeric2string(byte [] data)
    {
        String ret = "";
        for(int i = 0; i < data.length; i++)
        {
            byte hb = (byte)((data[i] & 0xF0) >> 4);
            byte lb = (byte)(data[i] & 0x0F);

            if (hb >= 0 && hb <= 9)
                ret += Byte.toString(hb);
            else if (hb == 0xF)
                ret += 'F';
            else
                break;

            if (lb >= 0 && lb <= 9)
                ret += Byte.toString(lb);
            else if (hb == 0xF)
                ret += 'F';
            else
                break;
        }
        return ret;
    }

    private static void putFileToMediaStore(Context ctx, File file) {
        try {
            new MediaScannerConnection.MediaScannerConnectionClient() {
                private MediaScannerConnection mMs;

                public void init() {
                    mMs = new MediaScannerConnection(ctx, this);
                    mMs.connect();
                }

                @Override
                public void onMediaScannerConnected() {

                    File pathFile = Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    mMs.scanFile(file.getAbsolutePath(), null); // <-- repeat for all files

                    mMs.disconnect();
                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                }

            }.init();
        } catch(Exception e) {
        }
    }

    private static String getLogDirectory() {
        if (new File("/storage/udisk/Download/").isDirectory())
            return "/storage/udisk/Download/";
        else
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
    }

    private static String getLogPath(String logName, boolean hasTime) {
        String path = getLogDirectory() + logName;
        if (hasTime)
            path += android.text.format.DateFormat.format(" yyyy-MM-dd_hhmmss.txt", new java.util.Date()).toString();
        else
            path += ".txt";
        return path;
    }

    public static String saveLogToFile(Context ctx, String logName, String log) {

        String logPath = getLogPath(logName, true);

        try {
            File logFile = new File(logPath);
            FileWriter writer = new FileWriter(logFile, false);
            writer.append(log);
            writer.flush();
            writer.close();
            putFileToMediaStore(ctx, logFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return logPath;
    }
}
