package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.creator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.nikitin.roadmaps.roadmapsbackendspring.utils.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Getter
public abstract class CreatorPDF implements BaseCreatorPDF {

	protected File file;
	protected Document document;
	protected PdfWriter pdfWriter;
	protected XMLParser xmlParser;

	private ElementList elements;

	protected BaseCreatorPDF create(File file, Rectangle pageSize) throws FileNotFoundException, DocumentException {
		this.document = new Document(pageSize);
		this.xmlParser = new XMLParser();
		this.elements = new ElementList();
		this.file = file;
		this.pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));

		this.document.open();

		log.info("The document has started to be created");

		return this;
	}

	@Override
	public List<Element> parse(String value) throws IOException {
		elements.clear();

		xmlParser.parse(new ByteArrayInputStream(
				value.getBytes())
		);

		return elements;
	}

	@Override
	public List<Element> parseWithFixClosingTag(String value, String... tags) throws IOException {
		return parse(fixClosingTag(value, tags));
	}

	@Override
	public BaseCreatorPDF configureXmlParser(BiFunction<ElementList, CreatorPDF, XMLParser> function) {
		xmlParser = function.apply(elements, this);

		return this;
	}

	@Override
	public BaseCreatorPDF configure(Function<CreatorPDF, Document> function) {
		document = function.apply(this);

		return this;
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

		log.info("Document Created");
	}

	private String fixClosingTag(String value, String... tags) {
		var finalValue = new StringBuilder(value);

		for (var tag : tags) {
			var jsoupElements = Jsoup.parse(finalValue.toString())
					.getElementsByTag(tag);

			for (var jsoupElement : jsoupElements) {
				var oldTag = jsoupElement.outerHtml();
				var fixedTag = jsoupElement.outerHtml().replace(">", "/>");

				var tempValue = finalValue.toString().replace(oldTag, fixedTag);

				finalValue = new StringBuilder(
						StringUtils.replaceAll(
								tempValue,
								oldTag,
								fixedTag
						)
				);
			}
		}

		return finalValue.toString();
	}
}
