package com.nikitin.roadmaps.views.roadmaps.div;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class RoadmapBodyDiv extends Div {

    private Accordion accordion = new Accordion();

    public RoadmapBodyDiv() {
        configurationAccordion();
        configurationRoadmapBodyDiv();
    }

    public void configurationRoadmapBodyDiv() {
        addClassName("div_element");
        add(accordion);
    }

    private void configurationAccordion() {
        accordion.addClassName("roadmap_accordion");
    }

    public void createAccordionPanel(String name, Component component) {
        var accordionPanel = new AccordionPanel(name, component);
        accordionPanel.addClassName("roadmap_accordion_panel");

        accordion.add(accordionPanel);
        accordion.open(accordionPanel);
    }
}
