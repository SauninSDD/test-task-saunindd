package ru.sovkom.backend.exceptions;

/**
 * Исключение, которое выбрасывается, когда корзина не найдена.
 */
public class CartNotFoundException extends RuntimeException {
    /**
     * Создает новое исключение с указанным сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public CartNotFoundException(String message) {
        super(message);
    }
}
