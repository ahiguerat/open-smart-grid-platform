package org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CfgEthernet {

	@XmlElement(name="ifaz")
	private List<Ifaz> ifaz = new ArrayList<Ifaz>();
	
	public CfgEthernet(){}
	
	public CfgEthernet(List<Ifaz> ifaz) {
		this.ifaz = ifaz;
	}
	
	public List<Ifaz> getIfaz() {
		return ifaz;
	}

	public void setIfaz(List<Ifaz> ifaz) {
		this.ifaz = ifaz;
	}
}
