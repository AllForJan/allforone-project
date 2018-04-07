package com.vaadin.starter.beveragebuddy.ui.views.priame_platby;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.beveragebuddy.backend.PriamaPlatba;
import com.vaadin.starter.beveragebuddy.backend.ZiadostiService;
import com.vaadin.starter.beveragebuddy.ui.MainLayout;

import java.util.List;


@Route(value = "platby", layout = MainLayout.class)
@PageTitle("Platby List")
public class ViewPriamePlatbyList extends VerticalLayout {
    private final TextField searchField = new TextField("", "Search");
    private final H2 header = new H2("Platby");
    private final Grid<PriamaPlatba> grid = new Grid<>();

    public ViewPriamePlatbyList() {
        initView();

        addSearchBar();
        addContent();

        updateView();
    }

    private void initView() {
        addClassName("categories-list");
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
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
        container.setAlignItems(FlexComponent.Alignment.STRETCH);

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setMultiSort(true);
        grid.setColumnReorderingAllowed(true);

        grid.addColumn(PriamaPlatba::getZiadatel).setHeader("Žiadateľ").setWidth("8em").setResizable(true).setKey("ziadatel")
                .setSortable(true).setComparator(PriamaPlatba::getZiadatel);
        grid.addColumn(PriamaPlatba::getPsc).setHeader("PSČ").setResizable(true).setSortable(true).setComparator(PriamaPlatba::getPsc);

        grid.addColumn(PriamaPlatba::getObec).setHeader("Obec").setResizable(true).setSortable(true).setComparator(PriamaPlatba::getObec);
        grid.addColumn(PriamaPlatba::getKodOpatrenia).setHeader("Kod opatrenia").setResizable(true).setSortable(true).setComparator(PriamaPlatba::getKodOpatrenia);
        grid.addColumn(PriamaPlatba::getSuma).setHeader("Suma").setResizable(true).setSortable(true).setComparator(PriamaPlatba::getSuma);
        grid.addColumn(PriamaPlatba::getRok).setHeader("Rok").setResizable(true).setSortable(true).setComparator(PriamaPlatba::getRok);
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

    private Button createEditButton(PriamaPlatba entity) {
        Button edit = new Button("Edit");
        // , event -> form.open(entity, AbstractEditorDialog.Operation.EDIT));
        edit.setIcon(new Icon("lumo", "edit"));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        return edit;
    }

    private void updateView() {
        List<PriamaPlatba> list = ZiadostiService.getInstance().findPriamaPlatba(searchField.getValue());
        grid.setItems(list);

        if (searchField.getValue().length() > 0) {
            header.setText("Search for “" + searchField.getValue() + "”");
        } else {
            header.setText("Platby");
        }
        grid.getColumnByKey("ziadatel").setFooter(""+list.size());
    }

}
