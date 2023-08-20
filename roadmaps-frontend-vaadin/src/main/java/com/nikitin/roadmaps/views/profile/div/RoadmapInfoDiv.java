package com.nikitin.roadmaps.views.profile.div;

import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class RoadmapInfoDiv extends Div implements LocaleChangeObserver {

    private TextField questionCompleteTextField;
    private TextField roadmapCompetenceTextField;
    private TextField learningTechnologiesTextField;
    private TextArea masteredTechnologiesTextField;
    private H4 accordionPanelNameH4;
    private Select<String> roadmapSelect;

    public RoadmapInfoDiv() {
        setClassName("profile_info_div");
        add(buildAccordionPanel());
    }

    private AccordionPanel buildAccordionPanel() {
        roadmapSelect = new Select<>();
        roadmapSelect.addClassNames("roadmap_select");
        roadmapSelect.getElement().addEventListener("click", click -> {
        }).addEventData("event.stopPropagation()");
        roadmapSelect.setItems("Java backend", "PHP backend");
        roadmapSelect.setValue("Java backend");

        accordionPanelNameH4 = new H4("Информация по карте развития");
        accordionPanelNameH4.addClassName("accordion_panel_name_h4");

        questionCompleteTextField = new TextField("Завершенные вопросы");
        questionCompleteTextField.addClassName("profile_text_field");
        questionCompleteTextField.setReadOnly(true);

        roadmapCompetenceTextField = new TextField("Уровень компеценции");
        roadmapCompetenceTextField.addClassName("profile_text_field");
        roadmapCompetenceTextField.setReadOnly(true);

        learningTechnologiesTextField = new TextField("Изучаемые технологии");
        learningTechnologiesTextField.addClassName("profile_text_field");
        learningTechnologiesTextField.setReadOnly(true);

        masteredTechnologiesTextField = new TextArea("Освоенные технологии");
        masteredTechnologiesTextField.addClassName("profile_text_field");
        masteredTechnologiesTextField.setReadOnly(true);

        Div accordionPanelNameDiv = new Div();
        accordionPanelNameDiv.addClassName("accordion_panel_name_div");
        accordionPanelNameDiv.add(accordionPanelNameH4, roadmapSelect);

        AccordionPanel accordionPanel = new AccordionPanel(accordionPanelNameDiv);
        accordionPanel.addClassName("profile_accordion_panel");
        accordionPanel.setOpened(true);
        accordionPanel.addContent(
                questionCompleteTextField,
                roadmapCompetenceTextField,
                learningTechnologiesTextField,
                masteredTechnologiesTextField);

        return accordionPanel;
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
