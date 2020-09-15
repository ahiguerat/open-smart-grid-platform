package com.smartgrid.ikusi.reports;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.smartgrid.ikusi.model.DeviceStation;
import com.smartgrid.ikusi.model.Station;

@Component
public class StationsReport extends Reports {
		
	protected PdfPTable getBody(List<?> list) {
		
		PdfPTable body = new PdfPTable(new float[] { 100});
		body.setWidthPercentage(100);
		@SuppressWarnings("unchecked")
		List<Station> stations = (List<Station>) list;
		
		for(Station station: ListUtils.emptyIfNull(stations)) {

			PdfPTable tbl = new PdfPTable(new float[] { 60, 40});
			tbl.setWidthPercentage(100);
			PdfPCell colStation = super.getTitleTable("Station: " + station.getName() , 2, PdfPCell.LEFT);
			PdfPCell colDevice = super.getSubTitleTable("Device", 1, PdfPCell.LEFT);
			PdfPCell colStatus = super.getSubTitleTable("Status", 1, PdfPCell.ALIGN_CENTER);
			tbl.addCell(colStation);
			tbl.addCell(colDevice);
			tbl.addCell(colStatus);
			
			PdfPCell cell;
			for(DeviceStation deviceStation : station.getDevices()) {
				String deviceId= deviceStation.getDevice() != null ? deviceStation.getDevice().getDeviceIdentification(): deviceStation.getDeviceIdentification();
				cell = super.cell(deviceId, 1, PdfPCell.LEFT);
				tbl.addCell(cell);
				String status = deviceStation.getDevice() != null ? deviceStation.getDevice().getDeviceLifecycleStatus().toString() : "";
				cell = super.cell(status, 1, PdfPCell.ALIGN_CENTER);
				tbl.addCell(cell);
			}
														
			cell =  new PdfPCell(tbl);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingBottom(15f);
			body.addCell(cell);
			
			
		}
		return body;		
	}
}
