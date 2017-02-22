package com.sodo.util;

public class StringUtil {
	public static final byte HEX = 1;
	public static final byte ASCII = 2;
	public static final int LCD_WIDTH = 16;
	public static final String specialSaveChars = "=: \t\r\n\f#!";
	public static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final char[] space8 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ',
			' ' };

	public static char toHexChar(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	public static int getSaveConvertLength(String theString) {
		return saveConvert(theString, null, 0, true, true, true);
	}

	public static int saveConvert(String theString, byte[] dst, int offset) {
		return saveConvert(theString, dst, offset, true, true, false);
	}

	public static int saveConvert(String theString, byte[] dst, int offset,
			boolean escapeSpace, boolean lengthFlag, boolean getLengthFlag) {
		int bytesLength = lengthFlag ? 1 : 0;
		if ((!getLengthFlag)
				&& ((dst == null) || (dst.length < offset + bytesLength)
						|| (dst.length < 1) || (offset < 0)))
			return -1;
		if (theString == null)
			theString = "";
		int length = theString.length();
		try {
			if (!getLengthFlag) {
				length = UTF8Util.char2ByteUTF8(theString, 0,
						theString.length(), dst, offset + bytesLength,
						dst.length);
				if (lengthFlag) {
					if (bytesLength == 2)
						System.arraycopy(
								NumberUtil.shortToByte2((short) length), 0,
								dst, offset, 2);
					else
						dst[offset] = ((byte) (length & 0xFF));
				}
				length += bytesLength;
			} else {
				length = UTF8Util.string2BytesUTF8Length(theString);
				length += bytesLength;
			}
		} catch (Exception e) {
			return -1;
		}

		return length;
	}

	public static String loadConvert(byte[] s, int offset, byte bytesLength)
			throws IllegalArgumentException {
		int bytesLen = bytesLength;
		if ((bytesLen < 0) || (bytesLen > 2))
			bytesLen = 0;
		if ((s == null) || (offset + bytesLen > s.length)) {
			throw new IllegalArgumentException("invalid byte arrary");
		}
		int len = s.length - offset;

		if (bytesLen == 1) {
			len = s[offset] & 0xFF;
		} else if (bytesLen == 2) {
			len = NumberUtil.byte2ToShort(s, offset);
		}
		offset += bytesLen;
		String str = UTF8Util.bytes2StringUTF8(s, offset, len, true);
		return str == null ? "" : str;
	}

	public static String returnString(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	public static String returnString(byte[] abyteInputBuffer) {
		if (abyteInputBuffer == null) {
			return "";
		}

		return toString(abyteInputBuffer);
	}

	public static String toHexString(byte[] abyteInputBuffer,
			int nInputBufferOffset, int nInputBufferLength, boolean bSpaceFlag,
			byte byteTishiFlag) {
		if ((abyteInputBuffer == null)
				|| (nInputBufferOffset < 0)
				|| (nInputBufferOffset > abyteInputBuffer.length)
				|| (nInputBufferLength < 0)
				|| (nInputBufferLength > abyteInputBuffer.length)
				|| (nInputBufferOffset + nInputBufferLength > abyteInputBuffer.length)) {
			return null;
		}

		StringBuffer strTemp = new StringBuffer();
		strTemp.setLength(0);

		strTemp.append(toHexString(abyteInputBuffer, nInputBufferOffset,
				nInputBufferLength, bSpaceFlag));

		if (byteTishiFlag == 1) {
			strTemp.insert(
					0,
					"[HEX   "
							+ fillZeroLeft(returnString(nInputBufferLength), 3)
							+ "B]");
		} else {
			strTemp.insert(
					0,
					"[ASCII "
							+ fillZeroLeft(
									returnString(nInputBufferLength * 2), 3)
							+ "B]");
		}

		return strTemp.toString();
	}

	public static String fillZeroLeft(String strInputBuffer,
			int nStrInputBufferLength) {
		return fillString(strInputBuffer, nStrInputBufferLength, '0', true);
	}

	public static String returnString(int intValue) {
		if (intValue < 0) {
			return "";
		}
		return String.valueOf(intValue);
	}

//	public static String returnString(short shortValue) {
//		return returnString(shortValue);
//	}
//
//	public static String returnString(byte byteValue) {
//		return returnString(byteValue);
//	}

	public static String trimSpace(String oldString) {
		if (oldString == null)
			return null;
		if (oldString.length() == 0) {
			return "";
		}
		StringBuffer sbuf = new StringBuffer();
		int oldLen = oldString.length();
		for (int i = 0; i < oldLen; i++) {
			if (' ' != oldString.charAt(i))
				sbuf.append(oldString.charAt(i));
		}
		String returnString = sbuf.toString();
		sbuf = null;
		return returnString;
	}

	public static String toString(byte[] buffer) {
		if (buffer == null) {
			return null;
		}
		return new String(buffer);
	}

	public static String[] buffer2Message(String bufferString, int width,
			int height) {
		int i = 0;
		int buffLen;
		if (bufferString == null)
			buffLen = 0;
		else
			buffLen = bufferString.length();
		int w;
		int h;
		if ((height < 1) && (width > 0)) {

			if (buffLen % width == 0)
				h = buffLen / width;
			else
				h = buffLen / width + 1;
			w = width;
		} else {
			if ((height > 0) && (width < 1)) {
				if (buffLen % height == 0)
					w = buffLen / height;
				else
					w = buffLen / height + 1;
				h = height;
			} else {

				if ((height > 0) && (width > 0)) {
					h = height;
					w = width;
				} else {
					return null;
				}
			}
		}

		String[] buff = new String[h];

		for (i = 0; i < h; i++) {
			if (w * (i + 1) < buffLen) {
				buff[i] = bufferString.substring(w * i, w * (i + 1));
			} else if ((w * (i + 1) >= buffLen) && (w * i < buffLen))
				buff[i] = bufferString.substring(w * i, buffLen);
			else {
				buff[i] = "";
			}
		}
		return buff;
	}

	public static String[] buffer2Message(String bufferString) {
		return buffer2Message(bufferString, 16, 3);
	}

	public static String fillString(String formatString, int length,
			char fillChar, boolean leftFillFlag) {
		if (formatString == null) {
			formatString = "";
		}
		int strLen = formatString.length();
		if (strLen >= length) {
			if (leftFillFlag) {
				return formatString.substring(strLen - length, strLen);
			}
			return formatString.substring(0, length);
		}

		StringBuffer sbuf = new StringBuffer();
		int fillLen = length - formatString.length();
		for (int i = 0; i < fillLen; i++) {
			sbuf.append(fillChar);
		}

		if (leftFillFlag) {
			sbuf.append(formatString);
		} else {
			sbuf.insert(0, formatString);
		}
		String returnString = sbuf.toString();
		sbuf = null;
		return returnString;
	}

	public static String fillSpace(String formatString, int length) {
		return fillString(formatString, length, ' ', false);
	}

	public static String formatLine(String formatString, boolean leftFillFlag) {
		return fillString(formatString, 16, ' ', leftFillFlag);
	}

	public static String fillShowSpace(String formatString) {
		if (formatString == null) {
			return "";
		}
		if (formatString.length() <= 16) {
			int len = 8 - formatString.length() / 2;
			StringBuffer sbuf = new StringBuffer();
			sbuf.append(space8, 0, len);
			sbuf.append(formatString);
			sbuf.append(space8, 0, len);
			sbuf.setLength(16);

			String returnString = sbuf.toString();
			sbuf = null;
			return returnString;
		}

		return formatString.substring(0, 16);
	}

	public static String fillZero(String formatString, int length) {
		return fillString(formatString, length, '0', true);
	}

	public static byte[] hexStringToBytes(String s) {
		if (s == null) {
			return null;
		}
		return hexStringToBytes(s, 0, s.length());
	}

	public static byte[] hexStringToBytes(String hexString, int offset,
			int count) {
		if ((hexString == null) || (offset < 0) || (count < 2)
				|| (offset + count > hexString.length())) {
			return null;
		}
		byte[] buffer = new byte[count >> 1];
		int stringLength = offset + count;
		int byteIndex = 0;
		for (int i = offset; i < stringLength; i++) {
			char ch = hexString.charAt(i);
			if (ch != ' ') {
				byte hex = isHexChar(ch);
				if (hex < 0)
					return null;
				int shift = byteIndex % 2 == 0 ? 4 : 0;
				int tmp97_96 = (byteIndex >> 1);
				byte[] tmp97_92 = buffer;
				tmp97_92[tmp97_96] = ((byte) (tmp97_92[tmp97_96] | hex << shift));
				byteIndex++;
			}
		}
		byteIndex >>= 1;
		if (byteIndex > 0) {
			if (byteIndex < buffer.length) {
				byte[] newBuff = new byte[byteIndex];
				System.arraycopy(buffer, 0, newBuff, 0, byteIndex);
				buffer = null;
				return newBuff;
			}
		} else
			buffer = null;

		return buffer;
	}

	private static void appendHex(StringBuffer stringbuffer, byte byte0) {
		stringbuffer.append(toHexChar(byte0 >> 4));
		stringbuffer.append(toHexChar(byte0));
	}

	public static String toHexString(byte[] abyte0, int beginIndex,
			int endIndex, boolean spaceFlag) {
		if (abyte0 == null)
			return null;
		if (abyte0.length == 0)
			return "";
		StringBuffer sbuf = new StringBuffer();
		appendHex(sbuf, abyte0[beginIndex]);
		for (int i = beginIndex + 1; i < endIndex; i++) {
			if (spaceFlag)
				sbuf.append(' ');
			appendHex(sbuf, abyte0[i]);
		}
		String returnString = sbuf.toString();
		sbuf = null;
		return returnString.replace(" ", "");
	}

	public static String toHexString(byte[] abyte0, int beginIndex, int endIndex) {
		if (abyte0 == null)
			return null;
		return toHexString(abyte0, beginIndex, endIndex, false);
	}

	public static String toHexString(byte[] abyte0, boolean spaceFlag) {
		if (abyte0 == null)
			return null;
		return toHexString(abyte0, 0, abyte0.length, spaceFlag);
	}

	public static String toHexString(byte[] abyte0) {
		if (abyte0 == null)
			return null;
		return toHexString(abyte0, 0, abyte0.length, true);
	}

	public static String toHexString(char achar0) {
		return toHexString((byte) achar0);
	}

	public static String toHexString(byte abyte0) {
		StringBuffer sbuf = new StringBuffer();
		appendHex(sbuf, abyte0);

		String returnString = sbuf.toString();
		sbuf = null;
		return returnString;
	}

	public static byte isHexChar(char ch) {
		if (('a' <= ch) && (ch <= 'f'))
			return (byte) (ch - 'a' + 10);
		if (('A' <= ch) && (ch <= 'F'))
			return (byte) (ch - 'A' + 10);
		if (('0' <= ch) && (ch <= '9')) {
			return (byte) (ch - '0');
		}
		return -1;
	}

	public static boolean isHexChar(String hexString, boolean checkSpaceFlag) {
		if ((hexString == null) || (hexString.length() == 0)) {
			return false;
		}
		int hexLen = hexString.length();
		int hexCharCount = 0;

		for (int i = 0; i < hexLen; i++) {
			char ch = hexString.charAt(i);
			if (ch == ' ') {
				if (checkSpaceFlag)
					return false;
			} else {
				if (isHexChar(ch) < 0) {
					return false;
				}
				hexCharCount++;
			}
		}

		if (hexCharCount % 2 != 0) {
			return false;
		}
		return true;
	}

	public static boolean isHexChar(String hexString) {
		return isHexChar(hexString, true);
	}

	public static boolean isLetterNumeric(String s) {
		int i = 0;
		int len = s.length();
		while ((i < len)
				&& ((Character.isLowerCase(s.charAt(i)))
						|| (Character.isUpperCase(s.charAt(i))) || (Character
							.isDigit(s.charAt(i))))) {
			i++;
		}
		return i >= len;
	}

	public static String toUnicodeStr(byte[] tmpBuf, int offset, int len) {
		int unicodeLen;
		if (len % 2 == 0) {
			unicodeLen = len / 2;
		} else {
			unicodeLen = 1 + len / 2;
		}

		StringBuffer unicodeBuf = new StringBuffer(unicodeLen);
		char tmpChar = '0';
		for (int idx = 0; idx < len; idx++) {
			if (idx % 2 == 0) {
				tmpChar = (char) (tmpBuf[idx] & 0xFF);
				if (idx >= len - 1) {
					unicodeBuf.append(tmpChar);
				}
			} else {
				tmpChar = (char) (tmpChar << '\b' | tmpBuf[idx] & 0xFF);
				unicodeBuf.append(tmpChar);
			}
		}
		return unicodeBuf.toString();
	}

	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}
}