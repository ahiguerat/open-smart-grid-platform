package com.smartgrid.ikusi.reports;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.smartgrid.ikusi.model.Line;
import com.smartgrid.ikusi.model.Station;

@Component
public class LineReport extends Reports {
		
	
	protected PdfPTable getBody(List<?> lista) {
		@SuppressWarnings("unchecked")
		List <Line> lines = (List<Line>) lista;		
		PdfPTable body = new PdfPTable(new float[] { 100});
		body.setWidthPercentage(100);
		
		for(Line line: ListUtils.emptyIfNull(lines)) {

			PdfPTable tbl = new PdfPTable(new float[] { 30, 70});
			tbl.setWidthPercentage(100);
			PdfPCell colLine = super.getTitleTable(String.format("(%s) %s", line.getCode(), line.getName()), 2, PdfPCell.LEFT);
			PdfPCell colCode = super.getSubTitleTable("Code", 1, PdfPCell.LEFT);
			PdfPCell colName = super.getSubTitleTable("Name", 1, PdfPCell.ALIGN_LEFT);
			tbl.addCell(colLine);
			tbl.addCell(colCode);
			tbl.addCell(colName);
			
			PdfPCell cell;
			Iterator<Station> stations = line.getStations().iterator();
			while (stations.hasNext()) {
				stations.next();				
			}
			
			for (Station sta : line.getStations()) {
				PdfPCell cellSta;		
				cellSta = super.cell(sta.getCode(), 1, PdfPCell.LEFT);
				tbl.addCell(cellSta);
				cellSta = super.cell(sta.getName(), 1, PdfPCell.ALIGN_LEFT);
				tbl.addCell(cellSta);
			}
														
			cell =  new PdfPCell(tbl);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingBottom(15f);
			body.addCell(cell);
			
			
		}
		return body;		
	}
}
