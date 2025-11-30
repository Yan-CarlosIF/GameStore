import Client.dao.ClientDAO;
import Services.DaoFactory;
import Client.entities.Client;

import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ClientDAO clientDAO = DaoFactory.getClientDAO();

    public static void main(String[] args) {
        int op;

        do {
            System.out.println("E-Commerce GameStore");
            System.out.println("0 - Sair");
            System.out.println("1 - Listar todos os clientes");
            System.out.println("2 - Buscar Cliente pelo CPF");
            System.out.print("Insira: ");
            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 0:
                    System.out.println("Programa finalizado.");
                    break;
                case 1:
                    clientDAO.find().forEach(System.out::println);
                    break;
                case 2: {
                    System.out.print("Insira o CPF do cliente que deseja buscar: ");
                    String cpf = sc.nextLine();

                    Client client = clientDAO.findByCPF(cpf);

                    if (client != null) System.out.println(client);
                } break;
                default:
                    System.out.println("\nOpção inválida!");
            }
        } while (op != 0);
    }
}
