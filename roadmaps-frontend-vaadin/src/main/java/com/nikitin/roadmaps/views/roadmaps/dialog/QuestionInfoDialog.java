package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.nikitin.roadmaps.client.RoadmapQuestionClient;
import com.nikitin.roadmaps.component.CustomDialog;
import com.nikitin.roadmaps.dto.request.RoadmapQuestionRequestDto;
import com.nikitin.roadmaps.dto.response.RoadmapQuestionResponseDto;
import com.nikitin.roadmaps.views.roadmaps.div.RoadmapChapterDiv;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class QuestionInfoDialog extends CustomDialog {

    private Button saveButton = new Button();
    private TextArea questionText = new TextArea();
    private TextArea answerText = new TextArea();
    private Checkbox isExplored = new Checkbox();

    private Boolean readOnly = true;

    private RoadmapQuestionResponseDto roadmapQuestionResponseDto;
    private RoadmapQuestionClient roadmapQuestionClient;
    private RoadmapChapterDiv roadmapChapterDiv;

    public QuestionInfoDialog(RoadmapQuestionResponseDto roadmapQuestionResponseDto, RoadmapQuestionClient roadmapQuestionClient, RoadmapChapterDiv roadmapChapterDiv) {
        this.roadmapQuestionResponseDto = roadmapQuestionResponseDto;
        this.roadmapQuestionClient = roadmapQuestionClient;
        this.roadmapChapterDiv = roadmapChapterDiv;

        configurationComponents();
    }

    private void configurationComponents() {
        isExplored.addClassName("question_is_explored");
        isExplored.setLabel("Изученный");
        isExplored.setValue(getRoadmapQuestionResponseDto().getIsExplored());
        isExplored.setReadOnly(readOnly);

        questionText.addClassName("question_text");
        questionText.setLabel("Вопрос:");
        questionText.setValue(getRoadmapQuestionResponseDto().getQuestion());
        questionText.setReadOnly(readOnly);

        answerText.addClassName("answer_text");
        answerText.setLabel("Ответ:");
        answerText.setValue(getRoadmapQuestionResponseDto().getAnswer());
        answerText.setReadOnly(readOnly);

        saveButton.addClassName("question_save_button");
        saveButton.setText("Сохранить");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setVisible(false);
        saveButton.addClickListener(event -> {
            var response = roadmapQuestionClient.patch(getRoadmapQuestionResponseDto().getId(), RoadmapQuestionRequestDto.builder()
                    .isExplored(isExplored.getValue())
                    .question(questionText.getValue())
                    .answer(answerText.getValue())
                    .build(), true);
            if (response.getStatusCode().is2xxSuccessful()) {
                roadmapChapterDiv.updatePrimaryGrid();
                roadmapChapterDiv.updateSecondaryGrid(getRoadmapQuestionResponseDto().getRoadmapTopicId());
                close();
            }
        });

        setHeaderNameText("Question info");
        getContentLayout().add(isExplored, questionText, answerText, saveButton);
        setWidth("600px");
    }

    public void setEditedStatus() {
        isExplored.setReadOnly(false);
        questionText.setReadOnly(false);
        answerText.setReadOnly(false);
        saveButton.setVisible(true);
    }
}
