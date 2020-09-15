package com.smartgrid.ikusi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.smartgrid.ikusi.model.Line;

@Repository
public interface LineRepository extends JpaRepository<Line, Long> {

	String lineNotOcupated = "select * from line l left join line_sub_station ls on l.id_line = ls.id_line where ls.id_line is null order by l.code";

	@Query(value = lineNotOcupated, nativeQuery = true)
	List<Line> getLineNotOcupated( );
		
	List<Line> findByOrderByIdLineDesc();
}
