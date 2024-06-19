package com.nikitin.roadmaps.roadmapsbackendspring.service.pdf.converter;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.nikitin.roadmaps.roadmapsbackendspring.exception.InternalServerException;

import java.util.ArrayList;
import java.util.List;

public class ImageTagConverter extends com.itextpdf.tool.xml.html.Image {

	@Override
	public List<Element> end(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
		final var attributes = tag.getAttributes();
		var src = attributes.get(HTML.Attribute.SRC);
		List<Element> elements = new ArrayList<>(1);

		if (null != src && !src.isEmpty()) {
			Image image = null;

			if (src.startsWith("data:image/")) {
				final var base64Data = src.substring(src.indexOf(",") + 1);

				try {
					image = Image.getInstance(Base64.decode(base64Data));
				} catch (Exception exception) {
					throw new InternalServerException(exception.getMessage());
				}

				if (image != null) {
					try {
						final HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
						elements.add(getCssAppliers().apply(new Chunk((com.itextpdf.text.Image) getCssAppliers().apply(image, tag, htmlPipelineContext), 0, 0, true), tag,
								htmlPipelineContext));
					} catch (NoCustomContextException e) {
						throw new RuntimeWorkerException(e);
					}
				}
			}

			if (image == null) {
				elements = super.end(ctx, tag, currentContent);
			}
		}

		return elements;
	}
}
