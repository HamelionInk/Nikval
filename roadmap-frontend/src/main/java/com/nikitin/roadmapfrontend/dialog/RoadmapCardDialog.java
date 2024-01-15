package com.nikitin.roadmapfrontend.dialog;

import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoadmapCardDialog<T extends View> extends MainDialog<T> {

    private final VerticalLayout roadmapCardLayout = new VerticalLayout();
    private final Checkbox favoriteCheckBox = new Checkbox("Добавить в избранные");
    private final TextField roadmapName = new TextField("Название карты");
    private final Button actionButton = new Button("Создать");

    public RoadmapCardDialog(T view) {
        super(view);

        buildBody();
    }

    private void buildBody() {
        roadmapCardLayout.addClassName("roadmap-card-layout");

        favoriteCheckBox.addClassName("favorite-check-box");
        roadmapName.addClassName("roadmap-card-name");
        actionButton.addClassName("create-card-button");

        roadmapCardLayout.add(favoriteCheckBox, roadmapName, actionButton);

        setBodyLayout(roadmapCardLayout);
    }

    public void addActionButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        actionButton.addClickListener(listener);
    }

    public void setActionButtonName(String value) {
        actionButton.setText(value);
    }

    public void setFavoriteCheckBoxValue(Boolean value) {
        favoriteCheckBox.setValue(value);
    }

    public Boolean getFavoriteCheckBoxValue() {
        return favoriteCheckBox.getValue();
    }

    public void setRoadmapName(String value) {
        roadmapName.setValue(value);
    }

    public String getRoadmapName() {
        return roadmapName.getValue();
    }
}
