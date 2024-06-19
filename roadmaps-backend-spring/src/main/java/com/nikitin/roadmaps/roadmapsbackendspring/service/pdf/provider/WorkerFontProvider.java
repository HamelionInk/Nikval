package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.provider;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.nikitin.roadmaps.roadmapsbackendspring.utils.enums.Fonts;

import java.util.Arrays;

public class WorkerFontProvider extends XMLWorkerFontProvider {

	public WorkerFontProvider() {
		Arrays.stream(Fonts.values())
				.forEach(font -> register(
						font.getPath() + font.getName(),
						font.getName()
				));
	}

	@Override
	public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
		return fontname == null ?
				super.getFont(Fonts.FREE_SANS.getName(), encoding, embedded, size, style, color) :
				super.getFont(fontname, encoding, embedded, size, style, color);
	}
}
