package org.opensmartgridplatform.adapter.protocol.orm.schema.getevents;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Evento {

	@XmlAttribute(name="feve")
	private String feve;
	
	@XmlAttribute(name="dst")
	private String dst;
	
	@XmlAttribute(name="qbit")
	private String qbit;
	
	@XmlAttribute(name="grupeve")
	private String grupeve;
	
	@XmlAttribute(name="tipeve")
	private String tipeve;
	
	@XmlAttribute(name="ceve")
	private String ceve;

	public Evento() {}

	public String getFeve() {
		return feve;
	}

	public void setFeve(String feve) {
		this.feve = feve;
	}

	public String getDst() {
		return dst;
	}

	public void setDst(String dst) {
		this.dst = dst;
	}

	public String getQbit() {
		return qbit;
	}

	public void setQbit(String qbit) {
		this.qbit = qbit;
	}

	public String getGrupeve() {
		return grupeve;
	}

	public void setGrupeve(String grupeve) {
		this.grupeve = grupeve;
	}

	public String getTipeve() {
		return tipeve;
	}

	public void setTipeve(String tipeve) {
		this.tipeve = tipeve;
	}

	public String getCeve() {
		return ceve;
	}

	public void setCeve(String ceve) {
		this.ceve = ceve;
	}
}
