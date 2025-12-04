package menus;

import models.Order.dao.OrderDAO;
import models.Order.entitites.Order;
import models.OrderProduct.dao.OrderProductDAO;
import models.OrderProduct.entities.OrderProduct;
import models.Product.entities.Product;
import Services.DaoFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OrderMenu {
    private static final OrderDAO orderDAO = DaoFactory.getOrderDAO();
    private static final OrderProductDAO orderProductDAO = DaoFactory.getOrderProductDAO();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static void show(Scanner sc) {
        int op;

        do {
            System.out.println("\n=== MENU PEDIDOS ===");
            System.out.println("0 - Voltar");
            System.out.println("1 - Listar todos os pedidos");
            System.out.println("2 - Buscar pedido por ID");
            System.out.println("3 - Buscar pedidos por CPF do cliente");
            System.out.println("4 - Ver produtos de um pedido");
            System.out.println("5 - Inserir pedido");
            System.out.println("6 - Adicionar produto ao pedido");
            System.out.println("7 - Atualizar pedido");
            System.out.println("8 - Deletar pedido");
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
                    findByClientCpf(sc);
                    break;
                case 4:
                    showOrderProducts(sc);
                    break;
                case 5:
                    insert(sc);
                    break;
                case 6:
                    addProductToOrder(sc);
                    break;
                case 7:
                    update(sc);
                    break;
                case 8:
                    delete(sc);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void listAll() {
        System.out.println("\n--- Lista de Pedidos ---");
        var orders = orderDAO.find();
        if (orders.isEmpty()) {
            System.out.println("Nenhum pedido encontrado.");
        } else {
            orders.forEach(System.out::println);
        }
    }

    private static void findById(Scanner sc) {
        System.out.print("Informe o ID: ");
        String id = sc.nextLine();
        Order order = orderDAO.findById(id);
        if (order != null) {
            System.out.println(order);
        }
    }

    private static void findByClientCpf(Scanner sc) {
        System.out.print("Informe o CPF do cliente: ");
        String cpf = sc.nextLine();
        var orders = orderDAO.findByClientCpf(cpf);
        if (orders.isEmpty()) {
            System.out.println("Nenhum pedido encontrado para este cliente.");
        } else {
            orders.forEach(System.out::println);
        }
    }

    private static void showOrderProducts(Scanner sc) {
        System.out.print("Informe o ID do pedido: ");
        String orderId = sc.nextLine();

        List<OrderProduct> orderProducts = orderProductDAO.findByOrderId(orderId);
        if (orderProducts.isEmpty()) {
            System.out.println("Nenhum produto encontrado neste pedido.");
        } else {
            System.out.println("Produtos do pedido:");
            List<Product> products = orderProductDAO.findProductsByOrderId(orderId);
            for (int i = 0; i < orderProducts.size(); i++) {
                OrderProduct op = orderProducts.get(i);
                Product p = products.get(i);
                System.out.printf("  - %s | Qtd: %d | Total: R$ %.2f%n",
                        p.getName(), op.getAmount(), op.getItemsTotal());
            }
        }
    }

    private static void insert(Scanner sc) {
        System.out.println("\n--- Inserir Pedido ---");
        System.out.print("CPF do cliente: ");
        String clientCpf = sc.nextLine();
        System.out.print("Valor total: ");
        BigDecimal totalValue = sc.nextBigDecimal();
        sc.nextLine();
        System.out.print("Status (pending, shipping, delivered, cancelled): ");
        String status = sc.nextLine();
        System.out.print("Data (dd/MM/yyyy, Enter para hoje): ");
        String dateStr = sc.nextLine();

        Date date = new Date();
        if (!dateStr.isEmpty()) {
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Formato inválido, usando data de hoje.");
            }
        }

        Order order = new Order(totalValue, status.isEmpty() ? "pending" : status, date, clientCpf);
        orderDAO.insert(order);
        System.out.println("Pedido inserido com sucesso! ID: " + order.getId());
    }

    private static void addProductToOrder(Scanner sc) {
        System.out.println("\n--- Adicionar Produto ao Pedido ---");
        System.out.print("ID do pedido: ");
        String orderId = sc.nextLine();
        System.out.print("ID do produto: ");
        String productId = sc.nextLine();
        System.out.print("Quantidade: ");
        int amount = sc.nextInt();
        sc.nextLine();
        System.out.print("Total do item (quantidade x preço unitário): ");
        BigDecimal itemsTotal = sc.nextBigDecimal();
        sc.nextLine();

        OrderProduct orderProduct = new OrderProduct(amount, itemsTotal, orderId, productId);
        orderProductDAO.insert(orderProduct);
        System.out.println("Produto adicionado ao pedido com sucesso!");
    }

    private static void update(Scanner sc) {
        System.out.println("\n--- Atualizar Pedido ---");
        System.out.print("Informe o ID do pedido a atualizar: ");
        String id = sc.nextLine();

        Order existingOrder = orderDAO.findById(id);
        if (existingOrder == null) {
            return;
        }

        System.out.println("Pedido encontrado: " + existingOrder);
        System.out.print("Novo valor total (0 para manter): ");
        BigDecimal totalValue = sc.nextBigDecimal();
        sc.nextLine();
        System.out.print("Novo status (Enter para manter): ");
        String status = sc.nextLine();
        System.out.print("Nova data (dd/MM/yyyy, Enter para manter): ");
        String dateStr = sc.nextLine();
        System.out.print("Novo CPF do cliente (Enter para manter): ");
        String clientCpf = sc.nextLine();

        Date date = existingOrder.getDate();
        if (!dateStr.isEmpty()) {
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido, mantendo data anterior.");
            }
        }

        Order updatedOrder = new Order(
                id,
                totalValue.compareTo(BigDecimal.ZERO) == 0 ? existingOrder.getTotalValue() : totalValue,
                status.isEmpty() ? existingOrder.getStatus() : status,
                date,
                clientCpf.isEmpty() ? existingOrder.getClientCpf() : clientCpf
        );

        orderDAO.update(updatedOrder);
        System.out.println("Pedido atualizado com sucesso!");
    }

    private static void delete(Scanner sc) {
        System.out.print("Informe o ID do pedido a deletar: ");
        String id = sc.nextLine();
        orderDAO.delete(id);
        System.out.println("Pedido deletado com sucesso!");
    }
}
