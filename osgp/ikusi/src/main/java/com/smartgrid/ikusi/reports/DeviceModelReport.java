package com.smartgrid.ikusi.reports;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.opensmartgridplatform.adapter.ws.schema.core.firmwaremanagement.DeviceModel;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

@Component
public class DeviceModelReport  extends Reports {
	
	protected PdfPTable getBody(List<?> list) {
		@SuppressWarnings("unchecked")
		List<DeviceModel> devicesModel = (List<DeviceModel>) list; 
		PdfPTable body = new PdfPTable(new float[] { 30, 30, 40 });
		body.setWidthPercentage(100);
		//body.setPaddingTop(5f);
		PdfPCell colDevice = super.getTitleTable("Manufacturer", 1, PdfPCell.LEFT);
		PdfPCell colModel = super.getTitleTable("Model code", 1, PdfPCell.LEFT);
		PdfPCell colStatus = super.getTitleTable("Description", 1, PdfPCell.ALIGN_CENTER);
		
		body.addCell(colDevice);
		body.addCell(colModel);
		body.addCell(colStatus);
		
		PdfPCell cell;
		for(DeviceModel device : ListUtils.emptyIfNull(devicesModel)) {
			cell = super.cell(device.getManufacturer(), 1, PdfPCell.ALIGN_LEFT);
			body.addCell(cell);
			cell = super.cell(device.getModelCode(), 1, PdfPCell.LEFT);
			body.addCell(cell);		
			cell = super.cell(device.getDescription().toString(), 1, PdfPCell.ALIGN_JUSTIFIED);
			body.addCell(cell);
		}
		
		return body;
	}

}
