package com.sodo.util;

public class NumberUtil {
	static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z' };

	static final char[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0',
			'0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '2',
			'2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3',
			'3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4',
			'4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
			'6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7',
			'7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8', '8',
			'8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9',
			'9' };

	static final char[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9' };

	public static final byte[] BCD_Digit = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 32, 33, 34, 35, 36, 37, 38, 39,
			40, 41, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 64, 65, 66, 67, 68,
			69, 70, 71, 72, 73, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 96, 97,
			98, 99, 100, 101, 102, 103, 104, 105, 112, 113, 114, 115, 116, 117,
			118, 119, 120, 121, -128, -127, -126, -125, -124, -123, -122, -121,
			-120, -119, -112, -111, -110, -109, -108, -107, -106, -105, -104,
			-103 };

	public static int getIntegerLength(int i) {
		return IntegerToBytes(i, null, 0, true, true, true);
	}

	public static byte[] intToBcd(int number, int length) {
		byte[] buf = new byte[length];
		buf = StringUtil.hexStringToBytes(StringUtil.fillString(
				String.valueOf(number), length * 2, '0', true));

		return buf;
	}

	public static int intToBcd(int number, int length, byte[] dst, int offset) {
		if ((dst == null) || (dst.length < offset + length) || (number < 0)) {
			return -1;
		}
		if ((number / pow(100, length)) > 0) {
			return -1;
		}

		int value = 0;
		for (int i = length - 1; i >= 0; i--) {
			if (number > 0) {
				value = number % 10;
				number = number / 10;
				dst[offset + i] = (byte) (value + ((number % 10) << 4));
				number = number / 10;
			} else {
				dst[offset + i] = 0x00;
			}
		}
		return length;
	}

	private static int pow(int number, int length) {
		// TODO Auto-generated method stub
		int rt = 1;
		for (int i = 0; i < length; i++) {
			rt = rt * number;
		}
		return rt;
	}

	public static byte[] byteToBcd(byte number, int length) {
		byte[] buf = new byte[length];
		buf = StringUtil.hexStringToBytes(StringUtil.fillString(
				String.valueOf(number), length * 2, '0', true));

		return buf;
	}

	public static int IntegerToBytes(int i, byte[] dst, int offset) {
		return IntegerToBytes(i, dst, offset, true, true, false);
	}

	public static int IntegerToBytes(int i, byte[] dst, int offset,
			boolean minusFlag, boolean lengthFlag, boolean getLengthFlag) {
		if ((!getLengthFlag)
				&& ((dst == null) || (dst.length < offset) || (dst.length < 1) || (offset < 0))) {
			return -1;
		}

		int charPos = 12;
		int length;
		byte[] buf;
		if (i == -2147483648) {
			if (minusFlag)
				buf = "-2147483648".getBytes();
			else {
				buf = "2147483648".getBytes();
			}
			length = buf.length;
			charPos = 0;
		} else {
			buf = new byte[12];
			char sign = '\000';

			if (i < 0) {
				sign = '-';
				i = -i;
			}

			while (i >= 65536) {
				int q = i / 100;

				int r = i - ((q << 6) + (q << 5) + (q << 2));
				i = q;
				buf[(--charPos)] = ((byte) DigitOnes[r]);
				buf[(--charPos)] = ((byte) DigitTens[r]);
			}

			do {
				int q = i * 52429 >>> 19;
				int r = i - ((q << 3) + (q << 1));
				buf[(--charPos)] = ((byte) digits[r]);
				i = q;
			} while (i != 0);

			if ((sign != 0) && (minusFlag)) {
				buf[(--charPos)] = ((byte) sign);
			}
			length = 12 - charPos;
		}

		if (!getLengthFlag) {
			if (dst != null)
				if (dst.length >= offset + length + (lengthFlag ? 1 : 0)) {
					if (lengthFlag) {
						dst[offset] = ((byte) length);
						offset++;
					}
					System.arraycopy(buf, charPos, dst, offset, length);
					length = offset + length;
				}
			length = -1;
		} else if (lengthFlag) {
			length++;
		}

		return length;
	}

