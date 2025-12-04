package models.OrderProduct.dao;

import models.OrderProduct.entities.OrderProduct;
import models.Product.entities.Product;

import java.util.List;

public interface OrderProductDAO {
    List<OrderProduct> find();
    OrderProduct findById(String id);
    List<OrderProduct> findByOrderId(String orderId);
    List<OrderProduct> findByProductId(String productId);
    List<Product> findProductsByOrderId(String orderId);
    void insert(OrderProduct orderProduct);
    void update(OrderProduct orderProduct);
    void delete(String id);
    void deleteAllByOrderId(String orderId);
}
