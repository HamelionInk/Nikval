package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.dto.filter.RoadmapFilter;
import com.nikitin.roadmaps.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.views.roadmaps.RoadmapInfoView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
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
public class CreateRoadmapDialog extends Dialog {

    private final Paragraph headerName = new Paragraph();
    private final TextField nameRoadmap = new TextField();
    private final Button closeButton = new Button();
    private final Button createButton = new Button();

    private RoadmapClient roadmapClient;
    private RoadmapInfoView roadmapInfoView;

    public CreateRoadmapDialog(RoadmapClient roadmapClient) {
        this.roadmapClient = roadmapClient;

        configurationComponents();
        configurationCreateChapterDialog();
    }

    private void configurationCreateChapterDialog() {
        addClassName("dialog");
        setWidth("450px");
        setDraggable(true);

        var headerDiv = new Div();
        headerDiv.setClassName("dialog_header_div");
        headerDiv.add(headerName, closeButton);

        getHeader().add(headerDiv);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassName("dialog_vertical_layout");
        verticalLayout.add(nameRoadmap, createButton);
        add(verticalLayout);
    }

    private void configurationComponents() {
        headerName.setText("Создать карту развития");
        headerName.addClassName("dialog_header_name");

        closeButton.setIcon(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addClassName("dialog_close_button");
        closeButton.addClickListener(event -> close());

        nameRoadmap.setLabel("Название");
        nameRoadmap.addClassName("dialog_name_text_field");

        createButton.setText("Создать");
        createButton.addClassName("dialog_create_button");
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
    }
}
