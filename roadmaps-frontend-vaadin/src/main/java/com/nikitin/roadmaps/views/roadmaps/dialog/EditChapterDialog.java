package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.nikitin.roadmaps.client.RoadmapChapterClient;
import com.nikitin.roadmaps.component.CustomDialog;
import com.nikitin.roadmaps.dto.request.RoadmapChapterRequestDto;
import com.nikitin.roadmaps.dto.response.RoadmapChapterResponseDto;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapChapterResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.views.roadmaps.RoadmapView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Setter
public class EditChapterDialog extends CustomDialog {
    private Long roadmapId;
    private RoadmapView roadmapView;
    private RoadmapChapterClient roadmapChapterClient;

    private ComboBox<RoadmapChapterResponseDto> chaptersComboBox = new ComboBox<>();
    private TextField nameChapter = new TextField();
    private Button saveButton = new Button();


    public EditChapterDialog(RoadmapChapterClient roadmapChapterClient) {
        this.roadmapChapterClient = roadmapChapterClient;

        configurationComponents();
    }

    private void configurationComponents() {
        chaptersComboBox.addClassName("dialog_chapters_combo_box");
        chaptersComboBox.addValueChangeListener(event -> Optional.ofNullable(chaptersComboBox.getValue())
                .ifPresent(roadmapChapterResponseDto -> nameChapter.setValue(roadmapChapterResponseDto.getName())));

        addOpenedChangeListener(event -> {
            chaptersComboBox.setItemLabelGenerator(RoadmapChapterResponseDto::getName);
            chaptersComboBox.setItems(getAllChaptersByRoadmapId());
        });

        nameChapter.addClassName("dialog_name_text_field");
        nameChapter.setLabel("Name chapter");

        saveButton.addClassName("dialog_delete_button");
        saveButton.setText("Edit");
        saveButton.addClickListener(event -> {
            var response = roadmapChapterClient.patch(chaptersComboBox.getValue().getId(), RoadmapChapterRequestDto.builder()
                    .name(nameChapter.getValue())
                    .build(), true);

            if (response.getStatusCode().is2xxSuccessful()) {
                roadmapView.updateData(roadmapId);
                close();
            }

        });

        getContentLayout().add(chaptersComboBox, nameChapter, saveButton);
        setHeaderNameText("Edit chapter");
    }

    private List<RoadmapChapterResponseDto> getAllChaptersByRoadmapId() {
        var response = roadmapChapterClient.getAllByRoadmapId(roadmapId, true);

        if (response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapChapterResponseDto.class).getRoadmapChapterResponseDtos();
        }

        return Collections.emptyList();
    }
}
