package models.Genre.dao.implementations;

import Services.DatabaseService;
import models.Genre.dao.GenreDAO;
import models.Genre.dao.implementations.exceptions.GenreNotFound;
import models.Genre.entities.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenreDAOJDBC implements GenreDAO {
    private final DatabaseService db = DatabaseService.getInstance();
    private final Connection connection = db.getConnection();

    @Override
    public List<Genre> find() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Genre> genres = new ArrayList<>();

        try {
            st = connection.prepareStatement("SELECT * FROM Genre");
            rs = st.executeQuery();

            while (rs.next()) {
                Genre genre = new Genre(
                        rs.getString("id"),
                        rs.getString("name")
                );
                genres.add(genre);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar gêneros", e);
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return genres;
    }

    @Override
    public Genre findById(String id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement("SELECT * FROM Genre WHERE id = ?");
            st.setString(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return new Genre(
                        rs.getString("id"),
                        rs.getString("name")
                );
            }

            throw new GenreNotFound();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar gênero por id", e);
        } catch (GenreNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return null;
    }

    @Override
    public Genre findByName(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement("SELECT * FROM Genre WHERE name = ?");
            st.setString(1, name);
            rs = st.executeQuery();

            if (rs.next()) {
                return new Genre(
                        rs.getString("id"),
                        rs.getString("name")
                );
            }

            throw new GenreNotFound();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar gênero por nome", e);
        } catch (GenreNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
            db.closeResultSet(rs);
        }

        return null;
    }

    @Override
    public void insert(Genre genre) {
        PreparedStatement st = null;
        ResultSet generatedKeys = null;

        try {
            st = connection.prepareStatement(
                "INSERT INTO Genre (name) VALUES (?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            st.setString(1, genre.getName());
            st.executeUpdate();

            generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                genre.setId(generatedKeys.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir gênero", e);
        } finally {
            db.closeResultSet(generatedKeys);
            db.closeStatement(st);
        }
    }

    @Override
    public void update(Genre genre) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                "UPDATE Genre SET name = ? WHERE id = ?"
            );
            st.setString(1, genre.getName());
            st.setString(2, genre.getId());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new GenreNotFound();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar gênero", e);
        } catch (GenreNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
        }
    }

    @Override
    public void delete(double id) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement("DELETE FROM Genre WHERE id = ?");
            st.setDouble(1, id);

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new GenreNotFound();
            }

            System.out.println("Gênero deletado com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar gênero", e);
        } catch (GenreNotFound e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeStatement(st);
        }
    }
}
