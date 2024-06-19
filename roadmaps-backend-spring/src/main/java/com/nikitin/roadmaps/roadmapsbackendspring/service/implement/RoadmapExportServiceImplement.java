package com.nikitin.roadmaps.roadmapsbackendspring.service.implement;

import com.itextpdf.text.*;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.html.HTML;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapQuestionFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.Roadmap;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapChapter;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapQuestion;
import com.nikitin.roadmaps.roadmapsbackendspring.entity.RoadmapTopic;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.InternalServerException;
import com.nikitin.roadmaps.roadmapsbackendspring.service.*;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.RoadmapCreatorPDF;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.creator.CreatorPDF;
import com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.event.PageEventPDF;
import com.nikitin.roadmaps.roadmapsbackendspring.utils.constants.FormatConstant;
import com.nikitin.roadmaps.roadmapsbackendspring.utils.constants.PathConstant;
import com.nikitin.roadmaps.roadmapsbackendspring.utils.enums.Fonts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoadmapExportServiceImplement implements RoadmapExportService {

	private static final String TEMP_FILE_NAME_PDF = "roadmap_%s_%s";

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
					.addPageEvent(new PageEventPDF())
					.addContent((creatorPDF, elements) -> {
						try {
							addTitle(
									roadmap,
									elements
							);

							addMainContent(
									roadmapChapters,
									roadmapTopics,
									roadmapQuestions,
									elements,
									creatorPDF
							);
						} catch (IOException | DocumentException exception) {
							throw new InternalServerException(exception.getMessage());
						}

						return elements;
					})
					.closeDocument();

		} catch (Exception exception) {
			throw new InternalServerException(exception.getMessage());
		}

		return new FileSystemResource(fileForExport);
	}

	private File generateTempFile(String fileName, Long fileNumber) {
		return new File(
				PathConstant.TEMP_PATH +
						String.format(TEMP_FILE_NAME_PDF, fileName, fileNumber) +
						FormatConstant.PDF
		);
	}

	private void addTitle(Roadmap roadmap, ElementList elements) throws IOException, DocumentException {
		var roadmapName = new Paragraph(
				roadmap.getName(),
				Fonts.FREE_SANS.create(50, 1)
		);

		roadmapName.setAlignment(Element.ALIGN_CENTER);
		roadmapName.setLeading(210);

		var roadmapAuthor = new Paragraph(
				"Автор : " + roadmap.getProfile().getFullName(),
				Fonts.FREE_SANS.create(20, 50)
		);
		roadmapAuthor.setFirstLineIndent(240);
		roadmapAuthor.setLeading(445);

		var documentCreatedDate = new Paragraph(
				"Дата создания : " + LocalDate.now(),
				Fonts.FREE_SANS.create(20, 50)
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
		creatorPDF.getDocument().newPage();

		for (var roadmapChapter : roadmapChapters) {
			var roadmapChapterName = new Paragraph(
					roadmapChapter.getName(),
					Fonts.FREE_SANS.create(40, 1)
			);
			roadmapChapterName.setAlignment(Element.ALIGN_CENTER);
			roadmapChapterName.setSpacingAfter(1000);

			var roadmapTopicsForChapter = roadmapTopics.stream()
					.filter(roadmapTopic -> roadmapTopic.getRoadmapChapter().equals(roadmapChapter))
					.collect(Collectors.toList());

			for (var roadmapTopicForChapter : roadmapTopicsForChapter) {
				var roadmapTopicName = new Paragraph(
						roadmapTopicForChapter.getName(),
						Fonts.FREE_SANS.create(40, Font.UNDERLINE)
				);
				roadmapTopicName.setAlignment(Element.ALIGN_CENTER);

				var roadmapQuestionsForTopic = roadmapQuestions.stream()
						.filter(roadmapQuestion -> roadmapQuestion.getRoadmapTopic().equals(roadmapTopicForChapter))
						.collect(Collectors.toList());

				for (var roadmapQuestionForTopic : roadmapQuestionsForTopic) {
					var roadmapQuestionName = new Paragraph(
							roadmapQuestionForTopic.getQuestion() + " :",
							Fonts.FREE_SANS.create(25, Font.BOLD)
					);
					roadmapQuestionName.setSpacingAfter(20);

					roadmapTopicName.add(roadmapQuestionName);

					roadmapTopicName.addAll(
							creatorPDF.parseWithFixClosingTag(
									roadmapQuestionForTopic.getAnswer(),
									HTML.Tag.IMG
							)
					);
				}

				roadmapChapterName.add(roadmapTopicName);
			}

			elements.add(roadmapChapterName);
		}
	}
}
