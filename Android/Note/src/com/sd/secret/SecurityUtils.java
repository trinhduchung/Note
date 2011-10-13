package com.sd.secret;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.util.Log;

/**
 * Helper class to manage cipher keys and encrypting and decrypting data.
 *  
 * @author Nguyen Manh Cuong
 */
public class SecurityUtils {
	/** Tag for logging purposes. */
	public static final String LOG_TAG = "Secrets";

	private static final String OLD_KEY_FACTORY = "PBEWithMD5AndDES";
	private static final String KEY_FACTORY = "PBEWITHSHA-256AND256BITAES-CBC-BC";

	private static final int OLD_KEY_ITERATION_COUNT = 10;
	private static final int KEY_ITERATION_COUNT = 100;

	private static final byte[] salt = {
		(byte)0xA4, (byte)0x0B, (byte)0xC8, (byte)0x34,
		(byte)0xD6, (byte)0x95, (byte)0xF3, (byte)0x13
	};

	private static Cipher encryptCipher;
	private static Cipher decryptCipher;
	private static Cipher oldDecryptCipher;

	/**
	 * Get the cipher used to encrypt data using the password given to the
	 * createCiphers() function.
	 */
	public static Cipher getEncryptionCipher() {
		return encryptCipher;
	}
  
	/**
	 * Get the cipher used to decrypt data using the password given to the
	 * createCiphers() function.
	 */
	public static Cipher getDecryptionCipher() {
		return decryptCipher;
	}
  
	/**
	 * Get the old cipher used to decrypt data using the password given to the
	 * createCiphers() function.
	 */
	public static Cipher getOldDecryptionCipher() {
		return oldDecryptCipher;
	}
  
	/**
	 * Create a pair of encryption and decryption ciphers based on the given
	 * password string.  The string is not stored internally.  This function
	 * needs to be called before calling getEncryptionCipher() or
	 * getDecryptionCipher().
	 * 
	 * @param password String to use for creating the ciphers.
	 * @return True if the ciphers were successfully created.
	 */
	public static boolean createCiphers(String password) {
		boolean succeeded = false;
    
		try {
			encryptCipher = createCipher(password,
                                KEY_FACTORY,
                                KEY_ITERATION_COUNT,
                                Cipher.ENCRYPT_MODE);
			decryptCipher = createCipher(password,
                                KEY_FACTORY,
                                KEY_ITERATION_COUNT,
                                Cipher.DECRYPT_MODE);
  
			// cases the old cipher will never be needed.  However, this would mean
			// having to store the password in memory for longer.
			oldDecryptCipher = createCipher(password,
                                    OLD_KEY_FACTORY,
                                    OLD_KEY_ITERATION_COUNT,
                                    Cipher.DECRYPT_MODE);
			succeeded = true;
		} 
		catch (Exception ex) {
			Log.d(LOG_TAG, "createCiphers", ex);
		}

		return succeeded;
	}

	/**
	 * Create a cipher with the given password to either encrypt or decrypt
	 * the secrets file.
	 * 
	 * @param password String to use for creating the ciphers.
	 * @param factory Secret key factory name.
	 * @param count Iteration count. 
	 * @param mode Either Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE. 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 */
	private static Cipher createCipher(String password, String factory, int count, int mode)
		throws NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {

		PBEKeySpec keyspec = new PBEKeySpec(password.toCharArray(),
		                                    salt,
		                                    count,
		                                    32);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(factory);
		SecretKey key = skf.generateSecret(keyspec);
		AlgorithmParameterSpec aps = new PBEParameterSpec(salt, count);
		Cipher cipher = Cipher.getInstance(factory);
		cipher.init(mode, key, aps);
		
		return cipher;
	}
}
