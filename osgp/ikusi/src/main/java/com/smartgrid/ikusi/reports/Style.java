package com.smartgrid.ikusi.reports;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;

@SuppressWarnings("deprecation")
public class Style {
	
	protected final BaseColor titlePdf = WebColors.getRGBColor("#0EA353");
	protected final BaseColor thLabels = WebColors.getRGBColor("#ededed");
	protected final BaseColor thLabelsSubtable = WebColors.getRGBColor("#3a3535");
	protected final  BaseColor thBackground = WebColors.getRGBColor("#0EA353");
	protected final  BaseColor thBackgroundSubtable = WebColors.getRGBColor("#f4f4f4");
	protected final BaseColor tdLabels = WebColors.getRGBColor("#656565");
	protected final BaseColor lines = WebColors.getRGBColor("#f6f6f6");
	
	protected final Font fntTitlePdf = FontFactory.getFont(FontFactory.HELVETICA, 14,titlePdf);
	
	protected final Font fntTitleBoldPdf = new Font(FontFamily.HELVETICA, 14, Font.BOLD, titlePdf);
	
	protected final Font fntThLabelBold = new Font(FontFamily.HELVETICA, 12, Font.BOLD, thLabels);	
	protected final Font fntThLabel = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, thLabels);
	protected final Font fntThLabelSubtable = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, thLabelsSubtable);
	
	protected final Font fntTdLabelBold = new Font(FontFamily.HELVETICA, 10, Font.BOLD, tdLabels);	
	protected final Font fntTdLabel = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, tdLabels);
	
	protected float padding = 7;
	
	
	protected Phrase getStylePhrase(String label, String msg) {
		Phrase lbl = new Phrase(label, fntThLabel);
		Chunk lblRes = new Chunk(msg, fntTdLabel);
		lbl.add(lblRes);
		return lbl;
	}
	
	protected Phrase getStylePhraseBold(String label, String msg) {
		Phrase lbl = new Phrase(label, fntThLabelBold);
		Chunk lblRes = new Chunk(msg, fntTdLabelBold);
		lbl.add(lblRes);
		return lbl;
	}
	
	protected PdfPCell getTitle(String msg, int colspan) {		
		PdfPCell cell =  new PdfPCell(new Phrase(msg, this.fntTitleBoldPdf));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPadding(this.padding);				
		if (colspan > 1 )
			cell.setColspan(colspan);		
		return cell;
	}
	
	protected PdfPCell getTitleTable(String msg, int colspan, int align) {
		PdfPCell cell =  new PdfPCell(new Phrase(msg, this.fntThLabel));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setBorderColor(this.thLabels);
		cell.setPadding(this.padding);
		cell.setHorizontalAlignment(align);
		cell.setBackgroundColor(this.thBackground);
		if (colspan > 1 )
			cell.setColspan(colspan);		
		return cell;
	}
	
	protected PdfPCell getSubTitleTable(String msg, int colspan, int align) {
		PdfPCell cell =  new PdfPCell(new Phrase(msg, this.fntThLabelSubtable));
		cell.setBorder(Rectangle.BOTTOM);
		cell.setBorderColor(this.thBackground);
		cell.setPadding(this.padding);
		cell.setHorizontalAlignment(align);
		cell.setBackgroundColor(this.thBackgroundSubtable);
		if (colspan > 1 )
			cell.setColspan(colspan);		
		return cell;
	}
	
	protected PdfPCell cell(String msg, int colspan, int align) {		
		PdfPCell info =  new PdfPCell(new Phrase(msg, this.fntTdLabel));
		info.setPaddingBottom(this.padding);
		info.setPaddingTop(this.padding);				
		info.setBorder(Rectangle.BOTTOM);
		info.setBorderColor(this.lines);									
		info.setHorizontalAlignment(align);
		if(colspan > 1)
			info.setColspan(colspan);
		return info;
	}
	
	
	protected PdfPCell cellBold(String msg, int colspan) {
		this.fntTdLabel.setSize(10);
		PdfPCell info =  new PdfPCell(new Phrase(msg, this.fntTdLabel));
		info.setPaddingBottom(this.padding);
		info.setPaddingTop(this.padding);
		info.setBorder(Rectangle.BOTTOM);
		info.setBorderColor(this.lines);
		info.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		if(colspan > 1)
			info.setColspan(colspan);
		return info;
	}

}
