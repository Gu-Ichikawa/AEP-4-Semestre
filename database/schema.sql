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

)