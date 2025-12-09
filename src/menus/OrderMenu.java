package menus;

import models.Client.dao.ClientDAO;
import models.Client.dao.implementations.exceptions.CPFNotValid;
import models.Order.dao.OrderDAO;
import models.Order.entitites.Order;
import models.OrderProduct.dao.OrderProductDAO;
import models.OrderProduct.entities.OrderProduct;
import models.Product.dao.ProductDAO;
import models.Product.entities.Product;
import Services.DaoFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OrderMenu {
    private static final ClientDAO clientDAO = DaoFactory.getClientDAO();
    private static final OrderDAO orderDAO = DaoFactory.getOrderDAO();
    private static final OrderProductDAO orderProductDAO = DaoFactory.getOrderProductDAO();
    private static final ProductDAO productDAO = DaoFactory.getProductDAO();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    static {
        sdf.setLenient(false); // Validação rigorosa de datas
    }

    private static boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }
        return cpf.matches("\\d{11}");
    }

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
        
        try {
            if (!isValidCPF(clientCpf)) {
                throw new CPFNotValid();
            }
        } catch (CPFNotValid e) {
            System.out.println(e.getMessage());
            return;
        }
        
        // se cliente não for encontrado joga exceção
        if (clientDAO.findByCPF(clientCpf) == null) return;

        System.out.print("Status (pending, shipping, delivered, cancelled): ");
        String status = sc.nextLine();
        
        // Validar status
        if (!status.isEmpty() && !status.equals("pending") && !status.equals("shipping") 
                && !status.equals("delivered") && !status.equals("cancelled")) {
            System.out.println("Status inválido! Usando 'pending'. Status válidos: pending, shipping, delivered, cancelled.");
            status = "pending";
        } else if (status.isEmpty()) {
            status = "pending";
        }

        System.out.print("Data (dd/MM/yyyy, Enter para hoje): ");
        String dateStr = sc.nextLine();

        Date date = new Date();
        if (!dateStr.isEmpty()) {
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Data inválida! Use o formato dd/MM/yyyy e verifique se o dia, mês e ano são válidos. Usando data de hoje.");
            }
        }

        // Criar o pedido
        Order order = new Order(new BigDecimal(0), status, date, clientCpf);
        int orderId = orderDAO.insert(order);
        System.out.println("Pedido criado com sucesso! ID: " + orderId);

        // Adicionar produtos ao pedido
        List<OrderProduct> orderProducts = new ArrayList<>();
        BigDecimal totalValue = new BigDecimal(0);
        double productId;
        
        System.out.println("\n--- Adicionar Produtos ao Pedido ---");
        System.out.println("Digite 0 como ID para finalizar.");
        
        do {
            // Listar produtos disponíveis
            System.out.println("\n--- Produtos Disponíveis ---");
            List<Product> products = productDAO.find();
            if (products.isEmpty()) {
                System.out.println("Nenhum produto disponível.");
                break;
            }
            for (Product p : products) {
                System.out.printf("ID: %.0f | %s | R$ %.2f | Estoque: %d%n", 
                    Double.parseDouble(p.getId()), p.getName(), p.getPrice(), p.getStock());
            }
            
            System.out.print("\nInforme o ID do produto (0 para finalizar): ");
            productId = sc.nextDouble();
            sc.nextLine();
            
            if (productId == 0) {
                break;
            }
            
            // Buscar produto
            Product selectedProduct = productDAO.findById(String.valueOf((int)productId));
            if (selectedProduct == null) {
                System.out.println("Produto não encontrado!");
                continue;
            }
            
            // Verificar se o produto já está no pedido
            OrderProduct existingOrderProduct = null;
            for (OrderProduct op : orderProducts) {
                if (op.getProductId().equals(selectedProduct.getId())) {
                    existingOrderProduct = op;
                    break;
                }
            }
            
            if (existingOrderProduct != null) {
                System.out.printf("Produto já está no pedido! Quantidade atual: %d%n", existingOrderProduct.getAmount());
                System.out.print("Nova quantidade: ");
                int newAmount = sc.nextInt();
                sc.nextLine();
                
                if (newAmount <= 0) {
                    System.out.println("Quantidade deve ser maior que zero!");
                    continue;
                }
                
                if (newAmount > selectedProduct.getStock()) {
                    System.out.println("Quantidade indisponível em estoque! Estoque atual: " + selectedProduct.getStock());
                    continue;
                }
                
                // Atualizar quantidade e total
                BigDecimal oldTotal = existingOrderProduct.getItemsTotal();
                totalValue = totalValue.subtract(oldTotal);
                
                BigDecimal newItemTotal = selectedProduct.getPrice().multiply(new BigDecimal(newAmount));
                existingOrderProduct.setAmount(newAmount);
                existingOrderProduct.setItemsTotal(newItemTotal);
                totalValue = totalValue.add(newItemTotal);
                
                System.out.printf("Quantidade atualizada: %s | Qtd: %d | Subtotal: R$ %.2f%n", 
                    selectedProduct.getName(), newAmount, newItemTotal);
            } else {
                System.out.print("Quantidade: ");
                int amount = sc.nextInt();
                sc.nextLine();
                
                if (amount <= 0) {
                    System.out.println("Quantidade deve ser maior que zero!");
                    continue;
                }
                
                if (amount > selectedProduct.getStock()) {
                    System.out.println("Quantidade indisponível em estoque! Estoque atual: " + selectedProduct.getStock());
                    continue;
                }
                
                BigDecimal itemTotal = selectedProduct.getPrice().multiply(new BigDecimal(amount));
                totalValue = totalValue.add(itemTotal);
                
                OrderProduct orderProduct = new OrderProduct(amount, itemTotal, String.valueOf(orderId), selectedProduct.getId());
                orderProducts.add(orderProduct);
                
                System.out.printf("Produto adicionado: %s | Qtd: %d | Subtotal: R$ %.2f%n", 
                    selectedProduct.getName(), amount, itemTotal);
            }
            
            System.out.printf("Total atual do pedido: R$ %.2f%n", totalValue);
            
        } while (productId != 0);
        
        // Salvar todos os produtos no pedido
        if (!orderProducts.isEmpty()) {
            for (OrderProduct op : orderProducts) {
                orderProductDAO.insert(op);
            }
            
            // Atualizar valor total do pedido
            order.setTotalValue(totalValue);
            orderDAO.update(order);
            
            System.out.println("\n✓ Pedido finalizado com sucesso!");
            System.out.printf("Total de produtos: %d%n", orderProducts.size());
            System.out.printf("Valor total: R$ %.2f%n", totalValue);
        } else {
            System.out.println("\nNenhum produto adicionado ao pedido.");
        }
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
        if (existingOrder == null) return;

        System.out.println("Pedido encontrado: " + existingOrder);
        System.out.print("Novo status (Enter para manter): ");
        String status = sc.nextLine();
        
        // Validar status se não estiver vazio
        if (!status.isEmpty() && !status.equals("pending") && !status.equals("shipping") 
                && !status.equals("delivered") && !status.equals("cancelled")) {
            System.out.println("Status inválido! Usando 'pending'. Status válidos: pending, shipping, delivered, cancelled.");
            status = "pending";
        }
        
        System.out.print("Novo CPF do cliente (Enter para manter): ");
        String clientCpf = sc.nextLine();
        
        try {
            if (!clientCpf.isEmpty() && !isValidCPF(clientCpf)) {
                throw new CPFNotValid();
            }
        } catch (CPFNotValid e) {
            System.out.println(e.getMessage());
            return;
        }
        
        System.out.print("Nova data (dd/MM/yyyy, Enter para manter): ");
        String dateStr = sc.nextLine();

        Date date = existingOrder.getDate();
        if (!dateStr.isEmpty()) {
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Data inválida! Use o formato dd/MM/yyyy e verifique se o dia, mês e ano são válidos. Mantendo data anterior.");
            }
        }

        // Adicionar produtos ao pedido
        List<OrderProduct> orderProducts = new ArrayList<>();
        BigDecimal totalValue = existingOrder.getTotalValue();
        double productId;
        
        System.out.println("\n--- Adicionar Novos Produtos ao Pedido ---");
        System.out.println("Digite 0 como ID para finalizar sem adicionar produtos.");
        System.out.print("Deseja adicionar produtos? (s/n): ");
        String addProducts = sc.nextLine();
        
        if (addProducts.equalsIgnoreCase("s")) {
            // Buscar produtos já existentes no pedido
            List<OrderProduct> existingProducts = orderProductDAO.findByOrderId(id);
            
            do {
                // Listar produtos disponíveis
                System.out.println("\n--- Produtos Disponíveis ---");
                List<Product> products = productDAO.find();
                if (products.isEmpty()) {
                    System.out.println("Nenhum produto disponível.");
                    break;
                }
                for (Product p : products) {
                    System.out.printf("ID: %.0f | %s | R$ %.2f | Estoque: %d%n", 
                        Double.parseDouble(p.getId()), p.getName(), p.getPrice(), p.getStock());
                }
                
                System.out.print("\nInforme o ID do produto (0 para finalizar): ");
                productId = sc.nextDouble();
                sc.nextLine();
                
                if (productId == 0) {
                    break;
                }
                
                // Buscar produto
                Product selectedProduct = productDAO.findById(String.valueOf((int)productId));
                if (selectedProduct == null) {
                    System.out.println("Produto não encontrado!");
                    continue;
                }
                
                // Verificar se o produto já está no pedido (na lista existente ou na nova)
                OrderProduct existingInDb = null;
                for (OrderProduct op : existingProducts) {
                    if (op.getProductId().equals(selectedProduct.getId())) {
                        existingInDb = op;
                        break;
                    }
                }
                
                OrderProduct existingInList = null;
                for (OrderProduct op : orderProducts) {
                    if (op.getProductId().equals(selectedProduct.getId())) {
                        existingInList = op;
                        break;
                    }
                }
                
                if (existingInDb != null) {
                    System.out.printf("Produto já está no pedido! Quantidade atual: %d%n", existingInDb.getAmount());
                    System.out.print("Nova quantidade: ");
                    int newAmount = sc.nextInt();
                    sc.nextLine();
                    
                    if (newAmount <= 0) {
                        System.out.println("Quantidade deve ser maior que zero!");
                        continue;
                    }
                    
                    if (newAmount > selectedProduct.getStock()) {
                        System.out.println("Quantidade indisponível em estoque! Estoque atual: " + selectedProduct.getStock());
                        continue;
                    }
                    
                    // Atualizar no banco de dados
                    BigDecimal oldTotal = existingInDb.getItemsTotal();
                    totalValue = totalValue.subtract(oldTotal);
                    
                    BigDecimal newItemTotal = selectedProduct.getPrice().multiply(new BigDecimal(newAmount));
                    existingInDb.setAmount(newAmount);
                    existingInDb.setItemsTotal(newItemTotal);
                    totalValue = totalValue.add(newItemTotal);
                    
                    orderProductDAO.update(existingInDb);
                    
                    System.out.printf("Quantidade atualizada no banco: %s | Qtd: %d | Subtotal: R$ %.2f%n", 
                        selectedProduct.getName(), newAmount, newItemTotal);
                } else if (existingInList != null) {
                    System.out.printf("Produto já está na lista! Quantidade atual: %d%n", existingInList.getAmount());
                    System.out.print("Nova quantidade: ");
                    int newAmount = sc.nextInt();
                    sc.nextLine();
                    
                    if (newAmount <= 0) {
                        System.out.println("Quantidade deve ser maior que zero!");
                        continue;
                    }
                    
                    if (newAmount > selectedProduct.getStock()) {
                        System.out.println("Quantidade indisponível em estoque! Estoque atual: " + selectedProduct.getStock());
                        continue;
                    }
                    
                    // Atualizar na lista
                    BigDecimal oldTotal = existingInList.getItemsTotal();
                    totalValue = totalValue.subtract(oldTotal);
                    
                    BigDecimal newItemTotal = selectedProduct.getPrice().multiply(new BigDecimal(newAmount));
                    existingInList.setAmount(newAmount);
                    existingInList.setItemsTotal(newItemTotal);
                    totalValue = totalValue.add(newItemTotal);
                    
                    System.out.printf("Quantidade atualizada: %s | Qtd: %d | Subtotal: R$ %.2f%n", 
                        selectedProduct.getName(), newAmount, newItemTotal);
                } else {
                    System.out.print("Quantidade: ");
                    int amount = sc.nextInt();
                    sc.nextLine();
                    
                    if (amount <= 0) {
                        System.out.println("Quantidade deve ser maior que zero!");
                        continue;
                    }
                    
                    if (amount > selectedProduct.getStock()) {
                        System.out.println("Quantidade indisponível em estoque! Estoque atual: " + selectedProduct.getStock());
                        continue;
                    }
                    
                    BigDecimal itemTotal = selectedProduct.getPrice().multiply(new BigDecimal(amount));
                    totalValue = totalValue.add(itemTotal);
                    
                    OrderProduct orderProduct = new OrderProduct(amount, itemTotal, id, selectedProduct.getId());
                    orderProducts.add(orderProduct);
                    
                    System.out.printf("Produto adicionado: %s | Qtd: %d | Subtotal: R$ %.2f%n", 
                        selectedProduct.getName(), amount, itemTotal);
                }
                
                System.out.printf("Total atual do pedido: R$ %.2f%n", totalValue);
                
            } while (productId != 0);
            
            // Salvar todos os produtos no pedido
            if (!orderProducts.isEmpty()) {
                for (OrderProduct op : orderProducts) {
                    orderProductDAO.insert(op);
                }
                System.out.printf("\n✓ %d produto(s) adicionado(s) ao pedido!%n", orderProducts.size());
            }
        }

        Order updatedOrder = new Order(
                id,
                totalValue,
                status.isEmpty() ? existingOrder.getStatus() : status,
                date,
                clientCpf.isEmpty() ? existingOrder.getClientCpf() : clientCpf
        );

        orderDAO.update(updatedOrder);
        System.out.println("Pedido atualizado com sucesso!");
        System.out.printf("Valor total do pedido: R$ %.2f%n", totalValue);
    }

    private static void delete(Scanner sc) {
        System.out.print("Informe o ID do pedido a deletar: ");
        double id = sc.nextDouble();
        sc.nextLine();
        orderDAO.delete(id);
    }
}
