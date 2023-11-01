package ru.sovkom.backend.views.favorites;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ru.sovkom.backend.entities.Client;

public class FavoriteForm extends FormLayout {
    TextField nameDish = new TextField("Название блюда");
    TextField idClient = new TextField("Id клиента");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");
    Binder<Client> binder = new BeanValidationBinder<>(Client.class);

    public FavoriteForm() {
        addClassName("user-form");
        binder.bindInstanceFields(this);


        add(nameDish,
                idClient,
                createButtonsLayout());
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
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }


    public void setClient(Client client) {
        binder.setBean(client);
    }

    public static abstract class FavoriteFormEvent extends ComponentEvent<FavoriteForm> {
        private final Client client;

        protected FavoriteFormEvent(FavoriteForm source, Client client) {
            super(source, false);
            this.client = client;
        }

        public Client getClient() {
            return client;
        }
    }

    public static class SaveEvent extends FavoriteFormEvent {
        SaveEvent(FavoriteForm source, Client client) {
            super(source, client);
        }
    }

    public static class DeleteEvent extends FavoriteFormEvent {
        DeleteEvent(FavoriteForm source, Client client) {
            super(source, client);
        }

    }

    public static class CloseEvent extends FavoriteFormEvent {
        CloseEvent(FavoriteForm source) {
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