package com.smartgrid.ikusi.reports;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("deprecation")
public class Footer extends PdfPageEventHelper {
	BaseColor colorMachineRow = WebColors.getRGBColor("#587474");
	Font colorFontMachineRow = new Font(FontFamily.HELVETICA, 10, Font.BOLD, colorMachineRow);
	private PdfTemplate t;

	public void onOpenDocument(PdfWriter writer, Document document) {
		t = writer.getDirectContent().createTemplate(30, 16);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		float width = document.getPageSize().getWidth();
		addFooter(writer, width);
	}

	private void addFooter(PdfWriter writer, float width) {
		PdfPTable footer = new PdfPTable(new float[] { 96, 4 });
		try {
			footer.setTotalWidth(width - 40);
			footer.setLockedWidth(false);

			PdfPCell info = new PdfPCell(
					new Phrase(String.format("Page %d /", writer.getPageNumber()), this.colorFontMachineRow));
			// info.setPaddingBottom(-3f);
			info.setBorder(Rectangle.NO_BORDER);
			info.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			footer.addCell(info);

			info = new PdfPCell(Image.getInstance(t));
			info.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			info.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			info.setBorder(Rectangle.NO_BORDER);
			footer.addCell(info);
			// write page
			footer.writeSelectedRows(0, -1, 34, 35, writer.getDirectContent());

		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}

	}

	public void onCloseDocument(PdfWriter writer, Document document) {
		int pageNumber = writer.getPageNumber();
		ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
				new Phrase(String.valueOf(pageNumber), this.colorFontMachineRow), 2, 4, 0);
	}
}