	public static int parseInt(byte[] s, int offset, int radix,
			boolean lengthFlag) throws NumberFormatException {
		if (s == null) {
			throw new NumberFormatException("null");
		}

		if (radix < 2) {
			throw new NumberFormatException("radix " + radix
					+ " less than Character.MIN_RADIX");
		}

		if (radix > 36) {
			throw new NumberFormatException("radix " + radix
					+ " greater than Character.MAX_RADIX");
		}

		int result = 0;
		boolean negative = false;
		int i = 0;
		int max = s.length - offset;

		if (lengthFlag) {
			max = s[offset];
			offset++;
		}

		if (max > 0) {
			int limit;
			if (s[(offset + 0)] == 45) {
				negative = true;
				limit = -2147483648;
				i++;
			} else {
				limit = -2147483647;
			}
			int multmin = limit / radix;
			if (i < max) {
				int digit = Character.digit((char) s[(offset + i++)], radix);
				if (digit < 0) {
					throw new NumberFormatException(
							"Byte array contains non-digit");
				}

				result = -digit;
			}

			while (i < max) {
				int digit = Character.digit((char) s[(offset + i++)], radix);
				if (digit < 0) {
					throw new NumberFormatException(
							"Byte array contains non-digit");
				}
				if (result < multmin) {
					throw new NumberFormatException(
							"Byte array contains non-digit");
				}
				result *= radix;
				if (result < limit + digit) {
					throw new NumberFormatException(
							"Byte array contains non-digit");
				}
				result -= digit;
			}
		} else {
			throw new NumberFormatException("Byte array contains non-digit");
		}
//		int multmin;
//		int limit;
		if (negative) {
			if (i > 1) {
				return result;
			}

			throw new NumberFormatException("Byte array contains non-digit");
		}

		return -result;
	}

	public static int parseIntWithLength(byte[] s, int offset)
			throws NumberFormatException {
		return parseInt(s, offset, 10, true);
	}

	public static int parseInt(byte[] s, int offset)
			throws NumberFormatException {
		return parseInt(s, offset, 10, false);
	}

	public static int numberToBytes(long number, byte[] buffer, int offset,
			int length, boolean bigEndian) {
		long tmp_num = number;
		if ((buffer == null) || (offset < 0) || (length < 1)
				|| (buffer.length < offset + length))
			return -1;
		length = offset + length;

		if (bigEndian) {
			for (int i = offset; i < length; i++) {
				buffer[i] = ((byte) (int) (tmp_num & 0xFF));
				tmp_num >>= 8;
			}
		} else {
			for (int i = length - 1; i >= offset; i--) {
				buffer[i] = ((byte) (int) (tmp_num & 0xFF));
				tmp_num >>= 8;
			}
		}
		return offset;
	}

	public static byte[] longToByte8(long number) {
		byte[] buffer = new byte[8];
		numberToBytes(number, buffer, 0, buffer.length, false);
		return buffer;
	}

	public static byte[] intToByte4(int number) {
		byte[] buffer = new byte[4];
		numberToBytes(number, buffer, 0, buffer.length, false);
		return buffer;
	}

	public static byte[] shortToByte2(short number) {
		byte[] buffer = new byte[2];
		numberToBytes(number, buffer, 0, buffer.length, false);
		return buffer;
	}

	public static long bytesToNumber(byte[] buffer, int offset, int length,
			boolean bigEndian) {
		if ((buffer == null) || (offset < 0) || (length < 1))
			throw new NullPointerException("invalid byte array ");
		if (buffer.length < offset + length) {
			throw new IndexOutOfBoundsException("invalid len: " + buffer.length);
		}
		length = offset + length;
		long number;
		if (bigEndian) {
			int i = length - 1;
			number = buffer[i] & 0xFF;
			i--;
			for (; i >= offset; i--) {
				number <<= 8;
				number |= buffer[i] & 0xFF;
			}
		} else {
			int i = offset;
			number = buffer[i] & 0xFF;
			i++;
			for (; i < length; i++) {
				number <<= 8;
				number |= buffer[i] & 0xFF;
			}
		}
		return number;
	}

