package com.vaadin.starter.beveragebuddy.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcons;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.starter.beveragebuddy.ui.views.categorieslist.CategoriesList;
import com.vaadin.starter.beveragebuddy.ui.views.poberatelia.ViewPoberateliaList;
import com.vaadin.starter.beveragebuddy.ui.views.priame_platby.ViewPriamePlatbyList;
import com.vaadin.starter.beveragebuddy.ui.views.reviewslist.ReviewsList;
import com.vaadin.starter.beveragebuddy.ui.views.ziadatelia.ViewZiadateliaList;
import com.vaadin.starter.beveragebuddy.ui.views.ziadosti.ViewZiadostiList;

@HtmlImport("frontend://styles/shared-styles.html")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Theme(Lumo.class)
public class MainLayout extends Div implements RouterLayout,
        AfterNavigationObserver, PageConfigurator {

    private static final String ACTIVE_ITEM_STYLE = "main-layout__nav-item--selected";
    private RouterLink categories;
    private RouterLink ziadatelia;
    private RouterLink ziadosti;
    private RouterLink platby;
    private RouterLink poberatelia;
    

    public MainLayout() {
        H2 title = new H2("#allforone");
        title.addClassName("main-layout__title");

        categories = new RouterLink(null, CategoriesList.class);
        categories.add(new Icon(VaadinIcons.ARCHIVES), new Text("Naši ľudia"));
        categories.addClassName("main-layout__nav-item");
        
        ziadosti = new RouterLink(null, ViewZiadostiList.class);
        ziadosti.add(new Icon(VaadinIcons.LINES_LIST), new Text("Žiadosti"));
        ziadosti.addClassName("main-layout__nav-item");

        ziadatelia = new RouterLink(null, ViewZiadateliaList.class);
        ziadatelia.add(new Icon(VaadinIcons.USERS), new Text("Žiadatelia"));
        ziadatelia.addClassName("main-layout__nav-item");

        platby = new RouterLink(null, ViewPriamePlatbyList.class);
        platby.add(new Icon(VaadinIcons.EURO), new Text("Platby"));
        platby.addClassName("main-layout__nav-item");

        poberatelia = new RouterLink(null, ViewPoberateliaList.class);
        poberatelia.add(new Icon(VaadinIcons.DIAMOND), new Text("Poberatelia"));
        poberatelia.addClassName("main-layout__nav-item");

        Div navigation = new Div(ziadatelia, poberatelia, ziadosti, platby, categories);

        navigation.addClassName("main-layout__nav");

        Div header = new Div(title, navigation);
        header.addClassName("main-layout__header");
        add(header);

        addClassName("main-layout");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String segment = event.getLocation().getFirstSegment();
        boolean categoriesActive = segment.equals(categories.getHref());
        boolean ziadostiActive = segment.equals(ziadosti.getHref());
        boolean ziadateliaActive = segment.equals(ziadatelia.getHref());
        boolean platbyActive = segment.equals(platby.getHref());
        boolean poberateliaActive = segment.equals(poberatelia.getHref());

        ziadatelia.setClassName(ACTIVE_ITEM_STYLE, ziadateliaActive);
        ziadosti.setClassName(ACTIVE_ITEM_STYLE, ziadostiActive);
        categories.setClassName(ACTIVE_ITEM_STYLE, categoriesActive);
        platby.setClassName(ACTIVE_ITEM_STYLE, platbyActive);
        poberatelia.setClassName(ACTIVE_ITEM_STYLE, poberateliaActive);
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes");
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
    }
}
