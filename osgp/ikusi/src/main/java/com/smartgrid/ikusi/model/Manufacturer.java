package com.smartgrid.ikusi.model;

public class Manufacturer {
	private Integer id;
	private String code;
	private String name;
	private boolean usePrefix;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getUsePrefix() {
		return usePrefix;
	}
	public void setUsePrefix(boolean usePrefix) {
		this.usePrefix = usePrefix;
	}
	

}
