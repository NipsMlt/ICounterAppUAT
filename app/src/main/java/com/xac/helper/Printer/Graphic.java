package com.xac.helper.Printer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Graphic {

    public enum Alignment {NONE, LEFT, CENTER, RIGHT}

    public static final int FONT_UNDERLINE = 0x01;
    public static final int FONT_ITALIC = 0x02;
    public static final int FONT_BOLD = 0x04;
    public static final int FONT_REVERSE = 0x08;
    public static final int FONT_LARGE = 0x10;

    private static Bitmap _bmp = null;
    private Typeface mTypeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);

    private final int SMALL_FONT = 20;
    private final int LARGE_FONT = 30;
    private final float ITALIC_SKEW = -0.25f;

    private final boolean isAntiAlias = true;
    private final boolean isDither = false;

    private int printerWidth = 48;

    public Graphic() {}

    public Graphic(int width) {
        printerWidth = width;
    }

    public Collection<byte[]> printLine(String textLeft, String textCenter, String textRight, int style) {
        Bitmap bitmap = generateBitmap(textLeft, textCenter, textRight, style);
        return printImage(bitmap, Alignment.LEFT);
//        return printRasterImage(bitmap, STYLE.Alignment.LEFT);
    }

    public Collection<byte[]> printRasterImage(Bitmap bitmap, Alignment alignment) {

        byte[] command = bitmapToCommands(bitmap, 0);
        ArrayList<byte[]> cmds = new ArrayList<byte[]>();

        int index = 0;
        while(true) {
            if (index + 2048 < command.length) {
                cmds.add(Arrays.copyOfRange(command, index, index + 2048));
            } else {
                cmds.add(Arrays.copyOfRange(command, index, command.length - index));
                break;
            }
        }
        return cmds;
    }
    public Collection<byte[]> printImage(Bitmap bitmap, Alignment align) {

        if (null == bitmap) {
            return null;
        }

        Collection<byte[]> collection = bitmapToCommands(bitmap, align);
//        Collection<byte[]> collection = printRasterImage(bitmap, align);


        return collection;
    }

    protected Bitmap generateBitmap(String textLeft, String textCenter, String textRight, int style) {

        Paint p = new Paint();
        p.setTypeface(mTypeface);

        // Set paint styles
        p.setTextSize(((style & FONT_LARGE) == FONT_LARGE) ? LARGE_FONT : SMALL_FONT);
        p.setFakeBoldText((style & FONT_BOLD) == FONT_BOLD);
        p.setTextSkewX(((style & FONT_ITALIC) == FONT_ITALIC)? ITALIC_SKEW : 0);
        p.setUnderlineText((style & FONT_UNDERLINE) == FONT_UNDERLINE);
        p.setAntiAlias(isAntiAlias);
        p.setDither(isDither);

        // Set bitmap width
        int width = 48;

        // y
        float baseline = (int) (-p.ascent() + 0.5f); //ascent() is negative

        // Set bitmap height
        int height = (int)(baseline + p.descent() + 0.5f);

        // x for each alignment
        int xLeft = 0;
        int xCenter = width >> 1;
        int xRight = width - 1;

        // Render the bitmap
        if ((null == _bmp) || (height != _bmp.getHeight()) || (width != _bmp.getWidth()))
        {
            if(null != _bmp) {
                Log.d("TextImagePrint", "Recycled Bitmap " + _bmp.getWidth() + " x " + _bmp.getHeight());
                _bmp.recycle();
            }
            _bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Log.d("TextImagePrint", "Created Bitmap " + _bmp.getWidth() + " x " + _bmp.getHeight());
        }

        Canvas canvas = new Canvas(_bmp);

        // Set the background and font colors
        if ((style & FONT_REVERSE) == FONT_REVERSE) {
            p.setColor(Color.BLACK);
            canvas.drawRect(0, 0, width, height, p);
            p.setColor(Color.WHITE);
        }
        else {
            p.setColor(Color.WHITE);
            canvas.drawRect(0, 0, width, height, p);
            p.setColor(Color.BLACK);
        }

        // Paint the text
        if (!textLeft.isEmpty()) {
            p.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(textLeft, xLeft, baseline, p);
        }

        if (!textCenter.isEmpty()) {
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(textCenter, xCenter, baseline, p);
        }

        if (!textRight.isEmpty()) {
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(textRight, xRight, baseline, p);
        }

        return _bmp;
    }

    private Collection<byte[]> bitmapToCommands(Bitmap bmp, Alignment align) {
        if(null == bmp)
            return null;

        ArrayList<byte[]> cmds = null;

        int nMaxCmdLen = 1152;//0x0480: default for TP_48 (48x24)
        if(printerWidth == 72)
            nMaxCmdLen = 1728;//0x06C0: for TP_72 (72x24)

        int height = bmp.getHeight();

        int [] _pixels = new int[bmp.getWidth() * height];

        bmp.getPixels(_pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), height);

        //ARGB pixels to Monochrome bytes
        byte[] raw = _pixelsARGB2Mono(_pixels, bmp.getWidth(), height, printerWidth, align, false);

        if(null != raw)
        {
            cmds = new ArrayList<byte[]>();
            int nb = 0;
            byte[] cmd = null;
            int nCmdLen = 0;

            //split monochrome bytes to print commands
            while(true)
            {
                nb = cmds.size() * nMaxCmdLen;
                if(nb >= raw.length)
                    break;

                int headerLength = 5;
                cmd = null;
                if((raw.length - nb) >= nMaxCmdLen)
                {
                    cmd = newGraphicCommand2(nMaxCmdLen);
                    nCmdLen = nMaxCmdLen;
                    System.arraycopy(raw, nb, cmd, headerLength, nCmdLen);
                }
                //reminder data
                else
                {
                    int r = raw.length % nMaxCmdLen;
                    cmd = newGraphicCommand2(r);
                    nCmdLen = ((r % printerWidth) == 0)? r : r - (r % printerWidth) + printerWidth;
                    System.arraycopy(raw, nb, cmd, headerLength, r);
                }
                cmds.add(cmd);
            }
        }
        else
            return null;

        return cmds;
    }

    private byte[] newGraphicCommand(int len) {
        byte[] cmd = new byte[len + 8];

        cmd[0] = 0x1B;
        cmd[1] = 0x2A;
        cmd[2] = (byte)(len & 0xFF);
        cmd[3] = (byte)(len >> 8);
        cmd[4] = 0x00;
        cmd[5] = 0x00;
        cmd[6] = 0x00;
        cmd[7] = (byte)printerWidth;

        return cmd;
    }

    private byte[] newGraphicCommand2(int len) {
        byte[] cmd = new byte[len + 5];

        cmd[0] = 0x1B;
        cmd[1] = 0x2A;
        cmd[2] = 0x00;
        cmd[3] = (byte)(len & 0xFF);
        cmd[4] = (byte)(len >> 8);

        return cmd;
    }

    private byte[] _pixelsARGB2Mono(int[] pixels, int width, int height, int monoWidth, Alignment align, boolean invert) {
        if(null == pixels)
            return null;

        byte[] _raw = new byte[monoWidth * height];

        //Arrays.fill(raw, (byte)0x0);
        int maxMonoWidth = monoWidth << 3;
        int k = 0;
        byte m = 0;//mono pixel

        if(Alignment.LEFT == align || Alignment.NONE == align)
        {
            for(int j = 0, pl = 0, rl = 0; j < height; j++, pl = pl + width, rl = rl + monoWidth)
            {
                //if(j < 35)Log.d(TAG, "==============j="+j);//DEBUG
                for(int i = 0, p = 0; i < monoWidth; i++)
                {
                    k = i << 3;
                    if(k + 8 <= width)
                        p = 8;
                    else if(k > width)
                        p = 0;
                    else
                        p = width - k;

                    m = 0;
                    //ARGB pixel 2 monochrome bit
                    for(int r = 0, pr = pl + k; r < p; r++)
                    {
                        m = (byte)((m << 1) + _argb2mono(pixels[pr + r], invert));

                        /*
                        //dump partial data for debug
                        if(j < 30 && i < 10)
                            Log.d(TAG, "Argb2Mono: " + String.format("%08X", pixels[pr + r]) + " => " + String.format("%08X", m));
                        //*/
                    }

                    //case: width of image is smaller width of print
                    if(p == 0)
                        m = 0;
                    else if(p < 8)
                        m = (byte)(m << (8 - p));

                    _raw[rl + i] = m;
                }
            }
        }
        else if(Alignment.CENTER == align)
        {
            //image is larger than or equals to paper
            if(width >= maxMonoWidth)
            {
                for(int j = 0, pl = (width - maxMonoWidth) >> 1, rl = 0; j < height; j++, pl = pl + width, rl = rl + monoWidth)
                {
                    for(int i = 0; i < monoWidth; i++)
                    {
                        k = i << 3;

                        m = 0;
                        //ARGB pixel 2 monochrome bit
                        for(int r = 0, pr = pl + k; r < 8; r++)
                        {
                            m = (byte)((m << 1) + _argb2mono(pixels[pr + r], invert));
                        }

                        _raw[rl + i] = m;
                    }
                }
            }
            //image is smaller than paper
            else
            {
                int emptyWidth = (maxMonoWidth - width) >> 1;
                for(int j = 0, pl = 0, rl = 0; j < height; j++, pl = pl + width, rl = rl + monoWidth)
                {
                    for(int i = 0, preByteEmpty = 0, postByteEmpty = 0, ps = 0; i < monoWidth; i++)
                    {
                        k = i << 3;
                        if(k + 8 <= emptyWidth)
                        {
                            preByteEmpty = 8;
                            ps = 0;
                            postByteEmpty = 0;
                        }
                        else if(k <= emptyWidth) //equals to if(k + 8 <= emptyWidth + 8)
                        {
                            preByteEmpty = emptyWidth - k;
                            ps = - preByteEmpty;

                            //image width is small than 8
                            postByteEmpty = 8 - preByteEmpty - width;
                            if(postByteEmpty < 0)
                                postByteEmpty = 0;
                        }
                        else if(k + 8 <= emptyWidth + width)
                        {
                            preByteEmpty = 0;
                            ps = ps + 8;
                            postByteEmpty = 0;
                        }
                        else if(k <= emptyWidth + width) //equals to if(k + 8 <= emptyWidth + width + 8)
                        {
                            preByteEmpty = 0;
                            ps = ps + 8;
                            postByteEmpty = k + 8 - emptyWidth - width;
                        }
                        else
                        {
                            preByteEmpty = 0;
                            //ps = ps + 8;
                            postByteEmpty = 8;
                        }

                        m = 0;
                        //pre-empty
                        if(preByteEmpty == 8)
                            m = 0;
                        else if(preByteEmpty > 0)
                            m = (byte)(m << preByteEmpty);

                        //ARGB pixel 2 monochrome bit
                        for(int r = preByteEmpty, pr = pl + ps; r < 8 - postByteEmpty; r++)
                        {
                            m = (byte)((m << 1) + _argb2mono(pixels[pr + r], invert));
                        }

                        //post-empty
                        if(postByteEmpty == 8)
                            m = 0;
                        else if(postByteEmpty > 0)
                            m = (byte)(m << postByteEmpty);

                        _raw[rl + i] = m;
                    }
                }
            }
        }
        else if(Alignment.RIGHT == align)
        {
            //image is larger than or equals to paper
            if(width >= maxMonoWidth)
            {
                for(int j = 0, pl = width - maxMonoWidth, rl = 0; j < height; j++, pl = pl + width, rl = rl + monoWidth)
                {
                    for(int i = 0; i < monoWidth; i++)
                    {
                        k = i << 3;

                        m = 0;
                        //ARGB pixel 2 monochrome bit
                        for(int r = 0, pr = pl + k; r < 8; r++)
                        {
                            m = (byte)((m << 1) + _argb2mono(pixels[pr + r], invert));
                        }

                        _raw[rl + i] = m;
                    }
                }
            }
            //image is smaller than paper
            else
            {
                for(int j = 0, pl = 0, rl = 0; j < height; j++, pl = pl + width, rl = rl + monoWidth)
                {
                    for(int i = 0, byteEmpty = 0, ps = 0; i < monoWidth; i++)
                    {
                        k = i << 3;
                        if(k + 8 + width <= maxMonoWidth)
                            byteEmpty = 8;
                        else if(k + width <= maxMonoWidth) //equals to if(k + 8 + width <= maxMonoWidth + 8)
                        {
                            byteEmpty = maxMonoWidth - width - k;
                            ps = - byteEmpty;
                        }
                        else
                        {
                            byteEmpty = 0;
                            ps = ps + 8;
                        }

                        m = 0;
                        //fill empty
                        if(byteEmpty == 8)
                            m = 0;
                        else if(byteEmpty > 0)
                            m = (byte)(m << byteEmpty);

                        //fill image
                        for(int r = byteEmpty, pr = pl + ps; r < 8; r++)
                        {
                            m = (byte)((m << 1) + _argb2mono(pixels[pr + r], invert));
                        }

                        _raw[rl + i] = m;
                    }
                }
            }
        }

        return _raw;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private int _argb2mono(int pixel, boolean invert) {
        int mono = 0;
        //
        int a = Color.alpha(pixel);
        int b = Color.blue(pixel);
        int g = Color.green(pixel);
        int r = Color.red(pixel);
        //
        if(
//TODO：check the colour with alpha
//            //absolutely black, it's only suitable for black background and white word
//            pixel == 0x00000000 ||

            //adjust alpha to edge printing. 0(fully transparent) to 255 (completely opaque)
//            (a >= 0x80 && (int)(0.299 * r + 0.587 * g + 0.114 * b) <= 0x80)
                (a >= 0x80 && (299 * r + 587 * g + 114 * b) <= 128000)
                )
        {
            if(invert)
                mono = 0;
            else
                mono = 1;
        }
        else
        {
            if(invert)
                mono = 1;
            else
                mono = 0;
        }

        return mono;
    }


    private byte[] bitmapToCommands(Bitmap bmp, int mode)
    {
        if(null == bmp)
        {
            return null;
        }

        if((mode < 0 || 3 < mode) && (mode < 48 || 51 < mode))
        {
            return null;
        }

        long t = 0L;
        int w = bmp.getWidth() >> 3;
        if(bmp.getWidth() % 8 != 0)
            w++;
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];

        t = System.currentTimeMillis();
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        //ARGB pixels to Monochrome bytes
        t = System.currentTimeMillis();
        byte[] raw = _pixelsARGB2Mono(pixels, bmp.getWidth(), bmp.getHeight(), w, Alignment.NONE, false);

        t = System.currentTimeMillis();
        //
        //max width of image is (256 * 256 + 256) * 8 = 526336 dots, so don't care about if the width is out of bound
        //max height of image is (16 * 256 + 256) = 4352 dots, only mind the height
        int copy_size = raw.length;
        int copy_h = bmp.getHeight();
        if(bmp.getHeight() >= 4352)
        {
            copy_size = bmp.getHeight() * 4352;
            copy_h = 4351;
        }
        byte[] cmd = new byte[copy_size + 8];
        cmd[0] = 0x1D;
        cmd[1] = 0x76;
        cmd[2] = 0x30;
        cmd[3] = (byte)mode;
        cmd[4] = (byte)(w & 0xFF);
        cmd[5] = (byte)(w >> 8);
        cmd[6] = (byte)(copy_h & 0xFF);
        cmd[7] = (byte)(copy_h >> 8);
        System.arraycopy(raw, 0, cmd, 8, copy_size);
        return cmd;
    }
}
