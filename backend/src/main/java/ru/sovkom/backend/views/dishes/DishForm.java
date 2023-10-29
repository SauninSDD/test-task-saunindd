package ru.sovkom.backend.views.dishes;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import ru.sovkom.backend.entities.Dish;

public class DishForm extends FormLayout {
    TextField name = new TextField("Name");
    NumberField price = new NumberField("Price");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Dish> binder = new BeanValidationBinder<>(Dish.class);

    public DishForm() {
        addClassName("dish-form");
        binder.bindInstanceFields(this);

        add(name,
                price,
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


    public void setDish(Dish dish) {
        binder.setBean(dish);
    }

    public static abstract class ContactFormEvent extends ComponentEvent<DishForm> {
        private final Dish dish;

        protected ContactFormEvent(DishForm source, Dish dish) {
            super(source, false);
            this.dish = dish;
        }

        public Dish getDish() {
            return dish;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(DishForm source, Dish dish) {
            super(source, dish);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(DishForm source, Dish dish) {
            super(source, dish);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
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