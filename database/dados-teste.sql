-- HemoPlay: dados de teste (somente desenvolvimento e testes automatizados)
-- Pré-requisito: rodar schema.sql e seed.sql em um banco recém-criado.
-- Todos os nomes, endereços e telefones são fictícios.

USE hemoplay;

-- 1. Hemocentros (tabela local_doacao, conforme o DER) -> ids 1 a 4
INSERT INTO local_doacao (nome, endereco, cidade, estado, telefone, horario_funcionamento) VALUES
('Hemocentro de Exemplo Centro', 'Rua Exemplo, 100', 'Cidade Exemplo', 'SP', '(00) 0000-0001', 'Seg a sex, 7h às 17h'),
('Hemocentro de Exemplo Zona Sul', 'Av. Modelo, 2000', 'Cidade Exemplo', 'SP', '(00) 0000-0002', 'Seg a sáb, 8h às 14h'),
('Hemocentro de Exemplo Norte', 'Rua Teste, 55', 'Outra Cidade Exemplo', 'PR', '(00) 0000-0003', 'Ter a sáb, 7h às 13h'),
('Hemocentro de Exemplo Leste', 'Praça Fictícia, 10', 'Outra Cidade Exemplo', 'PR', '(00) 0000-0004', 'Seg a sex, 9h às 18h');

-- 2. Evento sazonal -> id 1
INSERT INTO evento (nome, descricao, data_inicio, data_fim, tipo, ativo) VALUES
('Mês do Doador (exemplo)', 'Evento sazonal com missões e recompensas extras.', '2026-11-01', '2026-11-30', 'SAZONAL', TRUE);

-- 3. Campanhas -> ids 1 e 2
-- id_tipo_sanguineo 8 = O- (ordem definida no seed.sql)
INSERT INTO campanha (nome, descricao, data_inicio, data_fim, status, id_local, id_tipo_sanguineo) VALUES
('Estoque crítico do tipo O negativo (exemplo)', 'Doação prioritária para o tipo O negativo.', '2026-11-01', '2026-11-30', 'ATIVA', 1, 8),
('Doe no Norte (exemplo)', 'Campanha de coleta na unidade norte.', '2026-11-10', '2026-12-10', 'PLANEJADA', 3, NULL);

-- 4. Necessidades simuladas, uma por tipo sanguíneo (RF11)
-- ids de tipo: 1 A+, 2 A-, 3 B+, 4 B-, 5 AB+, 6 AB-, 7 O+, 8 O-
INSERT INTO necessidade_sanguinea (id_tipo_sanguineo, status, quantidade_simulada, data_atualizacao) VALUES
(1, 'ADEQUADO', 120, '2026-11-07 09:00:00'),
(2, 'ADEQUADO', 45,  '2026-11-07 09:00:00'),
(3, 'ATENCAO',  30,  '2026-11-07 09:00:00'),
(4, 'ADEQUADO', 40,  '2026-11-07 09:00:00'),
(5, 'ADEQUADO', 60,  '2026-11-07 09:00:00'),
(6, 'ATENCAO',  12,  '2026-11-07 09:00:00'),
(7, 'ADEQUADO', 110, '2026-11-07 09:00:00'),
(8, 'CRITICO',  8,   '2026-11-07 09:00:00');

