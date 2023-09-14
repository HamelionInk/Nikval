package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.nikitin.roadmaps.client.RoadmapChapterClient;
import com.nikitin.roadmaps.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.views.roadmaps.RoadmapView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class CreateChapterDialog extends Dialog {

    private Paragraph headerName = new Paragraph();
    private TextField nameChapter = new TextField();
    private Button closeButton = new Button();
    private Button createButton = new Button();

    private Long roadmapId;
    private RoadmapChapterClient roadmapChapterClient;
    private RoadmapView roadmapView;

    public CreateChapterDialog(RoadmapChapterClient roadmapChapterClient) {
        this.roadmapChapterClient = roadmapChapterClient;

        configurationComponents();
        configurationCreateChapterDialog();
    }

    private void configurationCreateChapterDialog() {
        addClassName("create_chapter_dialog");
        setWidth("450px");
        setDraggable(true);

        var headerDiv = new Div();
        headerDiv.setClassName("question_header_div");
        headerDiv.add(headerName, closeButton);

        getHeader().add(headerDiv);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassName("dialog_vertical_layout");
        verticalLayout.add(nameChapter, createButton);
        add(verticalLayout);
    }

    private void configurationComponents() {
        headerName.setText("Создать раздел");
        headerName.addClassName("header_name_h3");

        closeButton.setIcon(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClassName("close_button");
        closeButton.addClickListener(event -> close());

        nameChapter.setLabel("Название");
        nameChapter.addClassName("name_chapter_text_field");

        createButton.setText("Создать");
        createButton.addClassName("create_button");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.addClickListener(event -> {
            var response = roadmapChapterClient.create(RoadmapChapterRequestDto.builder()
                    .name(nameChapter.getValue())
                    .roadmapId(roadmapId)
                    .build(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                roadmapView.updateData(roadmapId);
                close();
            }
        });
    }
}
