package com.sd.model;

public class UserData {
	public String Username;
	public String Password;
	public boolean isAuthen;
	
	public UserData() {
		reset();
	}
	
	public UserData(String username,String pw) {
		Username = username;
		Password = pw;
	}
	
	public void reset() {
		Username = "";
		Password = "";
		isAuthen = false;
	}
}
