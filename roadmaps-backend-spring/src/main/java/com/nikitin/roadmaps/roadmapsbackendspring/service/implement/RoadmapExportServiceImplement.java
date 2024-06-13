package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.itextpdf.text.*;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
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
import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import com.nikitin.roadmaps.roadmapsbackendspring.service.*;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.RoadmapCreatorPDF;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.converter.ImageTagConverter;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.creator.CreatorPDF;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.event.PageEventPDF;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.utils.FontPDF;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoadmapExportServiceImplement implements RoadmapExportService {

	private static final String TEMP_PATH = System.getProperty("java.io.tmpdir");
	private static final String TEMP_FILE_NAME_PDF = "roadmap_%s_%s.pdf";

	private final RoadmapService roadmapService;
	private final RoadmapChapterService roadmapChapterService;
	private final RoadmapTopicService roadmapTopicService;
	private final RoadmapQuestionService roadmapQuestionService;

	@Override
	public Resource exportPDF(@NonNull Long id) {
		var roadmap = roadmapService.getEntityById(id);
		var roadmapChapters = roadmapChapterService.getAllEntity(RoadmapChapterFilter.builder()
				.roadmapIds(List.of(id))
				.build(), Sort.unsorted()
		);
		var roadmapTopics = roadmapTopicService.getAllEntity(RoadmapTopicFilter.builder()
				.roadmapChapterIds(roadmapChapters.stream()
						.map(RoadmapChapter::getId)
						.collect(Collectors.toList())
				)
				.build(), Sort.unsorted()
		);
		var roadmapQuestions = roadmapQuestionService.getAllEntity(RoadmapQuestionFilter.builder()
				.roadmapTopicIds(roadmapTopics.stream()
						.map(RoadmapTopic::getId)
						.collect(Collectors.toList())
				)
				.build(), Sort.unsorted()
		);
		var fileForExport = generateTempFile(roadmap.getName(), roadmap.getId());

		try {
			RoadmapCreatorPDF
					.createDocument(fileForExport, PageSize.A4)
					.configureXmlParser(this::configureXmlParser)
					.addPageEvent(new PageEventPDF())
					.addContent((creatorPDF, elements) -> {
						try {
							addTitle(roadmap, elements);
							addMainContent(roadmapChapters, roadmapTopics, roadmapQuestions, elements, creatorPDF);
						} catch (IOException | DocumentException e) {
							e.printStackTrace();
						}

						return elements;
					})
					.closeDocument();

		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}

		return new FileSystemResource(fileForExport);
	}

	private File generateTempFile(String fileName, Long fileNumber) {
		return new File(
				TEMP_PATH + String.format(TEMP_FILE_NAME_PDF, fileName, fileNumber)
		);
	}

	private void addTitle(Roadmap roadmap, ElementList elements) throws IOException, DocumentException {
		var roadmapName = new Paragraph(
				roadmap.getName(),
				FontPDF.buildFont(FontPDF.FREE_SANS, 50, 1)
		);
		roadmapName.setAlignment(Element.ALIGN_CENTER);
		roadmapName.setLeading(210);

		var roadmapAuthor = new Paragraph(
				"Автор : " + roadmap.getProfile().getFullName(),
				FontPDF.buildFont(FontPDF.FREE_SANS, 20, 50)
		);
		roadmapAuthor.setFirstLineIndent(240);
		roadmapAuthor.setLeading(445);

		var documentCreatedDate = new Paragraph(
				"Дата создания : " + LocalDate.now(),
				FontPDF.buildFont(FontPDF.FREE_SANS, 20, 50)
		);
		documentCreatedDate.setFirstLineIndent(240);

		elements.addAll(List.of(
				roadmapName,
				roadmapAuthor,
				documentCreatedDate)
		);
	}

	private void addMainContent(List<RoadmapChapter> roadmapChapters,
								List<RoadmapTopic> roadmapTopics,
								List<RoadmapQuestion> roadmapQuestions,
								ElementList elements,
								CreatorPDF creatorPDF) throws IOException, DocumentException {
		for (var roadmapChapter : roadmapChapters) {
			var roadmapChapterName = new Paragraph(
					roadmapChapter.getName(),
					FontPDF.buildFont(FontPDF.FREE_SANS, 40, 1)
			);
			roadmapChapterName.setAlignment(Element.ALIGN_CENTER);
			roadmapChapterName.setSpacingAfter(1000);

			var roadmapTopicsForChapter = roadmapTopics.stream()
					.filter(roadmapTopic -> roadmapTopic.getRoadmapChapter().equals(roadmapChapter))
					.collect(Collectors.toList());

			for (var roadmapTopicForChapter : roadmapTopicsForChapter) {
				var roadmapTopicName = new Paragraph(
						roadmapTopicForChapter.getName(),
						FontPDF.buildFont(FontPDF.FREE_SANS, 30, Font.UNDERLINE)
				);
				roadmapTopicName.setAlignment(Element.ALIGN_CENTER);

				var roadmapQuestionsForTopic = roadmapQuestions.stream()
						.filter(roadmapQuestion -> roadmapQuestion.getRoadmapTopic().equals(roadmapTopicForChapter))
						.collect(Collectors.toList());

				for (var roadmapQuestionForTopic : roadmapQuestionsForTopic) {
					var roadmapQuestionName = new Paragraph(
							roadmapQuestionForTopic.getQuestion() + " :",
							FontPDF.buildFont(FontPDF.FREE_SANS, 25, Font.BOLD)
					);
					roadmapQuestionName.setSpacingAfter(20);

					roadmapTopicName.add(roadmapQuestionName);
					roadmapTopicName.addAll(creatorPDF.parse(roadmapQuestionForTopic.getAnswer()));
				}

				roadmapChapterName.add(roadmapTopicName);
			}

			elements.add(roadmapChapterName);
		}
	}

	private XMLParser configureXmlParser(ElementList elements) {
		final var cssFiles = new CssFilesImpl();
		cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());

		final var cssResolver = new StyleAttrCSSResolver(cssFiles);

		final var tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
		tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
		tagProcessorFactory.addProcessor(new ImageTagConverter(), HTML.Tag.IMG);

		final var htmlPipelineContext = new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider()));
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
