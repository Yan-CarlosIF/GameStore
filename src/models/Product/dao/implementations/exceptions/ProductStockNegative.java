package models.Product.dao.implementations.exceptions;

public class ProductStockNegative extends RuntimeException {
    public ProductStockNegative() {
        super("Erro: Estoque não pode ser negativo!");
    }
    
    public ProductStockNegative(String message) {
        super(message);
    }
}
