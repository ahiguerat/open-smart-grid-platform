package com.smartgrid.ikusi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "severity", schema = "public")
public class Severity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_severity")
	private long idSeverity;
	
	@Column(name = "category")
	private String  category;
	
	@Column(name = "code")
	private String  code;
	
	@Column(name = "description")
	private String  description;
	
	@Column(name = "color")
	private String color;

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
	

	public long getIdSeverity() {
		return idSeverity;
	}

	public void setIdSeverity(long idSeverity) {
		this.idSeverity = idSeverity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
