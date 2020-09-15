package com.smartgrid.ikusi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartgrid.ikusi.model.Severity;

@Repository
public interface SeverityRepository extends JpaRepository<Severity, Long>  {

	Severity findFirstByCode(String code); 
}
