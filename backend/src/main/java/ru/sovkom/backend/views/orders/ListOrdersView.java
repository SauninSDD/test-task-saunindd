package ru.sovkom.backend.views.orders;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
import ru.sovkom.backend.entities.Order;
import ru.sovkom.backend.entities.OrderDish;
import ru.sovkom.backend.services.ClientService;
import ru.sovkom.backend.services.DishService;
import ru.sovkom.backend.services.OrderService;
import ru.sovkom.backend.views.mainLayout.MainLayout;

@SpringComponent
@Scope("prototype")
@PageTitle("Orders")
@Route(value = "orders", layout = MainLayout.class)
@PermitAll
public class ListOrdersView extends VerticalLayout {
    Grid<Order> grid = new Grid<>(Order.class);
    TextField filterText = new TextField();

    OrderForm form;

    OrderService orderService;

    ClientService clientService;

    DishService dishService;

    public ListOrdersView(OrderService orderService, ClientService clientService, DishService dishService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.dishService = dishService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeOrder();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new OrderForm(clientService.getAllUsers(), dishService.getListDishes());
        form.setWidth("25em");
        form.addSaveListener(this::saveOrder);
        form.addDeleteListener(this::deleteOrder);
        form.addCloseListener(e -> closeOrder());
    }

    private void saveOrder(OrderForm.SaveEvent event) {
        orderService.createOrder(event.getOrder());
        updateList();
        closeOrder();
    }

    private void deleteOrder(OrderForm.DeleteEvent event) {
        orderService.deleteOrder(event.getOrder().getId());
        updateList();
        closeOrder();
    }

    private void configureGrid() {
        grid.addClassNames("order-grid");
        grid.setSizeFull();
        grid.setColumns("id", "orderTrackNumber");
        grid.addColumn(order -> order.getClient().getUsername()).setHeader("Client");
        grid.addComponentColumn(order -> {
            VerticalLayout layout = new VerticalLayout();
            for (OrderDish orderDish : order.getDishesInOrder()) {
                layout.add(new Span(orderDish.getDish().getName() + " (" + orderDish.getOrderDishValue() + ")"));
            }
            return layout;
        }).setHeader("Dishes");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editOrder(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setWidth("300px");
        filterText.setPlaceholder("Поиск по трек-номеру заказа");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addOrderButton = new Button("Создать заказ");
        addOrderButton.addClickListener(click -> addOrder());

        var toolbar = new HorizontalLayout(filterText, addOrderButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editOrder(Order order) {
        if (order == null) {
            closeOrder();
        } else {
            form.setOrder(order);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeOrder() {
        form.setOrder(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addOrder() {
        grid.asSingleSelect().clear();
        editOrder(new Order());
    }

    private void updateList() {
        grid.setItems(orderService.getOrderByOrderTrackNumber(filterText.getValue()));
    }
}