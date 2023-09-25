package com.nikitin.roadmaps.views.roadmaps;

import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@Getter
@Setter
@PageTitle("Roadmap")
@Route(value = "roadmaps", layout = MainLayout.class)
@RolesAllowed(value = {"ROLE_USER"})
public class RoadmapInfoView extends VerticalLayout implements LocaleChangeObserver {

    private RoadmapClient roadmapClient;
    private Grid<RoadmapResponseDto> roadmapsGrid;

    private final VerticalLayout roadmapsLayout = new VerticalLayout();
    private final VerticalLayout defaultRoadmapsLayout = new VerticalLayout();

    private final Paragraph roadmapName = new Paragraph("My roadmaps :");

    private final Paragraph defaultRoadmapName = new Paragraph("Default roadmaps :");

    public RoadmapInfoView(@Autowired RoadmapClient roadmapClient) {
        addClassName("roadmaps_view");
        this.roadmapClient = roadmapClient;

        buildRoadmapsLayout();
        buildDefaultRoadmapsLayout();

        var roadmapSearch = generateSearchField();
        var roadmapHeaderLayout = new HorizontalLayout();
        roadmapHeaderLayout.addClassName("roadmaps_header_layout");
        roadmapHeaderLayout.add(roadmapName, roadmapSearch);

        var roadmapDefaultSearch = generateSearchField();
        var roadmapDefaultHeaderLayout = new HorizontalLayout();
        roadmapDefaultHeaderLayout.addClassName("roadmaps_header_layout");
        roadmapDefaultHeaderLayout.add(defaultRoadmapName, roadmapDefaultSearch);


        add(roadmapHeaderLayout, roadmapsLayout, roadmapDefaultHeaderLayout, defaultRoadmapsLayout);
    }

    private void buildRoadmapsLayout() {
        roadmapsLayout.addClassName("roadmaps_layout");
        roadmapName.addClassName("roadmaps_name");


        roadmapsLayout.add(generateGrid(getAllRoadmapsByProfileId()));
    }

    private void buildDefaultRoadmapsLayout() {
        defaultRoadmapsLayout.addClassName("roadmaps_layout");

        defaultRoadmapName.addClassName("roadmaps_name");

        defaultRoadmapsLayout.add(generateGrid(getAllRoadmapsByProfileId()));
    }

    private Grid<RoadmapResponseDto> generateGrid(List<RoadmapResponseDto> data) {
        roadmapsGrid = new Grid<>(RoadmapResponseDto.class, false);

        roadmapsGrid.addClassName("roadmaps_grid");
        roadmapsGrid.setAllRowsVisible(true);
        roadmapsGrid.setItems(data);

        roadmapsGrid.addItemDoubleClickListener(event ->
                UI.getCurrent().navigateToClient("http://localhost:8082/roadmaps/" + event.getItem().getId()));

        Grid.Column<RoadmapResponseDto> roadmapNameColumn = roadmapsGrid
                .addColumn(RoadmapResponseDto::getName);

        Grid.Column<RoadmapResponseDto> systemColumn = roadmapsGrid
                .addComponentColumn(roadmapResponseDto -> {
                    var icon = new Icon(VaadinIcon.TRASH);
                    icon.setColor("#86C232");

                    var deleteButton = new Button();
                    deleteButton.addClassName("roadmaps_delete_button");
                    deleteButton.setIcon(icon);
                    deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                    deleteButton.addClickListener(event -> {

                    });
                    return deleteButton;
                }).setWidth("70px").setFlexGrow(0);

        return roadmapsGrid;
    }

    private TextField generateSearchField() {
        var searchField = new TextField();
        searchField.addClassName("roadmaps_search");
        searchField.setPlaceholder("Search");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());

        return searchField;
    }

    private List<RoadmapResponseDto> getAllRoadmapsByProfileId() {
        var response = roadmapClient.getAllByProfileId((long) UI.getCurrent().getSession().getAttribute("profileId"), true);

        if(response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapResponseDto.class).getRoadmapResponseDtos();
        } else {
            return List.of();
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
