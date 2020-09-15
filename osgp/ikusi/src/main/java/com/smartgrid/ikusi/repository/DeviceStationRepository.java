package com.smartgrid.ikusi.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartgrid.ikusi.model.DeviceStation;

@Repository
public interface DeviceStationRepository extends JpaRepository<DeviceStation, Long> {
	String findAll = "select ds.* " + 
			"from device_station_ct ds " + 
			"join station_ct st on st.id_station_ct = ds.id_station_ct " +
			" order by st.name ";						
	
	String byLocation = "select ds.* " + 
			"from location l " + 
			"join sub_location sl on l.id_location = sl.id_location " + 
			"join line_sub_station ls on ls.id_sub_station = sl.id_sub_station " + 
			"join line_ct lc on lc.id_line = ls.id_line " + 
			"join device_station_ct ds on ds.id_station_ct = lc.id_station_ct " + 
			"where l.id_location in (:locations) ";
	
	String byLocationAndSubstation = "select ds.* " + 
			"from location l " + 
			"join sub_location sl on l.id_location = sl.id_location " + 
			"join line_sub_station ls on ls.id_sub_station = sl.id_sub_station " + 
			"join line_ct lc on lc.id_line = ls.id_line " + 
			"join device_station_ct ds on ds.id_station_ct = lc.id_station_ct " + 
			"where l.id_location in (:locations) and ls.id_sub_station in (:substations) ";
	
	String byLocationAndSubstationAndLine = "select ds.* " + 
			"from location l " + 
			"join sub_location sl on l.id_location = sl.id_location " + 
			"join line_sub_station ls on ls.id_sub_station = sl.id_sub_station " + 
			"join line_ct lc on lc.id_line = ls.id_line " + 
			"join device_station_ct ds on ds.id_station_ct = lc.id_station_ct " + 
			"where l.id_location in (:locations) and ls.id_sub_station in (:substations) and lc.id_line in (:lines) ";
	
	String byLocationAndSubstationAndLineAndStation = "select ds.* " + 
			"from location l " + 
			"join sub_location sl on l.id_location = sl.id_location " + 
			"join line_sub_station ls on ls.id_sub_station = sl.id_sub_station " + 
			"join line_ct lc on lc.id_line = ls.id_line " + 
			"join device_station_ct ds on ds.id_station_ct = lc.id_station_ct " + 
			"where l.id_location in (:locations) and ls.id_sub_station in (:substations) and lc.id_line in (:lines) " +
			" and ds.id_station_ct in (:stations)  ";
	
	@Transactional
	@Modifying
	Long deleteByDeviceIdentification(String deviceIdentification);
	
	@Query(value = findAll, nativeQuery = true)
	List<DeviceStation> findAll();
	
	@Query(value = byLocation, nativeQuery = true)
	List<DeviceStation> byLocation(@Param("locations") List<Integer> locations);
	
	@Query(value = byLocationAndSubstation, nativeQuery = true)
	List<DeviceStation> byLocationAndSubstation(@Param("locations") List<Integer> locations, @Param("substations") List<Integer> substations);
	
	@Query(value = byLocationAndSubstationAndLine, nativeQuery = true)
	List<DeviceStation> byLocationAndSubstationAndLine(@Param("locations") List<Integer> locations, 
													   @Param("substations") List<Integer> substations,
													   @Param("lines") List<Integer> lines);
	
	@Query(value = byLocationAndSubstationAndLineAndStation, nativeQuery = true)
	List<DeviceStation> byLocationAndSubstationAndLineAndStation(@Param("locations") List<Integer> locations, 
													   @Param("substations") List<Integer> substations,
													   @Param("lines") List<Integer> lines,
													   @Param("stations") List<Integer> stations);
	
	
}
