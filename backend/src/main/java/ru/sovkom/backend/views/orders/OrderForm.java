package ru.sovkom.backend.views.orders;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.entities.Dish;
import ru.sovkom.backend.entities.Order;

import java.util.List;

public class OrderForm extends FormLayout {
    TextField orderTrackNumber = new TextField("Трек-номер заказа");
    ComboBox<Client> client = new ComboBox<>("Клиент");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");

    Binder<Order> binder = new BeanValidationBinder<>(Order.class);

    Button viewDishesInOrderButton = new Button("Посмотреть блюда в заказе");
    public OrderForm(List<Client> clients) {
        addClassName("order-form");
        binder.bindInstanceFields(this);

        client.setItems(clients);
        client.setItemLabelGenerator(Client::getUsername);


        add(orderTrackNumber,
                client,
                createButtonsLayout(),
                viewDishesInOrderButton);
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }


    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    public void setOrder(Order order) {
        binder.setBean(order);
    }

    public static abstract class OrderFormEvent extends ComponentEvent<OrderForm> {
        private final Order order;

        protected OrderFormEvent(OrderForm source, Order order) {
            super(source, false);
            this.order = order;
        }

        public Order getOrder() {
            return order;
        }
    }

    public static class SaveEvent extends OrderFormEvent {
        SaveEvent(OrderForm source, Order order) {
            super(source, order);
        }
    }

    public static class DeleteEvent extends OrderFormEvent {
        DeleteEvent(OrderForm source, Order order) {
            super(source, order);
        }

    }

    public static class CloseEvent extends OrderFormEvent {
        CloseEvent(OrderForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
