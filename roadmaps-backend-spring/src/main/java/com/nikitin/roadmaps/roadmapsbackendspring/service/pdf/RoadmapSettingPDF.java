package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.parser.XMLParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapSettingPDF {

	private Font defaultFont;
	private Font titleFont;
	private String author;
	private XMLWorker xmlWorker;
	private XMLParser xmlParser;
}
