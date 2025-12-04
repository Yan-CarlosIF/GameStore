package menus;

import models.Product.dao.ProductDAO;
import models.Product.entities.Product;
import Services.DaoFactory;

import java.math.BigDecimal;
import java.util.Scanner;

public class ProductMenu {
    private static final ProductDAO productDAO = DaoFactory.getProductDAO();

    public static void show(Scanner sc) {
        int op;

        do {
            System.out.println("\n=== MENU PRODUTOS ===");
            System.out.println("0 - Voltar");
            System.out.println("1 - Listar todos os produtos");
            System.out.println("2 - Buscar produto por ID");
            System.out.println("3 - Inserir produto");
            System.out.println("4 - Atualizar produto");
            System.out.println("5 - Deletar produto");
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
                    insert(sc);
                    break;
                case 4:
                    update(sc);
                    break;
                case 5:
                    delete(sc);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void listAll() {
        System.out.println("\n--- Lista de Produtos ---");
        var products = productDAO.find();
        if (products.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
        } else {
            products.forEach(System.out::println);
        }
    }

    private static void findById(Scanner sc) {
        System.out.print("Informe o ID: ");
        String id = sc.nextLine();
        Product product = productDAO.findById(id);
        if (product != null) {
            System.out.println(product);
        }
    }

    private static void insert(Scanner sc) {
        System.out.println("\n--- Inserir Produto ---");
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

        Product product = new Product(name, price, description, stock);
        productDAO.insert(product);
        System.out.println("Produto inserido com sucesso!");
    }

    private static void update(Scanner sc) {
        System.out.println("\n--- Atualizar Produto ---");
        System.out.print("Informe o ID do produto a atualizar: ");
        String id = sc.nextLine();

        Product existingProduct = productDAO.findById(id);
        if (existingProduct == null) {
            return;
        }

        System.out.println("Produto encontrado: " + existingProduct);
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

        Product updatedProduct = new Product(
                id,
                name.isEmpty() ? existingProduct.getName() : name,
                price.compareTo(BigDecimal.ZERO) == 0 ? existingProduct.getPrice() : price,
                description.isEmpty() ? existingProduct.getDescription() : description,
                stock == -1 ? existingProduct.getStock() : stock
        );

        productDAO.update(updatedProduct);
        System.out.println("Produto atualizado com sucesso!");
    }

    private static void delete(Scanner sc) {
        System.out.print("Informe o ID do produto a deletar: ");
        String id = sc.nextLine();
        productDAO.delete(id);
        System.out.println("Produto deletado com sucesso!");
    }
}
