package models.Order.dao.implementations;

import models.Order.dao.OrderDAO;
import models.Order.dao.implementations.exceptions.OrderNotFound;
import models.Order.entitites.Order;
import Services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

public class OrderDAOJDBC implements OrderDAO {
    private final DatabaseService db = DatabaseService.getInstance();
    private final Connection connection = db.getConnection();

    @Override
    public List<Order> find() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Order> orders = new ArrayList<>();
        try {
            st = connection.prepareStatement("SELECT * FROM `Order`");
            rs = st.executeQuery();
            while (rs.next()) {
                Order o = new Order(
                        rs.getString("id"),
                        rs.getBigDecimal("total_value"),
                        rs.getString("status"),
                        rs.getDate("data"),
                        rs.getString("client_cpf")
                );
                orders.add(o);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedidos", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }
        return orders;
    }

    @Override
    public Order findById(String id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT * FROM `Order` WHERE id = ?");
            st.setString(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getString("id"),
                        rs.getBigDecimal("total_value"),
                        rs.getString("status"),
                        rs.getDate("data"),
                        rs.getString("client_cpf")
                );
            }
            throw new OrderNotFound();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido por id", e);
        } catch (OrderNotFound e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }
    }

    @Override
    public List<Order> findByClientCpf(String cpf) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Order> orders = new ArrayList<>();
        try {
            st = connection.prepareStatement("SELECT * FROM `Order` WHERE client_cpf = ?");
            st.setString(1, cpf);
            rs = st.executeQuery();
            while (rs.next()) {
                orders.add(new Order(
                        rs.getString("id"),
                        rs.getBigDecimal("total_value"),
                        rs.getString("status"),
                        rs.getDate("data"),
                        rs.getString("client_cpf")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedidos por cliente", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }
        return orders;
    }

    @Override
    public void insert(Order order) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                    "INSERT INTO `Order` (id, total_value, status, data, client_cpf) VALUES (?, ?, ?, ?, ?)"
            );
            st.setString(1, order.getId());
            st.setBigDecimal(2, order.getTotalValue() == null ? BigDecimal.ZERO : order.getTotalValue());
            st.setString(3, order.getStatus() == null ? "pending" : order.getStatus());
            java.sql.Date sqlDate = order.getDate() == null ? new java.sql.Date(new Date().getTime()) : new java.sql.Date(order.getDate().getTime());
            st.setDate(4, sqlDate);
            st.setString(5, order.getClientCpf());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir pedido", e);
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void update(Order order) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                    "UPDATE `Order` SET total_value = ?, status = ?, data = ?, client_cpf = ? WHERE id = ?"
            );
            st.setBigDecimal(1, order.getTotalValue() == null ? BigDecimal.ZERO : order.getTotalValue());
            st.setString(2, order.getStatus());
            st.setDate(3, new java.sql.Date(order.getDate().getTime()));
            st.setString(4, order.getClientCpf());
            st.setString(5, order.getId());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new OrderNotFound();
            }
        } catch (SQLException | OrderNotFound e) {
            throw new RuntimeException("Erro ao atualizar pedido", e);
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void delete(String id) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("DELETE FROM `Order` WHERE id = ?");
            st.setString(1, id);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new OrderNotFound();
            }
        } catch (SQLException | OrderNotFound e) {
            throw new RuntimeException("Erro ao deletar pedido", e);
        } finally {
            db.closeStatement(st);
        }
    }
}
