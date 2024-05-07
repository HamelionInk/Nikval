package com.nikitin.roadmapfrontend.icon;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class RoadmapImage {

	public static final String CUSTOM_NEWS_CARD = "custom-news-card";

	public static Optional<String> getImageUrl(String imageName) {
		return Optional.ofNullable(RoadmapImage.class.getResource("/static/image/" + imageName + ".jpg"))
				.map(url -> {
					var path = new File(url.getFile()).toPath();

					try {
						var mimeType = Files.probeContentType(path);

						return "data:" + mimeType + ";base64," + Base64.encodeBase64String(Files.readAllBytes(path));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
	}
}
