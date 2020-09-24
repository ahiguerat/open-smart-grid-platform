package org.opensmartgridplatform.adapter.protocol.orm.schema.getevents;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "EventList")
public class EventList {
	
	@XmlElement(name="evento")	
	private List<Evento> evento = new ArrayList<Evento>();

	
	public EventList() {}

	public List<Evento> getEventos() {
		return evento;
	}

	public void setEventos(List<Evento> eventos) {
		this.evento = eventos;
	}
	
}
