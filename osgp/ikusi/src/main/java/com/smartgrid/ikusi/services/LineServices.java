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
import com.smartgrid.ikusi.model.Station;
import com.smartgrid.ikusi.reports.LineReport;
import com.smartgrid.ikusi.repository.LineRepository;

@Service
public class LineServices {
	
	@Autowired
	private LineReport lineReport;

	@Autowired
	private LineRepository lineRepository;

	public List<Line> getList() {
		return this.lineRepository.findByOrderByIdLineDesc();
	}

	public Line save(Line line) {
		return this.lineRepository.save(line);
	}

	public Line delete(Line line) {
		this.lineRepository.deleteById(line.getIdLine());
		return line;
	}

	public List<Line> getLineNotOcupated() {
		return this.lineRepository.getLineNotOcupated();
	}

	public List<Line> findLineByIdAndStations(List<Long> ids, boolean showStations) {
		List<Line> lines = this.lineRepository.findAllById(ids);
		if (showStations) {
			for (Line line : ListUtils.emptyIfNull(lines)) {
				Iterator<Station> stations = line.getStations().iterator();
				while (stations.hasNext()) {
					stations.next();
				}
			}
		}
		return lines;
	}

	public Line edit(Line line) {
		return this.lineRepository.findById(line.getIdLine()).map(r -> {
			r.setName(line.getName());
			r.setCode(line.getCode());
			r.setDescription(line.getDescription());
			Iterator<Station> stations = r.getStations().iterator();
			while (stations.hasNext()) {
				stations.next();
			}
			if (line.getStations() != null) {
				if (r.getStations() == null)
					r.setStations(new ArrayList<Station>());
				r.getStations().addAll(line.getStations());
			}
			return this.lineRepository.save(r);
		}).orElseThrow(() -> new NotFoundException("Line not found with id " + line.getIdLine()));
	}

	public Line deleteStation(Line line) {
		return this.lineRepository.findById(line.getIdLine()).map(r -> {
			// Traerme todas las estaciones
			Iterator<Station> stations = r.getStations().iterator();
			while (stations.hasNext()) {
				stations.next();
			}
			// Revisar que la linea tenga estaciones y que tambien se tenga las estaciones a
			// eliminar.
			if (line.getStations() != null && r.getStations() != null) {
				List<Station> l = r.getStations().stream().filter(record ->

				line.getStations().stream().filter(lin -> lin.getIdStation() == record.getIdStation())
						.collect(Collectors.toList()).size() < 1).collect(Collectors.toList());

				r.setStations(l);

			}

			return this.lineRepository.save(r);
		}).orElseThrow(() -> new NotFoundException("Line not found with id " + line.getIdLine()));
	}
	
	public byte[] getReport() throws DocumentException, IOException {
		return this.lineReport.getFile(this.getList(), "LINE REPORTS");
	}
		
}
