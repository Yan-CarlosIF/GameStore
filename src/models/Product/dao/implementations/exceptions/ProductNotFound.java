package models.Product.dao.implementations.exceptions;

public class ProductNotFound extends Exception {
    public ProductNotFound() {
        super("Produto não foi encontrado");
    }
}