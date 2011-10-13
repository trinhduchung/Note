package com.sd.activity;

import com.sd.NoteApplication;
import com.sd.R;
import com.sd.R.id;
import com.sd.R.layout;
import com.sd.model.UserData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	EditText mUsername;
	EditText mPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		inflateView();
	}
	
	private void inflateView() {
		mUsername = (EditText) findViewById(R.id.edtUsername);
		mPassword = (EditText) findViewById(R.id.edtPassword);
	}
	
	public void onSubmit(View v) {
		String username = mUsername.getText().toString();
		String password = mPassword.getText().toString();
		
		UserData data = new UserData(username, password);
		NoteApplication.Instance().setUserdata(data);
		startActivity(new Intent(this, MainActivity.class));
	}
	
	public void onCancel(View v) {
		startActivity(new Intent(this, MainActivity.class));
	}
}
