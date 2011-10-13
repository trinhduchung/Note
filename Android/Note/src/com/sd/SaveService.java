package com.sd;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.sd.model.UserData;

public class SaveService {

	private static final String SAVE_FILENAME = "Settings.dat";
	private Context context;
    private UserData saveData;
    private static SaveService saveService;

	private FileOutputStream fOut = null;
	private ObjectOutputStream osw = null;
	
	private FileInputStream fin = null;
	private ObjectInputStream sin = null;

	public SaveService(Context context) {
	    this.context = context;
    }

	public static SaveService getInstance(Context context) {
		if (null == saveService) {
			saveService = new SaveService(context);
		}
	    return saveService;
	}

    public void save(UserData object) {
    	try {
    		fOut = context.openFileOutput(SAVE_FILENAME, Activity.MODE_PRIVATE);
            osw = new ObjectOutputStream(fOut);
            osw.writeObject(object);
            osw.close();
			Toast.makeText(context, "Settings write!",Toast.LENGTH_SHORT).show();
        }
    	catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	catch (IOException e) {
			e.printStackTrace();
        }
    }

    public UserData readLastState() {
    	if(checkExistedFile()) {
    		try {
    			fin = context.openFileInput(SAVE_FILENAME);
    			sin = new ObjectInputStream(fin);
    			saveData = (UserData) sin.readObject();
    			sin.close();
    			Toast.makeText(context, "Settings read!",Toast.LENGTH_SHORT).show();
            }
        	catch (StreamCorruptedException e) {
    	        e.printStackTrace();
            }
        	catch (IOException e) {
                e.printStackTrace();
            } 
        	catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    	}
        return saveData;
    }
    
    public boolean checkExistedFile() {
		FileInputStream fis;
		try {
			fis = context.openFileInput(SAVE_FILENAME);
			fis.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}