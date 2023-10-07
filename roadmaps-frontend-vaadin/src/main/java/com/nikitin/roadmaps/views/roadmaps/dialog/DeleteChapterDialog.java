package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.nikitin.roadmaps.client.RoadmapChapterClient;
import com.nikitin.roadmaps.component.CustomDialog;
import com.nikitin.roadmaps.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapChapterResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.views.roadmaps.RoadmapView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
@Getter
@Setter
public class DeleteChapterDialog extends CustomDialog {

    private Long roadmapId;
    private RoadmapView roadmapView;
    private RoadmapChapterClient roadmapChapterClient;

    private ComboBox<RoadmapChapterResponseDto> chaptersComboBox = new ComboBox<>();
    private Button deleteButton = new Button();

    public DeleteChapterDialog(RoadmapChapterClient roadmapChapterClient) {
        this.roadmapChapterClient = roadmapChapterClient;

        configurationComponents();
    }

    private void configurationComponents() {
        chaptersComboBox.addClassName("dialog_chapters_combo_box_del");

        addOpenedChangeListener(event -> {
            chaptersComboBox.setItemLabelGenerator(RoadmapChapterResponseDto::getName);
            chaptersComboBox.setItems(getAllChaptersByRoadmapId());
        });

        deleteButton.addClassName("dialog_delete_button");
        deleteButton.setText("Delete");
        deleteButton.addClickListener(event -> {
           var response = roadmapChapterClient.deleteById(chaptersComboBox.getValue().getId(), true);

           if (response.getStatusCode().is2xxSuccessful()) {
               roadmapView.updateData(roadmapId);
               close();
           }
        });

        getContentLayout().add(chaptersComboBox, deleteButton);
        setHeaderNameText("Delete chapter");
    }

    private List<RoadmapChapterResponseDto> getAllChaptersByRoadmapId() {
        var response = roadmapChapterClient.getAllByRoadmapId(roadmapId, true);

        if (response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapChapterResponseDto.class).getRoadmapChapterResponseDtos();
        }

        return Collections.emptyList();
    }
}
