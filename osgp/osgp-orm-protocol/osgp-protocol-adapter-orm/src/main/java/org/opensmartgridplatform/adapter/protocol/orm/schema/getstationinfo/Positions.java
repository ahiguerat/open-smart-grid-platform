package org.opensmartgridplatform.adapter.protocol.orm.schema.getstationinfo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Positions {
	
	@XmlElement(name="position")	
	private List<Position> position = new ArrayList<Position>();
	
	public Positions() {}

	public List<Position> getPosition() {
		return position;
	}

	public void setPosition(List<Position> position) {
		this.position = position;
	}
	
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder("Positions [");
		int index = 0;
		for (Position p : this.position) {
			str.append(p.toString());
			if (++index < this.position.size()) {
				str.append(", ");
			}
		};
		return str.toString() + "]";
	}
}
