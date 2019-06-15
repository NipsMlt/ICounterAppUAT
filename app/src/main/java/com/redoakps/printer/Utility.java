package com.redoakps.printer;

public final class Utility {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static String binToStr(byte[] bData) {
        return binToStr(bData, true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static String binToStr(byte[] bData, boolean includeSpace) {
        if (bData == null) {
            return "null";
        }

        final StringBuilder builder = new StringBuilder();
        for (byte b : bData) {
            if (includeSpace) {
                builder.append(String.format("%02X ", b));
            }
            else {
                builder.append(String.format("%02X", b));
            }
        }
        return builder.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static String binToStr(byte[] bData, boolean includeSpace, int maxLength) {
        if (bData == null) {
            return "null";
        }

        final StringBuilder builder = new StringBuilder();
        for (byte b : bData) {
            if (includeSpace) {
                builder.append(String.format("%02X ", b));
            }
            else {
                builder.append(String.format("%02X", b));
            }

            if (builder.length() >= maxLength) {
                builder.append("...");
                break;
            }
        }
        return builder.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static void delay(int timeout) {
        try {
            Thread.sleep(timeout);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
