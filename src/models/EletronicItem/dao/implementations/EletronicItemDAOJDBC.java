package models.EletronicItem.dao.implementations;

import Services.DatabaseService;
import models.EletronicItem.dao.EletronicItemDAO;
import models.EletronicItem.dao.implementations.exceptions.EletronicItemNotFound;
import models.EletronicItem.entities.EletronicItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class EletronicItemDAOJDBC implements EletronicItemDAO {
    private final DatabaseService db = DatabaseService.getInstance();
    private final Connection connection = db.getConnection();

    @Override
    public List<EletronicItem> find() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<EletronicItem> items = new ArrayList<>();

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock, " +
                "e.producer, e.type " +
                "FROM Product p " +
                "INNER JOIN EletronicItem e ON p.id = e.product_id"
            );
            rs = st.executeQuery();

            while (rs.next()) {
                EletronicItem item = new EletronicItem(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock"),
                        rs.getString("producer"),
                        rs.getString("type")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens eletrônicos", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return items;
    }

    @Override
    public EletronicItem findById(String id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock, " +
                "e.producer, e.type " +
                "FROM Product p " +
                "INNER JOIN EletronicItem e ON p.id = e.product_id " +
                "WHERE p.id = ?"
            );
            st.setString(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return new EletronicItem(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock"),
                        rs.getString("producer"),
                        rs.getString("type")
                );
            }

            throw new EletronicItemNotFound();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar item eletrônico por id", e);
        } catch (EletronicItemNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return null;
    }

    @Override
    public List<EletronicItem> findByProducer(String producer) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<EletronicItem> items = new ArrayList<>();

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock, " +
                "e.producer, e.type " +
                "FROM Product p " +
                "INNER JOIN EletronicItem e ON p.id = e.product_id " +
                "WHERE e.producer LIKE ?"
            );
            st.setString(1, "%" + producer + "%");
            rs = st.executeQuery();

            while (rs.next()) {
                items.add(new EletronicItem(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock"),
                        rs.getString("producer"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens eletrônicos por produtor", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return items;
    }

    @Override
    public List<EletronicItem> findByType(String type) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<EletronicItem> items = new ArrayList<>();

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock, " +
                "e.producer, e.type " +
                "FROM Product p " +
                "INNER JOIN EletronicItem e ON p.id = e.product_id " +
                "WHERE e.type LIKE ?"
            );
            st.setString(1, "%" + type + "%");
            rs = st.executeQuery();

            while (rs.next()) {
                items.add(new EletronicItem(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock"),
                        rs.getString("producer"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens eletrônicos por tipo", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return items;
    }

    @Override
    public void insert(EletronicItem eletronicItem) {
        PreparedStatement stProduct = null;
        PreparedStatement stEletronic = null;
        ResultSet generatedKeys = null;

        try {
            connection.setAutoCommit(false);

            // Inserir na tabela Product primeiro
            stProduct = connection.prepareStatement(
                "INSERT INTO Product (name, price, description, stock) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            stProduct.setString(1, eletronicItem.getName());
            stProduct.setBigDecimal(2, eletronicItem.getPrice() == null ? BigDecimal.ZERO : eletronicItem.getPrice());
            stProduct.setString(3, eletronicItem.getDescription());
            stProduct.setInt(4, eletronicItem.getStock());
            stProduct.executeUpdate();

            generatedKeys = stProduct.getGeneratedKeys();
            if (generatedKeys.next()) {
                String productId = generatedKeys.getString(1);
                eletronicItem.setId(productId);

                // Inserir na tabela EletronicItem
                stEletronic = connection.prepareStatement(
                    "INSERT INTO EletronicItem (product_id, producer, type) VALUES (?, ?, ?)"
                );
                stEletronic.setString(1, productId);
                stEletronic.setString(2, eletronicItem.getProducer());
                stEletronic.setString(3, eletronicItem.getType());
                stEletronic.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback", ex);
            }
            throw new RuntimeException("Erro ao inserir item eletrônico", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            db.closeResultSet(generatedKeys);
            db.closeStatement(stProduct);
            db.closeStatement(stEletronic);
        }
    }

    @Override
    public void update(EletronicItem eletronicItem) {
        PreparedStatement stProduct = null;
        PreparedStatement stEletronic = null;

        try {
            connection.setAutoCommit(false);

            // Atualizar tabela Product
            stProduct = connection.prepareStatement(
                "UPDATE Product SET name = ?, price = ?, description = ?, stock = ? WHERE id = ?"
            );
            stProduct.setString(1, eletronicItem.getName());
            stProduct.setBigDecimal(2, eletronicItem.getPrice() == null ? BigDecimal.ZERO : eletronicItem.getPrice());
            stProduct.setString(3, eletronicItem.getDescription());
            stProduct.setInt(4, eletronicItem.getStock());
            stProduct.setString(5, eletronicItem.getId());

            int rows = stProduct.executeUpdate();
            if (rows == 0) {
                throw new EletronicItemNotFound();
            }

            // Atualizar tabela EletronicItem
            stEletronic = connection.prepareStatement(
                "UPDATE EletronicItem SET producer = ?, type = ? WHERE product_id = ?"
            );
            stEletronic.setString(1, eletronicItem.getProducer());
            stEletronic.setString(2, eletronicItem.getType());
            stEletronic.setString(3, eletronicItem.getId());
            stEletronic.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback", ex);
            }
            throw new RuntimeException("Erro ao atualizar item eletrônico", e);
        } catch (EletronicItemNotFound e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback", ex);
            }
            System.out.println(e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            db.closeStatement(stProduct);
            db.closeStatement(stEletronic);
        }
    }

    @Override
    public void delete(double id) {
        PreparedStatement st = null;

        try {
            // Deletar da tabela Product (CASCADE irá deletar da tabela EletronicItem)
            st = connection.prepareStatement("DELETE FROM Product WHERE id = ?");
            st.setDouble(1, id);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new EletronicItemNotFound();
            }
            
            System.out.println("Item eletrônico deletado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar item eletrônico", e);
        } catch (EletronicItemNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
        }
    }
}