-- 5. Missões (RF07) -> 15 registros. Missões 13 e 14 pertencem ao evento 1.
INSERT INTO missao (nome, descricao, tipo, criterio, pontuacao, data_inicio, data_fim, ativa, id_evento) VALUES
('Completar o cadastro',      'Preencher nome, e-mail e tipo sanguíneo.',        'APRENDIZADO', 'Campos obrigatórios e tipo sanguíneo preenchidos', 20,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Conhecer os requisitos',    'Ler os critérios de doação.',                      'APRENDIZADO', 'Abrir a página de critérios',                      10,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Conhecer o intervalo',      'Ler a regra de intervalo entre doações.',          'APRENDIZADO', 'Abrir a regra de intervalo',                       10,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Visitar um local',          'Consultar um local de doação.',                    'ENGAJAMENTO', 'Abrir a lista de locais',                          15,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Ver seu histórico',         'Abrir o histórico de participação.',               'ENGAJAMENTO', 'Abrir o histórico uma vez',                        10,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Personalizar o mascote',    'Escolher a cor ou um acessório.',                  'ENGAJAMENTO', 'Salvar uma personalização',                        15,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Mantenha o ritmo',          'Acessar o app em 7 dias seguidos.',                'ENGAJAMENTO', 'Acessos em 7 dias consecutivos',                   40,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Registrar uma doação',      'Registrar uma doação no sistema.',                 'DOACAO',      'Doação com status REGISTRADA',                     50,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Doação confirmada',         'Ter uma doação confirmada.',                       'DOACAO',      'Doação com status CONFIRMADA',                     100, '2026-11-01', '2026-12-31', TRUE, NULL),
('Doar em campanha',          'Registrar doação em campanha ativa.',              'DOACAO',      'Doação vinculada a campanha ativa',                75,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Doar pelo estoque crítico', 'Doar para o tipo O negativo.',                     'DOACAO',      'Doação em campanha de tipo crítico',               80,  '2026-11-01', '2026-12-31', TRUE, NULL),
('Três doações no ano',       'Ter três doações confirmadas no mesmo ano.',       'DOACAO',      'Três doações CONFIRMADAS no ano',                  150, '2026-11-01', '2026-12-31', TRUE, NULL),
('Doação do mês do doador',   'Registrar uma doação durante o evento.',           'EVENTO',      'Doação dentro do período do evento',               60,  '2026-11-01', '2026-11-30', TRUE, 1),
('Dupla doação do mês',       'Ter duas doações confirmadas no evento.',          'EVENTO',      'Duas doações CONFIRMADAS no período do evento',    90,  '2026-11-01', '2026-11-30', TRUE, 1),
('Convide um amigo',          'Compartilhar o HemoPlay com um amigo.',            'ENGAJAMENTO', 'Convite registrado',                               25,  '2026-11-01', '2026-12-31', TRUE, NULL);

-- 6. Conquistas (RF09) -> 5 registros
INSERT INTO conquista (nome, descricao, criterio, recompensa) VALUES
('Primeiro Passo',    'Completou o cadastro.',                          'Preencher o cadastro com tipo sanguíneo',   'Laço vermelho'),
('Primeira Doação',   'Registrou a primeira doação confirmada.',        'Ter 1 doação com status CONFIRMADA',        'Colar de rubi'),
('Doador Constante',  'Doou três vezes.',                               'Ter 3 doações confirmadas',                 'Cor azul-noite'),
('Herói do Estoque',  'Doou para um tipo em estado crítico.',           'Doar em campanha com estoque crítico',      'Capa de sombra'),
('Mascote Vestido',   'Personalizou o mascote.',                        'Escolher acessórios para o mascote',        'Gravata borboleta');

-- 7. Itens do mascote (RF10) -> 25 registros
-- As imagens são caminhos de exemplo. Os arquivos ainda não existem.
INSERT INTO item (nome, descricao, tipo, imagem, requisito_desbloqueio) VALUES
('Laço vermelho',          'Laço clássico para o mascote.',                       'ACESSORIO', 'itens/001.png', 'Nível Iniciante'),
('Óculos de aviador',      'Óculos para dias de voo longo.',                      'ACESSORIO', 'itens/002.png', 'Missão Completar o cadastro'),
('Gravata borboleta',      'Visual elegante para o mascote.',                     'ACESSORIO', 'itens/003.png', 'Nível Apoiador'),
('Coroa de gotas',         'Coroa feita de gotas.',                               'ACESSORIO', 'itens/004.png', 'Nível Doador'),
('Colar de rubi',          'Colar com pedra vermelha.',                           'ACESSORIO', 'itens/005.png', 'Conquista Primeira Doação'),
('Capacete de explorador', 'Proteção para longas expedições.',                    'ACESSORIO', 'itens/006.png', 'Nível Guardião da Vida'),
('Bandana de herói',       'Bandana para os mais dedicados.',                     'ACESSORIO', 'itens/007.png', 'Missão Doação confirmada'),
('Lenço de seda',          'Lenço leve de cor vinho.',                            'ACESSORIO', 'itens/008.png', 'Missão Visitar um local'),
('Monóculo dourado',       'Acessório raro de colecionador.',                     'ACESSORIO', 'itens/009.png', 'Nível Herói da Vida'),
('Pulseira de contagem',   'Marca os dias de doação.',                            'ACESSORIO', 'itens/010.png', 'Missão Mantenha o ritmo'),
('Cor roxa',               'Cor roxa para o mascote.',                            'COR',       'itens/011.png', 'Nível Iniciante'),
('Cor dourada',            'Cor dourada para o mascote.',                         'COR',       'itens/012.png', 'Nível Doador'),
('Cor verde',              'Cor verde para o mascote.',                           'COR',       'itens/013.png', 'Nível Apoiador'),
('Cor azul-noite',         'Cor azul escura, para os constantes.',                'COR',       'itens/014.png', 'Conquista Doador Constante'),
('Cor prata',              'Cor prata para o mascote.',                           'COR',       'itens/015.png', 'Nível Guardião da Vida'),
('Capa vermelha',          'Capa clássica de herói.',                             'CAPA',      'itens/016.png', 'Nível Apoiador'),
('Capa de sombra',         'Capa escura, de quem enfrentou o estoque crítico.',   'CAPA',      'itens/017.png', 'Conquista Herói do Estoque'),
('Capa estelar',           'Capa com estrelas, para eventos.',                    'CAPA',      'itens/018.png', 'Evento Mês do Doador'),
('Capa de festa',          'Capa colorida de celebração.',                        'CAPA',      'itens/019.png', 'Evento Mês do Doador'),
('Asas de gota',           'Asas translúcidas.',                                  'EFEITO',    'itens/020.png', 'Nível Herói da Vida'),
('Brilho ao doar',         'Efeito de brilho após doação confirmada.',            'EFEITO',    'itens/021.png', 'Missão Doação confirmada'),
('Rastro vermelho',        'Rastro de luz ao mover o mascote.',                   'EFEITO',    'itens/022.png', 'Conquista Doador Constante'),
('Chapéu de mago',         'Chapéu pontudo clássico.',                            'CHAPEU',    'itens/023.png', 'Nível Doador'),
('Chapéu de festa',        'Chapéu para celebrar o evento.',                      'CHAPEU',    'itens/024.png', 'Evento Mês do Doador'),
('Coroa dourada',          'Coroa para o nível máximo.',                          'CHAPEU',    'itens/025.png', 'Nível Herói da Vida');

-- 8. Usuário de teste -> id 1
-- Necessário porque notificacao.id_usuario é obrigatório.
-- A senha é um texto de exemplo, não um hash BCrypt real. Login ainda não existe nesta entrega.
-- Para testar cadastro de verdade, use POST /usuarios no Postman.
INSERT INTO usuario (tipo_usuario, nome, email, senha, data_nasc, telefone, pontuacao, mascote_cor, mascote_acessorios, id_tipo_sanguineo, id_nivel) VALUES
('DOADOR', 'Usuário de Teste', 'teste@hemoplay.exemplo', 'SENHA_DE_EXEMPLO_NAO_USAR', '1995-05-20', '(00) 90000-0000', 240, 'vermelho', NULL, 7, 2);

-- Usuários que podem confirmar doações (RF05): id 2 = Administrador, id 3 = Responsável pelo Hemocentro.
-- Use um deles no PATCH /doacoes/{id}/confirmar, com o JSON { "idUsuarioConfirmacao": 2 }
INSERT INTO usuario (tipo_usuario, nome, email, senha, pontuacao, id_nivel) VALUES
('ADMINISTRADOR', 'Admin de Teste', 'admin@hemoplay.exemplo', 'SENHA_DE_EXEMPLO_NAO_USAR', 0, 1),
('RESPONSAVEL_HEMOCENTRO', 'Responsável de Teste', 'responsavel@hemoplay.exemplo', 'SENHA_DE_EXEMPLO_NAO_USAR', 0, 1);

-- 9. Notificações (RF13) -> 6 registros, uma de cada tipo previsto
INSERT INTO notificacao (id_usuario, titulo, mensagem, tipo, data_envio, lida) VALUES
(1, 'Nova campanha disponível',  'A campanha do tipo O negativo está ativa.',                         'CAMPANHA',   '2026-11-07 09:00:00', FALSE),
(1, 'Nova missão',               'Você tem uma nova missão: Doar em campanha.',                       'MISSAO',     '2026-11-07 09:05:00', FALSE),
(1, 'Conquista desbloqueada',    'Você desbloqueou Primeiro Passo.',                                  'CONQUISTA',  '2026-11-06 18:00:00', TRUE),
(1, 'Recompensa disponível',     'Você ganhou 20 pontos pela missão Completar o cadastro.',           'RECOMPENSA', '2026-11-06 18:10:00', TRUE),
(1, 'Evento começou',            'O Mês do Doador começou. Veja as missões especiais.',               'EVENTO',     '2026-11-01 08:00:00', FALSE),
(1, 'Mascote evoluiu',           'Seu mascote subiu para o visual de Apoiador.',                      'MASCOTE',    '2026-11-06 18:20:00', TRUE);


