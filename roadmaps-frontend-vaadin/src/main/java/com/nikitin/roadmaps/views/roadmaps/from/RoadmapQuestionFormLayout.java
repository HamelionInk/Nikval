package com.nikitin.roadmaps.views.roadmaps.from;

import com.nikitin.roadmaps.dto.request.RoadmapQuestionRequestDto;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Getter
@Service
public class RoadmapQuestionFormLayout extends FormLayout {

    private final TextField answerTextField = new TextField("Здесь должен быть ответ ))");

    public RoadmapQuestionFormLayout() {
        Stream.of(answerTextField)
                .forEach(textField -> {
                    textField.setReadOnly(true);
                    add(textField);
                });
    }

    public void setRoadmapQuestion(RoadmapQuestionRequestDto roadmapQuestionRequestDto) {
        answerTextField.setValue(roadmapQuestionRequestDto.getAnswer());
    }
}
