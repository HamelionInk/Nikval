package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.nikitin.roadmaps.client.RoadmapChapterClient;
import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.views.roadmaps.RoadmapView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CreateChapterDialog extends Dialog {

    private H3 headerName = new H3();
    private TextField nameChapter;
    private Button closeButton;
    private Button createButton;

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

        getHeader().add(headerName, closeButton);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassName("dialog_vertical_layout");
        verticalLayout.add(nameChapter, createButton);
        add(verticalLayout);
    }

    private void configurationComponents() {
        headerName = new H3("Создать раздел");
        headerName.addClassName("header_name_h3");

        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClassName("close_button");
        closeButton.addClickListener(event -> close());

        nameChapter = new TextField("Название");
        nameChapter.addClassName("name_chapter_text_field");

        createButton = new Button("Создать");
        createButton.addClassName("create_button");
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
