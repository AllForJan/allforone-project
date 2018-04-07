package com.vaadin.starter.beveragebuddy.ui.views.ziadatelia;

import java.awt.geom.RoundRectangle2D;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcons;
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

	private final TextField rokOdField = new TextField("", "Rok od");
	private final TextField rokDoField = new TextField("", "Rok do");

	private final H2 header = new H2("Žiadatelia");
	private final Grid<Ziadatel> grid = new Grid<>();

	// private final ZiadostiEditorDialog form = new
	// ZiadostiEditorDialog(this::save, this::delete);

	public ViewZiadateliaList() {
		initView();

		addSearchBar();
		addContent();

		rokOdField.setValue("2004");
		rokDoField.setValue("2017");
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

		rokOdField.setPrefixComponent(new Icon("lumo", "Od"));
		rokOdField.addClassName("view-toolbar__search-field");
		rokOdField.addValueChangeListener(e -> updateView());
		rokOdField.setValueChangeMode(ValueChangeMode.EAGER);

		rokOdField.setPattern("[0-9]*");
		rokOdField.setPreventInvalidInput(true);
		rokOdField.setWidth("100px");

		rokDoField.setPrefixComponent(new Icon("lumo", "Do"));
		rokDoField.addClassName("view-toolbar__search-field");
		rokDoField.addValueChangeListener(e -> updateView());
		rokDoField.setValueChangeMode(ValueChangeMode.EAGER);
		rokDoField.setPattern("[0-9]*");
		rokDoField.setPreventInvalidInput(true);
		rokDoField.setWidth("100px");

		// Button newButton = new Button(adaťNew category", new Icon("lumo", "search"));
		// newButton.getElement().setAttribute("theme", "primary");
		// newButton.addClassName("view-toolbar__button");
		// newButton.addClickListener(e -> form.open(new Category(),
		// AbstractEditorDialog.Operation.ADD));

		viewToolbar.add(searchField, rokOdField, rokDoField);
		add(viewToolbar);
	}

	private void addContent() {
		VerticalLayout container = new VerticalLayout();
		container.setClassName("view-container");
		container.setAlignItems(Alignment.STRETCH);

		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setMultiSort(true);
		grid.setColumnReorderingAllowed(true);

		grid.addColumn(Ziadatel::getZiadatel).setHeader("Žiadateľ").setWidth("8em").setResizable(true)
				.setKey("ziadatel").setSortable(true);
		grid.addColumn(Ziadatel::getIco).setHeader("IČO").setResizable(true).setSortable(true);
		// grid.addColumn(new
		// ComponentRenderer<>(this::createEditButton)).setFlexGrow(0);

		int from = 2004;
		int to = 2017;

		for (int i = from; i <= to; i++) {
			final int rok = i;
			grid.addColumn(ziadatel -> ziadatel.getVymeraZaRok(rok)).setHeader(rok + "").setWidth("3em")
					.setResizable(true);
		}

		grid.addColumn(ziadatel -> ziadatel.getMaxRozdielVymer(Integer.parseInt(rokOdField.getValue()),
				Integer.parseInt(rokDoField.getValue()), true)).setHeader("Indikátok výmer").setResizable(true)
				.setSortable(true);

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
		if (!StringUtils.isEmpty(rokDoField.getValue()) && !StringUtils.isEmpty(rokOdField.getValue())) {
			List<Ziadatel> list = ZiadostiService.getInstance().findZiadatelov(searchField.getValue(),
					Integer.parseInt(rokOdField.getValue()), Integer.parseInt(rokDoField.getValue()));
			grid.setItems(list);

			if (searchField.getValue().length() > 0) {
				header.setText("Search for “" + searchField.getValue() + "”");
			} else {
				header.setText("Žiadatelia");
			}
			grid.getColumnByKey("ziadatel").setFooter("" + list.size());
		}
	}

}
