# 🚛 Sistema De Monitoramento De Frotas

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

------------------------------------------------------------------------

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


# 📘 Relatório Técnico

## Sistema de Telemetria para Frotas de Caminhões com Integração ao OSRM

---

# 1️⃣ Visão Geral do OSRM

O **OSRM (Open Source Routing Machine)** é um motor de cálculo de rotas open-source baseado nos dados do:

➡ OpenStreetMap

Ele fornece funcionalidades similares a serviços como Google Maps e Mapbox, porém com a vantagem de ser **self-hosted** e sem limites comerciais quando executado em infraestrutura própria.

### Principais funcionalidades

- ✔ Cálculo da melhor rota
    
- ✔ Distância total
    
- ✔ Tempo estimado (ETA)
    
- ✔ Geometria detalhada da via (GeoJSON)
    
- ✔ Snap-to-road
    
- ✔ Map Matching
    
- ✔ Matriz de distâncias (table)
    

---

# 2️⃣ Funcionamento Interno

O fluxo de processamento do OSRM ocorre em seis etapas principais:

1. Download do mapa em formato `.osm.pbf`
    
2. Execução do `osrm-extract`
    
3. Aplicação de perfil de roteamento (ex: `car.lua`)
    
4. Execução do `osrm-contract` (Hierarquia de Contração - CH)
    
5. Inicialização do servidor (`osrm-routed`)
    
6. Consumo via API HTTP
    

### Pipeline técnico

OSM (.pbf)  
   ↓  
Extract  
   ↓  
Contract (CH)  
   ↓  
Servidor HTTP  
   ↓  
Backend Telemetria

---

# 3️⃣ Exemplo de Chamada HTTP

### Endpoint:

GET /route/v1/driving/lon1,lat1;lon2,lat2

### Exemplo público:

https://router.project-osrm.org/route/v1/driving/-56.0974,-15.6014;-56.1200,-15.6500?overview=full&geometries=geojson

### Resposta simplificada:

{  
  "routes": [  
    {  
      "distance": 12450.3,  
      "duration": 845.2,  
      "geometry": {  
        "coordinates": [  
          [-56.0974, -15.6014],  
          [-56.0980, -15.6020]  
        ]  
      }  
    }  
  ]  
}

---

# 4️⃣ Aplicação no Sistema de Telemetria

Integração direta com o serviço de detecção de desvio de rota.

## Problema tradicional

Sem motor de roteamento:

- Comparação por linha reta
    
- Falsos positivos de desvio
    
- GPS impreciso
    
- Dificuldade em calcular ETA real
    

## Com OSRM integrado

✔ Rota real baseada na malha viária  
✔ Snap do veículo à via correta  
✔ Cálculo preciso de ETA  
✔ Detecção real de desvio  
✔ Correção de ruído de GPS

---

# 5️⃣ Principais Modos de Operação

## 🔹 1. `route`

Calcula rota entre dois ou mais pontos.

Uso principal:

- ETA
    
- Planejamento
    
- Visualização de trajeto
    

---

## 🔹 2. `nearest`

Retorna a via mais próxima de uma coordenada.

Uso principal:

- Snap-to-road
    
- Correção de erro de GPS
    

---

## 🔹 3. `match` (Map Matching)

Endpoint:

/match/v1/driving/lon1,lat1;lon2,lat2;lon3,lat3

Função:

- Ajusta sequência de pontos GPS na via correta
    

Aplicação crítica para telemetria:

✔ Corrige imprecisão de GPS  
✔ Evita falsos alertas de desvio  
✔ Reconstrói trajetória real  
✔ Base para auditoria de percurso

Esse é o modo mais poderoso para frotas.

---

# 6️⃣ Arquitetura Recomendada

### Estrutura modular

[Dispositivos GPS]  
        ↓  
[Telemetria Service]  
        ↓  
[Roteamento Service]  
        ↓  
[OSRM]

Separar o roteamento em microserviço permite:

- Escalabilidade independente
    
- Cache dedicado
    
- Controle de carga
    
- Evolução futura (ex: perfis caminhão pesado)
    

---

# 7️⃣ Requisitos de Infraestrutura

O OSRM carrega em memória:

- Grafo da malha viária
    
- Índices espaciais (R-tree)
    
- Hierarquia de contração
    

### RAM impacta diretamente:

- Latência
    
- Throughput
    
- Estabilidade
    

### Porém, RAM não é suficiente

Também é necessário:

- CPU multi-core (8–32 cores ideal)
    
- SSD NVMe
    
- Backend assíncrono
    
- Pool de conexões HTTP otimizado
    
- Estratégia de cache
    

---

# 8️⃣ Escalabilidade

Para suportar ~1000 veículos simultâneos:

Recomenda-se:

- Cache Redis para rotas repetidas
    
- Rate limiting interno
    
- Monitoramento (CPU/RAM)
    
- Instâncias paralelas do OSRM
    
- Balanceador de carga
    

---

# 9️⃣ Conclusão Técnica

A integração do OSRM transforma o sistema de telemetria de:

> Rastreamento básico

Para:

> Plataforma inteligente de análise logística

Ele possibilita:

- Monitoramento avançado
    
- Detecção precisa de desvio
    
- Cálculo real de desempenho operacional
    
- Base tecnológica para expansão comercial


