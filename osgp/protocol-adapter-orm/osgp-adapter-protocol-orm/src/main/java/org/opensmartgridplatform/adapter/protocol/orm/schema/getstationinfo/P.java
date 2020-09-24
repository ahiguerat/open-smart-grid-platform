package org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class P {
	
	@XmlAttribute(name="code")
	private String code;
	
	@XmlAttribute(name="version")
	private String version;
	
	public P() {}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		return "P [code=" + this.code + ", version=" + this.version + "]";
	}
}
