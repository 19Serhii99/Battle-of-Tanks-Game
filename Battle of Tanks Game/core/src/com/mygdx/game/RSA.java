package com.mygdx.game;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {
	private static RSA rsa;

	private PublicKey key;

	private RSA() {

	}

	synchronized public static RSA getInstance() {
		if (rsa == null)
			rsa = new RSA();
		return rsa;
	}

	public void createKey(byte[] b) {
		try {
			final KeyFactory factory = KeyFactory.getInstance("RSA");
			final X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
			key = (RSAPublicKey) factory.generatePublic(spec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] encrypt(String string) throws Exception {
		final Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(string.getBytes("ISO-8859-1"));
	}
}