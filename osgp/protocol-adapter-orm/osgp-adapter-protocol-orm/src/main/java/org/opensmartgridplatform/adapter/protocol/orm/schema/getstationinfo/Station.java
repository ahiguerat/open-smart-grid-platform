package org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "station")
public class Station {
	
	@XmlElement(name = "rtuInfo")
	RtuInfo rtuInfo;
	
	public Station() {}
	
	public RtuInfo getRtuInfo() {
		return rtuInfo;
	}

	public void setRtuInfo(RtuInfo text) {
		this.rtuInfo = text;
	}
	
	@Override
	public String toString() {
		return "Station [rtuInfo=" + this.rtuInfo + "]";
	}
}
