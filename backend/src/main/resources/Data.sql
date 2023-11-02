

-- Вставка клиента 1
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (1, 'John Doe', '+1234567890', 'johndoe@example.com', 'password123');

-- Вставка клиента 2
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (2, 'Jane Smith', '+1234567890', 'janesmith@example.com', 'securepass');

-- Вставка клиента 3
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (3, 'Alice Johnson', '+9876543210', 'alice@example.com', 'mysecret');

-- Вставка клиента 4
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (4, 'Bob Brown', '+5555555555', 'bob@example.com', 'bobspass');

-- Вставка клиента 5
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (5, 'Eva White', '+1112223333', 'eva@example.com', 'evapass');

-- Вставка клиента 6
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (6, 'David_client Smith', '+1231231231', 'david_client@example.com', 'david_clientpass');

-- Вставка клиента 7
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (7, 'Mary Johnson', '+7778889999', 'mary@example.com', 'marypass');

-- Вставка клиента 8
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (8, 'Chris Black', '+4445556666', 'chris@example.com', 'chrispass');

-- Вставка клиента 9
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (9, 'Sara Wilson', '+3332221111', 'sara@example.com', 'sarapass');

-- Вставка клиента 10
INSERT INTO restaurant.clients (id_client, username, number, email, password)
VALUES (10, 'Tom Davis', '+9998887777', 'tom@example.com', 'tompass');

-- Вставка корзины для клиента 1
INSERT INTO restaurant.carts (id_client)
VALUES (1);

-- Вставка корзины для клиента 2
INSERT INTO restaurant.carts (id_client)
VALUES (2);

-- Вставка корзины для клиента 3
INSERT INTO restaurant.carts (id_client)
VALUES (3);

-- Вставка корзины для клиента 4
INSERT INTO restaurant.carts (id_client)
VALUES (4);

-- Вставка корзины для клиента 5
INSERT INTO restaurant.carts (id_client)
VALUES (5);

-- Вставка корзины для клиента 6
INSERT INTO restaurant.carts (id_client)
VALUES (6);

-- Вставка корзины для клиента 7
INSERT INTO restaurant.carts (id_client)
VALUES (7);

-- Вставка корзины для клиента 8
INSERT INTO restaurant.carts (id_client)
VALUES (8);

-- Вставка корзины для клиента 9
INSERT INTO restaurant.carts (id_client)
VALUES (9);

-- Вставка корзины для клиента 10
INSERT INTO restaurant.carts (id_client)
VALUES (10);


-- Вставка блюда 1
INSERT INTO restaurant.dishes (name, price)
VALUES ('Spaghetti Carbonara', 12.99);

-- Вставка блюда 2
INSERT INTO restaurant.dishes (name, price)
VALUES ('Margherita Pizza', 10.99);

-- Вставка блюда 3
INSERT INTO restaurant.dishes (name, price)
VALUES ('Grilled Salmon', 15.99);

-- Вставка блюда 4
INSERT INTO restaurant.dishes (name, price)
VALUES ('Caesar Salad', 8.99);

-- Вставка блюда 5
INSERT INTO restaurant.dishes (name, price)
VALUES ('Chocolate Cake', 6.99);

-- Вставка блюда 6
INSERT INTO restaurant.dishes (name, price)
VALUES ('Chicken Alfredo', 13.99);

-- Вставка блюда 7
INSERT INTO restaurant.dishes (name, price)
VALUES ('Tuna Sashimi', 14.99);

-- Вставка блюда 8
INSERT INTO restaurant.dishes (name, price)
VALUES ('Mushroom Risotto', 11.99);

-- Вставка блюда 9
INSERT INTO restaurant.dishes (name, price)
VALUES ('Beef Tacos', 9.99);

-- Вставка блюда 10
INSERT INTO restaurant.dishes (name, price)
VALUES ('Vegetable Stir-Fry', 10.99);


-- Создание заказов
-- Заказ 1 для клиента 1
INSERT INTO restaurant.orders (order_track_number, id_client)
VALUES ('ORDER123', 1);

-- Заказ 2 для клиента 2
INSERT INTO restaurant.orders (order_track_number, id_client)
VALUES ('ORDER456', 2);

-- Заказ 3 для клиента 3
INSERT INTO restaurant.orders (order_track_number, id_client)
VALUES ('ORDER789', 3);

-- Заказ 4 для клиента 4
INSERT INTO restaurant.orders (order_track_number, id_client)
VALUES ('ORDER321', 4);

-- Заказ 5 для клиента 5
INSERT INTO restaurant.orders (order_track_number, id_client)
VALUES ('ORDER654', 5);

-- Добавление блюд в заказы
-- Заказ 1: блюдо 1 (2 шт.) и блюдо 2 (1 шт.)
INSERT INTO restaurant.orders_dishes (id_order, id_dish, order_dish_value)
VALUES (1, 1, 2);

INSERT INTO restaurant.orders_dishes (id_order, id_dish, order_dish_value)
VALUES (1, 2, 1);

-- Заказ 2: блюдо 3 (3 шт.) и блюдо 4 (1 шт.)
INSERT INTO restaurant.orders_dishes (id_order, id_dish, order_dish_value)
VALUES (2, 3, 3);

INSERT INTO restaurant.orders_dishes (id_order, id_dish, order_dish_value)
VALUES (2, 4, 1);

-- Заказ 3: блюдо 5 (2 шт.)
INSERT INTO restaurant.orders_dishes (id_order, id_dish, order_dish_value)
VALUES (3, 5, 2);

-- Заказ 4: блюдо 6 (1 шт.) и блюдо 7 (2 шт.)
INSERT INTO restaurant.orders_dishes (id_order, id_dish, order_dish_value)
VALUES (4, 6, 1);

INSERT INTO restaurant.orders_dishes (id_order, id_dish, order_dish_value)
VALUES (4, 7, 2);

-- Заказ 5: блюдо 8 (2 шт.)
INSERT INTO restaurant.orders_dishes (id_order, id_dish, order_dish_value)
VALUES (5, 8, 2);
