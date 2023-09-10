package com.nikitin.roadmaps.views.roadmaps;

import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.views.MainLayout;
import com.nikitin.roadmaps.views.roadmaps.div.RoadmapBodyDiv;
import com.nikitin.roadmaps.views.roadmaps.div.RoadmapChapterDiv;
import com.nikitin.roadmaps.views.roadmaps.div.RoadmapHeaderDiv;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
@Getter
@Setter
@PageTitle("Roadmap")
@Route(value = "roadmaps", layout = MainLayout.class)
@RolesAllowed(value = {"ROLE_USER"})
public class RoadmapView extends VerticalLayout implements LocaleChangeObserver, HasUrlParameter<Long> {

    private RoadmapHeaderDiv roadmapHeaderDiv = new RoadmapHeaderDiv();
    private RoadmapBodyDiv roadmapBodyDiv = new RoadmapBodyDiv();
    private Accordion accordion = new Accordion();
    private RoadmapResponseDto roadmapResponseDto = new RoadmapResponseDto();

    private RoadmapClient roadmapClient;

    public RoadmapView(@Autowired RoadmapClient roadmapClient) {
        this.roadmapClient = roadmapClient;
        configurationAccordion();
        configurationRoadmapView();
    }

    private void configurationAccordion() {
        accordion.addClassName("roadmap_accordion");
    }

    private void configurationRoadmapView() {
        add(roadmapHeaderDiv, roadmapBodyDiv);
    }

    private void getRoadmapById(Long id) {
        var response = roadmapClient.getById(id, true);
        if (response.getStatusCode().is2xxSuccessful()) {
            Optional.ofNullable(response.getBody())
                    .ifPresent(body -> roadmapResponseDto = RestUtils.convertResponseToDto(body, RoadmapResponseDto.class));
        }
    }

    public void updateData(Long roadmapId) {
        getRoadmapById(roadmapId);
        remove(getRoadmapBodyDiv());
        setRoadmapBodyDiv(new RoadmapBodyDiv());
        configurationRoadmapBody();
        configurationRoadmapView();
    }

    private void configurationRoadmapBody() {
        roadmapHeaderDiv.getRoadmapName().setText(roadmapResponseDto.getName());
        roadmapHeaderDiv.setRoadmapResponseDto(getRoadmapResponseDto());
        roadmapHeaderDiv.setRoadmapClient(getRoadmapClient());
        roadmapHeaderDiv.setRoadmapView(this);

        roadmapResponseDto.getRoadmapChapterResponseDtos().forEach(roadmapChapterResponseDto -> {
            var roadmapChapter = new RoadmapChapterDiv(roadmapClient);
            roadmapChapter.setRoadmapChapterId(roadmapChapterResponseDto.getId());
            roadmapChapter.setRoadmapId(roadmapChapterResponseDto.getRoadmapId());
            roadmapChapter.updatePrimaryGrid();
            roadmapBodyDiv.createAccordionPanel(roadmapChapterResponseDto.getName(), roadmapChapter);
        });
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long roadmapId) {
        updateData(roadmapId);
    }
}
