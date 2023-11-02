package ru.sovkom.backend.views.clients;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.services.ClientService;
import com.vaadin.flow.component.button.Button;
import ru.sovkom.backend.views.mainLayout.MainLayout;

import java.util.List;

/**
 * Представление списка клиентов.
 */
@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Users")
public class ListClientsView extends VerticalLayout {
    Grid<Client> grid = new Grid<>(Client.class);
    TextField filterText = new TextField();

    ClientForm form;

    ClientService clientService;

    public ListClientsView(ClientService clientService) {
        this.clientService = clientService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeClient();
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
        form = new ClientForm(clientService);
        form.setWidth("25em");
        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeClient());
    }

    private void saveContact(ClientForm.SaveEvent event) {
        Client clientToSave = event.getClient();
        if (!isClientFieldsValid(clientToSave)) {
            Notification.show("Заполните все поля");
        } else if (!isUsernameUnique(clientToSave)) {
            Notification.show("Пользователь с таким именем уже существует");
        } else {
            clientService.saveClient(clientToSave);
            updateList();
            Notification.show("Пользователь успешно создан");
            closeClient();
        }
    }

    private boolean isUsernameUnique(Client client) {
        List<Client> existingClients = clientService.getUsersByUsername(client.getUsername());
        return existingClients.isEmpty() || (existingClients.size() == 1 && existingClients.get(0).getId() == client.getId());
    }

    private boolean isClientFieldsValid(Client client) {
        return client.getUsername() != null && !client.getUsername().isEmpty() ||
                client.getNumber() != null && !client.getNumber().isEmpty() ||
                client.getEmail() != null && !client.getEmail().isEmpty() ||
                client.getPassword() != null && !client.getPassword().isEmpty();
    }


    private void deleteContact(ClientForm.DeleteEvent event) {
        clientService.deleteClientById(event.getClient().getId());
        updateList();
        closeClient();
    }

    private void configureGrid() {
        grid.addClassNames("client-grid");
        grid.setSizeFull();
        grid.setColumns("id", "username", "number", "email", "password");
        grid.getColumnByKey("id").setHeader("ID Клиента");
        grid.getColumnByKey("username").setHeader("Имя Клиента");
        grid.getColumnByKey("number").setHeader("Номер Телефона");
        grid.getColumnByKey("password").setHeader("Пароль");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editClient(event.getValue()));
    }


    private Component getToolbar() {
        filterText.setWidth("300px");
        filterText.setPlaceholder("Найти клиента по имени");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addClientButton = new Button("Добавить клиента");
        addClientButton.addClickListener(click -> addClient());

        var toolbar = new HorizontalLayout(filterText, addClientButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editClient(Client client) {
        if (client == null) {
            closeClient();
        } else {
            form.setClient(client);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeClient() {
        form.setClient(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addClient() {
        grid.asSingleSelect().clear();
        editClient(new Client());
    }


    private void updateList() {
        grid.setItems(clientService.getUsersByUsername(filterText.getValue()));
    }
}
