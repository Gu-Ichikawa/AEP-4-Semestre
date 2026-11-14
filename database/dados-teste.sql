USE hemoplay;

INSERT INTO local_doacao (nome, endereco, cidade, estado, telefone, horario_funcionamento) VALUES
('Hemocentro de Exemplo Centro', 'Rua Exemplo, 100', 'Cidade Exemplo', 'SP', '(00) 0000-0001', 'Seg a sex, 7h às 17h'),
('Hemocentro de Exemplo Zona Sul', 'Av. Modelo, 2000', 'Cidade Exemplo', 'SP', '(00) 0000-0002', 'Seg a sáb, 8h às 14h'),
('Hemocentro de Exemplo Norte', 'Rua Teste, 55', 'Outra Cidade Exemplo', 'PR', '(00) 0000-0003', 'Ter a sáb, 7h às 13h'),
('Hemocentro de Exemplo Leste', 'Praça Fictícia, 10', 'Outra Cidade Exemplo', 'PR', '(00) 0000-0004', 'Seg a sex, 9h às 18h');

INSERT INTO evento (nome, descricao, data_inicio, data_fim, tipo, ativo) VALUES
('Mês do Doador (exemplo)', 'Evento sazonal com missões e recompensas extras.', '2026-11-01', '2026-11-30', 'SAZONAL', TRUE);

INSERT INTO campanha (nome, descricao, data_inicio, data_fim, status, id_local, id_tipo_sanguineo) VALUES
('Estoque crítico do tipo O negativo (exemplo)', 'Doação prioritária para o tipo O negativo.', '2026-11-01', '2026-11-30', 'ATIVA', 1, 8),
('Doe no Norte (exemplo)', 'Campanha de coleta na unidade norte.', '2026-11-10', '2026-12-10', 'PLANEJADA', 3, NULL);

INSERT INTO necessidade_sanguinea (id_tipo_sanguineo, status, quantidade_simulada, data_atualizacao) VALUES
(1, 'ADEQUADO', 120, '2026-11-07 09:00:00'),
(2, 'ADEQUADO', 45,  '2026-11-07 09:00:00'),
(3, 'ATENCAO',  30,  '2026-11-07 09:00:00'),
(4, 'ADEQUADO', 40,  '2026-11-07 09:00:00'),
(5, 'ADEQUADO', 60,  '2026-11-07 09:00:00'),
(6, 'ATENCAO',  12,  '2026-11-07 09:00:00'),
(7, 'ADEQUADO', 110, '2026-11-07 09:00:00'),
(8, 'CRITICO',  8,   '2026-11-07 09:00:00');

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

INSERT INTO conquista (nome, descricao, criterio, recompensa) VALUES
('Primeiro Passo',    'Completou o cadastro.',                          'Preencher o cadastro com tipo sanguíneo',   'Laço vermelho'),
('Primeira Doação',   'Registrou a primeira doação confirmada.',        'Ter 1 doação com status CONFIRMADA',        'Colar de rubi'),
('Doador Constante',  'Doou três vezes.',                               'Ter 3 doações confirmadas',                 'Cor azul-noite e Pulseira de contagem'),
('Herói do Estoque',  'Doou para um tipo em estado crítico.',           'Doar em campanha com estoque crítico',      NULL),
('Mascote Vestido',   'Personalizou o mascote.',                        'Equipar um acessório no mascote',           'Gravata borboleta');

