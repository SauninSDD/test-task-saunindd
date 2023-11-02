package ru.sovkom.backend.views.carts;

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
import ru.sovkom.backend.entities.Cart;
import ru.sovkom.backend.entities.CartItem;
import ru.sovkom.backend.services.CartService;
import ru.sovkom.backend.services.ClientService;
import ru.sovkom.backend.services.DishService;
import ru.sovkom.backend.views.mainLayout.MainLayout;

/**
 * Представление для отображения, управления корзинами.
 */
@SpringComponent
@Scope("prototype")
@PageTitle("Cart")
@Route(value = "cart", layout = MainLayout.class)
@PermitAll
@Slf4j
public class CartsView extends VerticalLayout {
    Grid<CartItem> grid = new Grid<>(CartItem.class);
    TextField clientId = new TextField();
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
        log.info("Что лежит в ивенте {}", event.getCartItem());
        if (event.getCartItem() != null) {
            Cart cart = event.getCartItem().getCart();
            CartItem cartItem = event.getCartItem();
            log.info("Началось удаление элемента корзины {}", cart);
            cartService.deleteDish(cart, cartItem);
            updateList();
            closeCart();
        } else {
            Notification.show("Чтобы что-то удалить, сначала нужно это создать (с) цитатная цитата");
        }

    }

    private void configureGrid() {
        grid.addClassNames("Cart-grid");
        grid.setSizeFull();
        grid.setColumns("dish.name", "quantity", "cart.client.id", "cart.client.username");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.getColumnByKey("dish.name").setHeader("Название");
        grid.getColumnByKey("quantity").setHeader("Количество");
        grid.getColumnByKey("cart.client.id").setHeader("ID Клиента");
        grid.getColumnByKey("cart.client.username").setHeader("Имя Клиента");
        grid.asSingleSelect().addValueChangeListener(event ->
                editCartItem(event.getValue()));
    }

    private Component getToolbar() {
        clientId.setWidth("300px");
        clientId.setPlaceholder("Найти корзину пользователя с ID");
        clientId.setClearButtonVisible(true);
        clientId.setValueChangeMode(ValueChangeMode.LAZY);
        clientId.addValueChangeListener(e -> updateList());

        Button addCartItemButton = new Button("Добавить блюдо в корзину");
        addCartItemButton.addClickListener(click -> addCartItem());

        var horizontalLayout = new HorizontalLayout(clientId, addCartItemButton);
        horizontalLayout.setAlignItems(Alignment.BASELINE);

        var verticalLayout = new VerticalLayout(horizontalLayout);
        verticalLayout.addClassName("toolbar");
        return verticalLayout;
    }


    public void editCartItem(CartItem cartItem) {
        if (cartItem == null) {
            closeCart();
        } else {
            form.setCartItem(cartItem);
            form.setVisible(true);
            addClassName("editing");

            if (cartItem.getCart() != null && cartItem.getDish() != null) {
                form.clientComboBox.setValue(cartItem.getCart().getClient());
                form.dishComboBox.setValue(cartItem.getDish());
            }
        }
    }


    private void closeCart() {
        form.setCartItem(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addCartItem() {
        form.clientComboBox.setValue(null);
        form.dishComboBox.setValue(null);
        grid.asSingleSelect().clear();
        editCartItem(new CartItem());
    }

    private void updateList() {
        grid.setItems(clientService.getClientCart(clientId.getValue()));

    }
}
