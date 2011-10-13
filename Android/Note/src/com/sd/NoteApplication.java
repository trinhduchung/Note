package com.sd;

import java.util.HashMap;

import android.app.Application;

import com.sd.model.UserData;
import com.sd.secret.FileUtils;
import com.sd.secret.SecurityUtils;

public class NoteApplication extends Application {
	private static NoteApplication mInstance;
	private HashMap<String, Object> mHashMap = new HashMap<String, Object>();
	private UserData userData;

	public NoteApplication() {
		super();
		loadUserData();
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public static NoteApplication Instance() {
		if(null == mInstance) {
			mInstance = new NoteApplication();
		} 
		return mInstance;
	}
	
	private void loadUserData() {
        SecurityUtils.createCiphers("09099");
        userData = FileUtils.loadUserData(this);
    }

    private void saveUserData() {
    	FileUtils.saveUserData(this, userData);
    }
	
    private void resetUserData() {
    	userData.reset();
    	FileUtils.deleteUserData(this);
        FileUtils.saveUserData(this, userData);
    }
	
    public UserData getUserdata() {
    	return this.userData;
    }
    
    public void setUserdata(UserData data) {
    	resetUserData();
    	userData = data;
    	saveUserData();
    }
    
	public Object get(String key) {
		return mHashMap.get(key);
	}
	
	public void put(String key, Object value) {
		mHashMap.put(key, value);
	}
	
	public void remove(String key) {
		mHashMap.remove(key);
	}
}
