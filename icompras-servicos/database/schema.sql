create database icomprasprodutos;
create table produtos (
	codigo serial not null primary key,
	nome varchar(100) not null,
	valor_unitario decimal(16,2) not null
);

create database icomprasclientes;
create table clientes (
	codigo serial not null primary key,
	nome varchar(150) not null,
	cpf char(11) not null,
	logradouro varchar(100),
	numero varchar(10),
	bairro varchar(100),
	email varchar(150),
	telefone varchar(20)
);

create database icompraspedidos;
create table pedido (
	codigo serial not null primary key,
	codigo_cliente bigint not null,
	data_pedido timestamp not null default now(),
	chave_pagamento text,
	observacoes text,
	status varchar(20) check (
		status in ('REALIZADO', 'PAGO', 'FATURADO', 'ENVIADO', 'ERRO_PAGAMENTO', 'PREPARANDO_ENVIO')
	),
	total decimal(16,2) not null,
	codigo_rastreio varchar(255),
	url_nf text
);

create table item_pedido (
	codigo serial not null primary key,
	codigo_pedido bigint not null references pedido (codigo),
	codigo_produto bigint not null,
	quantidade int not null,
	valor_unitario decimal(16,2) not null
);

CREATE TABLE pagamento_pedido (
    id SERIAL PRIMARY KEY,
    forma_pagamento VARCHAR(255),
    numero_cartao VARCHAR(255)
);

ALTER TABLE pedido
ADD COLUMN pagamento_id BIGINT;

ALTER TABLE pedido
ADD CONSTRAINT fk_pagamento
FOREIGN KEY (pagamento_id)
REFERENCES pagamento_pedido (id);