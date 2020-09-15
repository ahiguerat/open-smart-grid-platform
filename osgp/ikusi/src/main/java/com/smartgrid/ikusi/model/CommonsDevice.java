package com.smartgrid.ikusi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
public class CommonsDevice {
	
	@Column(name = "code")
	private String  code;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@CreationTimestamp
    @Column(name = "creation_time", nullable = false, updatable = false)  
	private Date created;
	
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@UpdateTimestamp
    @Column(name = "modify_time", insertable = false)  
	private Date modified;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

}
