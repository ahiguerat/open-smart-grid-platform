package org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Platform {
	
	@XmlAttribute(name="value")
	private String value;
	
	public Platform() {}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
