package com.xac.helper;

import java.nio.charset.Charset;
import java.util.Arrays;


public class VNG {
    /* Control Symbol */
    // Start of Text Symbol to mark the start of the Transaction Message/Response. 
    public static final byte STX = (byte)0x02;

    // End of Text symbol to mark the end of the Transaction Message/Response.
    public static final byte ETX = (byte)0x03;

    // Shift In Symbol to mark the start of the Transaction Message/Response.
    public static final byte SI = (byte)0x0F;

    // Shift Out symbol to mark the end of the Transaction Message/Response.
    public static final byte SO = (byte)0x0E;

    // Acknowledge symbol informs the Message/Response was received correctly
    public static final byte ACK = (byte)0x06;

    // Not Acknowledge symbol if the previous Transaction Message/Response was not received correctly
    public static final byte NAK = (byte)0x15;

    // Field Separator is used to separate the various data segments within the Message/ Response.
    public static final byte FS = (byte)0x1C;

    // Clear Screen Symbol is used to clear LCD
    public static final byte SUB = (byte)0x1A;

    // Record Separator is used to separate the binary data segments within the Message/Response.
    public static final byte RS = (byte)0x1E;

    // End-of-Transmission symbol informs the transaction is complete and terminate the communication
    public static final byte EOT = (byte)0x04;
    /* Control Symbol - End*/


    public static final int COMMAND_NONE = 0x0000;

    //For customize
    public static final int CUSTOMIZE_COMMAND = 0x0200;

    private final int BUFSIZE = 2048;
    public int parseIndex = 0;

    public byte[] buffer = new byte[BUFSIZE];
    public int cmdSize = 0;

    public VNG() {}

    public VNG(String ascCmd) {
        addData(ascCmd);
    }

    public VNG(byte [] data) {
        addData(data);
    }

    public void clear() {
        parseIndex = 0;
        cmdSize = 0;
    }
    public byte[] getCmdBuffer()
    {
        return Arrays.copyOfRange(buffer, 0, cmdSize);
    }
    public boolean addData(byte symbol) {
        if (cmdSize + 1 > BUFSIZE)
            return false;

        buffer[cmdSize++] = symbol;
        return true;
    }
    public boolean addRSLength(int length) {
        if (cmdSize + 3 + length > BUFSIZE)
            return false;

        buffer[cmdSize++ ] = VNG.RS;
        buffer[cmdSize++ ] = (byte)((length >> 8) & 0xFF);
        buffer[cmdSize++ ] = (byte)(length & 0xFF);

        return true;
    }
    public boolean addRSLenData(byte [] data) {
        return addRSLenData(data, data.length);
    }
    public boolean addRSLenData(byte [] data, int length) {
        if (cmdSize + 3 + length > BUFSIZE)
            return false;

        buffer[cmdSize++ ] = VNG.RS;
        buffer[cmdSize++ ] = (byte)((length >> 8) & 0xFF);
        buffer[cmdSize++ ] = (byte)(length & 0xFF);
        return addData(data, length);
    }
    public boolean addData(String ascData) {
        byte[] data = ascData.getBytes(Charset.forName("US-ASCII"));

        if (this.cmdSize + data.length > BUFSIZE)
            return false;

        this.addData(data, data.length);

        return true;
    }
    public boolean addData(byte[] data, int length) {
        if (this.cmdSize + length > BUFSIZE)
            return false;

        System.arraycopy(data, 0, buffer, cmdSize, length);
        cmdSize += length;

        return true;
    }
    public boolean addData(byte[] data) {
        if (data == null)
            return false;
        return addData(data, data.length);
    }

    public boolean tryToParse(String value) {
        String tmp = "";
        for(int i = 0 ; i < value.length() ; i++)
            tmp += (char)buffer[parseIndex++];
        if (tmp.equals(value))
            return true;
        else {
            parseIndex -= value.length();
            return false;
        }
    }

    public String parseStringToSymbol(byte symbol) {
        String tmp = "";
        int i = 0;
        for(i = parseIndex ; i < cmdSize ; i++) {
            if (buffer[i] == symbol)
                break;
            tmp += (char)buffer[i];
        }
        parseIndex = i+1;
        return tmp;
    }

    public String parseString(int length) {
        String tmp = "";
        for(int i = 0 ; i < length ; i++)
            tmp += (char)buffer[parseIndex++];
        return tmp;
    }

    public String parseString() {
        String tmp = "";
        for(int i = 0 ; i < cmdSize ; i++)
        {
            if ( buffer[parseIndex] >= 32 && buffer[parseIndex] <= 126)
                tmp += (char)buffer[parseIndex++];
            else
                return tmp;
        }
        return tmp;
    }
    public byte parseByte() {
        byte b = buffer[parseIndex++];
        return b;
    }
    public byte [] parseBytes(int length) {
        byte [] b = Arrays.copyOfRange(buffer, parseIndex, parseIndex + length);
        parseIndex += length;
        return b;
    }
    public int parseValue(int length) {
        int v = 0;
        for(int i = 0 ; i < length ; i++)
            v = v * 256 + (buffer[parseIndex++] & 0x000000ff);
        return v;
    }

    public byte [] parseRSLenData() {
        if (parseByte() != VNG.RS)
            return null;

        int length = parseValue(2);

        if (parseIndex + length > cmdSize)
            return null;

        return parseBytes(length);
    }

    public int parseRSLen() {
        if (parseByte() != VNG.RS)
            return -1;

        int length = parseValue(2);

        if (parseIndex + length > cmdSize)
            return -1;

        return length;
    }
}
