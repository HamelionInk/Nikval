package com.nikitin.roadmapfrontend.utils.editor;

import com.wontlost.ckeditor.Constants;
import com.wontlost.ckeditor.VaadinCKEditor;
import com.wontlost.ckeditor.VaadinCKEditorBuilder;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.util.Optional;

@UtilityClass
public class TextEditorBuilder {

	public VaadinCKEditor getClassicCKEditor(@Nullable String value) {
		var classicCKEditor = new VaadinCKEditorBuilder()
				.with(builder -> {
					builder.editorType = Constants.EditorType.CLASSIC;
					builder.theme = Constants.ThemeType.LIGHT;
					builder.hideToolbar = true;
					builder.height = "300px";
				})
				.createVaadinCKEditor();

		classicCKEditor.setLabel("Ответ");
		classicCKEditor.getStyle().clear();

		Optional.ofNullable(value)
				.ifPresent(text ->
						classicCKEditor.setValue(value)
				);

		return classicCKEditor;
	}
}
