-- ATENÇÃO: o DROP apaga o banco inteiro e todos os dados.
-- Use apenas em ambiente de desenvolvimento.
-- Ordem de execução: 1) schema.sql  2) seed.sql  3) dados-teste.sql
DROP DATABASE IF EXISTS hemoplay;

CREATE DATABASE hemoplay
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;


USE hemoplay;

CREATE TABLE tipo_sanguineo (
    id_tipo_sanguineo BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(2) NOT NULL,
    fator_rh CHAR(1) NOT NULL,

    CONSTRAINT uq_tipo_sanguineo_tipo_rh						
        UNIQUE (tipo, fator_rh),

    CONSTRAINT chk_tipo_sanguineo_tipo
        CHECK (tipo IN ('A', 'B', 'AB', 'O')),

    CONSTRAINT chk_tipo_sanguineo_fator_rh
        CHECK (fator_rh IN ('+', '-'))

);


CREATE TABLE nivel (
    id_nivel BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    pontuacao_min INT NOT NULL,
    pontuacao_max INT NULL,

    CONSTRAINT uq_nivel_nome
        UNIQUE (nome),

    CONSTRAINT chk_nivel_pontuacao_min
        CHECK (pontuacao_min >= 0),

    CONSTRAINT chk_nivel_pontuacao_max
        CHECK (pontuacao_max IS NULL OR pontuacao_max >= pontuacao_min)
);

CREATE TABLE local_doacao (
    id_local BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(60) NOT NULL,
    telefone VARCHAR(20) NULL,
    estado VARCHAR(2) NOT NULL,
    endereco VARCHAR(100) NOT NULL,
    cidade VARCHAR(30) NOT NULL,
    horario_funcionamento VARCHAR(100) NULL,
    descricao VARCHAR(500) NULL
);

CREATE TABLE evento (
    id_evento BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(500) NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    tipo VARCHAR(30) NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT chk_evento_datas
        CHECK (data_fim >= data_inicio)
);

CREATE TABLE conquista (
    id_conquista BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500) NULL,
    criterio VARCHAR(255) NOT NULL,
    recompensa VARCHAR(255) NULL,

    CONSTRAINT uq_conquista_nome
        UNIQUE (nome)
);

CREATE TABLE item (
    id_item BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500) NULL,
    tipo VARCHAR(30) NOT NULL,
    imagem VARCHAR(255) NULL,
    requisito_desbloqueio VARCHAR(255) NULL,

    CONSTRAINT uq_item_nome
        UNIQUE (nome)
);

CREATE TABLE usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_usuario VARCHAR(30) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha VARCHAR(100) NOT NULL, 
    data_nasc DATE NULL,
    telefone VARCHAR(20) NULL,
    pontuacao INT NOT NULL DEFAULT 0,
    mascote_cor VARCHAR(30) NULL,
    mascote_acessorios VARCHAR(255) NULL,
    id_tipo_sanguineo BIGINT NULL,
    id_nivel BIGINT NULL,

    CONSTRAINT uq_usuario_email
        UNIQUE (email),

    CONSTRAINT chk_usuario_tipo
        CHECK (tipo_usuario IN ('DOADOR', 'ADMINISTRADOR', 'RESPONSAVEL_HEMOCENTRO')),

    CONSTRAINT chk_usuario_pontuacao
        CHECK (pontuacao >= 0),

    CONSTRAINT fk_usuario_tipo_sanguineo
        FOREIGN KEY (id_tipo_sanguineo) REFERENCES tipo_sanguineo (id_tipo_sanguineo),

    CONSTRAINT fk_usuario_nivel
        FOREIGN KEY (id_nivel) REFERENCES nivel (id_nivel)
);

CREATE TABLE doacao (
    id_doacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_local BIGINT NULL,
    data_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_confirmacao DATETIME NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'REGISTRADA',

    CONSTRAINT chk_doacao_status
        CHECK (status IN ('REGISTRADA', 'CONFIRMADA')),

    CONSTRAINT chk_doacao_confirmacao
        CHECK (
            (status = 'REGISTRADA' AND data_confirmacao IS NULL)
            OR (status = 'CONFIRMADA' AND data_confirmacao IS NOT NULL)
        ),

    CONSTRAINT fk_doacao_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_doacao_local
        FOREIGN KEY (id_local) REFERENCES local_doacao (id_local)
        ON DELETE SET NULL
);

