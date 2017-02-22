package com.sodo.util;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class DESCrypto {

	private final static String crypto_Name = "DESede";
	private final static String crypto_Algorithm = "DESede/ECB/NoPadding";

	public static byte[] encrypt3DES(byte[] data, byte[] key) {
		try {
			Cipher c = Cipher.getInstance(crypto_Algorithm);
			c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, crypto_Name));

			byte[] desdata = data;
			if (data.length % 8 != 0) {
				desdata = new byte[(data.length / 8 + 1) * 8];
				System.arraycopy(data, 0, desdata, 0, data.length);
				Arrays.fill(desdata, data.length, desdata.length, (byte) 0x00);
			}
			return c.doFinal(desdata);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public static String encryptBase64(byte[] data, byte[] key) {
		return Base64.encodeBase64String(encrypt3DES(data, key));
	}

	public static String encryptHex(byte[] data, byte[] key) {
		return Hex.encodeHexString(encrypt3DES(data, key));
	}

	public static String encryptHexData(String hexData, String hexKey) {
		try {
			return Hex.encodeHexString(encrypt3DES(
					Hex.decodeHex(hexData.toCharArray()),
					Hex.decodeHex(hexKey.toCharArray())));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public static byte[] decrypt3DES(byte[] data, byte[] key) {
		try {
			Cipher cipher = Cipher.getInstance(crypto_Algorithm);
			cipher.init(Cipher.DECRYPT_MODE,
					new SecretKeySpec(key, crypto_Name));
			return cipher.doFinal(data);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String decryptBase64(byte[] data, byte[] key) {
		return Base64.encodeBase64String(decrypt3DES(data, key));
	}

	public static String decryptHex(byte[] data, byte[] key) {
		return Hex.encodeHexString(decrypt3DES(data, key));
	}

	public static String decryptHexData(String hexData, String hexKey) {
		try {
			return Hex.encodeHexString(decrypt3DES(
					Hex.decodeHex(hexData.toCharArray()),
					Hex.decodeHex(hexKey.toCharArray())));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] genSecretKey() {
		try {
			KeyGenerator kg = KeyGenerator.getInstance(crypto_Name);
			SecureRandom sr = new SecureRandom();
			kg.init(168, sr);
			return kg.generateKey().getEncoded();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public static String genBase64SecretKey() {
		byte[] key = genSecretKey();
		if (key == null)
			return null;

		return Base64.encodeBase64String(key);
	}

	public static String genHexSecretKey() {
		byte[] key = genSecretKey();
		if (key == null)
			return null;

		return Hex.encodeHexString(key);
	}

}
