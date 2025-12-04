package models.Product.dao.implementations;

import Services.DatabaseService;
import models.Product.dao.ProductDAO;
import models.Product.dao.implementations.exceptions.ProductNotFound;
import models.Product.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductDAOJDBC implements ProductDAO {
    private final DatabaseService db = DatabaseService.getInstance();
    private final Connection connection = db.getConnection();

    @Override
    public List<Product> find() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            st = connection.prepareStatement("SELECT * FROM Product");
            rs = st.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return products;
    }

    @Override
    public Product findById(String id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement("SELECT * FROM Product WHERE id = ?");
            st.setString(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock")
                );
            }

            throw new ProductNotFound();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por id", e);
        } catch (ProductNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return null;
    }

    @Override
    public void insert(Product product) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                    "INSERT INTO Product (id, name, price, description, stock) VALUES (?, ?, ?, ?, ?)"
            );
            st.setString(1, product.getId());
            st.setString(2, product.getName());
            st.setBigDecimal(3, product.getPrice() == null ? BigDecimal.ZERO : product.getPrice());
            st.setString(4, product.getDescription());
            st.setInt(5, product.getStock());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto", e);
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void update(Product product) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(
                    "UPDATE Product SET name = ?, price = ?, description = ?, stock = ? WHERE id = ?"
            );
            st.setString(1, product.getName());
            st.setBigDecimal(2, product.getPrice() == null ? BigDecimal.ZERO : product.getPrice());
            st.setString(3, product.getDescription());
            st.setInt(4, product.getStock());
            st.setString(5, product.getId());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new ProductNotFound();
            }
        } catch (SQLException | ProductNotFound e) {
            throw new RuntimeException("Erro ao atualizar produto", e);
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void delete(String id) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("DELETE FROM Product WHERE id = ?");
            st.setString(1, id);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new ProductNotFound();
            }
        } catch (SQLException | ProductNotFound e) {
            throw new RuntimeException("Erro ao deletar produto", e);
        } finally {
            db.closeStatement(st);
        }
    }
}