package com.smartgrid.ikusi.reports;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.smartgrid.ikusi.model.Line;
import com.smartgrid.ikusi.model.SubStation;


@Component
public class SubstationReport extends Reports {
	

	protected PdfPTable getBody(List<?> lista) {
		@SuppressWarnings("unchecked")
		List <SubStation> subStations = (List<SubStation>) lista;		
		PdfPTable body = new PdfPTable(new float[] { 100});
		body.setWidthPercentage(100);
		
		for(SubStation subStation: ListUtils.emptyIfNull(subStations)) {

			PdfPTable tbl = new PdfPTable(new float[] { 30, 70});
			tbl.setWidthPercentage(100);
			PdfPCell colLine = super.getTitleTable(String.format("(%s) %s", subStation.getCode(), subStation.getName()), 2, PdfPCell.LEFT);
			PdfPCell colCode = super.getSubTitleTable("Code", 1, PdfPCell.LEFT);
			PdfPCell colName = super.getSubTitleTable("Name", 1, PdfPCell.ALIGN_LEFT);
			tbl.addCell(colLine);
			tbl.addCell(colCode);
			tbl.addCell(colName);
			
			PdfPCell cell;
			Iterator<Line> lines = subStation.getLines().iterator();
			while (lines.hasNext()) {
				lines.next();				
			}
			
			for (Line line : subStation.getLines()) {
				PdfPCell cellSta;		
				cellSta = super.cell(line.getCode(), 1, PdfPCell.LEFT);
				tbl.addCell(cellSta);
				cellSta = super.cell(line.getName(), 1, PdfPCell.ALIGN_LEFT);
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
