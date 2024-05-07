package com.nikitin.roadmapfrontend.view;

import com.nikitin.roadmapfrontend.client.NewsCardClient;
import com.nikitin.roadmapfrontend.component.card.NewsCard;
import com.nikitin.roadmapfrontend.component.view.ViewHeader;
import com.nikitin.roadmapfrontend.configuration.security.SecurityService;
import com.nikitin.roadmapfrontend.component.dialog.newscard.NewsCardCreateDialog;
import com.nikitin.roadmapfrontend.dto.request.NewsCardRequestDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@PageTitle("Home Page")
@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "home", layout = MainView.class)
public class HomeView extends VerticalLayout implements View {

    private final NewsCardClient newsCardClient;

    public HomeView(@Autowired NewsCardClient newsCardClient) {
        this.newsCardClient = newsCardClient;

        addClassName("home-view");
        add(buildViewHeader(), buildNewsLayout());
    }

    private ViewHeader buildViewHeader() {
        var viewHeader = new ViewHeader("Главная страница");

        if (SecurityService.getAuthorities().contains("ROLE_ADMIN")) {
            var createNewsCardButton = new Button("Добавить новость");
            createNewsCardButton.addClickListener(event -> {
                var createNewsCardDialog = new NewsCardCreateDialog();
                createNewsCardDialog.addActionButtonClickListener(action -> {
                    newsCardClient.create(NewsCardRequestDto.builder()
                            .title(createNewsCardDialog.getTitleInputValue())
                            .description(createNewsCardDialog.getDescriptionInputValue())
                            .image(createNewsCardDialog.getNewsCardImageSrc())
                            .createdAt(createNewsCardDialog.getDateTimeValue())
                            .build());

                    refreshView();
                    createNewsCardDialog.close();
                });

                createNewsCardDialog.open();
            });

            viewHeader.addButton(createNewsCardButton);
        }

        return viewHeader;
    }

    private Div buildNewsLayout() {
        var newsLayout = new Div();
        newsLayout.addClassName("news-layout");

        getClient(NewsCardClient.class).getAll().getNewsCardResponseDtos().forEach(newsCardResponseDto ->
                newsLayout.add(new NewsCard<>(newsCardResponseDto, this))
        );

        return newsLayout;
    }

    @Override
    public void refreshView() {
        removeAll();

        add(buildViewHeader(), buildNewsLayout());
    }

    @Override
    public <T> T getClient(Class<T> clientType) {
        if (clientType.isInstance(newsCardClient)) {
            return clientType.cast(newsCardClient);
        }

        //todo - add exception
        return null;
    }
}
