package models.Genre.dao.implementations.exceptions;

public class GenreNotFound extends Exception {
    public GenreNotFound() {
        super("Gênero não foi encontrado");
    }
}
