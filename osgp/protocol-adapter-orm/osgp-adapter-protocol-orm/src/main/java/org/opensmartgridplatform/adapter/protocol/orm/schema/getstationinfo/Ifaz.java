package org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Ifaz {

	@XmlAttribute(name="name")
	private String name;
	
	@XmlAttribute(name="ip")
	private String ip;
	
	@XmlAttribute(name="mask")
	private String mask;
	
	@XmlAttribute(name="mac")
	private String mac;

	public Ifaz() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}	
}
