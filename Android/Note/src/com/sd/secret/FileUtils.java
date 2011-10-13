package com.sd.secret;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import android.content.Context;
import android.util.Log;

import com.sd.model.UserData;

public class FileUtils {
	/** Name of the secrets file. */
	public static final String SECRETS_FILE_NAME = "secrets";
	/** Tag for logging purposes. */
	public static final String LOG_TAG = "Secrets";
  
	/** Does the secrets file exist? */
	public static boolean secretsExist(Context context) {
		if(context.fileList() != null) {
			String[] filenames = context.fileList();
			for (String name : filenames) {
				if (name.equals(SECRETS_FILE_NAME)) {
					return true;
				}
			}
		}
		return false;
	}
  
	/**
	 * Saves the secrets to file using the password retrieved from the user.
	 * 
	 * @return True if saved successfully
	 * */
	public static boolean saveUserData(Context context, UserData userData) {
		Log.d(LOG_TAG, "FileUtils.saveSecrets");
    
		Cipher cipher = SecurityUtils.getEncryptionCipher();
		if (null == cipher)
			return false;
    
		ObjectOutputStream output = null;
		boolean success = false;
    
		try {
			output = new ObjectOutputStream(
					new CipherOutputStream(context.openFileOutput(SECRETS_FILE_NAME,
							Context.MODE_PRIVATE), cipher));
			output.writeObject(userData);
			success = true;
		} 
		catch (Exception ex) {
		} 
		finally {
			try {if (null != output) output.close();} catch (IOException ex) {}
		}
    
		return success;
	}
  
	/** Opens the secrets file using the password retrieved from the user. */
	public static UserData loadUserData(Context context) {
		Log.d(LOG_TAG, "FileUtils.loadSecrets");
    
		Cipher cipher = SecurityUtils.getDecryptionCipher();
		if (null == cipher)
			return null;

		UserData userData = null;
		ObjectInputStream input = null;
		
		if (secretsExist(context)) {
			try {
				input = new ObjectInputStream(
						new CipherInputStream(context.openFileInput(SECRETS_FILE_NAME), cipher));
				userData = (UserData) input.readObject();
			} 
			catch (Exception ex) {
				Log.e(LOG_TAG, "loadSecrets", ex);
			} 
			finally {
				try {if (null != input) input.close();} catch (IOException ex) {}
			}
	
			// If we were not able to load the secrets, try using the old decryption
			// cipher.
			if (null == userData) {
				try {
					cipher = SecurityUtils.getOldDecryptionCipher();
					input = new ObjectInputStream(
							new CipherInputStream(context.openFileInput(SECRETS_FILE_NAME), cipher));
					userData = (UserData) input.readObject();
				} 
				catch (Exception ex) {
					Log.e(LOG_TAG, "loadSecrets(old)", ex);
				} 
				finally {
					try {if (null != input) input.close();} catch (IOException ex) {}
				}
			}
		}

		if (null == userData) {
			userData = new UserData();
		}

		return userData;
	}

	/** Deletes all secrets from the phone. */
	public static boolean deleteUserData(Context context) {
		return context.deleteFile(SECRETS_FILE_NAME);
	}
}
