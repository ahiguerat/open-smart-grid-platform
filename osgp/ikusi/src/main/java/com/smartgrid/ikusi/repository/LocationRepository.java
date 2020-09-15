package com.smartgrid.ikusi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartgrid.ikusi.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	
	String locationNotOcupated = "select * from location l left join sub_location sl on l.id_location = sl.id_location where sl.id_location is null order by l.code";
	
	Location findByIdLocation(Long idLocation);
	
	@Query(value = locationNotOcupated, nativeQuery = true)
	List<Location> getLocationNotOcupated();
	
	List<Location> findByOrderByIdLocationDesc();
	
}
