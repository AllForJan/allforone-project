
package com.vaadin.starter.beveragebuddy.ui.views.ziadosti;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.starter.beveragebuddy.backend.ZiadostiService;
import com.vaadin.starter.beveragebuddy.backend.ZiadostDiely;
import com.vaadin.starter.beveragebuddy.ui.common.AbstractEditorDialog;

public class EditorZiadosti extends AbstractEditorDialog<ZiadostDiely> {

	private final TextField categoryNameField = new TextField("Name");

	public EditorZiadosti(BiConsumer<ZiadostDiely, Operation> itemSaver, Consumer<ZiadostDiely> itemDeleter) {
		super("category", itemSaver, itemDeleter);

		addNameField();
	}

	private void addNameField() {
		// getFormLayout().add(categoryNameField);
		//
		// getBinder().forField(categoryNameField)
		// .withConverter(String::trim, String::trim)
		// .withValidator(new StringLengthValidator(
		// "Category name must contain at least 3 printable characters",
		// 3, null))
		// .withValidator(
		// name -> CategoryService.getInstance()
		// .findCategories(name).size() == 0,
		// "Category name must be unique")
		// .bind(ZiadostDiely::setZiadatel, ZiadostDiely::setRok);
	}

	@Override
	protected void confirmDelete() {
		// int reviewCount = Service.getInstance()
		// .findReviews(getCurrentItem().getId()).size();
		// if (reviewCount > 0) {
		// openConfirmationDialog("Delete category",
		// "Are you sure you want to delete the “" + getCurrentItem().getName()
		// + "” category? There are " + reviewCount
		// + " reviews associated with this category.",
		// "Deleting the category will mark the associated reviews as “undefined”. "
		// + "You can edit individual reviews to select another category.");
		// }
	}
}
