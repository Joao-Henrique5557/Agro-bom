CREATE DATABASE IF NOT EXISTS db_agro_bom;
USE db_agro_bom;

-- =========================
-- CLIENTE
-- =========================
CREATE TABLE IF NOT EXISTS CLIENTE (
    cpf VARCHAR(14) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    endereco VARCHAR(150) NOT NULL
);

-- =========================
-- PRODUTO
-- =========================
CREATE TABLE IF NOT EXISTS PRODUTO (
    id_produto INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    preco DECIMAL(10,2) NOT NULL,
    unidade_medida VARCHAR(20) NOT NULL,
    quantidade_estoque INT NOT NULL DEFAULT 0,
    quantidade_ideal INT NOT NULL DEFAULT 0
);

-- =========================
-- PEDIDO
-- =========================
CREATE TABLE IF NOT EXISTS PEDIDO (
    id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    numero_pedido INT NOT NULL UNIQUE,
    data_pedido DATE NOT NULL,
    desconto DECIMAL(10,2) DEFAULT 0,
    valor_total DECIMAL(10,2) NOT NULL,
    cpf_cliente VARCHAR(14) NOT NULL,

    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (cpf_cliente)
        REFERENCES CLIENTE(cpf)
);

-- =========================
-- ITEM PEDIDO
-- =========================
CREATE TABLE IF NOT EXISTS ITEM_PEDIDO (
    id_item_pedido INT PRIMARY KEY AUTO_INCREMENT,
    quantidade_pedida INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,

    id_pedido INT NOT NULL,
    id_produto INT NOT NULL,

    CONSTRAINT fk_itempedido_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES PEDIDO(id_pedido),

    CONSTRAINT fk_itempedido_produto
        FOREIGN KEY (id_produto)
        REFERENCES PRODUTO(id_produto)
);

-- =========================
-- FORNECEDOR
-- =========================
CREATE TABLE IF NOT EXISTS FORNECEDOR (
    id_fornecedor INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL
);

-- =========================
-- PRODUTO_FORNECEDOR
-- =========================
CREATE TABLE IF NOT EXISTS PRODUTO_FORNECEDOR (
    id_produto_fornecedor INT PRIMARY KEY AUTO_INCREMENT,

    id_produto INT NOT NULL,
    id_fornecedor INT NOT NULL,

    CONSTRAINT fk_produtofornecedor_produto
        FOREIGN KEY (id_produto)
        REFERENCES PRODUTO(id_produto),

    CONSTRAINT fk_produtofornecedor_fornecedor
        FOREIGN KEY (id_fornecedor)
        REFERENCES FORNECEDOR(id_fornecedor),

    UNIQUE (id_produto, id_fornecedor)
);

-- =========================
-- SOLICITAÇÃO DE COMPRA
-- =========================
CREATE TABLE IF NOT EXISTS SOLICITACAO_COMPRA (
    id_solicitacao INT PRIMARY KEY AUTO_INCREMENT,
    numero_solicitacao INT NOT NULL UNIQUE,
    data_solicitacao DATE NOT NULL,

    situacao ENUM('ABERTA', 'ENCERRADA')
        NOT NULL DEFAULT 'ABERTA',

    valor_total DECIMAL(10,2) DEFAULT 0,

    id_fornecedor INT NOT NULL,

    CONSTRAINT fk_solicitacao_fornecedor
        FOREIGN KEY (id_fornecedor)
        REFERENCES FORNECEDOR(id_fornecedor)
);

-- =========================
-- ITEM SOLICITAÇÃO
-- =========================
CREATE TABLE IF NOT EXISTS ITEM_SOLICITACAO (
    id_item_solicitacao INT PRIMARY KEY AUTO_INCREMENT,

    quantidade_solicitada INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,

    id_solicitacao INT NOT NULL,
    id_produto INT NOT NULL,

    CONSTRAINT fk_itemsolicitacao_solicitacao
        FOREIGN KEY (id_solicitacao)
        REFERENCES SOLICITACAO_COMPRA(id_solicitacao),

    CONSTRAINT fk_itemsolicitacao_produto
        FOREIGN KEY (id_produto)
        REFERENCES PRODUTO(id_produto)
);
use db_agro_bom;
select * from CLIENTE;

