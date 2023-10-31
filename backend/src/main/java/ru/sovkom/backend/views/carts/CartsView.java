package ru.sovkom.backend.views.carts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import ru.sovkom.backend.entities.CartItem;
import ru.sovkom.backend.services.CartService;
import ru.sovkom.backend.services.ClientService;
import ru.sovkom.backend.services.DishService;
import ru.sovkom.backend.views.mainLayout.MainLayout;

@SpringComponent
@Scope("prototype")
@PageTitle("Cart")
@Route(value = "cart", layout = MainLayout.class)
@PermitAll
public class CartsView extends VerticalLayout {
    Grid<CartItem> grid = new Grid<>(CartItem.class);
    TextField clientId = new TextField("Id клиента");


    CartItemForm form;

    CartService cartService;

    ClientService clientService;

    DishService dishService;

    public CartsView(CartService cartService, ClientService clientService, DishService dishService) {
        this.cartService = cartService;
        this.clientService = clientService;
        this.dishService = dishService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeCart();
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
        form = new CartItemForm(dishService.getListDishes(), clientService.getAllUsers());
        form.setWidth("25em");
        form.addSaveListener(this::saveCart);
        form.addDeleteListener(this::deleteCartItem);
        form.addCloseListener(e -> closeCart());
    }

    private void saveCart(CartItemForm.SaveEventItem event) {
        if (form.clientComboBox.getValue() != null && form.dishComboBox.getValue() != null && form.quantity.getValue() > 0) {
            long clientIdBox = form.clientComboBox.getValue().getId();
            long cartId = cartService.getCartIdByClientId(clientIdBox).getId();
            long dishId = form.dishComboBox.getValue().getId();
            int quantity = event.getCartItem().getQuantity();
            clientId.setValue(String.valueOf(clientIdBox));
            cartService.addToCart(cartId, dishId, quantity);
            updateList();
            closeCart();
        }
    }


    private void deleteCartItem(CartItemForm.DeleteEventItem event) {
        long cartId = cartService.getCartIdByClientId(form.clientComboBox.getValue().getId()).getId();
        cartService.deleteDish(cartId, event.getCartItem().getDish().getId());
        updateList();
        closeCart();
    }

    private void configureGrid() {
        grid.addClassNames("Cart-grid");
        grid.setSizeFull();
        grid.setColumns("quantity");
        grid.addColumn(cartItem -> cartItem.getDish().getName()).setHeader("Dish");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addColumn(cartItem -> cartItem.getCart().getClient().getId()).setHeader("Id клиента");

        grid.asSingleSelect().addValueChangeListener(event ->
                editCartItem(event.getValue()));
    }

    private Component getToolbar() {
        clientId.setWidth("350px");
        clientId.setPlaceholder("Найти блюда в корзине пользователя с id");
        clientId.setClearButtonVisible(true);
        clientId.setValueChangeMode(ValueChangeMode.LAZY);
        clientId.addValueChangeListener(e -> updateList());

        Button addCartItemButton = new Button("Добавить блюдо в корзину");
        addCartItemButton.addClickListener(click -> addCartItem());

        var toolbar = new HorizontalLayout(clientId, addCartItemButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editCartItem(CartItem cartItem) {
        if (cartItem == null) {
            closeCart();
        } else {
            /*form.dishComboBox.setValue(cartItem.getDish());
            form.clientComboBox.setValue(cartItem.getCart().getClient());*/
            form.setCartItem(cartItem);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeCart() {
        form.setCartItem(null);
               form.setVisible(false);
        removeClassName("editing");
    }

    private void addCartItem() {

        grid.asSingleSelect().clear();
        editCartItem(new CartItem());
    }

    private void updateList() {
        grid.setItems(clientService.getClientCart(clientId.getValue()));

    }
}
