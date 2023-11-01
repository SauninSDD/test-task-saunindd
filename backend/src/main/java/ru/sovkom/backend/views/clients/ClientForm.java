package ru.sovkom.backend.views.clients;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.extern.slf4j.Slf4j;
import ru.sovkom.backend.entities.Client;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.ComponentEventListener;
import ru.sovkom.backend.entities.Dish;
import ru.sovkom.backend.services.ClientService;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

@Slf4j
public class ClientForm extends FormLayout {
    TextField userName = new TextField("Имя клиента");
    TextField number = new TextField("Номер телефона");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Пароль");


    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");

    Button viewFavoritesButton = new Button("Посмотреть избранные блюда");
    Binder<Client> binder = new BeanValidationBinder<>(Client.class);

    ClientService clientService;

    public ClientForm(ClientService clientService) {
        this.clientService = clientService;
        addClassName("user-form");
        binder.bindInstanceFields(this);

        viewFavoritesButton.addClickListener(event -> createFavoritesDialog(binder.getBean()));

        add(userName,
                number,
                email,
                password,
                createButtonsLayout(),
                viewFavoritesButton);
    }

    private void createFavoritesDialog(Client client) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Избранные блюда");
        Set<Dish> favoriteDish = client.getDishesFavorites();
        MultiSelectListBox<Dish> favoriteList = new MultiSelectListBox<>();
        favoriteList.setItems(favoriteDish);
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(favoriteList);

        Button deleteSelectedDish = new Button("Удалить");
        deleteSelectedDish.addClickListener(e -> {
            Set<Dish> selectedDish = new HashSet<>(favoriteList.getSelectedItems());
            deleteFavoriteDishes(selectedDish, client);
            favoriteList.setItems(client.getDishesFavorites());
        });
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(deleteSelectedDish, cancelButton);
        dialog.add(layout);
        dialog.open();
    }


    private void deleteFavoriteDishes(Set<Dish> selectedDish, Client client) {
        log.info("Список избранных на удаление {}", selectedDish);
        client.getDishesFavorites().removeAll(selectedDish);
        log.info("Список избранного после удаления {}", client.getDishesFavorites());

        clientService.saveClient(client);
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


    public void setClient(Client client) {
        binder.setBean(client);
    }

    public static abstract class ClientFormEvent extends ComponentEvent<ClientForm> {
        private final Client client;

        protected ClientFormEvent(ClientForm source, Client client) {
            super(source, false);
            this.client = client;
        }

        public Client getClient() {
            return client;
        }
    }

    public static class SaveEvent extends ClientFormEvent {
        SaveEvent(ClientForm source, Client client) {
            super(source, client);
        }
    }

    public static class DeleteEvent extends ClientFormEvent {
        DeleteEvent(ClientForm source, Client client) {
            super(source, client);
        }

    }

    public static class CloseEvent extends ClientFormEvent {
        CloseEvent(ClientForm source) {
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