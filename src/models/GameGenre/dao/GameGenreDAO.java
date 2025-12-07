package models.GameGenre.dao;

import models.GameGenre.entities.GameGenre;
import models.Genre.entities.Genre;
import models.Game.entities.Game;

import java.util.List;

public interface GameGenreDAO {
    List<GameGenre> find();
    List<Genre> findGenresByGameId(String gameId);
    List<Game> findGamesByGenreId(String genreId);
    void insert(GameGenre gameGenre);
    void delete(double genreId, double productId);
    void deleteAllByGameId(String gameId);
    void deleteAllByGenreId(String genreId);
}
