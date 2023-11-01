package ru.sovkom.backend.views.favorites;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
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
import ru.sovkom.backend.views.mainLayout.MainLayout;

@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "favorites", layout = MainLayout.class)
@PageTitle("Favorites")
public class FavoritesDishesView extends VerticalLayout {
    Grid<Client> grid = new Grid<>(Client.class);
    TextField filterText = new TextField();

    FavoriteForm form;

    ClientService clientService;

    public FavoritesDishesView(ClientService clientService) {
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
        form = new FavoriteForm();
        form.setWidth("25em");
        form.addSaveListener(this::addFavorite);
        form.addDeleteListener(this::deleteFavorite);
        form.addCloseListener(e -> closeClient());
    }

    private void addFavorite(FavoriteForm.SaveEvent event) {
        clientService.saveClient(event.getClient());
        updateList();
        closeClient();
    }

    private void deleteFavorite(FavoriteForm.DeleteEvent event) {
        clientService.deleteClientById(event.getClient().getId());
        updateList();
        closeClient();
    }

    private void configureGrid() {
        grid.addClassNames("client-grid");
        grid.setSizeFull();
        grid.setColumns("id","username", "number");

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