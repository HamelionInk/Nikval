package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.event;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.utils.ImagePDF;

import java.io.IOException;

public class PageEventPDF extends PdfPageEventHelper {

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			float width = document.getPageSize()
					.getWidth();
			float height = document.getPageSize()
					.getHeight();

			var backgroundImage = writer.getCurrentPageNumber() == 1 ?
					Image.getInstance(ClassLoader.getSystemResource(ImagePDF.FIRST_PAGE_BACKGROUND_PDF)) :
					Image.getInstance(ClassLoader.getSystemResource(ImagePDF.MAIN_PAGE_BACKGROUND_PDF));

			writer.getDirectContentUnder()
					.addImage(backgroundImage, width, 0, 0, height, 0, 0);
		} catch (DocumentException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
