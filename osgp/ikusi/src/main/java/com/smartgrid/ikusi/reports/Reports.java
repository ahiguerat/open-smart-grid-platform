package com.smartgrid.ikusi.reports;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Reports extends Header {
	
	public byte[] getFile(List<?> list, String title) throws DocumentException, IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Document document = new Document(PageSize.LETTER);		
		PdfWriter writer = PdfWriter.getInstance(document, stream);
		Footer footer = new Footer();
		writer.setPageEvent(footer);
		document.open();
		PdfPTable header = super.getHeader(title);
		PdfPTable body = this.getBody(list);
		document.add(header);
		document.add(body);
		document.close();
		return stream.toByteArray();
	}
	
	protected PdfPTable getBody( List<?> list) {
		PdfPTable body = new PdfPTable(new float[] { 100});
		body.setWidthPercentage(100);
		return body;
	}

}
