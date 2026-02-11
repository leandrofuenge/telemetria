-- =========================================
-- CRIAÇÃO DO BANCO (opcional)
-- =========================================
-- CREATE DATABASE telemetria;
-- USE telemetria;

-- =========================================
-- TABELA: clientes
-- =========================================
CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_razao_social VARCHAR(255),
    cnpj VARCHAR(20)
) ENGINE=InnoDB;

-- =========================================
-- TABELA: cargas
-- =========================================
CREATE TABLE cargas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255),
    peso DOUBLE,
    cliente_id BIGINT,
    CONSTRAINT fk_carga_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id)
        ON DELETE SET NULL
) ENGINE=InnoDB;

-- =========================================
-- TABELA: motoristas
-- =========================================
CREATE TABLE motoristas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    cnh VARCHAR(50),
    categoria_cnh VARCHAR(10)
) ENGINE=InnoDB;

-- =========================================
-- TABELA: veiculos
-- =========================================
CREATE TABLE veiculos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    modelo VARCHAR(255),
    capacidade_carga DOUBLE
) ENGINE=InnoDB;

-- =========================================
-- TABELA: manutencoes
-- =========================================
CREATE TABLE manutencoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    veiculo_id BIGINT,
    data_manutencao DATE,
    descricao VARCHAR(255),
    custo DOUBLE,
    tipo VARCHAR(50),
    CONSTRAINT fk_manutencao_veiculo
        FOREIGN KEY (veiculo_id)
        REFERENCES veiculos(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- =========================================
-- TABELA: usuarios
-- =========================================
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    ativo BOOLEAN DEFAULT TRUE,
    perfil VARCHAR(50),
    ultimo_acesso DATETIME
) ENGINE=InnoDB;

-- =========================================
-- TABELA: viagens
-- =========================================
CREATE TABLE viagens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    veiculo_id BIGINT,
    motorista_id BIGINT,
    carga_id BIGINT,
    data_saida DATETIME,
    data_chegada_prevista DATETIME,
    status VARCHAR(50),

    CONSTRAINT fk_viagem_veiculo
        FOREIGN KEY (veiculo_id)
        REFERENCES veiculos(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_viagem_motorista
        FOREIGN KEY (motorista_id)
        REFERENCES motoristas(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_viagem_carga
        FOREIGN KEY (carga_id)
        REFERENCES cargas(id)
        ON DELETE SET NULL
) ENGINE=InnoDB;

-- =========================================
-- TABELA: telemetria
-- =========================================
CREATE TABLE telemetria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    veiculo_id BIGINT,

    -- Localização
    latitude DOUBLE,
    longitude DOUBLE,
    velocidade DOUBLE,
    odometro DOUBLE,
    altitude DOUBLE,
    precisao_gps DOUBLE,
    satelites INT,
    direcao DOUBLE,
    aceleracao DOUBLE,
    inclinacao DOUBLE,

    -- Sensores
    ignicao BOOLEAN,
    nivel_combustivel DOUBLE,
    temperatura_motor DOUBLE,
    pressao_oleo DOUBLE,
    consumo_combustivel DOUBLE,
    tensao_bateria DOUBLE,
    carga_motor DOUBLE,
    torque_motor DOUBLE,

    -- Comportamento
    rpm DOUBLE,
    frenagem_brusca BOOLEAN,
    numero_frenagens INT,
    numero_aceleracoes_bruscas INT,
    pontuacao_motorista INT,
    tempo_motor_ligado INT,
    tempo_ocioso INT,

    -- Eventos
    colisao_detectada BOOLEAN,
    excesso_velocidade BOOLEAN,
    geofence_violada BOOLEAN,
    cinto_seguranca BOOLEAN,
    porta_aberta BOOLEAN,

    -- Ambiente
    temperatura_externa DOUBLE,
    umidade_externa DOUBLE,
    chuva_detectada BOOLEAN,

    -- Dispositivo
    sinal_gsm DOUBLE,
    sinal_gps DOUBLE,
    firmware_versao VARCHAR(100),
    imei_dispositivo VARCHAR(50),

    -- Manutenção
    manutencao_pendente BOOLEAN,
    proxima_revisao DATETIME,
    horas_motor DOUBLE,
    desgaste_freio DOUBLE,

    data_hora DATETIME,

    CONSTRAINT fk_telemetria_veiculo
        FOREIGN KEY (veiculo_id)
        REFERENCES veiculos(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- =========================================
-- ÍNDICES IMPORTANTES (Performance)
-- =========================================
CREATE INDEX idx_telemetria_veiculo ON telemetria(veiculo_id);
CREATE INDEX idx_telemetria_datahora ON telemetria(data_hora);
CREATE INDEX idx_viagem_status ON viagens(status);
