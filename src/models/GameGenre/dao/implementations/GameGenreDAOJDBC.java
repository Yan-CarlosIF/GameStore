package models.GameGenre.dao.implementations;

import Services.DatabaseService;
import models.GameGenre.dao.GameGenreDAO;
import models.GameGenre.dao.implementations.exceptions.GameGenreNotFound;
import models.GameGenre.entities.GameGenre;
import models.Genre.entities.Genre;
import models.Game.entities.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameGenreDAOJDBC implements GameGenreDAO {
    private final DatabaseService db = DatabaseService.getInstance();
    private final Connection connection = db.getConnection();

    @Override
    public List<GameGenre> find() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<GameGenre> gameGenres = new ArrayList<>();

        try {
            st = connection.prepareStatement("SELECT * FROM GameGenre");
            rs = st.executeQuery();

            while (rs.next()) {
                GameGenre gameGenre = new GameGenre(
                        rs.getString("genre_id"),
                        rs.getString("product_id")
                );
                gameGenres.add(gameGenre);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar relações jogo-gênero", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return gameGenres;
    }

    @Override
    public List<Genre> findGenresByGameId(String gameId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Genre> genres = new ArrayList<>();

        try {
            st = connection.prepareStatement(
                "SELECT g.id, g.name " +
                "FROM Genre g " +
                "INNER JOIN GameGenre gg ON g.id = gg.genre_id " +
                "WHERE gg.product_id = ?"
            );
            st.setString(1, gameId);
            rs = st.executeQuery();

            while (rs.next()) {
                genres.add(new Genre(
                        rs.getString("id"),
                        rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar gêneros do jogo", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return genres;
    }

    @Override
    public List<Game> findGamesByGenreId(String genreId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Game> games = new ArrayList<>();

        try {
            st = connection.prepareStatement(
                "SELECT p.id, p.name, p.price, p.description, p.stock, " +
                "ga.developer, ga.platform, ga.release_date " +
                "FROM Product p " +
                "INNER JOIN Game ga ON p.id = ga.product_id " +
                "INNER JOIN GameGenre gg ON ga.product_id = gg.product_id " +
                "WHERE gg.genre_id = ?"
            );
            st.setString(1, genreId);
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
            throw new RuntimeException("Erro ao buscar jogos do gênero", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return games;
    }

    @Override
    public void insert(GameGenre gameGenre) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                "INSERT INTO GameGenre (genre_id, product_id) VALUES (?, ?)"
            );
            st.setString(1, gameGenre.getGenreId());
            st.setString(2, gameGenre.getProductId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir relação jogo-gênero", e);
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void delete(double genreId, double productId) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                "DELETE FROM GameGenre WHERE genre_id = ? AND product_id = ?"
            );
            st.setDouble(1, genreId);
            st.setDouble(2, productId);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new GameGenreNotFound();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar relação jogo-gênero", e);
        } catch (GameGenreNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void deleteAllByGameId(String gameId) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                "DELETE FROM GameGenre WHERE product_id = ?"
            );
            st.setString(1, gameId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar relações do jogo", e);
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void deleteAllByGenreId(String genreId) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                "DELETE FROM GameGenre WHERE genre_id = ?"
            );
            st.setString(1, genreId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar relações do gênero", e);
        } finally {
            db.closeStatement(st);
        }
    }
}
