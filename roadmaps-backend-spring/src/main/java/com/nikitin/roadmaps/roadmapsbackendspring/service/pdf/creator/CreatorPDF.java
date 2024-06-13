package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.creator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.RoadmapSettingPDF;
import lombok.Getter;

import java.io.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
public abstract class CreatorPDF implements BaseCreatorPDF {

	protected File file;
	protected Document document;
	protected PdfWriter pdfWriter;
	protected XMLParser xmlParser;
	protected RoadmapSettingPDF roadmapSettingPDF;

	private ElementList elements;

	protected BaseCreatorPDF create(File file, Rectangle pageSize) throws FileNotFoundException, DocumentException {
		this.document = new Document(pageSize);
		this.xmlParser = new XMLParser();
		this.elements = new ElementList();
		this.file = file;
		this.pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));

		this.document.setMargins(20, 20, 20, 135);
		this.document.open();

		return this;
	}

	@Override
	public List<Element> parse(String value) throws IOException {
		elements.clear();
		xmlParser.parse(new ByteArrayInputStream(value.getBytes()));

		return elements;
	}

	@Override
	public BaseCreatorPDF configureXmlParser(Function<ElementList, XMLParser> function) {
		xmlParser = function.apply(elements);

		return this;
	}

	@Override
	public BaseCreatorPDF setting() {
		return null;
	}

	@Override
	public BaseCreatorPDF addPageEvent(PdfPageEventHelper event) {
		this.getPdfWriter().setPageEvent(event);

		return this;
	}

	@Override
	public BaseCreatorPDF addContent(BiFunction<CreatorPDF, ElementList, ElementList> function) throws DocumentException {
		var elements = function.apply(this, new ElementList());

		for (var element : elements) {
			document.add(element);
		}

		return this;
	}

	@Override
	public void closeDocument() {
		this.document.close();
		this.pdfWriter.close();
	}
}
