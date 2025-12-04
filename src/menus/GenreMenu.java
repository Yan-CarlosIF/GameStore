package menus;

import models.Genre.dao.GenreDAO;
import models.Genre.entities.Genre;
import models.GameGenre.dao.GameGenreDAO;
import models.Game.entities.Game;
import Services.DaoFactory;

import java.util.List;
import java.util.Scanner;

public class GenreMenu {
    private static final GenreDAO genreDAO = DaoFactory.getGenreDAO();
    private static final GameGenreDAO gameGenreDAO = DaoFactory.getGameGenreDAO();

    public static void show(Scanner sc) {
        int op;

        do {
            System.out.println("\n=== MENU GÊNEROS ===");
            System.out.println("0 - Voltar");
            System.out.println("1 - Listar todos os gêneros");
            System.out.println("2 - Buscar gênero por ID");
            System.out.println("3 - Buscar gênero por nome");
            System.out.println("4 - Ver jogos de um gênero");
            System.out.println("5 - Inserir gênero");
            System.out.println("6 - Atualizar gênero");
            System.out.println("7 - Deletar gênero");
            System.out.print("Escolha: ");
            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                case 1:
                    listAll();
                    break;
                case 2:
                    findById(sc);
                    break;
                case 3:
                    findByName(sc);
                    break;
                case 4:
                    showGamesByGenre(sc);
                    break;
                case 5:
                    insert(sc);
                    break;
                case 6:
                    update(sc);
                    break;
                case 7:
                    delete(sc);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void listAll() {
        System.out.println("\n--- Lista de Gêneros ---");
        var genres = genreDAO.find();
        if (genres.isEmpty()) {
            System.out.println("Nenhum gênero encontrado.");
        } else {
            genres.forEach(System.out::println);
        }
    }

    private static void findById(Scanner sc) {
        System.out.print("Informe o ID: ");
        String id = sc.nextLine();
        Genre genre = genreDAO.findById(id);
        if (genre != null) {
            System.out.println(genre);
        }
    }

    private static void findByName(Scanner sc) {
        System.out.print("Informe o nome: ");
        String name = sc.nextLine();
        Genre genre = genreDAO.findByName(name);
        if (genre != null) {
            System.out.println(genre);
        }
    }

    private static void showGamesByGenre(Scanner sc) {
        System.out.print("Informe o ID do gênero: ");
        String genreId = sc.nextLine();
        List<Game> games = gameGenreDAO.findGamesByGenreId(genreId);
        if (games.isEmpty()) {
            System.out.println("Nenhum jogo encontrado para este gênero.");
        } else {
            System.out.println("Jogos do gênero:");
            games.forEach(g -> System.out.println("  - " + g.getName() + " (" + g.getDeveloper() + ")"));
        }
    }

    private static void insert(Scanner sc) {
        System.out.println("\n--- Inserir Gênero ---");
        System.out.print("Nome: ");
        String name = sc.nextLine();

        Genre genre = new Genre(name);
        genreDAO.insert(genre);
        System.out.println("Gênero inserido com sucesso! ID: " + genre.getId());
    }

    private static void update(Scanner sc) {
        System.out.println("\n--- Atualizar Gênero ---");
        System.out.print("Informe o ID do gênero a atualizar: ");
        String id = sc.nextLine();

        Genre existingGenre = genreDAO.findById(id);
        if (existingGenre == null) {
            return;
        }

        System.out.println("Gênero encontrado: " + existingGenre);
        System.out.print("Novo nome: ");
        String name = sc.nextLine();

        if (!name.isEmpty()) {
            Genre updatedGenre = new Genre(id, name);
            genreDAO.update(updatedGenre);
            System.out.println("Gênero atualizado com sucesso!");
        } else {
            System.out.println("Nome não pode ser vazio. Operação cancelada.");
        }
    }

    private static void delete(Scanner sc) {
        System.out.print("Informe o ID do gênero a deletar: ");
        String id = sc.nextLine();
        genreDAO.delete(id);
        System.out.println("Gênero deletado com sucesso!");
    }
}
