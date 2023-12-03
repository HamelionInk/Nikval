package com.nikitin.roadmapfrontend.view;

import com.nikitin.roadmapfrontend.client.NewsCardClient;
import com.nikitin.roadmapfrontend.component.ViewHeader;
import com.nikitin.roadmapfrontend.dto.enums.VaadinSessionAttribute;
import com.nikitin.roadmapfrontend.dto.response.NewsCardResponseDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@PageTitle(value = "News")
@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "news", layout = MainView.class)
public class NewsView extends VerticalLayout implements HasUrlParameter<Long>, View {

    private final NewsCardClient newsCardClient;
    private NewsCardResponseDto newsCardResponseDto;

    private final Button returnButton = new Button("Назад");

    public NewsView(@Autowired NewsCardClient newsCardClient) {
        this.newsCardClient = newsCardClient;

        addClassName("news-view");
    }

    private ViewHeader buildViewHeader() {
        var viewHeader = new ViewHeader(newsCardResponseDto.getTitle());

        returnButton.addClickListener(event ->
                UI.getCurrent().navigate((String) UI.getCurrent().getSession().getAttribute(VaadinSessionAttribute.PREVIOUS_URL.getValue())));

        viewHeader.addButton(returnButton);

        return viewHeader;
    }

    private VerticalLayout buildDescriptionLayout() {
        var descriptionLayout = new VerticalLayout();
        descriptionLayout.addClassName("description-layout");

        var descriptionNews = new TextArea();
        descriptionNews.setReadOnly(true);
        descriptionNews.addClassName("description-news");
        descriptionNews.setValue(newsCardResponseDto.getDescription());

        descriptionLayout.add(descriptionNews);

        return descriptionLayout;
    }

    @Override
    public void refreshView() {
        removeAll();

        add(buildViewHeader(), buildDescriptionLayout());
    }

    @Override
    public <T> T getClient(Class<T> clientType) {
        if (clientType.isInstance(newsCardClient)) {
            return clientType.cast(newsCardClient);
        }

        //todo add exception
        return null;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        newsCardResponseDto = getClient(NewsCardClient.class).getById(id);

        refreshView();
    }
}
