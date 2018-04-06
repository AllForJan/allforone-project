package com.vaadin.starter.beveragebuddy.ui.views.ziadosti;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.starter.beveragebuddy.backend.ZiadostiService;
import com.vaadin.starter.beveragebuddy.backend.ZiadostDiely;
import com.vaadin.starter.beveragebuddy.ui.MainLayout;
import com.vaadin.starter.beveragebuddy.ui.common.AbstractEditorDialog;

@Route(value = "ziadosti", layout = MainLayout.class)
@PageTitle("Žiadosti s dielmi list")
public class ViewZiadostiList extends VerticalLayout {

	private final TextField searchField = new TextField("", "Search");
	private final H2 header = new H2("Žiadosti");
	private final Grid<ZiadostDiely> grid = new Grid<>();

	// private final ZiadostiEditorDialog form = new
	// ZiadostiEditorDialog(this::save, this::delete);

	public ViewZiadostiList() {
		initView();

		addSearchBar();
		addContent();

		updateView();
	}

	private void initView() {
		addClassName("categories-list");
		setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
	}

	private void addSearchBar() {
		Div viewToolbar = new Div();
		viewToolbar.addClassName("view-toolbar");

		searchField.setPrefixComponent(new Icon("lumo", "search"));
		searchField.addClassName("view-toolbar__search-field");
		searchField.addValueChangeListener(e -> updateView());
		searchField.setValueChangeMode(ValueChangeMode.EAGER);

		// Button newButton = new Button("New category", new Icon("lumo", "plus"));
		// newButton.getElement().setAttribute("theme", "primary");
		// newButton.addClassName("view-toolbar__button");
		// newButton.addClickListener(e -> form.open(new Category(),
		// AbstractEditorDialog.Operation.ADD));

		viewToolbar.add(searchField);
		add(viewToolbar);
	}

	private void addContent() {
		VerticalLayout container = new VerticalLayout();
		container.setClassName("view-container");
		container.setAlignItems(Alignment.STRETCH);

		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setMultiSort(true);
		grid.setColumnReorderingAllowed(true);

		grid.addColumn(ZiadostDiely::getZiadatel).setHeader("Žiadataľ").setWidth("8em").setResizable(true)
				.setKey("ziadatel");
		grid.addColumn(ZiadostDiely::getIco).setHeader("IČO").setResizable(true);
		grid.addColumn(ZiadostDiely::getRok).setHeader("Rok").setResizable(true);
		grid.addColumn(ZiadostDiely::getLokalita).setHeader("Lokalita").setResizable(true);
		grid.addColumn(ZiadostDiely::getDiel).setHeader("Diel").setResizable(true);
		grid.addColumn(ZiadostDiely::getKultura).setHeader("Kultura").setResizable(true);
		grid.addColumn(ZiadostDiely::getVymera).setHeader("Vymera").setResizable(true);

		grid.addColumn(new ComponentRenderer<>(this::createEditButton)).setFlexGrow(0);

		grid.setColumnReorderingAllowed(true);
		grid.setSelectionMode(SelectionMode.MULTI);

		container.add(header, grid);
		add(container);
	}

	private Button createEditButton(ZiadostDiely entity) {
		Button edit = new Button("Edit");
		// , event -> form.open(entity, AbstractEditorDialog.Operation.EDIT));
		edit.setIcon(new Icon("lumo", "edit"));
		edit.addClassName("review__edit");
		edit.getElement().setAttribute("theme", "tertiary");
		return edit;
	}

	private String getReviewCount(Category category) {
		List<Review> reviewsInCategory = ReviewService.getInstance().findReviews(category.getName());
		int sum = reviewsInCategory.stream().mapToInt(Review::getCount).sum();
		return Integer.toString(sum);
	}

	private void updateView() {
		List<ZiadostDiely> categories = ZiadostiService.getInstance().findZiadostDiely(null);
		grid.setItems(categories);

		if (searchField.getValue().length() > 0) {
			header.setText("Search for “" + searchField.getValue() + "”");
		} else {
			header.setText("Žiadosti");
		}
		grid.getColumnByKey("ziadatel").setFooter("" + categories.size());
	}

}
