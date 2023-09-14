package com.nikitin.roadmaps.views.roadmaps.dialog;

import com.nikitin.roadmaps.client.RoadmapChapterClient;
import com.vaadin.flow.component.dialog.Dialog;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class DeleteChapterDialog extends Dialog {

    private RoadmapChapterClient roadmapChapterClient;

    public DeleteChapterDialog(RoadmapChapterClient roadmapChapterClient) {
        this.roadmapChapterClient = roadmapChapterClient;

    }
}
