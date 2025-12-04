package models.OrderProduct.dao.implementations;

import Services.DatabaseService;
import models.OrderProduct.dao.OrderProductDAO;
import models.OrderProduct.dao.implementations.exceptions.OrderProductNotFound;
import models.OrderProduct.entities.OrderProduct;
import models.Product.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class OrderProductDAOJDBC implements OrderProductDAO {
    private final DatabaseService db = DatabaseService.getInstance();
    private final Connection connection = db.getConnection();

    @Override
    public List<OrderProduct> find() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<OrderProduct> orderProducts = new ArrayList<>();

        try {
            st = connection.prepareStatement("SELECT * FROM OrderProduct");
            rs = st.executeQuery();

            while (rs.next()) {
                OrderProduct orderProduct = new OrderProduct(
                        rs.getString("id"),
                        rs.getInt("amount"),
                        rs.getBigDecimal("items_total"),
                        rs.getString("order_id"),
                        rs.getString("product_id")
                );
                orderProducts.add(orderProduct);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens de pedidos", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return orderProducts;
    }

    @Override
    public OrderProduct findById(String id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement("SELECT * FROM OrderProduct WHERE id = ?");
            st.setString(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return new OrderProduct(
                        rs.getString("id"),
                        rs.getInt("amount"),
                        rs.getBigDecimal("items_total"),
                        rs.getString("order_id"),
                        rs.getString("product_id")
                );
            }

            throw new OrderProductNotFound();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item de pedido por id", e);
        } catch (OrderProductNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return null;
    }

    @Override
    public List<OrderProduct> findByOrderId(String orderId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<OrderProduct> orderProducts = new ArrayList<>();

        try {
            st = connection.prepareStatement("SELECT * FROM OrderProduct WHERE order_id = ?");
            st.setString(1, orderId);
            rs = st.executeQuery();

            while (rs.next()) {
                orderProducts.add(new OrderProduct(
                        rs.getString("id"),
                        rs.getInt("amount"),
                        rs.getBigDecimal("items_total"),
                        rs.getString("order_id"),
                        rs.getString("product_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens do pedido", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return orderProducts;
    }

    @Override
    public List<OrderProduct> findByProductId(String productId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<OrderProduct> orderProducts = new ArrayList<>();

        try {
            st = connection.prepareStatement("SELECT * FROM OrderProduct WHERE product_id = ?");
            st.setString(1, productId);
            rs = st.executeQuery();

            while (rs.next()) {
                orderProducts.add(new OrderProduct(
                        rs.getString("id"),
                        rs.getInt("amount"),
                        rs.getBigDecimal("items_total"),
                        rs.getString("order_id"),
                        rs.getString("product_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedidos do produto", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return orderProducts;
    }

    @Override
    public List<Product> findProductsByOrderId(String orderId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock " +
                "FROM Product p " +
                "INNER JOIN OrderProduct op ON p.id = op.product_id " +
                "WHERE op.order_id = ?"
            );
            st.setString(1, orderId);
            rs = st.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos do pedido", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return products;
    }

    @Override
    public void insert(OrderProduct orderProduct) {
        PreparedStatement st = null;
        ResultSet generatedKeys = null;

        try {
            st = connection.prepareStatement(
                "INSERT INTO OrderProduct (amount, items_total, order_id, product_id) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            st.setInt(1, orderProduct.getAmount());
            st.setBigDecimal(2, orderProduct.getItemsTotal() == null ? BigDecimal.ZERO : orderProduct.getItemsTotal());
            st.setString(3, orderProduct.getOrderId());
            st.setString(4, orderProduct.getProductId());
            st.executeUpdate();

            generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderProduct.setId(generatedKeys.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir item de pedido", e);
        } finally {
            db.closeResultSet(generatedKeys);
            db.closeStatement(st);
        }
    }

    @Override
    public void update(OrderProduct orderProduct) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                "UPDATE OrderProduct SET amount = ?, items_total = ?, order_id = ?, product_id = ? WHERE id = ?"
            );
            st.setInt(1, orderProduct.getAmount());
            st.setBigDecimal(2, orderProduct.getItemsTotal() == null ? BigDecimal.ZERO : orderProduct.getItemsTotal());
            st.setString(3, orderProduct.getOrderId());
            st.setString(4, orderProduct.getProductId());
            st.setString(5, orderProduct.getId());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new OrderProductNotFound();
            }
        } catch (SQLException | OrderProductNotFound e) {
            throw new RuntimeException("Erro ao atualizar item de pedido", e);
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void delete(String id) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement("DELETE FROM OrderProduct WHERE id = ?");
            st.setString(1, id);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new OrderProductNotFound();
            }
        } catch (SQLException | OrderProductNotFound e) {
            throw new RuntimeException("Erro ao deletar item de pedido", e);
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void deleteAllByOrderId(String orderId) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement("DELETE FROM OrderProduct WHERE order_id = ?");
            st.setString(1, orderId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar itens do pedido", e);
        } finally {
            db.closeStatement(st);
        }
    }
}
