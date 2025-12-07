package models.Game.dao;

import models.Game.entities.Game;

import java.util.List;

public interface GameDAO {
    List<Game> find();
    Game findById(String id);
    List<Game> findByDeveloper(String developer);
    List<Game> findByPlatform(String platform);
    void insert(Game game);
    void update(Game game);
    void delete(double id);
}
