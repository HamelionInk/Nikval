package com.nikitin.roadmapfrontend.component.dialog;

import com.nikitin.roadmapfrontend.component.CustomComponent;
import com.nikitin.roadmapfrontend.utils.constants.DialogNameConstant;
import com.nikitin.roadmapfrontend.utils.constants.StyleClassConstant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;

public class AbstractDialog extends Dialog implements CustomComponent {

	protected Button closeButton;
	protected Button actionButton;

	@Override
	public void buildComponent() {
		closeButton = new Button(DialogNameConstant.CLOSE_BUTTON);
		closeButton.addClickListener(event -> close());
		closeButton.addClassName(StyleClassConstant.DIALOG_CUSTOM_BUTTON);

		actionButton = new Button(DialogNameConstant.ACTION_BUTTON);
		actionButton.addClassName(StyleClassConstant.DIALOG_CUSTOM_BUTTON);

		getFooter().add(actionButton, closeButton);
		addClassName(StyleClassConstant.DIALOG_LAYOUT);
	}
}
