package com.nikitin.roadmaps.views.roadmaps;

import com.nikitin.roadmaps.client.RoadmapClient;
import com.nikitin.roadmaps.dto.filter.RoadmapFilter;
import com.nikitin.roadmaps.dto.request.RoadmapRequestDto;
import com.nikitin.roadmaps.dto.response.RoadmapResponseDto;
import com.nikitin.roadmaps.dto.response.pageable.PageableRoadmapResponseDto;
import com.nikitin.roadmaps.util.RestUtils;
import com.nikitin.roadmaps.views.MainLayout;
import com.nikitin.roadmaps.views.roadmaps.dialog.CreateRoadmapDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
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
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
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
    private Grid<RoadmapResponseDto> defaultRoadmapsGrid;

    private final VerticalLayout roadmapsLayout = new VerticalLayout();
    private final VerticalLayout defaultRoadmapsLayout = new VerticalLayout();

    private final Button createRoadmapButton = new Button();

    private final Paragraph roadmapName = new Paragraph("My roadmaps :");
    private final Paragraph defaultRoadmapName = new Paragraph("Default roadmaps :");

    private final CreateRoadmapDialog createRoadmapDialog;

    public RoadmapInfoView(@Autowired RoadmapClient roadmapClient) {
        addClassName("roadmaps_view");
        this.roadmapClient = roadmapClient;

        buildRoadmapsLayout();
        buildDefaultRoadmapsLayout();

        var roadmapSearch = generateSearchField();
        roadmapSearch.addValueChangeListener(event -> roadmapsGrid.setItems(getAll(RoadmapFilter.builder()
                .profileIds(List.of((long) UI.getCurrent().getSession().getAttribute("profileId")))
                .startWithName(event.getValue())
                .custom(true)
                .build())));
        var roadmapHeaderLayout = new HorizontalLayout();
        roadmapHeaderLayout.addClassName("roadmaps_header_layout");
        roadmapHeaderLayout.add(roadmapName, roadmapSearch);

        var roadmapDefaultSearch = generateSearchField();
        roadmapDefaultSearch.addValueChangeListener(event -> defaultRoadmapsGrid.setItems(getAll(RoadmapFilter.builder()
                .profileIds(List.of((long) UI.getCurrent().getSession().getAttribute("profileId")))
                .startWithName(event.getValue())
                .custom(false)
                .build())));
        var roadmapDefaultHeaderLayout = new HorizontalLayout();
        roadmapDefaultHeaderLayout.addClassName("roadmaps_header_layout");
        roadmapDefaultHeaderLayout.add(defaultRoadmapName, roadmapDefaultSearch);

        createRoadmapDialog = new CreateRoadmapDialog(roadmapClient);
        createRoadmapDialog.setRoadmapInfoView(this);

        add(roadmapHeaderLayout, roadmapsLayout, roadmapDefaultHeaderLayout, defaultRoadmapsLayout);
    }

    private void buildRoadmapsLayout() {
        roadmapsLayout.addClassName("roadmaps_layout");
        roadmapName.addClassName("roadmaps_name");

        roadmapsGrid = generateGrid(getAll(RoadmapFilter.builder()
                .profileIds(List.of((long) UI.getCurrent().getSession().getAttribute("profileId")))
                .custom(true)
                .build()));

        createRoadmapButton.addClassName("roadmaps_create_button");
        var createIcon = new Icon(VaadinIcon.PLUS);
        createRoadmapButton.setPrefixComponent(createIcon);
        createRoadmapButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        createRoadmapButton.addClickListener(event -> createRoadmapDialog.open());

        roadmapsLayout.add(roadmapsGrid, createRoadmapButton);
    }

    private void buildDefaultRoadmapsLayout() {
        defaultRoadmapsLayout.addClassName("roadmaps_layout");

        defaultRoadmapName.addClassName("roadmaps_name");

        defaultRoadmapsGrid = generateGrid(getAll(RoadmapFilter.builder()
                .profileIds(List.of((long) UI.getCurrent().getSession().getAttribute("profileId")))
                .custom(false)
                .build()));

        defaultRoadmapsLayout.add(defaultRoadmapsGrid);
    }

    private Grid<RoadmapResponseDto> generateGrid(List<RoadmapResponseDto> data) {
        var grid = new Grid<>(RoadmapResponseDto.class, false);

        grid.addClassName("roadmaps_grid");
        grid.setAllRowsVisible(true);
        grid.setItems(data);

        grid.addItemDoubleClickListener(event ->
                UI.getCurrent().navigateToClient("http://localhost:8082/roadmaps/" + event.getItem().getId()));

        Grid.Column<RoadmapResponseDto> roadmapNameColumn = grid
                .addColumn(RoadmapResponseDto::getName);

        Grid.Column<RoadmapResponseDto> systemColumn = grid
                .addComponentColumn(roadmapResponseDto -> {
                    var icon = new Icon(VaadinIcon.TRASH);
                    icon.setColor("#86C232");

                    var deleteButton = new Button();
                    deleteButton.addClassName("roadmaps_delete_button");
                    deleteButton.setIcon(icon);
                    deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                    deleteButton.addClickListener(event -> {
                        roadmapClient.deleteById(roadmapResponseDto.getId(), true);
                        if (roadmapResponseDto.getCustom()) {
                            roadmapsGrid.setItems(getAll(RoadmapFilter.builder()
                                    .profileIds(List.of((long) UI.getCurrent().getSession().getAttribute("profileId")))
                                    .custom(true)
                                    .build()));
                        } else {
                            defaultRoadmapsGrid.setItems(getAll(RoadmapFilter.builder()
                                    .profileIds(List.of((long) UI.getCurrent().getSession().getAttribute("profileId")))
                                    .custom(false)
                                    .build()));
                        }
                    });
                    return deleteButton;
                }).setWidth("70px").setFlexGrow(0);

        return grid;
    }

    private TextField generateSearchField() {
        var searchField = new TextField();
        searchField.addClassName("roadmaps_search");
        searchField.setPlaceholder("Search");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());

        return searchField;
    }

    public List<RoadmapResponseDto> getAll(RoadmapFilter roadmapFilter) {
        var response = roadmapClient.getAll(roadmapFilter, true);

        if (response.getStatusCode().is2xxSuccessful()) {
            return RestUtils.convertResponseToDto(response.getBody(), PageableRoadmapResponseDto.class).getRoadmapResponseDtos();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}
