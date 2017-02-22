package com.sodo.util;


public class UTF8Util {
	private static int DEFAULT_CHAR_SIZE = 64;
	private static char[] DEFAULT_CHAR_ARRAY = new char[DEFAULT_CHAR_SIZE];

	private static byte[] outputByte = new byte[3];
	private static byte[] DEFAULT_BYTE_ARRAY = new byte[DEFAULT_CHAR_SIZE * 3];

	private static int bytes2charsUTF8(byte[] buf, int offset, int length,
			char[] cbuf, boolean bigEndian) {
		int cpos = 0;
		int pos = offset;

		byte b1 = 0;
		byte b2 = 0;
		while (pos < offset + length) {
			if ((buf[pos] & 0x80) == 0) {
				b1 = 0;
				b2 = buf[pos];
				pos++;
			} else if ((buf[pos] & 0xE0) == 192) {
				if ((buf[(pos + 1)] & 0x80) == 128) {
					b1 = (byte) ((buf[pos] & 0x1F) >> 2 & 0xFF);
					b2 = (byte) ((buf[pos] & 0x3) << 6 | buf[(pos + 1)] & 0x3F & 0xFF);
					pos += 2;
				} else {
					b1 = 0;
					b2 = 63;
					pos++;
				}
			} else if ((buf[pos] & 0xF0) == 224) {
				if (((buf[(pos + 1)] & 0x80) == 128)
						&& ((buf[(pos + 2)] & 0x80) == 128)) {
					b1 = (byte) (((buf[pos] & 0xF) << 4 | (buf[(pos + 1)] & 0x3F) >> 2) & 0xFF);
					b2 = (byte) ((buf[(pos + 1)] & 0x3) << 6 | buf[(pos + 2)] & 0x3F & 0xFF);
					pos += 3;
				} else if ((buf[(pos + 1)] & 0x80) == 128) {
					b1 = 0;
					b2 = 63;
					pos += 2;
				} else {
					b1 = 0;
					b2 = 63;
					pos++;
				}
			} else {
				b1 = 0;
				b2 = 0;
				pos++;
				continue;
			}
			if (bigEndian)
				cbuf[cpos] = ((char) (((b1 & 0xFF) << 8 | b2 & 0xFF) & 0xFFFF));
			else {
				cbuf[cpos] = ((char) (((b2 & 0xFF) << 8 | b1 & 0xFF) & 0xFFFF));
			}
			cpos++;
		}
		return cpos;
	}

	private static int bytesUTF8len(byte[] buf, int offset, int length) {
		int len = 0;
		int bufLength = offset + length;
		if (offset >= buf.length)
			return len;
		if (bufLength > buf.length)
			bufLength = buf.length;
		for (int i = offset; i < bufLength; i++) {
			if (((buf[i] & 0x80) == 0) || ((buf[i] & 0xC0) == 192)) {
				len++;
			}
		}
		return len;
	}

	public static String bytes2StringUTF8(byte[] buf, int offset, int length,
			boolean bigEndian) {
		int len = bytesUTF8len(buf, offset, length);
		char[] cbuf = null;
		if (len > DEFAULT_CHAR_SIZE)
			cbuf = new char[len];
		else {
			cbuf = DEFAULT_CHAR_ARRAY;
		}
		len = bytes2charsUTF8(buf, offset, length, cbuf, bigEndian);
		String str = new String(cbuf, 0, len);
		return str;
	}

	public static String bytes2StringUTF8(byte[] buf) {
		return bytes2StringUTF8(buf, 0, buf.length, true);
	}

	public static String bytes2StringUNICODE(byte[] buf, int offset,
			int length, boolean bigEndian) {
		if ((buf != null) && (offset >= 0) && (length >= 2)
				&& (buf.length >= offset + length)) {
			int charsLen = length / 2;
			char[] cbuf = new char[charsLen];
			for (int i = 0; i < charsLen; i++) {
				if (bigEndian)
					cbuf[i] = ((char) (((buf[(i * 2 + offset)] & 0xFF) << 8 | buf[(i * 2 + 1 + offset)] & 0xFF) & 0xFFFF));
				else {
					cbuf[i] = ((char) (((buf[(i * 2 + 1 + offset)] & 0xFF) << 8 | buf[(i * 2 + offset)] & 0xFF) & 0xFFFF));
				}
			}
			String str = new String(cbuf, 0, charsLen);
			cbuf = null;
			return str;
		}

		return null;
	}

	public static String bytes2StringUNICODE(byte[] buf) {
		return bytes2StringUNICODE(buf, 0, buf.length, true);
	}

	public static int string2BytesUTF8Length(String str) {
		return char2ByteUTF8(str, 0, str.length(), null, 0, 0);
	}

	public static byte[] string2BytesUTF8(String str) {
		byte[] bufByte;
		if (str.length() > DEFAULT_CHAR_SIZE)
			bufByte = new byte[str.length() * 3];
		else {
			bufByte = DEFAULT_BYTE_ARRAY;
		}
		int byteLen = char2ByteUTF8(str, 0, str.length(), bufByte, 0,
				bufByte.length);
		byte[] ret = new byte[byteLen];
		System.arraycopy(bufByte, 0, ret, 0, byteLen);
		return ret;
	}

	public static int char2ByteUTF8(String input, int inOff, int inEnd,
			byte[] output, int outOff, int outEnd) {
		int charOff = inOff;
		int byteOff = outOff;

		while (charOff < inEnd) {
			char inputChar = input.charAt(charOff);
			int outputSize;
			int inputSize;
			if (inputChar < '') {
				outputByte[0] = ((byte) inputChar);
				inputSize = 1;
				outputSize = 1;
			} else {
				if (inputChar < 'ࠀ') {
					outputByte[0] = ((byte) (0xC0 | inputChar >> '\006' & 0x1F));
					outputByte[1] = ((byte) (0x80 | inputChar & 0x3F));
					inputSize = 1;
					outputSize = 2;
				} else {
					outputByte[0] = ((byte) (0xE0 | inputChar >> '\f' & 0xF));
					outputByte[1] = ((byte) (0x80 | inputChar >> '\006' & 0x3F));
					outputByte[2] = ((byte) (0x80 | inputChar & 0x3F));
					inputSize = 1;
					outputSize = 3;
				}
			}
			if (output != null) {
				if (byteOff + outputSize > outEnd) {
					throw new RuntimeException();
				}
				for (int i = 0; i < outputSize; i++)
					output[(byteOff++)] = outputByte[i];
			} else {
				byteOff += outputSize;
			}
			charOff += inputSize;
		}
		return byteOff - outOff;
	}
}