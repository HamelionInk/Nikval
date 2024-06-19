package com.nikitin.roadmaps.roadmapsbackendspring.utils.enums;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.nikitin.roadmaps.roadmapsbackendspring.utils.constants.FormatConstant;
import com.nikitin.roadmaps.roadmapsbackendspring.utils.constants.PathConstant;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public enum Fonts {

	FREE_SANS(PathConstant.FONT_PATH, "Free_Sans", FormatConstant.TTF);

	Fonts(String path, String name, String format) {
		this.path = path;
		this.name = name;
		this.format = format;
	}

	final String path;
	final String name;
	final String format;

	@SneakyThrows
	public Font create(Integer size, Integer style) {
		var fullFontPath = this.getPath() + this.getName() + this.getFormat();

		return new Font(
				BaseFont.createFont(fullFontPath, "cp1251", BaseFont.EMBEDDED),
				size,
				style
		);
	}
}