INSERT INTO item (nome, descricao, tipo, imagem, requisito_desbloqueio) VALUES
('Cor vermelha',           'A cor original do mascote.',                          'COR',       'img/mascote/cor-vermelha.jpg',   'Nível Iniciante'),
('Cor roxa',               'Para quem completou o cadastro.',                     'COR',       'img/mascote/cor-roxa.jpg',       'Missão Completar o cadastro'),
('Cor verde',              'Cor verde para o mascote.',                           'COR',       'img/mascote/cor-verde.jpg',      'Nível Apoiador'),
('Cor azul-noite',         'Cor azul escura, para os constantes.',                'COR',       'img/mascote/cor-azul-noite.jpg', 'Conquista Doador Constante'),
('Laço vermelho',          'Laço clássico para o mascote.',                       'ACESSORIO', 'img/mascote/laco.jpg',           'Conquista Primeiro Passo'),
('Óculos redondos',        'Para quem estudou os requisitos antes de doar.',      'ACESSORIO', 'img/mascote/oculos.jpg',         'Missão Conhecer os requisitos'),
('Capacete de explorador', 'Para quem registrou a primeira doação.',              'ACESSORIO', 'img/mascote/capacete.jpg',       'Missão Registrar uma doação'),
('Colar de rubi',          'Colar com uma gota de rubi.',                         'ACESSORIO', 'img/mascote/colar.jpg',          'Conquista Primeira Doação'),
('Lenço de seda',          'Lenço leve de cor vinho.',                            'ACESSORIO', 'img/mascote/lenco.jpg',          'Nível Doador'),
('Gravata borboleta',      'Visual elegante para o mascote.',                     'ACESSORIO', 'img/mascote/gravata.jpg',        'Conquista Mascote Vestido'),
('Bandana de herói',       'Edição especial do Mês do Doador.',                   'ACESSORIO', 'img/mascote/bandana.jpg',        'Evento Mês do Doador (exemplo)'),
('Pulseira de contagem',   'Marca as doações já feitas.',                         'ACESSORIO', 'img/mascote/pulseira.jpg',       'Conquista Doador Constante'),
('Coroa de gotas',         'Coroa para os guardiões da vida.',                    'ACESSORIO', 'img/mascote/coroa.jpg',          'Nível Guardião da Vida'),
('Monóculo dourado',       'Acessório raro, só para heróis.',                     'ACESSORIO', 'img/mascote/monoculo.jpg',       'Nível Herói da Vida');

INSERT INTO usuario (tipo_usuario, nome, email, senha, data_nasc, telefone, pontuacao, mascote_cor, mascote_acessorios, id_tipo_sanguineo, id_nivel) VALUES
('DOADOR', 'Usuário de Teste', 'teste@hemoplay.exemplo', 'SENHA_DE_EXEMPLO_NAO_USAR', '1995-05-20', '(00) 90000-0000', 240, 'Cor vermelha', NULL, 7, 2);

INSERT INTO usuario (tipo_usuario, nome, email, senha, pontuacao, id_nivel) VALUES
('ADMINISTRADOR', 'Admin de Teste', 'admin@hemoplay.exemplo', 'SENHA_DE_EXEMPLO_NAO_USAR', 0, 1),
('RESPONSAVEL_HEMOCENTRO', 'Responsável de Teste', 'responsavel@hemoplay.exemplo', 'SENHA_DE_EXEMPLO_NAO_USAR', 0, 1);

INSERT INTO notificacao (id_usuario, titulo, mensagem, tipo, data_envio, lida) VALUES
(1, 'Nova campanha disponível',  'A campanha do tipo O negativo está ativa.',                         'CAMPANHA',   '2026-11-07 09:00:00', FALSE),
(1, 'Nova missão',               'Você tem uma nova missão: Doar em campanha.',                       'MISSAO',     '2026-11-07 09:05:00', FALSE),
(1, 'Conquista desbloqueada',    'Você desbloqueou Primeiro Passo.',                                  'CONQUISTA',  '2026-11-06 18:00:00', TRUE),
(1, 'Recompensa disponível',     'Você ganhou 20 pontos pela missão Completar o cadastro.',           'RECOMPENSA', '2026-11-06 18:10:00', TRUE),
(1, 'Evento começou',            'O Mês do Doador começou. Veja as missões especiais.',               'EVENTO',     '2026-11-01 08:00:00', FALSE),
(1, 'Mascote evoluiu',           'Seu mascote subiu para o visual de Apoiador.',                      'MASCOTE',    '2026-11-06 18:20:00', TRUE);


