package models.Product.dao;

import models.Product.entities.Product;

import java.util.List;

public interface ProductDAO {
    List<Product> find();
    Product findById(String id);
    void insert(Product product);
    void update(Product product);
    void delete(double id);
}