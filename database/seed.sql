
USE hemoplay;

INSERT INTO tipo_sanguineo (tipo, fator_rh) VALUES
    ('A', '+'), ('A', '-'),
    ('B', '+'), ('B', '-'),
    ('AB', '+'), ('AB', '-'),
    ('O', '+'), ('O', '-');

INSERT INTO nivel (nome, pontuacao_min, pontuacao_max) VALUES
    ('Iniciante', 0, 99),
    ('Apoiador', 100, 299),
    ('Doador', 300, 599),
    ('Guardião da Vida', 600, 999),
    ('Herói da Vida', 1000, NULL);
