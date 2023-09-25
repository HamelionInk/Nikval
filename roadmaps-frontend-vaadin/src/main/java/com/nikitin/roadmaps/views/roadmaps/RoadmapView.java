package com.nikitin.roadmaps.views.roadmaps;

import com.nikitin.roadmaps.client.RoadmapChapterClient;
import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.client.RoadmapQuestionClient;
import com.nikitin.roadmaps.client.RoadmapTopicClient;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapChapterResponseDto;
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

    private RoadmapClient roadmapClient;
    private RoadmapChapterClient roadmapChapterClient;
    private RoadmapTopicClient roadmapTopicClient;
    private RoadmapQuestionClient roadmapQuestionClient;

    public RoadmapView(@Autowired RoadmapClient roadmapClient, @Autowired RoadmapChapterClient roadmapChapterClient,
                       @Autowired RoadmapTopicClient roadmapTopicClient, @Autowired RoadmapQuestionClient roadmapQuestionClient) {
        this.roadmapClient = roadmapClient;
        this.roadmapChapterClient = roadmapChapterClient;
        this.roadmapTopicClient = roadmapTopicClient;
        this.roadmapQuestionClient = roadmapQuestionClient;
        configurationAccordion();
        configurationRoadmapView();
    }

    private void configurationAccordion() {
        accordion.addClassName("roadmap_accordion");
    }

    private void configurationRoadmapView() {
        addClassName("roadmaps_view");

        add(roadmapHeaderDiv, roadmapBodyDiv);
    }

    private RoadmapResponseDto getRoadmapById(Long id) {
        var response = roadmapClient.getById(id, true);
        if (response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), RoadmapResponseDto.class);
        }
        return new RoadmapResponseDto();
    }

    private PageableRoadmapChapterResponseDto getAllByRoadmapId(Long id) {
        var response = roadmapChapterClient.getAllByRoadmapId(id, true);
        if (response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapChapterResponseDto.class);
        }
        return new PageableRoadmapChapterResponseDto();
    }

    public void updateData(Long roadmapId) {
        remove(getRoadmapBodyDiv());
        setRoadmapBodyDiv(new RoadmapBodyDiv());
        configurationRoadmapBody(getRoadmapById(roadmapId));
        configurationRoadmapView();
    }

    private void configurationRoadmapBody(RoadmapResponseDto roadmapResponseDto) {
        roadmapHeaderDiv.getRoadmapName().setText(roadmapResponseDto.getName());
        roadmapHeaderDiv.setRoadmapResponseDto(roadmapResponseDto);
        roadmapHeaderDiv.setRoadmapClient(getRoadmapClient());
        roadmapHeaderDiv.setRoadmapChapterClient(getRoadmapChapterClient());
        roadmapHeaderDiv.setRoadmapView(this);

        getAllByRoadmapId(roadmapResponseDto.getId()).getRoadmapChapterResponseDtos().forEach(roadmapChapterResponseDto -> {
            var roadmapChapter = new RoadmapChapterDiv(roadmapClient, roadmapTopicClient, roadmapQuestionClient);
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
