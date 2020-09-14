package org.opensmartgridplatform.dto.valueobjects.orm;

import java.io.Serializable;

public class PositionDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4301893579061155735L;
	
	private String numPos;

	private String type;

	private String name;

	private String status;

	public PositionDto(String numPos, String type, String name, String status) {
		super();
		this.numPos = numPos;
		this.type = type;
		this.name = name;
		this.status = status;
	}

	public String getNumPos() {
		return numPos;
	}

	public void setNumPos(String numPos) {
		this.numPos = numPos;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
