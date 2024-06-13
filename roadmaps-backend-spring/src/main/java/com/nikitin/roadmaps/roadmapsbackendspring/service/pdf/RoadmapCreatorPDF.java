package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.creator.CreatorPDF;

import java.io.File;
import java.io.FileNotFoundException;

public class RoadmapCreatorPDF extends CreatorPDF {

	private RoadmapCreatorPDF(File file, Rectangle pageSize) throws DocumentException, FileNotFoundException {
		create(file, pageSize);
	}

	public static RoadmapCreatorPDF createDocument(File file, Rectangle pageSize) throws DocumentException, FileNotFoundException {
		return new RoadmapCreatorPDF(file, pageSize);
	}
}
