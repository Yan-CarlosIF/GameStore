package models.OrderProduct.dao.implementations.exceptions;

public class OrderProductNotFound extends Exception {
    public OrderProductNotFound() {
        super("Item do pedido não foi encontrado");
    }
}
