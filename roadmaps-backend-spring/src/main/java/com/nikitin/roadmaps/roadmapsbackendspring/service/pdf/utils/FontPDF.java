package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import lombok.experimental.UtilityClass;

import java.io.IOException;

import static com.itextpdf.text.pdf.BaseFont.SYMBOL;

@UtilityClass
public class FontPDF {

	public static final String TIMES_NEW_ROMAN = "/assets/Times_New_Roman.ttf";
	public static final String FREE_SANS = "/assets/Free_Sans.ttf";


	public Font buildFont(String fontName, Integer size, Integer font) throws IOException, DocumentException {
		return new Font(
				BaseFont.createFont(fontName, "cp1251", BaseFont.EMBEDDED),
				size,
				font
		);
	}

	public Font buildFont(String fontName) {
		return FontFactory.getFont(fontName, "cp1251", true);
	}
}
