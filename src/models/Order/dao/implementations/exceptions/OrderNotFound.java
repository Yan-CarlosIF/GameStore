package models.Order.dao.implementations.exceptions;

public class OrderNotFound extends Exception {
    public OrderNotFound() {
        super("Pedido não foi encontrado");
    }
}
