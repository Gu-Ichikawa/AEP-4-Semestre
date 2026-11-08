CREATE DATABASE hemoplay;
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
    horario_funcionamento VARCHAR(100) NULL     
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