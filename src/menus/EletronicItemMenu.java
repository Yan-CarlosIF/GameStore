package menus;

import models.EletronicItem.dao.EletronicItemDAO;
import models.EletronicItem.entities.EletronicItem;
import Services.DaoFactory;

import java.math.BigDecimal;
import java.util.Scanner;

public class EletronicItemMenu {
    private static final EletronicItemDAO eletronicItemDAO = DaoFactory.getEletronicItemDAO();

    public static void show(Scanner sc) {
        int op;

        do {
            System.out.println("\n=== MENU ITENS ELETRÔNICOS ===");
            System.out.println("0 - Voltar");
            System.out.println("1 - Listar todos os itens eletrônicos");
            System.out.println("2 - Buscar item por ID");
            System.out.println("3 - Buscar itens por produtor");
            System.out.println("4 - Buscar itens por tipo");
            System.out.println("5 - Inserir item eletrônico");
            System.out.println("6 - Atualizar item eletrônico");
            System.out.println("7 - Deletar item eletrônico");
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
                    findByProducer(sc);
                    break;
                case 4:
                    findByType(sc);
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
        System.out.println("\n--- Lista de Itens Eletrônicos ---");
        var items = eletronicItemDAO.find();
        if (items.isEmpty()) {
            System.out.println("Nenhum item eletrônico encontrado.");
        } else {
            items.forEach(System.out::println);
        }
    }

    private static void findById(Scanner sc) {
        System.out.print("Informe o ID: ");
        String id = sc.nextLine();
        EletronicItem item = eletronicItemDAO.findById(id);
        if (item != null) {
            System.out.println(item);
        }
    }

    private static void findByProducer(Scanner sc) {
        System.out.print("Informe o produtor: ");
        String producer = sc.nextLine();
        var items = eletronicItemDAO.findByProducer(producer);
        if (items.isEmpty()) {
            System.out.println("Nenhum item encontrado para este produtor.");
        } else {
            items.forEach(System.out::println);
        }
    }

    private static void findByType(Scanner sc) {
        System.out.print("Informe o tipo: ");
        String type = sc.nextLine();
        var items = eletronicItemDAO.findByType(type);
        if (items.isEmpty()) {
            System.out.println("Nenhum item encontrado para este tipo.");
        } else {
            items.forEach(System.out::println);
        }
    }

    private static void insert(Scanner sc) {
        System.out.println("\n--- Inserir Item Eletrônico ---");
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
        System.out.print("Produtor: ");
        String producer = sc.nextLine();
        System.out.print("Tipo: ");
        String type = sc.nextLine();

        EletronicItem item = new EletronicItem(name, price, description, stock, producer, type);
        eletronicItemDAO.insert(item);
        System.out.println("Item eletrônico inserido com sucesso! ID: " + item.getId());
    }

    private static void update(Scanner sc) {
        System.out.println("\n--- Atualizar Item Eletrônico ---");
        System.out.print("Informe o ID do item a atualizar: ");
        String id = sc.nextLine();

        EletronicItem existingItem = eletronicItemDAO.findById(id);
        if (existingItem == null) {
            return;
        }

        System.out.println("Item encontrado: " + existingItem);
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
        System.out.print("Novo produtor (Enter para manter): ");
        String producer = sc.nextLine();
        System.out.print("Novo tipo (Enter para manter): ");
        String type = sc.nextLine();

        EletronicItem updatedItem = new EletronicItem(
                id,
                name.isEmpty() ? existingItem.getName() : name,
                price.compareTo(BigDecimal.ZERO) == 0 ? existingItem.getPrice() : price,
                description.isEmpty() ? existingItem.getDescription() : description,
                stock == -1 ? existingItem.getStock() : stock,
                producer.isEmpty() ? existingItem.getProducer() : producer,
                type.isEmpty() ? existingItem.getType() : type
        );

        eletronicItemDAO.update(updatedItem);
        System.out.println("Item eletrônico atualizado com sucesso!");
    }

    private static void delete(Scanner sc) {
        System.out.print("Informe o ID do item a deletar: ");
        String id = sc.nextLine();
        eletronicItemDAO.delete(id);
        System.out.println("Item eletrônico deletado com sucesso!");
    }
}
