package models.Genre.dao;

import models.Genre.entities.Genre;

import java.util.List;

public interface GenreDAO {
    List<Genre> find();
    Genre findById(String id);
    Genre findByName(String name);
    void insert(Genre genre);
    void update(Genre genre);
    void delete(String id);
}
