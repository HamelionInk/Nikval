package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.component.CustomDialog;
import com.nikitin.roadmaps.dto.filter.RoadmapFilter;
import com.nikitin.roadmaps.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.views.roadmaps.RoadmapInfoView;
import com.vaadin.flow.component.UI;
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

import java.util.List;

@Slf4j
@Getter
@Setter
public class CreateRoadmapDialog extends CustomDialog {

    private final TextField nameRoadmap = new TextField();
    private final Button createButton = new Button();

    private RoadmapClient roadmapClient;
    private RoadmapInfoView roadmapInfoView;

    public CreateRoadmapDialog(RoadmapClient roadmapClient) {
        this.roadmapClient = roadmapClient;

        configurationComponents();
    }

    private void configurationComponents() {
        nameRoadmap.addClassName("dialog_name_text_field");
        nameRoadmap.setLabel("Name roadmap");

        createButton.addClassName("dialog_create_button");
        createButton.setText("Create");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.addClickListener(event -> {
            var response = roadmapClient.create(RoadmapRequestDto.builder()
                    .name(nameRoadmap.getValue())
                    .profileId((long) UI.getCurrent().getSession().getAttribute("profileId"))
                    .custom(true)
                    .build(), true);

            if (response.getStatusCode().is2xxSuccessful()) {
                roadmapInfoView.getRoadmapsGrid().setItems(roadmapInfoView.getAll(RoadmapFilter.builder()
                        .profileIds(List.of((long) UI.getCurrent().getSession().getAttribute("profileId")))
                        .custom(true)
                        .build()));
                close();
            }
        });

        getContentLayout().add(nameRoadmap, createButton);
        setHeaderNameText("Create roadmap");
    }
}
