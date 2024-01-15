package com.nikitin.roadmapfrontend.dialog;

import com.nikitin.roadmapfrontend.icon.RoadmapIcon;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MainDialog<T extends View> extends Dialog {

    private T view;

    private Paragraph headerName = new Paragraph("Диалоговое окно");
    private Button closeButton = new Button("Закрыть");
    private HorizontalLayout headerLayout = new HorizontalLayout();

    public MainDialog(T view) {
        this.view = view;

        configureDialog();
        buildDialogHeader();
    }

    private void configureDialog() {
        addClassName("main-dialog");
        setDraggable(true);
    }

    private void buildDialogHeader() {
        headerLayout.addClassName("header-layout-dialog");

        headerName.addClassName("header-name-dialog");

        closeButton.addClassName("close-button-dialog");
        closeButton.addClickListener(event ->
                close()
        );

        headerLayout.add(headerName, closeButton);

        getHeader().add(headerLayout);
    }

    public void setCloseButtonIcon(RoadmapIcon icon) {
        closeButton.setIcon(icon.create());
    }

    public void setHeaderName(String text) {
        headerName.setText(text);
    }

    public void setBodyLayout(Component layout) {
        add(layout);
    }
}

