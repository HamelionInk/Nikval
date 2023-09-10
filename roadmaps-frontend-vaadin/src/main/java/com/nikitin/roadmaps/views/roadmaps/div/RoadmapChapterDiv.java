package com.nikitin.roadmaps.views.roadmaps.div;

import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapQuestionResponseDto;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapTopicResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.views.roadmaps.RoadmapView;
import com.nikitin.roadmaps.views.roadmaps.from.RoadmapQuestionFormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Getter
@Setter
public class RoadmapChapterDiv extends Div {

    private SplitLayout splitLayout = new SplitLayout();
    private Grid<RoadmapTopicResponseDto> primaryGrid = new Grid<>(RoadmapTopicResponseDto.class, false);
    private Grid<RoadmapQuestionResponseDto> secondaryGrid = new Grid<>(RoadmapQuestionResponseDto.class, false);
    private MenuBar primaryMenuBar = new MenuBar();
    private MenuBar secondaryMenuBar = new MenuBar();

    private Long selectedTopicId;
    private Long roadmapChapterId;
    private Long roadmapId;
    private RoadmapClient roadmapClient;

    public RoadmapChapterDiv(RoadmapClient roadmapClient) {
        this.roadmapClient = roadmapClient;

        configurationPrimaryGrid();
        configurationSecondaryGrid();

        configurationPrimaryMenuBar();
        configurationSecondaryMenuBar();

        configurationSplitLayout();
        configurationRoadmapBodyDiv();
    }

    private void configurationPrimaryGrid() {
        primaryGrid.setClassName("roadmap_primary_grid");
        primaryGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        primaryGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        primaryGrid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(item -> {
            setSelectedTopicId(item.getId());
            updateSecondaryGrid(getSelectedTopicId());
        }));

        Grid.Column<RoadmapTopicResponseDto> topicNameColumn = primaryGrid
                .addColumn(RoadmapTopicResponseDto::getName).setHeader("Название темы");

        Grid.Column<RoadmapTopicResponseDto> numberOfQuestionColumn = primaryGrid
                .addColumn(RoadmapTopicResponseDto::getNumberOfQuestion).setHeader("Количество вопросов");

        Grid.Column<RoadmapTopicResponseDto> progressColumn = primaryGrid
                .addComponentColumn(roadmapTopicResponseDto -> {
                    var progressBar = new ProgressBar();
                    progressBar.setMax(roadmapTopicResponseDto.getNumberOfQuestion());
                    progressBar.setValue(roadmapTopicResponseDto.getNumberExploredQuestion());

                    return progressBar;
                }).setHeader("Прогресс");
    }

    private void configurationSecondaryGrid() {
        secondaryGrid.setClassName("roadmap_secondary_grid");
        secondaryGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        Grid.Column<RoadmapQuestionResponseDto> questionColumn = secondaryGrid
                .addColumn(RoadmapQuestionResponseDto::getQuestion).setHeader("Вопрос");

        Grid.Column<RoadmapQuestionResponseDto> isExploredColumn = secondaryGrid
                .addColumn(RoadmapQuestionResponseDto::getIsExplored).setHeader("Статус");
    }

    private void configurationPrimaryMenuBar() {
        primaryMenuBar.addClassName("roadmap_menu_bar");
        primaryMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);

        var addTopic = new Icon(VaadinIcon.PLUS);
        addTopic.addClickListener(event -> {
            var response = roadmapClient.createTopic(getRoadmapId(), RoadmapTopicRequestDto.builder()
                    .name("Новая тема")
                    .roadmapQuestionRequestDtos(new ArrayList<>())
                    .build(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updatePrimaryGrid();
            }
        });

        var deleteTopic = new Icon(VaadinIcon.MINUS);
        deleteTopic.addClickListener(event -> {
            primaryGrid.getSelectedItems().stream().findFirst().ifPresent(item -> {
                var response = roadmapClient.deleteTopicById(item.getId(), true);
                if (response.getStatusCode().is2xxSuccessful()) {
                    updatePrimaryGrid();
                    updateSecondaryGrid(null);
                }
            });
        });

        var editTopic = new Icon(VaadinIcon.EDIT);
        editTopic.addClickListener(event -> {

        });

        Stream.of(addTopic, deleteTopic, editTopic).forEach(item -> primaryMenuBar.addItem(item));
    }

    private void configurationSecondaryMenuBar() {
        secondaryMenuBar.addClassName("roadmap_menu_bar");
        secondaryMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);

        var addQuestion = new Icon(VaadinIcon.PLUS);
        addQuestion.addClickListener(event -> {
            var response = roadmapClient.createQuestion(getSelectedTopicId(), RoadmapQuestionRequestDto.builder()
                    .question("Новый вопрос")
                    .answer("Новый ответ")
                    .isExplored(false)
                    .build(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updatePrimaryGrid();
                updateSecondaryGrid(getSelectedTopicId());
            }
        });

        var deleteQuestion = new Icon(VaadinIcon.MINUS);
        deleteQuestion.addClickListener(event -> secondaryGrid.getSelectedItems().stream().findFirst().ifPresent(item -> {
            var response = roadmapClient.deleteQuestionById(item.getId(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updatePrimaryGrid();
                updateSecondaryGrid(getSelectedTopicId());
            }
        }));

        var editQuestion = new Icon(VaadinIcon.EDIT);
        var exploredQuestion = new Icon(VaadinIcon.CHECK);

        Stream.of(addQuestion, deleteQuestion, editQuestion, exploredQuestion).forEach(item -> secondaryMenuBar.addItem(item));
    }

    private void configurationSplitLayout() {
        splitLayout.addClassName("roadmap_split_layout");
        splitLayout.addToPrimary(primaryMenuBar, primaryGrid);
        splitLayout.addToSecondary(secondaryMenuBar, secondaryGrid);
    }

    private void configurationRoadmapBodyDiv() {
        addClassName("roadmap_div_element");

        add(splitLayout);
    }

    public void updatePrimaryGrid() {
        var response = roadmapClient.getAllTopicByChapterId(getRoadmapChapterId(), true);
        if (response.getStatusCode().is2xxSuccessful()) {
            var responseBody = RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapTopicResponseDto.class);
            primaryGrid.setItems(responseBody.getRoadmapTopicResponseDtos());
        }
    }

    public void updateSecondaryGrid(Long topicId) {
        if (Objects.nonNull(topicId)) {
            var response = roadmapClient.getAllQuestionByTopicId(topicId, true);
            if (response.getStatusCode().is2xxSuccessful()) {
                var responseBody = RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapQuestionResponseDto.class);
                secondaryGrid.setItems(responseBody.getRoadmapQuestionResponseDtos());
            }
        } else {
            secondaryGrid.setItems();
        }
    }
}
