package models.Game.dao.implementations.exceptions;

public class GameNotFound extends Exception {
    public GameNotFound() {
        super("Jogo não foi encontrado");
    }
}
