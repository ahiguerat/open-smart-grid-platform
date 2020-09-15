package com.smartgrid.ikusi.reports;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.Manufacturer;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

@Component
public class ManufacturerReport extends Reports {
	
	protected PdfPTable getBody(List<?> list) {
		@SuppressWarnings("unchecked")
		List<Manufacturer> manufacturer = (List<Manufacturer>) list;
		PdfPTable body = new PdfPTable(new float[] { 30, 30, 40 });
		body.setWidthPercentage(100);
		//body.setPaddingTop(5f);
		PdfPCell colDevice = super.getTitleTable("Code", 1, PdfPCell.LEFT);
		PdfPCell colModel = super.getTitleTable("Name", 1, PdfPCell.LEFT);
		PdfPCell colStatus = super.getTitleTable("Use Prefix", 1, PdfPCell.ALIGN_CENTER);
		
		body.addCell(colDevice);
		body.addCell(colModel);
		body.addCell(colStatus);
		
		PdfPCell cell;
		for(Manufacturer model : ListUtils.emptyIfNull(manufacturer)) {
			cell = super.cell(model.getCode(), 1, PdfPCell.ALIGN_LEFT);
			body.addCell(cell);
			cell = super.cell(model.getName(), 1, PdfPCell.LEFT);
			body.addCell(cell);		
			cell = super.cell(String.format("%s",model.isUsePrefix()), 1, PdfPCell.ALIGN_CENTER);
			body.addCell(cell);
		}
		
		return body;
	}

}
