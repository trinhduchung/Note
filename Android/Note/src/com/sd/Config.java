package com.sd;

public class Config {
	public static enum activity {
		NoteActivity ,
		LoginActivity,
		SplashActivity;
		
		public String toString() {
			return name().toString();
		}
	}
}
