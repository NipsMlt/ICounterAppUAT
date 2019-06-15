package com.xac.helper.Printer;

/**
 * Created by simon_chen on 2017/12/5.
 */

public class ESC {

    //region Command Definition

    public static final byte NULL = 0x0;
    public static final byte ESC = 0x1B;
    public static final byte STX = 0x70;
    public static final byte SRX = 0x71;
    public static final byte ERR = 0x72;
    public static final byte CR = 0x0D;
    public static final byte LF = 0x0A;
    public static final byte HT = 0x09;
    public static final byte SO = 0x0E;
    public static final byte DC1 = 0x11;
    public static final byte DC2 = 0x12;
    public static final byte DC3 = 0x13;
    public static final byte DC4 = 0x14;
    public static final byte GS = 0x1D;
    public static final byte CAN = 0x18;

    public static final byte[] CMD_RESET = { (byte)ESC, (byte)'@' };
    public static final byte[] CMD_FEED_PAPER = { (byte)ESC, (byte)'J' };
    public static final byte[] CMD_CANCEL = { (byte)GS, (byte)CAN };
    public static final byte[] CMD_GET_VERSION = { (byte)GS, (byte)'I' };
    public static final byte[] CMD_SELECT_FOND = { (byte)ESC, (byte)'M' };
    public static final byte[] CMD_DOUBLE_WIDTH = { (byte)ESC, (byte)'W' };
    public static final byte[] CMD_DOUBLE_HEIGHT = { (byte)ESC, (byte)'w' };
    public static final byte[] CMD_BOLD_ON = { ESC, (byte)'E' };
    public static final byte[] CMD_BOLD_OFF = { ESC, (byte)'F' };
    public static final byte[] CMD_ITALICS_ON = { ESC, (byte)'4' };
    public static final byte[] CMD_ITALICS_OFF = { ESC, (byte)'5' };
    public static final byte[] CMD_UNDERLINE = { ESC, (byte)'-' };

    public static final byte[] CMD_MARGIN_RIGHT = { ESC, (byte)'Q' };
    public static final byte[] CMD_MARGIN_LEFT = { ESC, (byte)'l' };
    public static final byte[] CMD_CUTTER = { GS, (byte)'o' };
    public static final byte[] CMD_GET_STATUS = { GS, (byte)'r' };

    public static final byte[] CMD_AUTO_STATUS_BACK = { GS, (byte)'a' };

    public static final byte[] CMD_CONTROL_REBOOT = { DC1, (byte)'r' };
    public static final byte[] CMD_CONTROL_DOWNLOAD_MODE = { DC1, (byte)'d' };
    public static final byte[] CMD_CONTROL_WRITE_CONFIG = { DC2, (byte)'w' };
    public static final byte[] CMD_CONTROL_READ_CONFIG = { DC2, (byte)'r' };

    public static final byte[] CMD_IMAGE_START_POS = { GS, (byte)'v', (byte)'1' };

    public static final byte[] CMD_BARDCODE = { ESC, (byte)'B' };

    public static final byte[] CMD_QR_CODE_MODEL = { ESC, (byte)'y', (byte)'S', (byte)'0' };
    public static final byte[] CMD_QR_CODE_ERR_CORRECTION_LEVEL = { ESC, (byte)'y', (byte)'S', (byte)'1' };
    public static final byte[] CMD_QR_CODE_CELL_SIZE = { ESC, (byte)'y', (byte)'S', (byte)'2' };
    public static final byte[] CMD_QR_CODE_START_POS = { ESC, (byte)'y', (byte)'S', (byte)'3' };
    public static final byte[] CMD_QR_CODE_DATA = { ESC, (byte)'y', (byte)'D' };
    public static final byte[] CMD_QR_CODE_PRINT = { ESC, (byte)'y', (byte)'P' };


    // Barcode / QR Code
    public static final String[] BARDCODE_TYPE = { "00 : EAN-13", "01 : EAN-8", "02 : Interleaved 2 of 5", "03 : Code 39", "04 : Code 128" };
    public static final String[] QR_CODE_ERR_CORRECTION_LEVEL = { "00 : L (7%)", "01 : M (15%)", "02 : Q (25%)", "03 : H (30%)" };
    public static final String[] QR_CODE_TYPE = { "00 : number", "01 : english character", "02 : binary", "03 : kanji (Shift JIS)" };

    //endregion
}
