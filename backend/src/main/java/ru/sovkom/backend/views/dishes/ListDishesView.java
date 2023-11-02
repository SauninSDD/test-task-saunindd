package ru.sovkom.backend.views.dishes;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import ru.sovkom.backend.entities.Dish;
import ru.sovkom.backend.services.ClientService;
import ru.sovkom.backend.services.DishService;
import ru.sovkom.backend.views.mainLayout.MainLayout;

/**
 * Представление для меню.
 */
@SpringComponent
@Scope("prototype")
@PageTitle("Dishes")
@Route(value = "dishes", layout = MainLayout.class)
@PermitAll
@Slf4j
public class ListDishesView extends VerticalLayout {
    Grid<Dish> grid = new Grid<>(Dish.class);
    TextField filterText = new TextField();

    DishForm form;

    DishService dishService;

    ClientService clientService;

    public ListDishesView(DishService dishService, ClientService clientService) {
        this.dishService = dishService;
        this.clientService = clientService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeDish();
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
        form = new DishForm(clientService.getAllUsers(), clientService);
        form.setWidth("25em");
        form.addSaveListener(this::saveDish);
        form.addDeleteListener(this::deleteDish);
        form.addCloseListener(e -> closeDish());
    }

    private void saveDish(DishForm.SaveEvent event) {
        Dish dishToSave = event.getDish();

        if (isDishFieldsValid(dishToSave)) {
            dishService.addDish(dishToSave);
            updateList();
            closeDish();
        } else {
            Notification.show("Заполните все поля");
        }
    }

    private boolean isDishFieldsValid(Dish dish) {
        return dish.getName() != null && !dish.getName().isEmpty() &&
                dish.getPrice() != null;
    }

    private void deleteDish(DishForm.DeleteEvent event) {
        log.info("Блюдо на удаление {}", event);
        if (!dishService.deleteDish(event.getDish())) {
            Notification.show("Мы ведь не хотим обидеть клиентов, поэтому давайте совершенно законно сначала удалим блюдо из их избранных :) . Мы понимаем, что это странное решение, но это необходимо для тестирования связи ManyToMany и кастомного запроса через query. Лааадно, мы попробуем, но в бд будет ошибка внешнего ключа, лучше создать новый продукт и попробовать его удалить");
        }
        updateList();
        closeDish();
    }

    private void configureGrid() {
        grid.addClassNames("dish-grid");
        grid.setSizeFull();
        grid.setColumns("id", "name", "price");
        grid.getColumnByKey("id").setHeader("ID Блюда");
        grid.getColumnByKey("name").setHeader("Название");
        grid.getColumnByKey("price").setHeader("Стоимость");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editDish(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setWidth("300px");
        filterText.setPlaceholder("Найти блюдо по названию");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());


        Button addDishButton = new Button("Добавить блюдо");
        addDishButton.addClickListener(click -> addDish());

        var toolbar = new HorizontalLayout(filterText, addDishButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editDish(Dish dish) {
        if (dish == null) {
            closeDish();
        } else {
            form.setDish(dish);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    public void closeDish() {
        form.setDish(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addDish() {
        grid.asSingleSelect().clear();
        editDish(new Dish());
    }

    private void updateList() {
        grid.setItems(dishService.getDishesByName(filterText.getValue()));
    }
}
