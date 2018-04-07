package com.vaadin.starter.beveragebuddy.ui.views.poberatelia;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.beveragebuddy.backend.Ziadatel;
import com.vaadin.starter.beveragebuddy.backend.rpvs.PoberatelSumar;
import com.vaadin.starter.beveragebuddy.backend.rpvs.RpvsService;
import com.vaadin.starter.beveragebuddy.ui.MainLayout;

@Route(value = "poberatelia", layout = MainLayout.class)
@PageTitle("Poberatelia List")
public class ViewPoberateliaList extends VerticalLayout {

	private final TextField searchField = new TextField("", "Search");

	private final TextField rokOdField = new TextField("", "Rok od");
	private final TextField rokDoField = new TextField("", "Rok do");
	// private final ComboBox<String> comboBox = new ComboBox<>(null,
	// Arrays.asList("suma hektárov", "pocet lokalit"));

	private final H2 header = new H2("Poberatelia platieb");
	private final Grid<PoberatelSumar> grid = new Grid<>();

	int from = 2015;
	int to = 2016;

	public ViewPoberateliaList() {
		initView();

		addSearchBar();
		addContent();

		rokOdField.setValue("2015");
		rokDoField.setValue("2016");
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

		// comboBox.setValue("suma hektárov");
		// comboBox.addValueChangeListener((combo) -> {
		// if(!combo.getOldValue().equals(combo.getValue())) {
		// comboBoxChanged(combo.getValue());
		// }
		// });

		// Button newButton = new Button(adaťNew category", new Icon("lumo", "search"));
		// newButton.getElement().setAttribute("theme", "primary");
		// newButton.addClassName("view-toolbar__button");
		// newButton.addClickListener(e -> form.open(new Category(),
		// AbstractEditorDialog.Operation.ADD));

		// viewToolbar.add(searchField, rokOdField, rokDoField,comboBox);
		viewToolbar.add(searchField);
		add(viewToolbar);

	}

	private void addContent() {
		VerticalLayout container = new VerticalLayout();
		container.setClassName("view-container");
		container.setAlignItems(Alignment.STRETCH);

		grid.setSelectionMode(SelectionMode.SINGLE);

		createAllColumns();

		container.add(header, grid);

		add(container);
	}

	private void createAllColumns() {

		grid.addColumn(poberatel -> poberatel.getPoberatel().getName()).setHeader("Pobetateľ").setResizable(true)
				.setSortable(true).setKey("pobetatel");
		grid.addColumn(poberatel -> poberatel.getPoberatel().getStreet()).setHeader("Ulica").setResizable(true)
				.setSortable(true);
		grid.addColumn(poberatel -> poberatel.getPoberatel().getZipCode()).setHeader("PSČ").setResizable(true)
				.setSortable(true);
		grid.addColumn(poberatel -> poberatel.getPoberatel().getCity()).setHeader("Mesto").setResizable(true)
				.setSortable(true);
		grid.addColumn(poberatel -> poberatel.getPoberatel().getBirthDate()).setHeader("Dátum Narodenia")
				.setResizable(true).setSortable(true);

		createYearColumns();

		grid.addColumn(poberatel -> poberatel.getSumaVsetkychPlatieb()).setHeader("Celková suma").setResizable(true)
				.setSortable(true);

	}

	private void createYearColumns() {
		for (int i = from; i <= to; i++) {
			final int rok = i;
			grid.addColumn(ziadatel -> ziadatel.getSumaPlatieb(rok))		
					.setHeader("Platby "+rok).setKey(rok + "").setWidth("3em").setResizable(true);
			
		}

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
			List<PoberatelSumar> list = RpvsService.getInstance().findPoberatelov(searchField.getValue(),
					Integer.parseInt(rokOdField.getValue()), Integer.parseInt(rokDoField.getValue()));

			grid.setItems(list);

			if (searchField.getValue().length() > 0) {
				header.setText("Search for “" + searchField.getValue() + "”");
			} else {
				header.setText("Poberatelia");
			}
			grid.getColumnByKey("poberatel").setFooter("" + list.size()).setHeader("Poberateľ");
		}
	}

	private void comboBoxChanged(String value) {
		for (int i = from; i <= to; i++) {
			grid.removeColumnByKey(i + "");
		}

		createYearColumns();
	}
}