CREATE TABLE campanha (
    id_campanha BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(500) NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    id_local BIGINT NULL,
    id_tipo_sanguineo BIGINT NULL,

    CONSTRAINT chk_campanha_datas
        CHECK (data_fim >= data_inicio),

    CONSTRAINT fk_campanha_local
        FOREIGN KEY (id_local) REFERENCES local_doacao (id_local)
        ON DELETE SET NULL,

    CONSTRAINT fk_campanha_tipo_sanguineo
        FOREIGN KEY (id_tipo_sanguineo) REFERENCES tipo_sanguineo (id_tipo_sanguineo)
);

CREATE TABLE participacao_campanha (
    id_participacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_campanha BIGINT NOT NULL,
    data_participacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,

    CONSTRAINT uq_participacao_campanha
        UNIQUE (id_usuario, id_campanha),

    CONSTRAINT fk_participacao_campanha_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_participacao_campanha_campanha
        FOREIGN KEY (id_campanha) REFERENCES campanha (id_campanha)
        ON DELETE CASCADE
);

CREATE TABLE necessidade_sanguinea (
    id_necessidade BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_tipo_sanguineo BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    quantidade_simulada INT NOT NULL DEFAULT 0,
    data_atualizacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_necessidade_quantidade
        CHECK (quantidade_simulada >= 0),

    CONSTRAINT fk_necessidade_tipo_sanguineo
        FOREIGN KEY (id_tipo_sanguineo) REFERENCES tipo_sanguineo (id_tipo_sanguineo)
);

CREATE TABLE missao (
    id_missao BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(500) NULL,
    tipo VARCHAR(30) NOT NULL,
    criterio VARCHAR(255) NULL,
    pontuacao INT NOT NULL DEFAULT 0,
    data_inicio DATE NULL,
    data_fim DATE NULL,
    ativa BOOLEAN NOT NULL DEFAULT TRUE,
    id_evento BIGINT NULL,

    CONSTRAINT chk_missao_pontuacao
        CHECK (pontuacao >= 0),

    CONSTRAINT chk_missao_datas
        CHECK (data_inicio IS NULL OR data_fim IS NULL OR data_fim >= data_inicio),

    CONSTRAINT fk_missao_evento
        FOREIGN KEY (id_evento) REFERENCES evento (id_evento)
        ON DELETE SET NULL
);

CREATE TABLE participacao_missao (
    id_participacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_missao BIGINT NOT NULL,
    data_inicio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_conclusao DATETIME NULL,
    status VARCHAR(20) NOT NULL,
    recompensa_recebida BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_participacao_missao
        UNIQUE (id_usuario, id_missao),

    CONSTRAINT fk_participacao_missao_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_participacao_missao_missao
        FOREIGN KEY (id_missao) REFERENCES missao (id_missao)
        ON DELETE CASCADE
);

CREATE TABLE usuario_conquista (
    id_usuario BIGINT NOT NULL,
    id_conquista BIGINT NOT NULL,
    data_desbloqueio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_usuario_conquista
        PRIMARY KEY (id_usuario, id_conquista),

    CONSTRAINT fk_usuario_conquista_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_usuario_conquista_conquista
        FOREIGN KEY (id_conquista) REFERENCES conquista (id_conquista)
        ON DELETE CASCADE
);

CREATE TABLE inventario (
    id_inventario BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    id_item BIGINT NOT NULL,
    data_desbloqueio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    equipado BOOLEAN NOT NULL DEFAULT FALSE,
    quantidade INT NOT NULL DEFAULT 1,

    CONSTRAINT uq_inventario_usuario_item
        UNIQUE (id_usuario, id_item),

    CONSTRAINT chk_inventario_quantidade
        CHECK (quantidade >= 1),

    CONSTRAINT fk_inventario_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_inventario_item
        FOREIGN KEY (id_item) REFERENCES item (id_item)
        ON DELETE CASCADE
);

CREATE TABLE notificacao (
    id_notificacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_usuario BIGINT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    data_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lida BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_notificacao_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON DELETE CASCADE
);