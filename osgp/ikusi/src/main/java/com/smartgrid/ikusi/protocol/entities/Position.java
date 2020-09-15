package com.smartgrid.ikusi.protocol.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Position extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column
	private String numPos;

	@Column
	private String type;

	@Column
	private String name;

	@Column
	private String status;
	
	@ManyToOne
    private OrmRtuDevice ormRtuDevice;

	public Position() {
	}

	public Position(String numPos, String type, String name, String status, OrmRtuDevice rtuDevice) {
		super();
		this.numPos = numPos;
		this.type = type;
		this.name = name;
		this.status = status;
		this.ormRtuDevice = rtuDevice;
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
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
