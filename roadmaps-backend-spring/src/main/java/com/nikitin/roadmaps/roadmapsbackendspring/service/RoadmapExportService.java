package com.nikitin.roadmaps.roadmapsbackendspring.service;

import lombok.NonNull;
import org.springframework.core.io.Resource;

public interface RoadmapExportService {

	Resource exportPDF(@NonNull Long id);
}
