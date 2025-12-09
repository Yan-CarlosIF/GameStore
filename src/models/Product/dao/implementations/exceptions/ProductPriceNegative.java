package models.Product.dao.implementations.exceptions;

public class ProductPriceNegative extends RuntimeException {
    public ProductPriceNegative() {
        super("Erro: Preço não pode ser negativo!");
    }
    
    public ProductPriceNegative(String message) {
        super(message);
    }
}
