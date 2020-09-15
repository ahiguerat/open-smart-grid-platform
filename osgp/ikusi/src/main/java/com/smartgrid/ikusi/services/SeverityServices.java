package com.smartgrid.ikusi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartgrid.ikusi.model.Severity;
import com.smartgrid.ikusi.repository.SeverityRepository;

@Service
public class SeverityServices {

	@Autowired
	private SeverityRepository severityRepository;
	
	public Severity save(Severity model) {
		return this.severityRepository.save(model);
	}
	
	public Severity delete(long idSeverity) {
		this.severityRepository.deleteById(idSeverity);
		return this.findById(idSeverity);
	}
	
	public List<Severity> getList(){
		return this.severityRepository.findAll();
	}
	
	public Severity findById(Long idSeverity) {
		Optional<Severity> response = this.severityRepository.findById(idSeverity);
		return response.isPresent() ? response.get():null;
	}
	
	public Severity findByCode(String code) {
		return this.severityRepository.findFirstByCode(code);
		
	}
		
}
