create schema if not exists restaurant;


-- DELETE FROM clients WHERE id_client = 1;

    insert into restaurant.clients(id_client, email, number, password, username) values(1,2,1,1,1);

    INSERT INTO restaurant.dishes(id_dish, name, price) VALUES (1,'jdj',1);

    insert into restaurant.clients_favorites(id_client, id_dish) VALUES (1,1);

    INSERT INTO restaurant.carts(id_cart, id_client) VALUES (1,1);

    INSERT INTO restaurant.cart_items(id_cart_item, quantity, id_cart, id_dish) VALUES (1,1,1,1);

    INSERT INTO restaurant.orders(id_order, order_track_number, id_client) VALUES (1,1123,1);

    INSERT INTO restaurant.orders_dishes(id_order_dish, order_dish_value, id_dish, id_order) VALUES (1,1,1,1);
