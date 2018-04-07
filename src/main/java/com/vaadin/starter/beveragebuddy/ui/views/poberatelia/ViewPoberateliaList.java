package com.vaadin.starter.beveragebuddy.ui.views.poberatelia;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.TemplateRenderer;
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
import com.vaadin.starter.beveragebuddy.backend.ZiadostiService;
import com.vaadin.starter.beveragebuddy.ui.MainLayout;

@Route(value = "ziadatelia", layout = MainLayout.class)
@PageTitle("Žiadatelia List")
public class ViewPoberateliaList extends VerticalLayout {

	private final TextField searchField = new TextField("", "Search");

	private final TextField rokOdField = new TextField("", "Rok od");
	private final TextField rokDoField = new TextField("", "Rok do");
	private final ComboBox<String> comboBox = new ComboBox<>(null, Arrays.asList("suma hektárov", "pocet lokalit"));

	private final H2 header = new H2("Poberatelia platieb");
	private final Grid<Ziadatel> grid = new Grid<>();

	private final int from = 2004;
	private final int to = 2017;

	// private final ZiadostiEditorDialog form = new
	// ZiadostiEditorDialog(this::save, this::delete);

	public ViewPoberateliaList() {
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

		comboBox.setValue("suma hektárov");
		comboBox.addValueChangeListener((combo) -> {
			if(!combo.getOldValue().equals(combo.getValue())) {
				comboBoxChanged(combo.getValue());
			}
		});

		// Button newButton = new Button(adaťNew category", new Icon("lumo", "search"));
		// newButton.getElement().setAttribute("theme", "primary");
		// newButton.addClassName("view-toolbar__button");
		// newButton.addClickListener(e -> form.open(new Category(),
		// AbstractEditorDialog.Operation.ADD));

		//viewToolbar.add(searchField, rokOdField, rokDoField,comboBox);
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
		grid.addColumn(TemplateRenderer.<Ziadatel>of("<div><b>[[item.name]]</b><br><a target=\"_blank\" href=\"http://www.finstat.sk/[[item.ico]]\">[[item.ico]]</a></div>")
				.withProperty("name", ziadatel -> ziadatel.getZiadatel())
				.withProperty("ico", ziadatel -> ziadatel.getIco()))
				// .withProperty("adr", ziadatel ->
				// ziadatel.getAdresaString())).setHeader("Žiadateľ").setWidth("12em")
				.setKey("ziadatel");


		grid.addColumn(ziadatel -> new DecimalFormat("#,###").format(ziadatel.getMaxRozdielVymer(
				Integer.parseInt(rokOdField.getValue()), Integer.parseInt(rokDoField.getValue()))))
				.setHeader("Nárast výmery v ha").setResizable(true).setSortable(true)
				.setComparator((person1, person2) -> person1
						.getMaxRozdielVymer(Integer.parseInt(rokOdField.getValue()),
								Integer.parseInt(rokDoField.getValue()))
						.compareTo(person2.getMaxRozdielVymer(Integer.parseInt(rokOdField.getValue()),
								Integer.parseInt(rokDoField.getValue()))));

		grid.addColumn(ziadatel -> new DecimalFormat("#,###").format(ziadatel.getMinRozdielVymer(
				Integer.parseInt(rokOdField.getValue()), Integer.parseInt(rokDoField.getValue()))))
				.setHeader("Pokles výmery v ha").setResizable(true).setSortable(true)
				.setComparator((person1, person2) -> person1
						.getMinRozdielVymer(Integer.parseInt(rokOdField.getValue()),
								Integer.parseInt(rokDoField.getValue()))
						.compareTo(person2.getMinRozdielVymer(Integer.parseInt(rokOdField.getValue()),
								Integer.parseInt(rokDoField.getValue()))));


		grid.addColumn(ziadatel -> ziadatel.getMaximumLokalit()).setHeader("Počet lokalít").setResizable(true)
				.setSortable(true)
				.setComparator(Ziadatel::getMaximumLokalit)
				.setKey("lokalita");

		createYearColumns();
	}

	private void createYearColumns() {
		if("suma hektárov".equals(comboBox.getValue())) {
			for (int i = from; i <= to; i++) {
				final int rok = i;
				grid.addColumn(ziadatel -> ziadatel.getVymeraZaRok(rok)+(ziadatel.getMaxRok(Integer.parseInt(rokOdField.getValue()), Integer.parseInt(rokDoField.getValue()))==rok?" +":"")+(ziadatel.getMinRok(Integer.parseInt(rokOdField.getValue()), Integer.parseInt(rokDoField.getValue()))==rok?" -":"")).setHeader(rok + "").setKey(rok + "").setWidth("3em")
						.setResizable(true);
			}
		} else {
			for (int i = from; i <= to; i++) {
				final int rok = i;
				grid.addColumn(ziadatel -> ziadatel.getLokalityZaRok(rok)).setHeader(rok + "").setKey(rok + "").setWidth("3em")
						.setResizable(true);
			}
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
			List<Ziadatel> list = ZiadostiService.getInstance().findZiadatelov(searchField.getValue(),
					Integer.parseInt(rokOdField.getValue()), Integer.parseInt(rokDoField.getValue()));
			
			grid.setItems(list);

			if (searchField.getValue().length() > 0) {
				header.setText("Search for “" + searchField.getValue() + "”");
			} else {
				header.setText("Žiadatelia");
			}
			grid.getColumnByKey("ziadatel").setFooter("" + list.size()).setHeader("Žiadateľ");

		}
	}

	private void comboBoxChanged(String value) {
		for (int i = from; i <= to; i++) {
			grid.removeColumnByKey(i + "");
		}

		createYearColumns();
	}
}
