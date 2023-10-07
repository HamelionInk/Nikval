package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.nikitin.roadmaps.client.RoadmapChapterClient;
import com.nikitin.roadmaps.component.CustomDialog;
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
public class CreateChapterDialog extends CustomDialog {

    private TextField nameChapter = new TextField();
    private Button createButton = new Button();

    private Long roadmapId;
    private RoadmapChapterClient roadmapChapterClient;
    private RoadmapView roadmapView;

    public CreateChapterDialog(RoadmapChapterClient roadmapChapterClient) {
        this.roadmapChapterClient = roadmapChapterClient;

        configurationComponents();
    }

    private void configurationComponents() {
        nameChapter.setLabel("Name chapter");
        nameChapter.addClassName("dialog_name_text_field");

        createButton.setText("Create");
        createButton.addClassName("dialog_create_button");
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

        getContentLayout().add(nameChapter, createButton);
        setHeaderNameText("Create chapter");
    }
}
