package ru.sovkom.backend.views.dishes;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.entities.Dish;
import ru.sovkom.backend.services.ClientService;

import java.util.List;

/**
 * Форма для создания, редактирования блюд.
 */
public class DishForm extends FormLayout {

    TextField name = new TextField("Название");
    NumberField price = new NumberField("Стоимость");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");
    Binder<Dish> binder = new BeanValidationBinder<>(Dish.class);

    ClientService clientService;

    ComboBox<Client> clientComboBox = new ComboBox<>("Клиент");
    public DishForm(List<Client> clientList, ClientService clientService) {
        this.clientService = clientService;
        addClassName("dish-form");
        binder.bindInstanceFields(this);
        clientComboBox.setItems(clientList);
        clientComboBox.setItemLabelGenerator(Client::getUsername);

        Button buttonAddFavorite = new Button("Добавить блюдо в избранное", e -> {
            addFavorites();
            clientComboBox.clear();

            fireEvent(new CloseEvent(this));
        });

        add(name,
                price,
                createButtonsLayout(),
                clientComboBox,
                buttonAddFavorite);
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

    private void addFavorites() {
        boolean addedToFavorites = clientService.addDishToFavorites(clientComboBox.getValue(), binder.getBean());

        if (addedToFavorites) {
            Notification.show("Добавлено в избранное клиента " + clientComboBox.getValue().getUsername());
        } else {
            Notification.show("Блюдо уже было добавлено в избранное");
        }
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    public void setDish(Dish dish) {
        binder.setBean(dish);
    }

    public static abstract class DishFormEvent extends ComponentEvent<DishForm> {
        private final Dish dish;

        protected DishFormEvent(DishForm source, Dish dish) {
            super(source, false);
            this.dish = dish;
        }

        public Dish getDish() {
            return dish;
        }
    }

    public static class SaveEvent extends DishFormEvent {
        SaveEvent(DishForm source, Dish dish) {
            super(source, dish);
        }
    }

    public static class DeleteEvent extends DishFormEvent {
        DeleteEvent(DishForm source, Dish dish) {
            super(source, dish);
        }

    }


    public static class CloseEvent extends DishFormEvent {
        CloseEvent(DishForm source) {
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
