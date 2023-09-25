package com.nikitin.roadmaps.views.roadmaps.div;

import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.client.RoadmapQuestionClient;
import com.nikitin.roadmaps.client.RoadmapTopicClient;
import com.nikitin.roadmaps.dto.filter.RoadmapTopicFilter;
import com.nikitin.roadmaps.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.dto.request.RoadmapTopicRequestDto;
import com.nikitin.roadmaps.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.dto.response.RoadmapTopicResponseDto;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapQuestionResponseDto;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapTopicResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.views.roadmaps.dialog.QuestionInfoDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

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
    private RoadmapTopicClient roadmapTopicClient;
    private RoadmapQuestionClient roadmapQuestionClient;

    public RoadmapChapterDiv(RoadmapClient roadmapClient, RoadmapTopicClient roadmapTopicClient, RoadmapQuestionClient roadmapQuestionClient) {
        this.roadmapClient = roadmapClient;
        this.roadmapTopicClient = roadmapTopicClient;
        this.roadmapQuestionClient = roadmapQuestionClient;

        configurationPrimaryGrid();
        configurationSecondaryGrid();

        configurationPrimaryMenuBar();
        configurationSecondaryMenuBar();

        configurationSplitLayout();
        configurationRoadmapBodyDiv();
    }

    private Component configurationEditedTopicNameColumn() {
        var editedTopicNameColumn = new TextField();
        editedTopicNameColumn.addClassName("edited_topic_name_column");

        var saveEditedTopicName = new Button(VaadinIcon.CHECK.create());
        saveEditedTopicName.addClassName("save_edited_topic_name");
        saveEditedTopicName.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY_INLINE);
        saveEditedTopicName.addClickListener(event -> {
            var response = roadmapTopicClient.patch(getSelectedTopicId(), RoadmapTopicRequestDto.builder()
                    .name(editedTopicNameColumn.getValue())
                    .build(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updatePrimaryGrid();
            }
        });

        var cancelEditedTopicName = new Button(VaadinIcon.CLOSE.create());
        cancelEditedTopicName.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);
        cancelEditedTopicName.addClassName("cancel_edited_topic_name");
        cancelEditedTopicName.addClickListener(event -> primaryGrid.getEditor().cancel());

        var buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button_layout");
        buttonLayout.add(saveEditedTopicName, cancelEditedTopicName);

        editedTopicNameColumn.setSuffixComponent(buttonLayout);
        editedTopicNameColumn.setWidthFull();

        return editedTopicNameColumn;
    }

    private Component configurationEditedQuestionColumn() {
        var editedQuestionColumn = new TextArea();
        editedQuestionColumn.addClassName("edited_question_column");

        var saveEditedQuestion = new Button(VaadinIcon.CHECK.create());
        saveEditedQuestion.addClassName("save_edited_topic_name");
        saveEditedQuestion.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY_INLINE);
        saveEditedQuestion.addClickListener(event -> secondaryGrid.getSelectedItems().stream().findFirst().ifPresent(item -> {
            var response = roadmapQuestionClient.patch(item.getId(), RoadmapQuestionRequestDto.builder()
                    .question(editedQuestionColumn.getValue())
                    .build(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updateSecondaryGrid(getSelectedTopicId());
            }
        }));

        var cancelEditedQuestion = new Button(VaadinIcon.CLOSE.create());
        cancelEditedQuestion.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);
        cancelEditedQuestion.addClassName("cancel_edited_topic_name");
        cancelEditedQuestion.addClickListener(event -> secondaryGrid.getEditor().cancel());

        var buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button_layout");
        buttonLayout.add(saveEditedQuestion, cancelEditedQuestion);

        editedQuestionColumn.setSuffixComponent(buttonLayout);
        editedQuestionColumn.setWidthFull();

        return editedQuestionColumn;
    }

    private void configurationPrimaryGrid() {
        primaryGrid.setClassName("roadmap_primary_grid");
        primaryGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        primaryGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        var primaryEditor = primaryGrid.getEditor();
        Binder<RoadmapTopicResponseDto> primaryBinder = new Binder<>(RoadmapTopicResponseDto.class);
        primaryEditor.setBinder(primaryBinder);
        primaryEditor.setBuffered(true);

        primaryGrid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(item -> {
            setSelectedTopicId(item.getId());
            updateSecondaryGrid(getSelectedTopicId());
        }));

        Grid.Column<RoadmapTopicResponseDto> topicNameColumn = primaryGrid
                .addColumn(RoadmapTopicResponseDto::getName).setHeader("Название темы").setWidth("240px");

        Grid.Column<RoadmapTopicResponseDto> numberOfQuestionColumn = primaryGrid
                .addColumn(RoadmapTopicResponseDto::getNumberOfQuestion).setHeader("Количество вопросов");

        Grid.Column<RoadmapTopicResponseDto> progressColumn = primaryGrid
                .addComponentColumn(roadmapTopicResponseDto -> {
                    var progressBar = new ProgressBar();
                    progressBar.setMax(roadmapTopicResponseDto.getNumberOfQuestion());
                    progressBar.setValue(roadmapTopicResponseDto.getNumberExploredQuestion());
                    if (progressBar.getValue() <= (progressBar.getMax() / 4)) {
                        progressBar.addThemeVariants(ProgressBarVariant.LUMO_ERROR);
                    }
                    if (progressBar.getValue() >= (progressBar.getMax() / 1.5)) {
                        progressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
                    }

                    return progressBar;
                }).setHeader("Прогресс");

        topicNameColumn.setEditorComponent(configurationEditedTopicNameColumn());
    }

    private void configurationSecondaryGrid() {
        secondaryGrid.setClassName("roadmap_secondary_grid");
        primaryGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        secondaryGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        var secondaryEditor = secondaryGrid.getEditor();
        Binder<RoadmapQuestionResponseDto> secondaryBinder = new Binder<>(RoadmapQuestionResponseDto.class);
        secondaryEditor.setBinder(secondaryBinder);
        secondaryEditor.setBuffered(true);

        secondaryGrid.addItemDoubleClickListener(event -> {
            var questionInfoDialog = new QuestionInfoDialog(event.getItem(), roadmapQuestionClient, this);
            questionInfoDialog.open();
        });

        Grid.Column<RoadmapQuestionResponseDto> questionColumn = secondaryGrid
                .addColumn(RoadmapQuestionResponseDto::getQuestion).setHeader("Вопрос");

        Grid.Column<RoadmapQuestionResponseDto> isExploredColumn = secondaryGrid
                .addComponentColumn(roadmapQuestionResponseDto -> {
                    if (roadmapQuestionResponseDto.getIsExplored()) {
                        return createStatusIcon(VaadinIcon.CHECK, "green");
                    } else {
                        return createStatusIcon(VaadinIcon.CLOSE, "red");
                    }
                }).setHeader("Статус")
                .setWidth("100px")
                .setFlexGrow(0);

        questionColumn.setEditorComponent(configurationEditedQuestionColumn());
    }

    private void configurationPrimaryMenuBar() {
        primaryMenuBar.addClassName("roadmap_menu_bar");
        primaryMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);

        var addTopic = new Icon(VaadinIcon.PLUS);
        addTopic.setColor("#86C232");
        addTopic.addClickListener(event -> {
            var response = roadmapTopicClient.create(RoadmapTopicRequestDto.builder()
                    .name("Новая тема")
                    .roadmapChapterId(getRoadmapChapterId())
                    .build(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updatePrimaryGrid();
            }
        });

        var deleteTopic = new Icon(VaadinIcon.MINUS);
        deleteTopic.setColor("#86C232");
        deleteTopic.addClickListener(event -> primaryGrid.getSelectedItems().stream().findFirst().ifPresent(item -> {
            var response = roadmapTopicClient.deleteById(item.getId(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updatePrimaryGrid();
                updateSecondaryGrid(null);
            }
        }));

        var editTopic = new Icon(VaadinIcon.EDIT);
        editTopic.setColor("#86C232");
        editTopic.addClickListener(event -> primaryGrid.getSelectedItems().stream().findFirst().ifPresent(item -> {
            var editor = primaryGrid.getEditor();

            if (editor.isOpen()) {
                editor.cancel();
            } else {
                primaryGrid.getEditor().editItem(item);
            }
        }));

        Stream.of(addTopic, deleteTopic, editTopic).forEach(item -> primaryMenuBar.addItem(item));
    }

    private void configurationSecondaryMenuBar() {
        secondaryMenuBar.addClassName("roadmap_menu_bar");
        secondaryMenuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);

        var addQuestion = new Icon(VaadinIcon.PLUS);
        addQuestion.setColor("#86C232");
        addQuestion.addClickListener(event -> {
            var response = roadmapQuestionClient.create(RoadmapQuestionRequestDto.builder()
                    .question("Новый вопрос")
                    .answer("Новый ответ")
                    .roadmapTopicId(getSelectedTopicId())
                    .isExplored(false)
                    .build(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updatePrimaryGrid();
                updateSecondaryGrid(getSelectedTopicId());
            }
        });

        var deleteQuestion = new Icon(VaadinIcon.MINUS);
        deleteQuestion.setColor("#86C232");
        deleteQuestion.addClickListener(event -> secondaryGrid.getSelectedItems().stream().findFirst().ifPresent(item -> {
            var response = roadmapQuestionClient.deleteById(item.getId(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updatePrimaryGrid();
                updateSecondaryGrid(getSelectedTopicId());
            }
        }));

        var editQuestion = new Icon(VaadinIcon.EDIT);
        editQuestion.setColor("#86C232");
        editQuestion.addClickListener(event -> secondaryGrid.getSelectedItems().stream().findFirst().ifPresent(item -> {
            var questionInfoDialog = new QuestionInfoDialog(item, roadmapQuestionClient, this);
            questionInfoDialog.setEditedStatus();
            questionInfoDialog.open();
        }));

        var exploredQuestion = new Icon(VaadinIcon.CHECK);
        exploredQuestion.setColor("#86C232");
        exploredQuestion.addClickListener(event -> secondaryGrid.getSelectedItems().stream().findFirst().ifPresent(item -> {
            var response = roadmapQuestionClient.patch(item.getId(), RoadmapQuestionRequestDto.builder()
                    .isExplored(!item.getIsExplored())
                    .build(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                updatePrimaryGrid();
                updateSecondaryGrid(item.getRoadmapTopicId());
            }
        }));

        Stream.of(addQuestion, deleteQuestion, editQuestion, exploredQuestion).forEach(item -> secondaryMenuBar.addItem(item));
    }

    private void configurationSplitLayout() {
        var searchTopics = new TextField();
        searchTopics.addClassName("roadmap_search");
        searchTopics.setPlaceholder("Поиск");
        searchTopics.setClearButtonVisible(true);
        searchTopics.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchTopics.addValueChangeListener(event -> {
            var response = roadmapTopicClient.getAll(RoadmapTopicFilter.builder()
                    .roadmapChapterId(getRoadmapChapterId())
                    .startWithName(event.getValue())
                    .build(), true);
            log.info(event.getValue());
            if (response.getStatusCode().is2xxSuccessful()) {
                var responseBody = RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapTopicResponseDto.class);
                primaryGrid.setItems(responseBody.getRoadmapTopicResponseDtos());
                if (!StringUtils.hasText(event.getValue())) {
                    updatePrimaryGrid();
                }
            }
        });

        var primaryMenu = new HorizontalLayout();
        primaryMenu.addClassName("roadmap_menu");
        primaryMenu.add(primaryMenuBar, searchTopics);

        var searchQuestions = new TextField();
        searchQuestions.addClassName("roadmap_search");
        searchQuestions.setPlaceholder("Поиск");
        searchQuestions.setClearButtonVisible(true);
        searchQuestions.setPrefixComponent(VaadinIcon.SEARCH.create());

        var secondaryMenu = new HorizontalLayout();
        secondaryMenu.addClassName("roadmap_menu");
        secondaryMenu.add(secondaryMenuBar, searchQuestions);

        splitLayout.addClassName("roadmap_split_layout");
        splitLayout.addToPrimary(primaryMenu, primaryGrid);
        splitLayout.addToSecondary(secondaryMenu, secondaryGrid);
    }

    private void configurationRoadmapBodyDiv() {
        addClassName("roadmap_div_element");

        add(splitLayout);
    }

    private Component createStatusIcon(VaadinIcon icon, String color) {
        var statusIcon = new Icon(icon);
        statusIcon.setClassName("question_status");
        statusIcon.setColor(color);

        return statusIcon;
    }

    public void updatePrimaryGrid() {
        var response = roadmapTopicClient.getAllByChapterId(getRoadmapChapterId(), true);
        if (response.getStatusCode().is2xxSuccessful()) {
            var responseBody = RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapTopicResponseDto.class);
            primaryGrid.setItems(responseBody.getRoadmapTopicResponseDtos());
        }
    }

    public void updateSecondaryGrid(Long topicId) {
        if (Objects.nonNull(topicId)) {
            var response = roadmapQuestionClient.getAllByTopicId(topicId, true);
            if (response.getStatusCode().is2xxSuccessful()) {
                var responseBody = RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapQuestionResponseDto.class);
                secondaryGrid.setItems(responseBody.getRoadmapQuestionResponseDtos());
            }
        } else {
            secondaryGrid.setItems();
        }
    }
}
