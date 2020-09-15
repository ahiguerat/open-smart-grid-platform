package com.smartgrid.ikusi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.smartgrid.ikusi.model.SubStation;

@Repository
public interface SubStationRepository extends JpaRepository<SubStation, Long> {


	String substationNotOcupated = "select * from sub_station ss left join sub_location sl on ss.id_sub_station = sl.id_sub_station where sl.id_sub_station is null order by ss.code";

	@Query(value = substationNotOcupated, nativeQuery = true)
	List<SubStation> getSubstationNotOcupated();
	
	List<SubStation> findByOrderByIdSubStationDesc();
}
