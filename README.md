markdown
# Reprocessador de Eventos

Projeto em Kotlin/Java utilizando Spring Boot, Spring Batch, MongoDB e Kafka para reprocessamento de eventos.

## Visão Geral

Este projeto realiza o reprocessamento de eventos armazenados em uma collection MongoDB (EventA), enviando-os para um tópico Kafka após limpeza de dados relacionados em outra collection (EventB).

## Tecnologias Utilizadas

- Kotlin / Java
- Spring Boot
- Spring Batch
- MongoDB
- Kafka
- Gradle

## Estrutura do Projeto

- `ReprocessBatchConfig`: Configuração dos jobs e steps do Spring Batch.
- `EventA`, `EventB`: Collections do MongoDB.
- Repositórios para acesso aos dados do MongoDB.
- KafkaTemplate para envio de eventos.

## Como Funciona

1. **Limpeza da Collection B:** Remove todos os registros de `EventB` relacionados ao `idOperation` informado.
2. **Reprocessamento de Eventos:** Lê eventos de `EventA` filtrando por `idOperation` e intervalo de datas, processa e envia para um tópico Kafka.

## Configuração

### Pré-requisitos

- Java 17+
- Docker (para MongoDB e Kafka, se necessário)
- Gradle

### Variáveis de Ambiente

Configure as variáveis de ambiente ou `application.yml` para:

- Conexão com MongoDB
- Configuração do Kafka

Exemplo de `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/seubanco
  kafka:
    bootstrap-servers: localhost:9092