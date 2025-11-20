-- Script de inicialização do banco de dados
-- Este script é executado automaticamente quando o container MySQL é criado

USE gamestore;

-- Tabela de Clientes
CREATE TABLE IF NOT EXISTS `Client` (
  `cpf` VARCHAR(15) PRIMARY KEY,
  `phone` VARCHAR(11) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `street` VARCHAR(100) NOT NULL,
  `city` VARCHAR(50) NOT NULL,
  `house_number` INTEGER NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Pedidos
CREATE TABLE IF NOT EXISTS `Order` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `total_value` DECIMAL(10,2) NOT NULL,
  `status` ENUM('delivered','shipping','pending','cancelled') NOT NULL DEFAULT 'pending',
  `data` DATE NOT NULL,
  `client_cpf` VARCHAR(15) NOT NULL,
  FOREIGN KEY (`client_cpf`) REFERENCES `Client`(`cpf`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS `Product` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `description` TEXT,
  `stock` INTEGER NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Jogos (herda de Product)
CREATE TABLE IF NOT EXISTS `Game` (
  `product_id` INT PRIMARY KEY,
  `developer` VARCHAR(100) NOT NULL,
  `platform` VARCHAR(100) NOT NULL,
  `release_date` DATE NOT NULL,
  FOREIGN KEY (`product_id`) REFERENCES `Product`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Itens Eletrônicos (herda de Product)
CREATE TABLE IF NOT EXISTS `EletronicItem` (
  `product_id` INT PRIMARY KEY,
  `producer` VARCHAR(50) NOT NULL,
  `type` VARCHAR(50) NOT NULL,
  FOREIGN KEY (`product_id`) REFERENCES `Product`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Gêneros
CREATE TABLE IF NOT EXISTS `Genre` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de relacionamento Game-Genre (N:N)
CREATE TABLE IF NOT EXISTS `GameGenre` (
  `genre_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  PRIMARY KEY (`genre_id`, `product_id`),
  FOREIGN KEY (`genre_id`) REFERENCES `Genre`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`product_id`) REFERENCES `Game`(`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Produtos do Pedido
CREATE TABLE IF NOT EXISTS `OrderProduct` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `amount` INTEGER NOT NULL,
  `items_total` DECIMAL(10,2) NOT NULL,
  `order_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  FOREIGN KEY (`order_id`) REFERENCES `Order`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`product_id`) REFERENCES `Product`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para melhorar performance
CREATE INDEX idx_order_client ON `Order`(`client_cpf`);
CREATE INDEX idx_order_status ON `Order`(`status`);
CREATE INDEX idx_order_date ON `Order`(`data`);
CREATE INDEX idx_product_name ON `Product`(`name`);
CREATE INDEX idx_client_email ON `Client`(`email`);

-- ============================================
-- DADOS DE EXEMPLO (opcional - remova se não quiser)
-- ============================================

-- Inserir clientes
INSERT INTO `Client` (`cpf`, `phone`, `name`, `email`, `street`, `city`, `house_number`) VALUES
('12345678901', '85999887766', 'João Silva', 'joao@email.com', 'Rua das Flores', 'Fortaleza', 123),
('98765432100', '85988776655', 'Maria Santos', 'maria@email.com', 'Av. Beira Mar', 'Fortaleza', 456),
('11122233344', '85977665544', 'Pedro Oliveira', 'pedro@email.com', 'Rua do Sol', 'Aracati', 789);

-- Inserir gêneros
INSERT INTO `Genre` (`name`) VALUES
('Ação'),
('Aventura'),
('RPG'),
('Estratégia'),
('Esportes');

-- Inserir produtos (jogos)
INSERT INTO `Product` (`name`, `price`, `description`, `stock`) VALUES
('The Legend of Zelda: Breath of the Wild', 299.90, 'Jogo de aventura em mundo aberto', 15),
('FIFA 24', 249.90, 'Simulador de futebol', 20),
('Elden Ring', 279.90, 'RPG de ação desafiador', 10),
('Civilization VI', 199.90, 'Jogo de estratégia por turnos', 8);

-- Inserir dados específicos de jogos
INSERT INTO `Game` (`product_id`, `developer`, `platform`, `release_date`) VALUES
(1, 'Nintendo', 'Nintendo Switch', '2017-03-03'),
(2, 'EA Sports', 'PlayStation 5', '2023-09-29'),
(3, 'FromSoftware', 'PC/PlayStation/Xbox', '2022-02-25'),
(4, 'Firaxis Games', 'PC', '2016-10-21');

-- Relacionar jogos com gêneros
INSERT INTO `GameGenre` (`genre_id`, `product_id`) VALUES
(1, 1), -- Zelda - Ação
(2, 1), -- Zelda - Aventura
(5, 2), -- FIFA - Esportes
(1, 3), -- Elden Ring - Ação
(3, 3), -- Elden Ring - RPG
(4, 4); -- Civilization - Estratégia

-- Inserir produtos eletrônicos
INSERT INTO `Product` (`name`, `price`, `description`, `stock`) VALUES
('Mouse Gamer RGB', 150.00, 'Mouse com iluminação RGB e DPI ajustável', 30),
('Teclado Mecânico', 350.00, 'Teclado mecânico com switches blue', 25),
('Headset 7.1', 280.00, 'Headset surround 7.1 com microfone', 18);

INSERT INTO `EletronicItem` (`product_id`, `producer`, `type`) VALUES
(5, 'Logitech', 'Periférico'),
(6, 'Razer', 'Periférico'),
(7, 'HyperX', 'Audio');

-- Inserir pedidos
INSERT INTO `Order` (`total_value`, `status`, `data`, `client_cpf`) VALUES
(549.80, 'delivered', '2024-11-01', '12345678901'),
(279.90, 'shipping', '2024-11-15', '98765432100'),
(630.00, 'pending', '2024-11-18', '11122233344');

-- Inserir itens dos pedidos
INSERT INTO `OrderProduct` (`amount`, `items_total`, `order_id`, `product_id`) VALUES
(1, 299.90, 1, 1), -- Zelda
(1, 249.90, 1, 2), -- FIFA
(1, 279.90, 2, 3), -- Elden Ring
(2, 280.00, 3, 7), -- 2x Headset
(1, 350.00, 3, 6); -- Teclado

-- Mensagem de sucesso
SELECT 'Banco de dados inicializado com sucesso!' AS Status;