package org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Position {

	@XmlAttribute(name = "numPos")
	private String numPos;

	@XmlElement(name = "type")
	private Type type;

	@XmlElement(name = "name")
	private Name name;

	@XmlElement(name = "cnx")
	private Cnx cnx;

	@XmlElement(name = "products")
	private Products products;

	public Position() {
	}

	public String getNumPos() {
		return numPos;
	}

	public void setNumPos(String numPos) {
		this.numPos = numPos;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Cnx getCnx() {
		return cnx;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public void setCnx(Cnx cnx) {
		this.cnx = cnx;
	}

	@Override
	public String toString() {
		String position = "Position [ ";
		if(this.numPos != null)
			position+="numPos=" + this.numPos +", ";
		if(this.type != null)
			position+="type=" + this.type.getValue() +", ";
		if(this.name != null)
			position+="name=" + this.name.getValue() +", ";
		if(this.cnx != null)
			position+="cnx=" + this.cnx.getStatus() +", ";
		if(this.products != null)
			position+= this.products.toString();
		return position + "]";
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Type {

		@XmlAttribute(name = "value")
		private String value;

		public Type() {
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Name {

		@XmlAttribute(name = "value")
		private String value;

		public Name() {
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Cnx {

		@XmlAttribute(name = "status")
		private String status;

		public Cnx() {
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
}