	public static long byte8ToLong(byte[] buffer, int offset) {
		return bytesToNumber(buffer, offset, 8, false);
	}

	public static int byte4ToInt(byte[] buffer, int offset) {
		return (int) bytesToNumber(buffer, offset, 4, false);
	}

	public static int byte4ToIntBigEndian(byte[] buffer, int offset) {
		return (int) bytesToNumber(buffer, offset, 4, true);
	}

	public static short byte2ToShort(byte[] buffer, int offset) {
		return (short) (int) bytesToNumber(buffer, offset, 2, false);
	}

	public static short byte2ToShort(byte[] buffer) {
		return (short) (int) bytesToNumber(buffer, 0, 2, false);
	}

	public static int LongToString(long number, byte[] buf, int offset,
			int length, boolean bcdFlag) {
		if ((buf == null) || (offset < 0) || (length < 1)
				|| (buf.length < offset + length))
			return -1;
		length = offset + length;
		int charPos = length - 1;
		int numberLength = charPos;
		boolean numberLengthFlag = true;
		int index = 0;
		int radix = bcdFlag ? 100 : 10;

		if (number > 0L) {
			number = -number;
		}
		for (; charPos >= offset; charPos--) {
			if (0L != number) {
				index = (int) -(number % radix);
				number /= radix;
			} else {
				index = 0;
			}
			if ((numberLengthFlag) && (0L == number)) {
				numberLengthFlag = !numberLengthFlag;
				numberLength = length - charPos;
			}
			if (bcdFlag)
				buf[charPos] = BCD_Digit[index];
			else {
				buf[charPos] = ((byte) digits[index]);
			}
		}

		return numberLength;
	}

	public static byte[] LongToString(long number, boolean bcdFlag,
			int bufferSize) {
		int size = bufferSize > 0 ? bufferSize : 20;
		byte[] buffer = new byte[size];
		int length = LongToString(number, buffer, 0, size, bcdFlag);
		if (bufferSize > 0)
			return buffer;
		byte[] buf = new byte[length];
		System.arraycopy(buffer, size - length, buf, 0, length);
		buffer = null;
		return buf;
	}

	public static int LongToAscii(long number, byte[] buf, int offset,
			int length) {
		return LongToString(number, buf, offset, length, false);
	}

	public static byte[] LongToAscii(long number, int bufferSize) {
		return LongToString(number, false, bufferSize);
	}

	public static byte[] LongToAscii(long number) {
		return LongToString(number, false, -1);
	}

	public static int LongToBCD(long number, byte[] buf, int offset, int length) {
		return LongToString(number, buf, offset, length, true);
	}

	public static int LongToBCD(long number, byte[] buf) {
		if (buf == null)
			return -1;
		return LongToString(number, buf, 0, buf.length, true);
	}

	public static byte[] LongToBCD(long number) {
		return LongToString(number, true, -1);
	}

	public static int BCDToInt(byte[] bcdByte) {
		if (bcdByte == null)
			return -2147483648;
		return (int) BCDToLong(bcdByte, 0, bcdByte.length);
	}

	public static long BCDToLong(byte[] bcdByte) {
		if (bcdByte == null)
			return -2147483648L;
		return BCDToLong(bcdByte, 0, bcdByte.length);
	}

	public static long BCDToLong(byte[] buf, int offset, int length) {
		if ((buf == null) || (offset < 0))
			return -1L;
		if ((length < 0) || (buf.length < offset + length))
			length = buf.length - offset;
		int charPos = offset + length - 1;
		long result = 0L;
		long j = 1L;

		if (length > 10)
			length = 10;
		for (int i = 0; i < length; i++) {
			int high = buf[charPos] >> 4 & 0xF;
			int low = buf[charPos] & 0xF;
			if ((high > 9) || (low > 9))
				return -2L;
			high = high * 10 + low;
			if ((i == 9)
					&& ((high > 9) || ((high == 9) && (result > 223372036854775807L))))
				return -3L;
			result += high * j;

			j = (j << 6) + (j << 5) + (j << 2);
			charPos--;
		}
		return result;
	}
}