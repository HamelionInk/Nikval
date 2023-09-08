/* package com.nikitin.roadmaps.views.roadmaps;

import com.nikitin.roadmaps.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.views.MainLayout;
import com.nikitin.roadmaps.views.roadmaps.dialog.CreateChapterDialog;
import com.nikitin.roadmaps.views.roadmaps.from.RoadmapQuestionFormLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@PageTitle("Create")
@Route(value = "create-roadmap", layout = MainLayout.class)
@RolesAllowed(value = {"ROLE_USER"})
public class CreateRoadmapView extends VerticalLayout {

    private H3 viewNameH1;
    private TextField roadmapNameTextField;
    private CreateChapterDialog createChapterDialog;
    private Button createChapterButton;
    private Accordion accordion;

    private List<RoadmapsThemeRequestDto> roadmapsThemeRequestDtos;
    private List<RoadmapQuestionRequestDto> roadmapQuestionRequestDtos;
    private RoadmapsThemeRequestDto roadmapsThemeRequestDto;

    public CreateRoadmapView() {
        addClassName("create_roadmap_view");

        roadmapsThemeRequestDtos = new ArrayList<>();
        roadmapQuestionRequestDtos = new ArrayList<>();
        roadmapsThemeRequestDto = new RoadmapsThemeRequestDto();

        buildComponents();
        buildAccordionPanel();

        var headerRoadmapDiv = new Div();
        headerRoadmapDiv.addClassName("header_roadmap_div");
        headerRoadmapDiv.add(viewNameH1, roadmapNameTextField);

        var bodyRoadmapDiv = new Div();
        bodyRoadmapDiv.addClassName("body_roadmap_div");
        bodyRoadmapDiv.add(accordion, createChapterButton);

        add(headerRoadmapDiv, bodyRoadmapDiv);
    }

    private void buildComponents() {
        createChapterDialog = new CreateChapterDialog();

        viewNameH1 = new H3("Создание новой карты развития");
        viewNameH1.addClassNames("view_name_h3");

        roadmapNameTextField = new TextField();
        roadmapNameTextField.addClassName("roadmap_name_text_field");
        roadmapNameTextField.setPlaceholder("Название");

        createChapterButton = new Button("Создать раздел");
        createChapterButton.addClassName("create_chapter_button");
        createChapterButton.addClickListener(event -> createChapterDialog.open());
        createChapterDialog.getCreateButton().addClickListener(event -> {
            var chapterName = createChapterDialog.getNameChapterTextField().getValue();
            createChapter(chapterName);
            createChapterDialog.close();
        });
    }

    private void buildAccordionPanel() {
        accordion = new Accordion();
        accordion.addClassName("accordion_panel");
        accordion.setVisible(false);
    }

    private void createChapter(String name) {
        var createPrimaryThemeItem = createIcon(VaadinIcon.PLUS);
        var editPrimaryThemeItem = createIcon(VaadinIcon.EDIT);
        var deletePrimaryThemeItem = createIcon(VaadinIcon.MINUS);
        AtomicInteger themeCount = new AtomicInteger();

        var primaryMenuBar = createMenuBar(createPrimaryThemeItem, editPrimaryThemeItem, deletePrimaryThemeItem);
        var primaryGrid = createPrimaryGrid();

        deletePrimaryThemeItem.addClickListener(event -> primaryGrid.getSelectedItems()
                .forEach(roadmapsThemeRequestDto -> {
                    roadmapsThemeRequestDtos.remove(roadmapsThemeRequestDto);
                    primaryGrid.setItems(roadmapsThemeRequestDtos);
                }));

        editPrimaryThemeItem.addClickListener(event -> primaryGrid.getSelectedItems()
                .forEach(roadmapsThemeRequestDto -> {
                    if (primaryGrid.getEditor().isOpen()) {
                        primaryGrid.getEditor().cancel();
                    } else {
                        primaryGrid.getEditor().editItem(roadmapsThemeRequestDto);
                    }
                }));

        createPrimaryThemeItem.addClickListener(event -> {
            themeCount.getAndIncrement();

            roadmapsThemeRequestDtos.add(RoadmapsThemeRequestDto.builder()
                    .name("Название темы " + themeCount)
                    .build());

            primaryGrid.setItems(roadmapsThemeRequestDtos);
        });


        var createSecondaryThemeItem = createIcon(VaadinIcon.PLUS);
        var editSecondaryThemeItem = createIcon(VaadinIcon.EDIT);
        var deleteSecondaryThemeItem = createIcon(VaadinIcon.MINUS);
        var learnedSecondaryThemeItem = createIcon(VaadinIcon.CHECK);
        AtomicInteger questionCount = new AtomicInteger();

        var secondaryMenuBar = createMenuBar(createSecondaryThemeItem, editSecondaryThemeItem, deleteSecondaryThemeItem, learnedSecondaryThemeItem);
        var secondaryGrid = createSecondaryGrid();

        deleteSecondaryThemeItem.addClickListener(event -> secondaryGrid.getSelectedItems()
                .forEach(roadmapQuestionRequestDto -> {
                    roadmapQuestionRequestDtos.remove(roadmapQuestionRequestDto);
                    this.roadmapsThemeRequestDto.setQuestionsNumber(roadmapQuestionRequestDtos.size());

                    secondaryGrid.setItems(roadmapQuestionRequestDtos);
                    primaryGrid.setItems(roadmapsThemeRequestDtos);
                }));

        editSecondaryThemeItem.addClickListener(event -> secondaryGrid.getSelectedItems()
                .forEach(roadmapQuestionRequestDto -> {
                    if (secondaryGrid.getEditor().isOpen()) {
                        secondaryGrid.getEditor().cancel();
                    } else {
                        secondaryGrid.getEditor().editItem(roadmapQuestionRequestDto);
                    }
                }));

        createSecondaryThemeItem.addClickListener(event -> {
            questionCount.getAndIncrement();

            roadmapQuestionRequestDtos.add(RoadmapQuestionRequestDto.builder()
                    .question("Вопрос " + questionCount)
                    .answer("Ответ " + questionCount)
                    .build());

            primaryGrid.getSelectedItems()
                    .forEach(roadmapsThemeRequestDto -> this.roadmapsThemeRequestDto = roadmapsThemeRequestDto);

            this.roadmapsThemeRequestDto.setQuestionsNumber(roadmapQuestionRequestDtos.size());
            primaryGrid.setItems(roadmapsThemeRequestDtos);

            secondaryGrid.setItems(roadmapQuestionRequestDtos);
        });

        learnedSecondaryThemeItem.addClickListener(event -> secondaryGrid.getSelectedItems()
                .forEach(roadmapQuestionRequestDto -> {
                    roadmapQuestionRequestDto.setIsLearned(!roadmapQuestionRequestDto.getIsLearned());
                }));


        primaryGrid.addCellFocusListener(event -> event.getItem()
                .ifPresent(item -> {
                    roadmapQuestionRequestDtos = item.getQuestions();
                    secondaryGrid.setItems(roadmapQuestionRequestDtos);
                }));

        var splitLayout = new SplitLayout();

        accordion.add(createAccordionPanel(name, splitLayout));
        accordion.setVisible(true);
        splitLayout.addToPrimary(primaryMenuBar, primaryGrid);
        splitLayout.addToSecondary(secondaryMenuBar, secondaryGrid);
    }

    private Grid<RoadmapsThemeRequestDto> createPrimaryGrid() {
        var primaryGrid = new Grid<>(RoadmapsThemeRequestDto.class, false);
        primaryGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        primaryGrid.setItems(roadmapsThemeRequestDtos);

        var primaryEditor = primaryGrid.getEditor();
        Binder<RoadmapsThemeRequestDto> primaryBinder = new Binder<>(RoadmapsThemeRequestDto.class);
        primaryEditor.setBinder(primaryBinder);
        primaryEditor.setBuffered(true);

        Grid.Column<RoadmapsThemeRequestDto> themeNameColumn = primaryGrid
                .addColumn(RoadmapsThemeRequestDto::getName).setHeader("Название темы");

        Grid.Column<RoadmapsThemeRequestDto> questionNumberColumn = primaryGrid
                .addColumn(RoadmapsThemeRequestDto::getQuestionsNumber).setHeader("Количество вопросов");

        Grid.Column<RoadmapsThemeRequestDto> progressColumn = primaryGrid
                .addComponentColumn(roadmapsThemeRequestDto -> new ProgressBar()).setHeader("Прогресс");

        var themeNameEditField = new TextField();
        themeNameEditField.setWidthFull();
        var saveButton = new Button("Сохранить", event -> primaryEditor.save());
        var cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> primaryEditor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout horizontalLayout = new HorizontalLayout(saveButton, cancelButton);

        primaryBinder.forField(themeNameEditField)
                .asRequired("Имя не может быть пустым")
                .bind(RoadmapsThemeRequestDto::getName, RoadmapsThemeRequestDto::setName);
        themeNameColumn.setEditorComponent(themeNameEditField);
        progressColumn.setEditorComponent(horizontalLayout);

        return primaryGrid;
    }

    private Grid<RoadmapQuestionRequestDto> createSecondaryGrid() {
        var secondaryGrid = new Grid<>(RoadmapQuestionRequestDto.class, false);
        secondaryGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        secondaryGrid.setItems(roadmapQuestionRequestDtos);
        secondaryGrid.setItemDetailsRenderer(new ComponentRenderer<>(RoadmapQuestionFormLayout::new, RoadmapQuestionFormLayout::setRoadmapQuestion));
        secondaryGrid.setDetailsVisibleOnClick(false);

        var secondaryEditor = secondaryGrid.getEditor();
        Binder<RoadmapQuestionRequestDto> secondaryBinder = new Binder<>(RoadmapQuestionRequestDto.class);
        secondaryEditor.setBinder(secondaryBinder);
        secondaryEditor.setBuffered(true);

        Grid.Column<RoadmapQuestionRequestDto> questionNameColumn = secondaryGrid
                .addColumn(RoadmapQuestionRequestDto::getQuestion).setHeader("Вопросы");

        Grid.Column<RoadmapQuestionRequestDto> detailsColumn = secondaryGrid
                .addComponentColumn(roadmapQuestionRequestDto -> {
                    var detailsButton = new Button("Подробности");
                    detailsButton.addClickListener(event -> secondaryGrid.setDetailsVisible(roadmapQuestionRequestDto, !secondaryGrid.isDetailsVisible(roadmapQuestionRequestDto)));

                    return detailsButton;
                }).setWidth("200px").setFlexGrow(0);

        var questionEditField = new TextField();
        questionEditField.setWidthFull();
        var saveButton = new Button("Сохранить", event -> secondaryEditor.save());
        var cancelButton = new Button(VaadinIcon.CLOSE.create(), event -> secondaryEditor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout horizontalLayout = new HorizontalLayout(saveButton, cancelButton);

        secondaryBinder.forField(questionEditField)
                .asRequired("Имя не может быть пустым")
                .bind(RoadmapQuestionRequestDto::getQuestion, RoadmapQuestionRequestDto::setQuestion);
        questionNameColumn.setEditorComponent(questionEditField);
        detailsColumn.setEditorComponent(horizontalLayout);

        return secondaryGrid;
    }

    private AccordionPanel createAccordionPanel(String name, Component component) {
        var accordionPanel = new AccordionPanel(name, component);
        accordionPanel.addClassName("roadmap_accordion_panel");

        return accordionPanel;
    }

    private MenuBar createMenuBar(Icon... icons) {
        MenuBar menuBar = new MenuBar();
        menuBar.addClassName("accordion_panel_name_div");
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);

        Arrays.stream(icons)
                .forEach(menuBar::addItem);

        return menuBar;
    }

    private Icon createIcon(VaadinIcon iconName) {
        Icon icon = new Icon();
        return new Icon(iconName);
    }
}

 */
