package org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Products {
	
	@XmlElement(name="p")	
	private List<P> p = new ArrayList<P>();
	
	public Products(){}
	
	public Products(List<P> p) {
		this.p = p;
	}

	public List<P> getP() {
		return p;
	}

	public void setP(List<P> p) {
		this.p = p;
	}
	
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder("Products [");
		int index = 0;
		for (P p : this.p) {
			str.append(p.toString());
			if (++index < this.p.size()) {
				str.append(", ");
			}
		};
		return str.toString() + "]";
	}
}
