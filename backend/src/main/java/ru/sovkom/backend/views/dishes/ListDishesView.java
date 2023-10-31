package ru.sovkom.backend.views.dishes;

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
import ru.sovkom.backend.entities.Dish;
import ru.sovkom.backend.services.DishService;
import ru.sovkom.backend.views.mainLayout.MainLayout;

@SpringComponent
@Scope("prototype")
@PageTitle("Dishes")
@Route(value = "dishes", layout = MainLayout.class)
@PermitAll
public class ListDishesView extends VerticalLayout {
    Grid<Dish> grid = new Grid<>(Dish.class);
    TextField filterText = new TextField();

    DishForm form;

    DishService dishService;

    public ListDishesView(DishService dishService) {
        this.dishService = dishService;
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
        form = new DishForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveDish);
        form.addDeleteListener(this::deleteDish);
        form.addCloseListener(e -> closeDish());
    }

    private void saveDish(DishForm.SaveEvent event) {
        dishService.addDish(event.getDish());
        updateList();
        closeDish();
    }

    private void deleteDish(DishForm.DeleteEvent event) {
        dishService.deleteDish(event.getDish().getId());
        updateList();
        closeDish();
    }

    private void configureGrid() {
        grid.addClassNames("dish-grid");
        grid.setSizeFull();
        grid.setColumns("id", "name", "price");
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

    private void closeDish() {
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