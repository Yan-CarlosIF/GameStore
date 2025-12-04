package menus;

import models.Client.dao.ClientDAO;
import models.Client.entities.Client;
import Services.DaoFactory;

import java.util.Scanner;

public class ClientMenu {
    private static final ClientDAO clientDAO = DaoFactory.getClientDAO();

    public static void show(Scanner sc) {
        int op;

        do {
            System.out.println("\n=== MENU CLIENTES ===");
            System.out.println("0 - Voltar");
            System.out.println("1 - Listar todos os clientes");
            System.out.println("2 - Buscar cliente por CPF");
            System.out.println("3 - Inserir cliente");
            System.out.println("4 - Atualizar cliente");
            System.out.println("5 - Deletar cliente");
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
                    findByCpf(sc);
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
        System.out.println("\n--- Lista de Clientes ---");
        var clients = clientDAO.find();
        if (clients.isEmpty()) {
            System.out.println("Nenhum cliente encontrado.");
        } else {
            clients.forEach(System.out::println);
        }
    }

    private static void findByCpf(Scanner sc) {
        System.out.println("\n--- Procurar Cliente pelo CPF ---");
        System.out.print("Informe o CPF: ");
        String cpf = sc.nextLine();
        Client client = clientDAO.findByCPF(cpf);
        if (client != null) {
            System.out.println(client);
        }
    }

    private static void insert(Scanner sc) {
        System.out.println("\n--- Inserir Cliente ---");
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Nome: ");
        String name = sc.nextLine();
        System.out.print("Telefone: ");
        String phone = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Rua: ");
        String street = sc.nextLine();
        System.out.print("Cidade: ");
        String city = sc.nextLine();
        System.out.print("Número da casa: ");
        int houseNumber = sc.nextInt();
        sc.nextLine();

        Client client = new Client(cpf, phone, name, email, street, city, houseNumber);
        clientDAO.insert(client);
        System.out.println("Cliente inserido com sucesso!");
    }

    private static void update(Scanner sc) {
        System.out.println("\n--- Atualizar Cliente ---");
        System.out.print("Informe o CPF do cliente a atualizar: ");
        String cpf = sc.nextLine();

        Client existingClient = clientDAO.findByCPF(cpf);
        if (existingClient == null) {
            return;
        }

        System.out.println("Cliente encontrado: " + existingClient);
        System.out.print("Novo nome (Enter para manter): ");
        String name = sc.nextLine();
        System.out.print("Novo telefone (Enter para manter): ");
        String phone = sc.nextLine();
        System.out.print("Novo email (Enter para manter): ");
        String email = sc.nextLine();
        System.out.print("Nova rua (Enter para manter): ");
        String street = sc.nextLine();
        System.out.print("Nova cidade (Enter para manter): ");
        String city = sc.nextLine();
        System.out.print("Novo número da casa (0 para manter): ");
        int houseNumber = sc.nextInt();
        sc.nextLine();

        Client updatedClient = new Client(
                cpf,
                phone.isEmpty() ? existingClient.getPhone() : phone,
                name.isEmpty() ? existingClient.getName() : name,
                email.isEmpty() ? existingClient.getEmail() : email,
                street.isEmpty() ? existingClient.getStreet() : street,
                city.isEmpty() ? existingClient.getCity() : city,
                houseNumber == 0 ? existingClient.getHouseNumber() : houseNumber
        );

        clientDAO.update(updatedClient);
        System.out.println("Cliente atualizado com sucesso!");
    }

    private static void delete(Scanner sc) {
        System.out.println("\n--- Deletar Cliente ---");
        System.out.print("Informe o CPF do cliente a deletar: ");
        String cpf = sc.nextLine();
        clientDAO.delete(cpf);
        System.out.println("Cliente deletado com sucesso!");
    }
}
