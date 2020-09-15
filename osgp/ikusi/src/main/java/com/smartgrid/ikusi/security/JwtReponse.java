package com.smartgrid.ikusi.security;

import java.io.Serializable;

import lombok.NonNull;

//@NoArgsConstructor
//@RequiredArgsConstructor
//@Getter
//@Setter
//@ToString
public class JwtReponse implements Serializable {
	private static final long serialVersionUID = 1L;
	@NonNull
	private String jwttoken;
	
	public JwtReponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}
	
	
	public JwtReponse( ) {
		
	}
	
	
	
	public String getJwttoken() {
		return jwttoken;
	}
	public void setJwttoken(String jwttoken) {
		this.jwttoken = jwttoken;
	}

}
