package com.sd.model;

public class FolderInfo {
	int id;
	String name;
	int img;
	int icon;
	String password;
	int count;
	public FolderInfo(int id, String name, int img, int icon, String password, int count) {
		super();
		this.id = id;
		this.name = name;
		this.img = img;
		this.icon = icon;
		this.password = password;
		this.count = count;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImg() {
		return img;
	}
	public void setImg(int img) {
		this.img = img;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
