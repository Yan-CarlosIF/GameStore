package models.Game.dao.implementations;

import Services.DatabaseService;
import models.Game.dao.GameDAO;
import models.Game.dao.implementations.exceptions.GameNotFound;
import models.Game.entities.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class GameDAOJDBC implements GameDAO {
    private final DatabaseService db = DatabaseService.getInstance();
    private final Connection connection = db.getConnection();

    @Override
    public List<Game> find() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Game> games = new ArrayList<>();

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock, " +
                "g.developer, g.platform, g.release_date " +
                "FROM Product p " +
                "INNER JOIN Game g ON p.id = g.product_id"
            );
            rs = st.executeQuery();

            while (rs.next()) {
                Game game = new Game(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock"),
                        rs.getString("developer"),
                        rs.getString("platform"),
                        rs.getDate("release_date")
                );
                games.add(game);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar jogos", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return games;
    }

    @Override
    public Game findById(String id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock, " +
                "g.developer, g.platform, g.release_date " +
                "FROM Product p " +
                "INNER JOIN Game g ON p.id = g.product_id " +
                "WHERE p.id = ?"
            );
            st.setString(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return new Game(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock"),
                        rs.getString("developer"),
                        rs.getString("platform"),
                        rs.getDate("release_date")
                );
            }

            throw new GameNotFound();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar jogo por id", e);
        } catch (GameNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return null;
    }

    @Override
    public List<Game> findByDeveloper(String developer) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Game> games = new ArrayList<>();

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock, " +
                "g.developer, g.platform, g.release_date " +
                "FROM Product p " +
                "INNER JOIN Game g ON p.id = g.product_id " +
                "WHERE g.developer LIKE ?"
            );
            st.setString(1, "%" + developer + "%");
            rs = st.executeQuery();

            while (rs.next()) {
                games.add(new Game(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock"),
                        rs.getString("developer"),
                        rs.getString("platform"),
                        rs.getDate("release_date")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar jogos por desenvolvedor", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return games;
    }

    @Override
    public List<Game> findByPlatform(String platform) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Game> games = new ArrayList<>();

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock, " +
                "g.developer, g.platform, g.release_date " +
                "FROM Product p " +
                "INNER JOIN Game g ON p.id = g.product_id " +
                "WHERE g.platform LIKE ?"
            );
            st.setString(1, "%" + platform + "%");
            rs = st.executeQuery();

            while (rs.next()) {
                games.add(new Game(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("price"),
                        rs.getString("description"),
                        rs.getInt("stock"),
                        rs.getString("developer"),
                        rs.getString("platform"),
                        rs.getDate("release_date")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar jogos por plataforma", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return games;
    }

    @Override
    public void insert(Game game) {
        PreparedStatement stProduct = null;
        PreparedStatement stGame = null;
        ResultSet generatedKeys = null;

        try {
            connection.setAutoCommit(false);

            // Inserir na tabela Product primeiro
            stProduct = connection.prepareStatement(
                "INSERT INTO Product (name, price, description, stock) VALUES (?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            stProduct.setString(1, game.getName());
            stProduct.setBigDecimal(2, game.getPrice() == null ? BigDecimal.ZERO : game.getPrice());
            stProduct.setString(3, game.getDescription());
            stProduct.setInt(4, game.getStock());
            stProduct.executeUpdate();

            generatedKeys = stProduct.getGeneratedKeys();
            if (generatedKeys.next()) {
                String productId = generatedKeys.getString(1);
                game.setId(productId);

                // Inserir na tabela Game
                stGame = connection.prepareStatement(
                    "INSERT INTO Game (product_id, developer, platform, release_date) VALUES (?, ?, ?, ?)"
                );
                stGame.setString(1, productId);
                stGame.setString(2, game.getDeveloper());
                stGame.setString(3, game.getPlatform());
                java.sql.Date sqlDate = game.getReleaseDate() == null ? 
                    new java.sql.Date(new java.util.Date().getTime()) : 
                    new java.sql.Date(game.getReleaseDate().getTime());
                stGame.setDate(4, sqlDate);
                stGame.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback", ex);
            }
            throw new RuntimeException("Erro ao inserir jogo", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            db.closeResultSet(generatedKeys);
            db.closeStatement(stProduct);
            db.closeStatement(stGame);
        }
    }

    @Override
    public void update(Game game) {
        PreparedStatement stProduct = null;
        PreparedStatement stGame = null;

        try {
            connection.setAutoCommit(false);

            // Atualizar tabela Product
            stProduct = connection.prepareStatement(
                "UPDATE Product SET name = ?, price = ?, description = ?, stock = ? WHERE id = ?"
            );
            stProduct.setString(1, game.getName());
            stProduct.setBigDecimal(2, game.getPrice() == null ? BigDecimal.ZERO : game.getPrice());
            stProduct.setString(3, game.getDescription());
            stProduct.setInt(4, game.getStock());
            stProduct.setString(5, game.getId());

            int rows = stProduct.executeUpdate();
            if (rows == 0) {
                throw new GameNotFound();
            }

            // Atualizar tabela Game
            stGame = connection.prepareStatement(
                "UPDATE Game SET developer = ?, platform = ?, release_date = ? WHERE product_id = ?"
            );
            stGame.setString(1, game.getDeveloper());
            stGame.setString(2, game.getPlatform());
            stGame.setDate(3, new java.sql.Date(game.getReleaseDate().getTime()));
            stGame.setString(4, game.getId());
            stGame.executeUpdate();

            connection.commit();
        } catch (SQLException | GameNotFound e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao fazer rollback", ex);
            }
            throw new RuntimeException("Erro ao atualizar jogo", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            db.closeStatement(stProduct);
            db.closeStatement(stGame);
        }
    }

    @Override
    public void delete(String id) {
        PreparedStatement st = null;

        try {
            // Deletar da tabela Product (CASCADE irá deletar da tabela Game)
            st = connection.prepareStatement("DELETE FROM Product WHERE id = ?");
            st.setString(1, id);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new GameNotFound();
            }
        } catch (SQLException | GameNotFound e) {
            throw new RuntimeException("Erro ao deletar jogo", e);
        } finally {
            db.closeStatement(st);
        }
    }
}
