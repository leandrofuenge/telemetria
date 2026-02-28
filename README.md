# 🚛 Fleet Telemetry System

![Java](https://img.shields.io/badge/Java-17%2B-blue) ![Spring
Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Kafka](https://img.shields.io/badge/Streaming-Kafka-black)
![Redis](https://img.shields.io/badge/Cache-Redis-red)
![Docker](https://img.shields.io/badge/Container-Docker-blue)
![Status](https://img.shields.io/badge/Status-MVP%20Robusto-success)

------------------------------------------------------------------------

## 📌 Overview

Sistema de monitoramento em tempo real para frotas de caminhões e
carretas com foco em:

-   📡 Telemetria em tempo real\
-   📍 Rastreamento GPS\
-   🛣 Gestão inteligente de rotas\
-   👨‍✈️ Gestão de motoristas\
-   🔧 Manutenção preditiva\
-   📊 Análise de desempenho\
-   🌐 Operação resiliente em baixa conectividade

------------------------------------------------------------------------

## 🏗 Arquitetura

            Dispositivos IoT / GPS
                      │
                      ▼
              API Gateway (Spring Cloud)
                      │
                      ▼
            Microserviços (Spring WebFlux)
                      │
                      ▼
             Event Streaming (Kafka)
                      │
            ┌─────────┴─────────┐
            ▼                   ▼
      Processadores        Cache (Redis)
            │                   │
            ▼                   │
    Banco Relacional       Resposta Rápida
     (MySQL)                    │
            │                   ▼
            └──────► TimescaleDB (Séries Temporais)
                               │
                               ▼
                        Frontend (React + Mapas)

------------------------------------------------------------------------

## 🔧 Tech Stack

### Backend

-   Java 17+
-   Spring Boot
-   Spring WebFlux (Reativo)
-   Spring Data JPA
-   JWT + RBAC
-   WebSocket/STOMP
-   Quartz Scheduler
-   Rate Limiting

### Banco de Dados

-   MySQL (dados relacionais)
-   TimescaleDB (séries temporais)
-   Redis (cache + pub/sub)

### Mensageria

-   Apache Kafka
-   RabbitMQ (alternativo)

### Observabilidade

-   Prometheus
-   Grafana
-   ELK Stack
-   Spring Actuator

### Infraestrutura

-   Docker
-   Docker Compose

------------------------------------------------------------------------

## 🚀 Principais Funcionalidades

### 📡 Telemetria em Tempo Real

-   Atualização instantânea via WebSocket
-   Processamento de sensores
-   Persistência otimizada para séries temporais
-   API IoT dedicada

### 🛣 Gestão de Rotas

-   Planejamento de rotas
-   Estimativa de chegada (ETA)
-   Detecção automática de desvios
-   Alertas inteligentes

### 🔐 Segurança

-   JWT com refresh token
-   RBAC (ADMIN, GESTOR, OPERADOR, MOTORISTA)
-   MFA para administradores
-   Auditoria completa
-   Criptografia em trânsito e repouso

### 🔥 Funcionalidades Avançadas

-   Roteamento inteligente (peso, pedágios, trânsito)
-   Monitoramento de carga (temperatura, umidade, impacto)
-   Comunicação motorista ↔ gestor
-   Controle de jornada (conformidade legal)

------------------------------------------------------------------------

## 🛡 Resiliência e Performance

Implementado para operar em ambientes adversos:

-   Buffer local offline com sincronização posterior
-   Compressão de dados
-   Retry com backoff exponencial
-   Redução adaptativa de frequência
-   Priorização de eventos críticos
-   Backpressure
-   Cache warming
-   Rate limiting

------------------------------------------------------------------------

## 📊 KPIs (Em evolução)

-   Eficiência da frota
-   Consumo médio
-   Ociosidade
-   Custo por veículo
-   Alertas de manutenção
-   Excesso de velocidade

------------------------------------------------------------------------

## 🧪 Ambiente de Teste

-   Simulador de GPS
-   Rotas entre capitais brasileiras
-   Eventos simulados
-   Cargas e consumo simulados

------------------------------------------------------------------------

## 🗺 Roadmap

### ✅ Fase 1 -- MVP

-   Telemetria básica
-   Rastreamento GPS
-   Persistência em banco
-   Autenticação JWT

### 🔄 Fase 2 -- Escala e Performance

-   Integração Kafka
-   Cache Redis
-   WebSocket tempo real
-   Observabilidade completa

### 🚧 Fase 3 -- Inteligência

-   Manutenção preditiva
-   Algoritmo de roteamento inteligente
-   Análise comportamental de motoristas

### 🎯 Fase 4 -- Expansão

-   Machine Learning
-   Integração com ERPs
-   Multi-tenant
-   Internacionalização LATAM

------------------------------------------------------------------------

## 🏆 Diferenciais

-   🌎 Foco Brasil / LATAM
-   📡 Offline-first
-   ⚡ Arquitetura preparada para alta escala
-   💰 Compatível com dispositivos GPS de baixo custo
-   🎯 Interface simplificada

------------------------------------------------------------------------

## 📌 Status Atual

✔ MVP funcional\
✔ MVP robusto\
✔ Arquitetura escalável\
✔ Preparado para crescimento


## Relatório Técnico – Sistema de Telemetria para Frotas de Caminhões

Este projeto tem como objetivo propor e implementar uma arquitetura escalável para um sistema de telemetria voltado a empresas de transporte rodoviário, permitindo o monitoramento eficiente de grandes volumes de dados provenientes de dispositivos embarcados em caminhões.

### 1. Processamento Concorrente

Foi implementado um modelo baseado em **threads**, possibilitando a execução paralela de múltiplas tarefas dentro do mesmo processo. Essa abordagem melhora significativamente:

- O desempenho geral do sistema
    
- A responsividade das requisições
    
- A eficiência no uso de recursos computacionais
    

### 2. Processamento Assíncrono e Fila de Execução

Também foi adotado o **processamento assíncrono** para execução de tarefas de longa duração, como o tratamento e análise dos dados telemétricos enviados pelos dispositivos embarcados.

Considerando um cenário com até 100.000 caminhões transmitindo dados simultaneamente, a arquitetura foi projetada para evitar sobrecarga imediata do sistema. Para isso, foi implementado um mecanismo de fila de processamento, permitindo o desacoplamento entre a ingestão dos dados e seu processamento efetivo.

Essa estratégia evita gargalos e mantém a estabilidade mesmo sob alta taxa de eventos.

### 3. Streaming de Eventos

Para suportar alto volume de dados em tempo real, foi integrado o Apache Kafka como plataforma de streaming distribuído.

O uso do Kafka permite:

- Alta taxa de throughput (milhões de eventos)
    
- Escalabilidade horizontal
    
- Persistência confiável dos eventos
    
- Processamento paralelo por meio de particionamento
    

Essa abordagem é adequada para sistemas de telemetria que exigem ingestão contínua e processamento distribuído.

### 4. Cache em Memória

Foi implementado o Redis como mecanismo de cache em memória.

Em funções de pré-processamento, como `preprocessarDadosUrbanos`, o sistema consulta primeiramente o Redis antes de acessar o banco de dados relacional. Isso reduz:

- Latência de resposta
    
- Carga no banco de dados
    
- Reprocessamento desnecessário
    

Essa estratégia melhora significativamente o desempenho em cenários de alta leitura.

### 5. Controle de Fluxo (Rate Limiting)

Foi implementado um mecanismo de **rate limiting**, responsável por controlar a quantidade de requisições ou eventos permitidos dentro de um intervalo de tempo específico.

Esse mecanismo garante:

- Proteção contra sobrecarga
    
- Estabilidade operacional
    
- Controle de fluxo
    
- Isolamento de falhas causadas por dispositivos com comportamento anômalo
