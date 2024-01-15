package com.nikitin.roadmapfrontend.component.tree;

import com.nikitin.roadmapfrontend.client.RoadmapChapterClient;
import com.nikitin.roadmapfrontend.client.RoadmapClient;
import com.nikitin.roadmapfrontend.dto.data.RoadmapData;
import com.nikitin.roadmapfrontend.dto.filter.RoadmapChapterFilter;
import com.nikitin.roadmapfrontend.view.View;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.Div;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RoadmapTree<T extends View> extends Div implements DropTarget<RoadmapTree<T>> {

}
