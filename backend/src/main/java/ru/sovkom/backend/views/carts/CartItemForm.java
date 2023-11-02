package ru.sovkom.backend.views.carts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ru.sovkom.backend.entities.CartItem;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.entities.Dish;

import java.util.List;

/**
 * Форма для редактирования элементов корзины.
 */
public class CartItemForm extends FormLayout {
    ComboBox<Dish> dishComboBox = new ComboBox<>("Выберите блюдо");
    ComboBox<Client> clientComboBox = new ComboBox<>("Выберите клиента");
    IntegerField quantity = new IntegerField("Количество");
    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");
    Binder<CartItem> binder = new BeanValidationBinder<>(CartItem.class);

    public CartItemForm(List<Dish> dishes, List<Client> clients) {
        addClassName("cart-form");
        binder.bindInstanceFields(this);

        dishComboBox.setItems(dishes);
        dishComboBox.setItemLabelGenerator(Dish::getName);

        clientComboBox.setItems(clients);
        clientComboBox.setItemLabelGenerator(Client::getUsername);

        add(dishComboBox, clientComboBox, quantity, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEventItem(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEventItem(this)));
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEventItem(this, binder.getBean()));
        }
    }

    public void setCartItem(CartItem cartItem) {
        binder.setBean(cartItem);
    }


    public static abstract class CartItemFormEvent extends ComponentEvent<CartItemForm> {
        private final CartItem cartItem;

        protected CartItemFormEvent(CartItemForm source, CartItem cartItem) {
            super(source, false);
            this.cartItem = cartItem;
        }

        public CartItem getCartItem() {
            return cartItem;
        }
    }

    public static class SaveEventItem extends CartItemFormEvent {
        SaveEventItem(CartItemForm source, CartItem cartItem) {
            super(source, cartItem);
        }
    }

    public static class DeleteEventItem extends CartItemFormEvent {
        DeleteEventItem(CartItemForm source, CartItem cartItem) {
            super(source, cartItem);
        }

    }

    public static class CloseEventItem extends CartItemFormEvent {
        CloseEventItem(CartItemForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEventItem> listener) {
        return addListener(DeleteEventItem.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEventItem> listener) {
        return addListener(SaveEventItem.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEventItem> listener) {
        return addListener(CloseEventItem.class, listener);
    }


}
