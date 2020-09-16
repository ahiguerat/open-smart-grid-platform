package org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class RtuInfo {

	@XmlElement(name = "platform")
	private Platform platform;

	@XmlElement(name = "cfgEthernet")
	private CfgEthernet cfgEthernet;

	@XmlElement(name = "products")
	private Products products;

	@XmlElement(name = "positions")
	private Positions positions;

	public RtuInfo() {
	}

	public Platform getPlatform() {
		return this.platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public CfgEthernet getCfgEthernet() {
		return cfgEthernet;
	}

	public void setCfgEthernet(CfgEthernet cfgEthernet) {
		this.cfgEthernet = cfgEthernet;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public Positions getPositions() {
		return positions;
	}

	public void setPositions(Positions positions) {
		this.positions = positions;
	}

	@Override
	public String toString() {
		return "RtuInfo [platform=" + this.platform.getValue() + ", products=" + this.products + ", positions="
				+ this.positions + "]";
	}
}