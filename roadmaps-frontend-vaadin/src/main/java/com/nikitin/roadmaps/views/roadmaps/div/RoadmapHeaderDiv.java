package com.nikitin.roadmaps.views.roadmaps.div;

import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.views.roadmaps.RoadmapView;
import com.nikitin.roadmaps.views.roadmaps.dialog.CreateChapterDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class RoadmapHeaderDiv extends Div {

    private H1 roadmapName = new H1();
    private Div buttonDiv = new Div();
    private Button addChapterButton = new Button();
    private Button deleteChapterButton = new Button();
    private Button editNameChapterButton = new Button();
    private Button shareRoadmapButton = new Button();
    private CreateChapterDialog createChapterDialog = new CreateChapterDialog();

    private RoadmapResponseDto roadmapResponseDto = new RoadmapResponseDto();

    private RoadmapClient roadmapClient;
    private RoadmapView roadmapView;

    public RoadmapHeaderDiv() {
        configurationRoadmapName();

        configurationHeaderButton();
        configurationButtonDiv();

        configurationRoadmapHeaderDiv();
    }

    private void configurationHeaderButton() {
        addChapterButton.addClassName("roadmap_button");
        addChapterButton.setText("Добавить раздел");
        addChapterButton.addClickListener(event -> {
            createChapterDialog.setRoadmapView(getRoadmapView());
            createChapterDialog.setRoadmapId(getRoadmapResponseDto().getId());
            createChapterDialog.setRoadmapClient(getRoadmapClient());
            createChapterDialog.open();
        });

        deleteChapterButton.addClassName("roadmap_button");
        deleteChapterButton.setText("Удалить раздел");

        editNameChapterButton.addClassName("roadmap_button");
        editNameChapterButton.setText("Переименовать раздел");

        shareRoadmapButton.addClassName("roadmap_button");
        shareRoadmapButton.setText("Поделиться");
    }

    private void configurationButtonDiv() {
        buttonDiv.setClassName("roadmap_button_div");
        buttonDiv.add(addChapterButton, deleteChapterButton, editNameChapterButton, shareRoadmapButton);
    }

    private void configurationRoadmapName() {
        roadmapName.addClassNames("roadmap_name_h1");
    }

    private void configurationRoadmapHeaderDiv() {
        addClassName("div_element");
        add(roadmapName, buttonDiv);
    }
}
