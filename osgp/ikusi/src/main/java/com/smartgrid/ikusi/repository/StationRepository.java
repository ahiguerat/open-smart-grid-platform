package com.smartgrid.ikusi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smartgrid.ikusi.model.Station;

@Repository
@Transactional
public interface StationRepository extends JpaRepository<Station, Long> {
	String stationNotOcupated = "select * from station_ct sc left join line_ct lc on sc.id_station_ct = lc.id_station_ct where lc.id_station_ct is null order by sc.code";
	String stationWithDetail = "select sc from Station sc join sc.devices where sc.id = :id";
	
	@Query(value = stationNotOcupated, nativeQuery = true)
	List<Station> getStationNotAssociated();
	
//	@Query(value = "Select * from station_ct", nativeQuery = true)
//	List<Station> findStations();
	//@Query(value = stationWithDetail, nativeQuery = true)
	Station findByIdStation(Long id);
	
	List<Station> findByOrderByCodeAsc();
}
