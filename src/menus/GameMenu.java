package menus;

import models.Game.dao.GameDAO;
import models.Game.entities.Game;
import models.GameGenre.dao.GameGenreDAO;
import models.GameGenre.entities.GameGenre;
import models.Genre.entities.Genre;
import Services.DaoFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GameMenu {
    private static final GameDAO gameDAO = DaoFactory.getGameDAO();
    private static final GameGenreDAO gameGenreDAO = DaoFactory.getGameGenreDAO();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static void show(Scanner sc) {
        int op;

        do {
            System.out.println("\n=== MENU JOGOS ===");
            System.out.println("0 - Voltar");
            System.out.println("1 - Listar todos os jogos");
            System.out.println("2 - Buscar jogo por ID");
            System.out.println("3 - Buscar jogos por desenvolvedor");
            System.out.println("4 - Buscar jogos por plataforma");
            System.out.println("5 - Ver gêneros de um jogo");
            System.out.println("6 - Inserir jogo");
            System.out.println("7 - Atualizar jogo");
            System.out.println("8 - Deletar jogo");
            System.out.println("9 - Adicionar gênero a um jogo");
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
                    findByDeveloper(sc);
                    break;
                case 4:
                    findByPlatform(sc);
                    break;
                case 5:
                    showGameGenres(sc);
                    break;
                case 6:
                    insert(sc);
                    break;
                case 7:
                    update(sc);
                    break;
                case 8:
                    delete(sc);
                    break;
                case 9:
                    addGenreToGame(sc);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void listAll() {
        System.out.println("\n--- Lista de Jogos ---");
        var games = gameDAO.find();
        if (games.isEmpty()) {
            System.out.println("Nenhum jogo encontrado.");
        } else {
            games.forEach(System.out::println);
        }
    }

    private static void findById(Scanner sc) {
        System.out.print("Informe o ID: ");
        String id = sc.nextLine();
        Game game = gameDAO.findById(id);
        if (game != null) {
            System.out.println(game);
        }
    }

    private static void findByDeveloper(Scanner sc) {
        System.out.print("Informe o desenvolvedor: ");
        String developer = sc.nextLine();
        var games = gameDAO.findByDeveloper(developer);
        if (games.isEmpty()) {
            System.out.println("Nenhum jogo encontrado para este desenvolvedor.");
        } else {
            games.forEach(System.out::println);
        }
    }

    private static void findByPlatform(Scanner sc) {
        System.out.print("Informe a plataforma: ");
        String platform = sc.nextLine();
        var games = gameDAO.findByPlatform(platform);
        if (games.isEmpty()) {
            System.out.println("Nenhum jogo encontrado para esta plataforma.");
        } else {
            games.forEach(System.out::println);
        }
    }

    private static void showGameGenres(Scanner sc) {
        System.out.print("Informe o ID do jogo: ");
        String gameId = sc.nextLine();
        List<Genre> genres = gameGenreDAO.findGenresByGameId(gameId);
        if (genres.isEmpty()) {
            System.out.println("Nenhum gênero encontrado para este jogo.");
        } else {
            System.out.println("Gêneros do jogo:");
            genres.forEach(g -> System.out.println("  - " + g.getName()));
        }
    }

    private static void insert(Scanner sc) {
        System.out.println("\n--- Inserir Jogo ---");
        System.out.print("Nome: ");
        String name = sc.nextLine();
        System.out.print("Preço: ");
        BigDecimal price = sc.nextBigDecimal();
        sc.nextLine();
        System.out.print("Descrição: ");
        String description = sc.nextLine();
        System.out.print("Estoque: ");
        int stock = sc.nextInt();
        sc.nextLine();
        System.out.print("Desenvolvedor: ");
        String developer = sc.nextLine();
        System.out.print("Plataforma: ");
        String platform = sc.nextLine();
        System.out.print("Data de lançamento (dd/MM/yyyy): ");
        String dateStr = sc.nextLine();

        try {
            Date releaseDate = sdf.parse(dateStr);
            Game game = new Game(name, price, description, stock, developer, platform, releaseDate);
            gameDAO.insert(game);
            System.out.println("Jogo inserido com sucesso! ID: " + game.getId());
        } catch (ParseException e) {
            System.out.println("Erro: Formato de data inválido!");
        }
    }

    private static void update(Scanner sc) {
        System.out.println("\n--- Atualizar Jogo ---");
        System.out.print("Informe o ID do jogo a atualizar: ");
        String id = sc.nextLine();

        Game existingGame = gameDAO.findById(id);
        if (existingGame == null) {
            return;
        }

        System.out.println("Jogo encontrado: " + existingGame);
        System.out.print("Novo nome (Enter para manter): ");
        String name = sc.nextLine();
        System.out.print("Novo preço (0 para manter): ");
        BigDecimal price = sc.nextBigDecimal();
        sc.nextLine();
        System.out.print("Nova descrição (Enter para manter): ");
        String description = sc.nextLine();
        System.out.print("Novo estoque (-1 para manter): ");
        int stock = sc.nextInt();
        sc.nextLine();
        System.out.print("Novo desenvolvedor (Enter para manter): ");
        String developer = sc.nextLine();
        System.out.print("Nova plataforma (Enter para manter): ");
        String platform = sc.nextLine();
        System.out.print("Nova data de lançamento (dd/MM/yyyy, Enter para manter): ");
        String dateStr = sc.nextLine();

        Date releaseDate = existingGame.getReleaseDate();
        if (!dateStr.isEmpty()) {
            try {
                releaseDate = sdf.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido, mantendo data anterior.");
            }
        }

        Game updatedGame = new Game(
                id,
                name.isEmpty() ? existingGame.getName() : name,
                price.compareTo(BigDecimal.ZERO) == 0 ? existingGame.getPrice() : price,
                description.isEmpty() ? existingGame.getDescription() : description,
                stock == -1 ? existingGame.getStock() : stock,
                developer.isEmpty() ? existingGame.getDeveloper() : developer,
                platform.isEmpty() ? existingGame.getPlatform() : platform,
                releaseDate
        );

        gameDAO.update(updatedGame);
        System.out.println("Jogo atualizado com sucesso!");
    }

    private static void delete(Scanner sc) {
        System.out.print("Informe o ID do jogo a deletar: ");
        String id = sc.nextLine();
        gameDAO.delete(id);
        System.out.println("Jogo deletado com sucesso!");
    }

    private static void addGenreToGame(Scanner sc) {
        System.out.print("Informe o ID do jogo: ");
        String gameId = sc.nextLine();
        System.out.print("Informe o ID do gênero: ");
        String genreId = sc.nextLine();

        GameGenre gameGenre = new GameGenre(genreId, gameId);
        gameGenreDAO.insert(gameGenre);
        System.out.println("Gênero adicionado ao jogo com sucesso!");
    }
}
