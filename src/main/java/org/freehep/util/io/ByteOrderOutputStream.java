// Copyright 2001-2005, FreeHEP.
package org.freehep.util.io;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UTFDataFormatException;

/**
 * Class to write bytes and pairs of bytes in both little and big endian order.
 *
 * @author Mark Donszelmann
 * @author Charles Loomis
 * @version $Id: src/main/java/org/freehep/util/io/ByteOrderOutputStream.java effd8b4f3966 2005/11/19 07:52:18 duns $
 */
public class ByteOrderOutputStream extends BitOutputStream implements DataOutput {

    protected boolean little;
    protected int written;

    public ByteOrderOutputStream(OutputStream out) {
        this(out, false);
    }

    public ByteOrderOutputStream(OutputStream out, boolean littleEndian) {
        super(out);
        little = littleEndian;
        written = 0;
    }

    public int size() throws IOException {
        return written;
    }

    public synchronized void write(int b) throws IOException {
        super.write(b);
        written++;
    }

    public void writeBoolean(boolean b) throws IOException {
        if (b) {
            write(1);
        } else {
            write(0);
        }
    }

    public void writeChar(int c) throws IOException {
    	if (little) {
    	    write(c & 0xFF);
    	    write((c >>> 8) & 0xFF);
    	} else {
    	    write((c >>> 8) & 0xFF);
    	    write(c & 0xFF);
    	}
    }

    /**
     * Write a signed byte. */
    public void writeByte(int b)
        throws IOException {

        byteAlign();
    	write(b);
    }

    public void writeByte(byte[] bytes)
        throws IOException {

        byteAlign();
        for (int i=0; i<bytes.length; i++) {
            write(bytes[i]);
        }
    }

    /**
     * Write an unsigned byte. */
    public void writeUnsignedByte(int ub)
        throws IOException {

        byteAlign();
    	write(ub);
    }

    public void writeUnsignedByte(int[] bytes)
        throws IOException {

        byteAlign();
        for (int i=0; i<bytes.length; i++) {
            write(bytes[i]);
        }
    }

    /**
     * Write a signed short. */
    public void writeShort(int s)
        throws IOException {

        byteAlign();
        if (little) {
            write(s & 0xFF);
            write((s >>> 8) & 0xFF);
        } else {
            write((s >>> 8) & 0xFF);
            write(s & 0xFF);
        }
    }

    public void writeShort(short[] shorts)
        throws IOException {

        for (int i=0; i<shorts.length; i++) {
            writeShort(shorts[i]);
        }
    }

    /**
     * Write an unsigned short. */
    public void writeUnsignedShort(int s)
        throws IOException {

        byteAlign();
        if (little) {
            write(s & 0xFF);
            write((s >>> 8) & 0xFF);
        } else {
            write((s >>> 8) & 0xFF);
            write(s & 0xFF);
        }
    }

    public void writeUnsignedShort(int[] shorts)
        throws IOException {

        for (int i=0; i<shorts.length; i++) {
            writeUnsignedShort(shorts[i]);
        }
    }

    /**
     * Write a signed integer. */
    public void writeInt(int i)
        throws IOException {

        if (little) {
            write(i & 0xFF);
            write((i >>> 8) & 0xFF);
            write((i >>> 16) & 0xFF);
            write((i >>> 24) & 0xFF);
        } else {
            write((i >>> 24) & 0xFF);
            write((i >>> 16) & 0xFF);
            write((i >>> 8) & 0xFF);
            write(i & 0xFF);
        }
    }

    public void writeInt(int[] ints)
        throws IOException {

        for (int i=0; i<ints.length; i++) {
            writeInt(ints[i]);
        }
    }

    /**
     * Write an unsigned integer. */
    public void writeUnsignedInt(long i)
        throws IOException {

        if (little) {
            write((int)(i & 0xFF));
            write((int)((i >>> 8) & 0xFF));
            write((int)((i >>> 16) & 0xFF));
            write((int)((i >>> 24) & 0xFF));
        } else {
            write((int)((i >>> 24) & 0xFF));
            write((int)((i >>> 16) & 0xFF));
            write((int)((i >>> 8) & 0xFF));
            write((int)(i & 0xFF));
        }
    }

    public void writeUnsignedInt(long[] ints)
        throws IOException {

        for (int i=0; i<ints.length; i++) {
            writeUnsignedInt(ints[i]);
        }
    }

    public void writeLong(long l) throws IOException {
        if (little) {
            write((int)(l & 0xFF));
            write((int)((l >>> 8) & 0xFF));
            write((int)((l >>> 16) & 0xFF));
            write((int)((l >>> 24) & 0xFF));
            write((int)((l >>> 32) & 0xFF));
            write((int)((l >>> 40) & 0xFF));
            write((int)((l >>> 48) & 0xFF));
            write((int)((l >>> 56) & 0xFF));
        } else {
            write((int)((l >>> 56) & 0xFF));
            write((int)((l >>> 48) & 0xFF));
            write((int)((l >>> 40) & 0xFF));
            write((int)((l >>> 32) & 0xFF));
            write((int)((l >>> 24) & 0xFF));
            write((int)((l >>> 16) & 0xFF));
            write((int)((l >>> 8) & 0xFF));
            write((int)(l & 0xFF));
        }
    }

    public void writeFloat(float f) throws IOException {
	    writeInt(Float.floatToIntBits(f));
    }

    public void writeDouble(double d) throws IOException {
	    writeLong(Double.doubleToLongBits(d));
    }

    public void writeBytes(String s) throws IOException {
        for (int i=0; i<s.length(); i++) {
            writeByte(s.charAt(i));
        }
    }

    public void writeChars(String s) throws IOException {
        for (int i=0; i<s.length(); i++) {
            writeChar(s.charAt(i));
        }
    }

    public void writeString(String s) throws IOException {
        writeUTF(s);
    }

    public void writeUTF(String s) throws IOException {
        writeUTF(s, this);
    }

    public void writeAsciiZString(String s) throws IOException {
        writeBytes(s);
        writeByte(0);
    }

    // should have been in DataOutputStream, but is not visible
    public static void writeUTF(String s, DataOutput dos) throws IOException {
    	int strlen = s.length();
    	int utflen = 0;
     	char[] charr = new char[strlen];
    	int c, count = 0;

    	s.getChars(0, strlen, charr, 0);

    	for (int i = 0; i < strlen; i++) {
    	    c = charr[i];
    	    if ((c >= 0x0001) && (c <= 0x007F)) {
    		    utflen++;
    	    } else if (c > 0x07FF) {
    		    utflen += 3;
    	    } else {
    		    utflen += 2;
    	    }
    	}

    	if (utflen > 65535)
    	    throw new UTFDataFormatException();

    	byte[] bytearr = new byte[utflen+2];
    	bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
    	bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);
    	for (int i = 0; i < strlen; i++) {
    	    c = charr[i];
    	    if ((c >= 0x0001) && (c <= 0x007F)) {
    		    bytearr[count++] = (byte) c;
    	    } else if (c > 0x07FF) {
    		    bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
    		    bytearr[count++] = (byte) (0x80 | ((c >>  6) & 0x3F));
    		    bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
    	    } else {
    		    bytearr[count++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
    		    bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
    	    }
    	}
        dos.write(bytearr);
    }
}