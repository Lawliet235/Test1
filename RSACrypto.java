package com.sodo.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class RSACrypto {

	private final static String crypto_Name = "RSA";
	private final static String crypto_Algorithm = "RSA/ECB/PKCS1Padding";
	private final static String sign_Algorithm = "SHA1WithRSA";

	public static byte[] decryptByPrivateKey(byte[] data, byte[] privateKey) {
		try {
			PKCS8EncodedKeySpec pkcs8keySpce = new PKCS8EncodedKeySpec(
					privateKey);
			KeyFactory keyFactory = KeyFactory.getInstance(crypto_Name);
			PrivateKey pk = keyFactory.generatePrivate(pkcs8keySpce);
			Cipher cipher = Cipher.getInstance(crypto_Algorithm);
			cipher.init(Cipher.DECRYPT_MODE, pk);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
			for (int i = 0; i < data.length; i += 256) {
				baos.write(cipher.doFinal(ArrayUtil.subByteArray(data, i, 256)));
			}
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String decryptHexByPrivateKey(byte[] data, byte[] privateKey) {
		return Hex.encodeHexString(decryptByPrivateKey(data, privateKey));
	}

	public static String decryptHexByPrivateKey(String hexData,
			String hexPrivateKey) {
		try {
			return Hex.encodeHexString(decryptByPrivateKey(
					Hex.decodeHex(hexData.toCharArray()),
					Hex.decodeHex(hexPrivateKey.toCharArray())));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
				publicKey);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(crypto_Name);
			PublicKey pk = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(crypto_Algorithm);
			cipher.init(Cipher.DECRYPT_MODE, pk);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
			for (int i = 0; i < data.length; i += 256) {
				baos.write(cipher.doFinal(ArrayUtil.subByteArray(data, i, 256)));
			}
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String decryptHexByPublicKey(byte[] data, byte[] publicKey) {
		return Hex.encodeHexString(decryptByPublicKey(data, publicKey));
	}

	public static String decryptHexByPublicKey(String hexData,
			String hexPublicKey) {
		try {
			return Hex.encodeHexString(decryptByPublicKey(
					Hex.decodeHex(hexData.toCharArray()),
					Hex.decodeHex(hexPublicKey.toCharArray())));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey) {
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
					publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(crypto_Name);
			PublicKey pk = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance(crypto_Algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, pk);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(245);
			for (int i = 0; i < data.length; i += 245) {
				baos.write(cipher.doFinal(ArrayUtil.subByteArray(data, i, 245)));
			}
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String encryptHexByPublicKey(byte[] data, byte[] publicKey) {
		return Hex.encodeHexString(encryptByPublicKey(data, publicKey));

	}

	public static String encryptHexByPublicKey(String hexData,
			String hexPublicKey) {
		try {
			return Hex.encodeHexString(encryptByPublicKey(
					Hex.decodeHex(hexData.toCharArray()),
					Hex.decodeHex(hexPublicKey.toCharArray())));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public static byte[] encryptByPrivateKey(byte[] data, byte[] privateKey) {
		try {
			PKCS8EncodedKeySpec pkcs8keySpce = new PKCS8EncodedKeySpec(
					privateKey);
			KeyFactory keyFactory = KeyFactory.getInstance(crypto_Name);
			PrivateKey pk = keyFactory.generatePrivate(pkcs8keySpce);
			Cipher cipher = Cipher.getInstance(crypto_Algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, pk);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(245);
			for (int i = 0; i < data.length; i += 245) {
				baos.write(cipher.doFinal(ArrayUtil.subByteArray(data, i, 245)));
			}
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String encryptHexByPrivateKey(byte[] data, byte[] privateKey) {
		return Hex.encodeHexString(encryptByPrivateKey(data, privateKey));
	}

	public static String encryptHexByPrivateKey(String hexData,
			String hexPrivateKey) {
		try {
			return Hex.encodeHexString(encryptByPrivateKey(
					Hex.decodeHex(hexData.toCharArray()),
					Hex.decodeHex(hexPrivateKey.toCharArray())));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] signByPrivatekey(byte[] data, byte[] privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyf = KeyFactory.getInstance(crypto_Name);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature
					.getInstance(sign_Algorithm);
			signature.initSign(priKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String signByPrivatekey(String hexData, String hexPrivateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Hex.decodeHex(hexPrivateKey.toCharArray()));
			KeyFactory keyf = KeyFactory.getInstance(crypto_Name);
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			java.security.Signature signature = java.security.Signature
					.getInstance(sign_Algorithm);
			signature.initSign(priKey);
			signature.update(Hex.decodeHex(hexData.toCharArray()));
			byte[] signed = signature.sign();
			return Hex.encodeHexString(signed);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static boolean checkSignByPublickey(byte[] data, byte[] signValue,
			byte[] publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(crypto_Name);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(publicKey));
			java.security.Signature signature = java.security.Signature
					.getInstance(sign_Algorithm);

			signature.initVerify(pubKey);
			signature.update(data);

			boolean bverify = signature.verify(signValue);
			return bverify;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean checkSignByPublickey(String hexData,
			String hexSignValue, String hexPublicKey) {
		try {
			return checkSignByPublickey(Hex.decodeHex(hexData.toCharArray()),
					Hex.decodeHex(hexSignValue.toCharArray()),
					Hex.decodeHex(hexPublicKey.toCharArray()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static byte[] loadKeyFromFile(String filePath) {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(fis));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = buffReader.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}

			return Base64.decodeBase64(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static boolean checkSignByPublickey(String hexData,
			String hexSignValue, String hexModulus, String hexExponent) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			BigInteger modulus = new BigInteger(hexModulus, 16);
			BigInteger exp = new BigInteger(hexExponent, 16);
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exp);
			RSAPublicKey publicKey = (RSAPublicKey) keyFactory
					.generatePublic(publicKeySpec);
			java.security.Signature signature = java.security.Signature
					.getInstance(sign_Algorithm);

			signature.initVerify(publicKey);
			signature.update(Hex.decodeHex(hexData.toCharArray()));

			boolean bverify = signature.verify(Hex.decodeHex(hexSignValue
					.toCharArray()));
			return bverify;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}


}
