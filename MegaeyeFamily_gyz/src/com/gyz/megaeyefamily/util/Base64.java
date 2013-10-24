package com.gyz.megaeyefamily.util;

import java.io.ByteArrayOutputStream;

public class Base64 {
	private static final char[] base64EncodeChars = new char[] { 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '+', '/' };

	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
			60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
			-1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
			-1, -1 };

	private static final char last2byte = (char) Integer
			.parseInt("00000011", 2);
	private static final char last4byte = (char) Integer
			.parseInt("00001111", 2);
	private static final char last6byte = (char) Integer
			.parseInt("00111111", 2);
	private static final char lead6byte = (char) Integer
			.parseInt("11111100", 2);
	private static final char lead4byte = (char) Integer
			.parseInt("11110000", 2);
	private static final char lead2byte = (char) Integer
			.parseInt("11000000", 2);


	private Base64() {
	}

	 /**
	 * 将字节数组编码为字符�?
	 *
	 * @param data
	 * 将要进行编码的数�?
	 */
	
	 public static String encode(byte[] data) {
	 StringBuffer sb = new StringBuffer();
	 int len = data.length;
	 int i = 0;
	 int b1, b2, b3;
	
	 while (i < len) {
	 b1 = data[i++] & 0xff;
	 if (i == len) {
	 sb.append(base64EncodeChars[b1 >>> 2]);
	 sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
	 sb.append("==");
	 break;
	 }
	 b2 = data[i++] & 0xff;
	 if (i == len) {
	 sb.append(base64EncodeChars[b1 >>> 2]);
	 sb.append(base64EncodeChars[((b1 & 0x03) << 4)
	 | ((b2 & 0xf0) >>> 4)]);
	 sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
	 sb.append("=");
	 break;
	 }
	 b3 = data[i++] & 0xff;
	 if (i == len) {
	 sb.append(base64EncodeChars[b1 >>> 2]);
	
	 sb.append(base64EncodeChars[((b1 & 0x03) << 4)
	 | ((b2 & 0xf0) >>> 4)]);
	 sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
	 | ((b3 & 0xc0) >>> 6)]);
	 sb.append(base64EncodeChars[b3 & 0x3f]);
	 }
	 }
	 return sb.toString();
	 }

	/**
	 * Base64 encoding.
	 * 
	 * @param from
	 *            The src data.
	 * @return
	 */
	public static String qq_encode(byte[] from) {
		StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
		int num = 0;
		char currentByte = 0;
		for (int i = 0; i < from.length; i++) {
			num = num % 8;
			while (num < 8) {
				switch (num) {
				case 0:
					currentByte = (char) (from[i] & lead6byte);
					currentByte = (char) (currentByte >>> 2);
					break;
				case 2:
					currentByte = (char) (from[i] & last6byte);
					break;
				case 4:
					currentByte = (char) (from[i] & last4byte);
					currentByte = (char) (currentByte << 2);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead2byte) >>> 6;
					}
					break;
				case 6:
					currentByte = (char) (from[i] & last2byte);
					currentByte = (char) (currentByte << 4);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead4byte) >>> 4;
					}
					break;
				}
				to.append(base64EncodeChars[currentByte]);
				num += 6;
			}
		}
		if (to.length() % 4 != 0) {
			for (int i = 4 - to.length() % 4; i > 0; i--) {
				to.append("=");
			}
		}
		return to.toString();
	}

	/**
	 * 将base64字符串解码为字节数组
	 * 
	 * @param str
	 *            将要解码的base64字符�?
	 */
	public static byte[] decode(String str) {
		if (str != null && str != "") {
			byte[] data = str.getBytes();
			int len = data.length;
			ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
			int i = 0;
			int b1, b2, b3, b4;

			while (i < len) {

				/* b1 */
				do {
					b1 = base64DecodeChars[data[i++]];
				} while (i < len && b1 == -1);
				if (b1 == -1) {
					break;
				}

				/* b2 */
				do {
					b2 = base64DecodeChars[data[i++]];
				} while (i < len && b2 == -1);
				if (b2 == -1) {
					break;
				}
				buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

				/* b3 */
				do {
					b3 = data[i++];
					if (b3 == 61) {
						return buf.toByteArray();
					}
					b3 = base64DecodeChars[b3];
				} while (i < len && b3 == -1);
				if (b3 == -1) {
					break;
				}
				buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

				/* b4 */
				do {
					b4 = data[i++];
					if (b4 == 61) {
						return buf.toByteArray();
					}
					b4 = base64DecodeChars[b4];
				} while (i < len && b4 == -1);
				if (b4 == -1) {
					break;
				}
				buf.write((int) (((b3 & 0x03) << 6) | b4));
			}
			return buf.toByteArray();
		} else {
			return null;
		}

	}
}