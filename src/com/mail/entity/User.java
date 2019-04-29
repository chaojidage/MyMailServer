package com.mail.entity;

public class User {
	private int userid;
	private String name;
	private String password;
	private String email;
	
	public User() {

	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswoed() {
		return password;
	}

	public void setPasswoed(String passwoed) {
		this.password = passwoed;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
