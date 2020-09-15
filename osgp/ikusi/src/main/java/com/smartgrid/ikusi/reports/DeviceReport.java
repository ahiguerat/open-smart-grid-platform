package com.smartgrid.ikusi.reports;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.opensmartgridplatform.adapter.ws.schema.core.devicemanagement.Device;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

@Component
public class DeviceReport extends Reports {
	
	protected PdfPTable getBody(List<?> list) {
		@SuppressWarnings("unchecked")
		List<Device> devices = (List<Device>) list;
		PdfPTable body = new PdfPTable(new float[] { 35, 30, 35 });
		body.setWidthPercentage(100);
		//body.setPaddingTop(5f);
		PdfPCell colDevice = super.getTitleTable("Device", 1, PdfPCell.LEFT);
		PdfPCell colModel = super.getTitleTable("Model code", 1, PdfPCell.LEFT);
		PdfPCell colStatus = super.getTitleTable("Status", 1, PdfPCell.ALIGN_CENTER);
		
		body.addCell(colDevice);
		body.addCell(colModel);
		body.addCell(colStatus);
		
		PdfPCell cell;
		for(Device device : ListUtils.emptyIfNull(devices)) {
			cell = super.cell(device.getDeviceIdentification(), 1, PdfPCell.LEFT);
			body.addCell(cell);
			cell = super.cell(device.getDeviceModel().getModelCode(), 1, PdfPCell.ALIGN_LEFT);
			body.addCell(cell);
			cell = super.cell(device.getDeviceLifecycleStatus().toString(), 1, PdfPCell.ALIGN_CENTER);
			body.addCell(cell);
		}
		
		return body;
	}
}