-- =======================================================
-- CONSULTAS DOS 6 RELATÓRIOS GERENCIAIS (ENUNCIADO)
-- =======================================================

-- RELATÓRIO 1: Posição semanal de estoque
-- Descrição, quantidade existente, unidade de medida, quantidade mínima ideal
SELECT nome, descricao, quantidade_estoque, unidade_medida, quantidade_ideal
FROM PRODUTO
ORDER BY nome;

-- RELATÓRIO 2: Pedidos recebidos em cada mês do ano (ex: mês 5, ano 2026)
-- Número do pedido, nome/fone/endereço do cliente, produtos (descrição, unidade, qtd pedida)
SELECT p.numero_pedido, p.data_pedido,
       c.nome AS nome_cliente, c.telefone, c.endereco,
       pr.descricao AS produto_descricao, pr.unidade_medida,
       ip.quantidade_pedida
FROM PEDIDO p
JOIN CLIENTE c  ON p.cpf_cliente = c.cpf
JOIN ITEM_PEDIDO ip ON p.id_pedido = ip.id_pedido
JOIN PRODUTO pr ON ip.id_produto = pr.id_produto
WHERE MONTH(p.data_pedido) = 5 AND YEAR(p.data_pedido) = 2026
ORDER BY p.numero_pedido;

-- RELATÓRIO 3: Pedidos em intervalo de datas
-- Valor total, desconto e número de cada pedido
SELECT numero_pedido, data_pedido, desconto, valor_total
FROM PEDIDO
WHERE data_pedido BETWEEN '2026-01-01' AND '2026-05-31'
ORDER BY data_pedido;

-- RELATÓRIO 4: Fornecedores de cada produto (Nome, CGC/CNPJ, telefone)
SELECT pr.nome AS produto, f.nome AS fornecedor, f.cnpj, f.telefone
FROM PRODUTO_FORNECEDOR pf
JOIN PRODUTO pr   ON pf.id_produto    = pr.id_produto
JOIN FORNECEDOR f ON pf.id_fornecedor = f.id_fornecedor
ORDER BY pr.nome, f.nome;

-- RELATÓRIO 5: Solicitações de compra mês a mês (ex: mês 5, ano 2026)
-- Nome/CGC do fornecedor, produtos e quantidades, número e situação da solicitação
SELECT sc.numero_solicitacao, sc.data_solicitacao, sc.situacao,
       f.nome AS fornecedor, f.cnpj,
       pr.nome AS produto, its.quantidade_solicitada
FROM SOLICITACAO_COMPRA sc
JOIN FORNECEDOR f         ON sc.id_fornecedor  = f.id_fornecedor
JOIN ITEM_SOLICITACAO its ON sc.id_solicitacao = its.id_solicitacao
JOIN PRODUTO pr           ON its.id_produto    = pr.id_produto
WHERE MONTH(sc.data_solicitacao) = 5 AND YEAR(sc.data_solicitacao) = 2026
ORDER BY sc.numero_solicitacao;

-- RELATÓRIO 6: Volume financeiro (solicitações e pedidos) nos últimos 12 meses, mês a mês
SELECT mes, ano,
       SUM(total_pedidos)      AS total_pedidos,
       SUM(total_solicitacoes) AS total_solicitacoes
FROM (
    SELECT MONTH(data_pedido) AS mes, YEAR(data_pedido) AS ano,
           SUM(valor_total) AS total_pedidos, 0 AS total_solicitacoes
    FROM PEDIDO
    WHERE data_pedido >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
    GROUP BY YEAR(data_pedido), MONTH(data_pedido)
    UNION ALL
    SELECT MONTH(data_solicitacao) AS mes, YEAR(data_solicitacao) AS ano,
           0 AS total_pedidos, SUM(valor_total) AS total_solicitacoes
    FROM SOLICITACAO_COMPRA
    WHERE data_solicitacao >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
    GROUP BY YEAR(data_solicitacao), MONTH(data_solicitacao)
) AS sub
GROUP BY ano, mes
ORDER BY ano, mes;
