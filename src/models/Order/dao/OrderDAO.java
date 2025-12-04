package models.Order.dao;

import models.Order.entitites.Order;

import java.util.List;

public interface OrderDAO {
    List<Order> find();
    Order findById(String id);
    List<Order> findByClientCpf(String cpf);
    void insert(Order order);
    void update(Order order);
    void delete(String id);
}
