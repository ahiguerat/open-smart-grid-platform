package com.smartgrid.ikusi.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {		 
	 private String username;	
	 private String password;
	 private boolean remember;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isRemember() {
		return remember;
	}
	public void setRemember(boolean remember) {
		this.remember = remember;
	}
}
