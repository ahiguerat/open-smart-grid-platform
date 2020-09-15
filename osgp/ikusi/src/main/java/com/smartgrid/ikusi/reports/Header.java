package com.smartgrid.ikusi.reports;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.ClassPathResource;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class Header extends Style {

	protected PdfPTable getHeader(String leyend) throws MalformedURLException, IOException, DocumentException {
		float padding = 3;
		
		PdfPTable header = new PdfPTable(new float[] { 30, 200 });
		String logo = "ikusi2.jpg";

		header.setWidthPercentage(100);
		header.setPaddingTop(0f);
		Image image = Image.getInstance(new ClassPathResource(logo).getURL());
		image.scaleAbsolute(63, 70);

		PdfPCell shield = new PdfPCell(image);
		shield.setPaddingBottom(padding);
		shield.setBorder(Rectangle.NO_BORDER);
		header.addCell(shield);
		
		PdfPCell title = super.getTitle(leyend, 1);
		title.setVerticalAlignment(Element.ALIGN_MIDDLE);
		title.setBorder(Rectangle.NO_BORDER);
		title.setPaddingBottom(padding);
		header.addCell(title);
		
	

		PdfPCell line = new PdfPCell(new Phrase(" "));
		line.setPadding(padding);
		line.setBorder(Rectangle.TOP);
		line.setBorderColor(super.thBackground);
		line.setColspan(2);
		line.setBorderWidth((float) 1.5);
		line.setPaddingBottom(0);

		header.addCell(line);

		return header;
	}

}
