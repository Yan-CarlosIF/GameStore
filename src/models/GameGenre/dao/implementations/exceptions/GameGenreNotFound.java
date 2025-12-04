package models.GameGenre.dao.implementations.exceptions;

public class GameGenreNotFound extends Exception {
    public GameGenreNotFound() {
        super("Relação Jogo-Gênero não foi encontrada");
    }
}
