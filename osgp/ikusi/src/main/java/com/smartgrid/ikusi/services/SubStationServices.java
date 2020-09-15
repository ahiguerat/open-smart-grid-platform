package com.smartgrid.ikusi.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.smartgrid.ikusi.config.NotFoundException;
import com.smartgrid.ikusi.model.Line;
import com.smartgrid.ikusi.model.SubStation;
import com.smartgrid.ikusi.reports.SubstationReport;
import com.smartgrid.ikusi.repository.SubStationRepository;

@Service
public class SubStationServices {

	@Autowired
	private SubstationReport substationReport;
	
	@Autowired
	private SubStationRepository subStationRepository;

	public List<SubStation> getSubstationNotOcupated() {
		return this.subStationRepository.getSubstationNotOcupated();
	}

	public List<SubStation> findAll() {
		return this.subStationRepository.findByOrderByIdSubStationDesc();
	}

	public SubStation save(SubStation model) {
		this.subStationRepository.save(model);
		return model;
	}

	public SubStation delete(SubStation model) {
		this.subStationRepository.deleteById(model.getIdSubStation());
		return model;
	}

	public List<SubStation> findSubstationByIdAndLines(List<Long> ids, boolean showLines) {
		List<SubStation> subs = this.subStationRepository.findAllById(ids);
		if (showLines) {
			for (SubStation record : ListUtils.emptyIfNull(subs)) {
				Iterator<Line> item = record.getLines().iterator();
				while (item.hasNext()) {
					item.next();
				}
			}
		}
		return subs;
	}

	public SubStation edit(SubStation subStation) {
		return this.subStationRepository.findById(subStation.getIdSubStation()).map(r -> {
			r.setName(subStation.getName());
			r.setCode(subStation.getCode());
			r.setDescription(subStation.getDescription());
			Iterator<Line> item = r.getLines().iterator();
			while (item.hasNext()) {
				item.next();
				
			}
			if (subStation.getLines() != null) {
				if (r.getLines() == null)
					r.setLines(new ArrayList<Line>());
				r.getLines().addAll(subStation.getLines());
			}
			return this.subStationRepository.save(r);

		}).orElseThrow(() -> new NotFoundException("Substation not found with id " + subStation.getIdSubStation()));
	}

	public SubStation deleteLine(SubStation subStation) {
		return this.subStationRepository.findById(subStation.getIdSubStation()).map(r -> {
			Iterator<Line> item = r.getLines().iterator();
			while (item.hasNext()) {
				item.next();
			}
			if (subStation.getLines() != null && r.getLines() != null) {
				List<Line> lines = r.getLines().stream()
						.filter(record -> subStation.getLines().stream()
								.filter(pre -> pre.getIdLine() == record.getIdLine()).collect(Collectors.toList())
								.size() < 1)
						.collect(Collectors.toList());
				r.setLines(lines);
			}
			return this.subStationRepository.save(r);
		}).orElseThrow(() -> new NotFoundException("Substation not found with id " + subStation.getIdSubStation()));
	}
	
	public byte[] getReport() throws DocumentException, IOException {
		return substationReport.getFile(findAll(), "SUBSTATION REPORTS");
	}

}
