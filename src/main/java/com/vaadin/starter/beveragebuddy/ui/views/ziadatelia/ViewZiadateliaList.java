package com.vaadin.starter.beveragebuddy.ui.views.ziadatelia;

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
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.beveragebuddy.backend.Category;
import com.vaadin.starter.beveragebuddy.backend.CategoryService;
import com.vaadin.starter.beveragebuddy.backend.Review;
import com.vaadin.starter.beveragebuddy.backend.ReviewService;
import com.vaadin.starter.beveragebuddy.backend.Ziadatel;
import com.vaadin.starter.beveragebuddy.backend.ZiadostiService;
import com.vaadin.starter.beveragebuddy.backend.ZiadostDiely;
import com.vaadin.starter.beveragebuddy.ui.MainLayout;
import com.vaadin.starter.beveragebuddy.ui.common.AbstractEditorDialog;

@Route(value = "ziadatelia", layout = MainLayout.class)
@PageTitle("Žiadatelia List")
public class ViewZiadateliaList extends VerticalLayout {

	private final TextField searchField = new TextField("", "Search");
	private final H2 header = new H2("Žiadatelia");
	private final Grid<Ziadatel> grid = new Grid<>();

	// private final ZiadostiEditorDialog form = new
	// ZiadostiEditorDialog(this::save, this::delete);

	public ViewZiadateliaList() {
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

		grid.addColumn(Ziadatel::getZiadatel).setHeader("Žiadataľ").setWidth("8em").setResizable(true).setKey("ziadatel")
				.setSortable(true);
		grid.addColumn(Ziadatel::getIco).setHeader("IČO").setResizable(true).setSortable(true);
		grid.addColumn(new ComponentRenderer<>(this::createEditButton)).setFlexGrow(0);

		//grid.addColumn(TemplateRenderer.<Ziadatel>of("<b>[[item.listZiadostDiely.size]]</b>")
		//		.withProperty(Ziadatel::getListZiadostDiely, z -> z.getListZiadostDiely().size()))
		//		.setHeader("Žiadosti");

//		grid.setItemDetailsRenderer(new ComponentRenderer<>(Ziadatel -> {
//			VerticalLayout layout = new VerticalLayout();
//			layout.add(
//					new Label("Address: " + person.getAddress().getStreet() + " " + person.getAddress().getNumber()));
//			layout.add(new Label("Year of birth: " + person.getYearOfBirth()));
//			return layout;
//		}));

		container.add(header, grid);
		add(container);
	}

	private Button createEditButton(Ziadatel entity) {
		Button edit = new Button("Edit");
		// , event -> form.open(entity, AbstractEditorDialog.Operation.EDIT));
		edit.setIcon(new Icon("lumo", "edit"));
		edit.addClassName("review__edit");
		edit.getElement().setAttribute("theme", "tertiary");
		return edit;
	}

	private void updateView() {
		List<Ziadatel> list = ZiadostiService.getInstance().findZiadatelov(searchField.getValue());
		grid.setItems(list);

		if (searchField.getValue().length() > 0) {
			header.setText("Search for “" + searchField.getValue() + "”");
		} else {
			header.setText("Žiadatelia");
		}
		grid.getColumnByKey("ziadatel").setFooter(""+list.size());
	}

}
