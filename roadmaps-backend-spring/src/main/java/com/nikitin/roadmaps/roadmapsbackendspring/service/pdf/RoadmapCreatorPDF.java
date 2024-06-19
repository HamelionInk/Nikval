package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.converter.ImageTagConverter;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.creator.CreatorPDF;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.provider.WorkerFontProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;

public class RoadmapCreatorPDF extends CreatorPDF {

	private RoadmapCreatorPDF(File file, Rectangle pageSize) throws DocumentException, FileNotFoundException {
		create(file, pageSize);
		configure(this::configureDocument);
		configureXmlParser(this::configureXmlParser);
	}

	public static RoadmapCreatorPDF createDocument(File file, Rectangle pageSize) throws DocumentException, FileNotFoundException {
		return new RoadmapCreatorPDF(file, pageSize);
	}

	private Document configureDocument(CreatorPDF creatorPDF) {
		var document = creatorPDF.getDocument();

		document.setMargins(
				20,
				20,
				20,
				135
		);

		return document;
	}

	private XMLParser configureXmlParser(ElementList elements, CreatorPDF creatorPDF) {
		final var cssFiles = new CssFilesImpl();
		cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());

		final var cssResolver = new StyleAttrCSSResolver(cssFiles);

		final var tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
		tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
		tagProcessorFactory.addProcessor(new ImageTagConverter(), HTML.Tag.IMG);

		var fontProvider = new WorkerFontProvider();
		var url = Thread.currentThread().getContextClassLoader().getResource("assets/font/Free_Sans.ttf").getPath();

		fontProvider.register(url, "Free_Sans");

		final var htmlPipelineContext = new HtmlPipelineContext(new CssAppliersImpl(fontProvider));
		htmlPipelineContext
				.setAcceptUnknown(true)
				.autoBookmark(true)
				.setTagFactory(tagProcessorFactory);

		var end = new ElementHandlerPipeline(elements, null);
		final var htmlPipeline = new HtmlPipeline(htmlPipelineContext, end);
		final var pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

		final var xmlWorker = new XMLWorker(pipeline, true);

		return new XMLParser(true, xmlWorker, Charset.forName("cp1251"));
	}
}
