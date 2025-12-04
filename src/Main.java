import menus.*;

import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int op;

        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║       E-COMMERCE GAMESTORE           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  0 - Sair                            ║");
            System.out.println("║  1 - Gerenciar Clientes              ║");
            System.out.println("║  2 - Gerenciar Produtos              ║");
            System.out.println("║  3 - Gerenciar Jogos                 ║");
            System.out.println("║  4 - Gerenciar Itens Eletrônicos     ║");
            System.out.println("║  5 - Gerenciar Gêneros               ║");
            System.out.println("║  6 - Gerenciar Pedidos               ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Escolha uma opção: ");
            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 0:
                    System.out.println("Programa finalizado. Até logo!");
                    break;
                case 1:
                    ClientMenu.show(sc);
                    break;
                case 2:
                    ProductMenu.show(sc);
                    break;
                case 3:
                    GameMenu.show(sc);
                    break;
                case 4:
                    EletronicItemMenu.show(sc);
                    break;
                case 5:
                    GenreMenu.show(sc);
                    break;
                case 6:
                    OrderMenu.show(sc);
                    break;
                default:
                    System.out.println("\nOpção inválida!");
            }
        } while (op != 0);

        sc.close();
    }
}
