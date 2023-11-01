package ru.sovkom.backend.views.mainLayout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import ru.sovkom.backend.security.SecurityService;
import ru.sovkom.backend.views.carts.CartsView;
import ru.sovkom.backend.views.dishes.ListDishesView;
import ru.sovkom.backend.views.clients.ListClientsView;
import ru.sovkom.backend.views.orders.ListOrdersView;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Тестовое задание");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

            String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Выйти из профиля " + u, e -> securityService.logout());
        logout.getStyle().set("color", "red");

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }


    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Клиенты", ListClientsView.class),
                new RouterLink("Меню", ListDishesView.class),
                new RouterLink("Корзины", CartsView.class),
                new RouterLink("Заказы", ListOrdersView.class)
        ));
    }
}