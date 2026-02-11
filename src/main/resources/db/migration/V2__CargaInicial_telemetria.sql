-- =========================================
-- DADOS INICIAIS
-- =========================================

-- CLIENTES
INSERT INTO clientes (nome_razao_social, cnpj) VALUES
('TransLog LTDA', '12.345.678/0001-99'),
('Carga Pesada Brasil', '98.765.432/0001-11');

-- CARGAS
INSERT INTO cargas (descricao, peso, cliente_id) VALUES
('Carga de Eletrônicos', 1200.50, 1),
('Carga de Alimentos Refrigerados', 3500.00, 2);

-- MOTORISTAS
INSERT INTO motoristas (nome, cpf, cnh, categoria_cnh) VALUES
('João da Silva', '123.456.789-00', '12345678901', 'E'),
('Carlos Oliveira', '987.654.321-00', '10987654321', 'D');

-- VEÍCULOS
INSERT INTO veiculos (placa, modelo, capacidade_carga) VALUES
('ABC1D23', 'Volvo FH 540', 25000),
('XYZ9K87', 'Scania R450', 22000);

-- MANUTENÇÕES
INSERT INTO manutencoes (veiculo_id, data_manutencao, descricao, custo, tipo) VALUES
(1, '2025-01-10', 'Troca de óleo e filtros', 1500.00, 'Preventiva'),
(2, '2025-01-15', 'Substituição de pastilhas de freio', 2200.00, 'Corretiva');

-- USUÁRIOS
INSERT INTO usuarios (login, senha, nome, email, ativo, perfil, ultimo_acesso) VALUES
('admin', '123456', 'Administrador Geral', 'admin@telemetria.com', TRUE, 'ADMIN', NOW()),
('gestor1', '123456', 'Gestor Operacional', 'gestor@telemetria.com', TRUE, 'GESTOR', NOW());

-- VIAGENS
INSERT INTO viagens (veiculo_id, motorista_id, carga_id, data_saida, data_chegada_prevista, status) VALUES
(1, 1, 1, NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY), 'EM_TRANSITO'),
(2, 2, 2, NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY), 'EM_TRANSITO');

-- TELEMETRIA (dados simulados)
INSERT INTO telemetria (
    veiculo_id,
    latitude,
    longitude,
    velocidade,
    odometro,
    altitude,
    precisao_gps,
    satelites,
    direcao,
    aceleracao,
    inclinacao,
    ignicao,
    nivel_combustivel,
    temperatura_motor,
    pressao_oleo,
    consumo_combustivel,
    tensao_bateria,
    carga_motor,
    torque_motor,
    rpm,
    frenagem_brusca,
    numero_frenagens,
    numero_aceleracoes_bruscas,
    pontuacao_motorista,
    tempo_motor_ligado,
    tempo_ocioso,
    colisao_detectada,
    excesso_velocidade,
    geofence_violada,
    cinto_seguranca,
    porta_aberta,
    temperatura_externa,
    umidade_externa,
    chuva_detectada,
    sinal_gsm,
    sinal_gps,
    firmware_versao,
    imei_dispositivo,
    manutencao_pendente,
    proxima_revisao,
    horas_motor,
    desgaste_freio,
    data_hora
) VALUES
(
    1,
    -23.5505,
    -46.6333,
    80.5,
    150000,
    760,
    1.2,
    12,
    180,
    0.5,
    2.0,
    TRUE,
    65.0,
    90.0,
    4.5,
    2.8,
    24.5,
    70.0,
    1800.0,
    1500,
    FALSE,
    3,
    1,
    95,
    7200,
    600,
    FALSE,
    FALSE,
    FALSE,
    TRUE,
    FALSE,
    28.0,
    60.0,
    FALSE,
    85.0,
    90.0,
    'v1.0.0',
    '359881234567890',
    FALSE,
    DATE_ADD(NOW(), INTERVAL 30 DAY),
    4500,
    20.0,
    NOW()
);
