package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.creator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.parser.XMLParser;

import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface BaseCreatorPDF {

	BaseCreatorPDF configureXmlParser(BiFunction<ElementList, CreatorPDF, XMLParser> function);

	BaseCreatorPDF addPageEvent(PdfPageEventHelper event);

	BaseCreatorPDF addContent(BiFunction<CreatorPDF, ElementList, ElementList> function) throws DocumentException;

	BaseCreatorPDF configure(Function<CreatorPDF, Document> function);

	List<Element> parse(String value) throws IOException;

	List<Element> parseWithFixClosingTag(String value, String... tags) throws IOException;

	void closeDocument();
}