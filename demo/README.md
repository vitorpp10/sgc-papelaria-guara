# SGC - Sistema de Gestão Comercial (Papelaria Guará)

## Entrega 3 - Sistema Completo

Este projeto consiste na implementação completa de uma API REST desenvolvida com Spring Boot e um frontend em HTML/JS com Bootstrap. A aplicação possui autenticação JWT, controle de acesso, operações de CRUD para clientes e produtos, **registro de vendas com controle de estoque e relatórios de vendas**, além de tratamento global de exceções.

---

## Rubrica Atendida

### 1. Modelo de Dados
* 5 entidades mapeadas: Cliente, Produto, Usuario, Venda e ItemVenda
* 5 repositórios Spring Data JPA
* Relacionamentos 1:N e N:1 (Venda <-> ItemVenda) e validações de dados configuradas

### 2. Controle de Acesso
* Autenticação via JWT com Spring Security
* Filtro de autenticação customizado na requisição
* Endpoints protegidos de acordo com o perfil do usuário

### 3. API REST
* Endpoints cobrindo os métodos POST, GET, PUT e DELETE
* CRUD funcional para o gerenciamento de clientes e produtos
* Endpoints para registro e login de usuários
* **Endpoints de Vendas (PDV) e Geração de Relatórios por período**

### 4. Tratamento de Erros
* 3 exceções customizadas para regras de negócio e busca
* Manipulador global de exceções com @ControllerAdvice
* Respostas com códigos HTTP e mensagens estruturadas

### 5. Camadas do Sistema
* Camada de controle para exposição dos endpoints
* Camada de serviço para execução das regras de negócio
* Camada de persistência para integração com o banco de dados
* Uso de DTOs para transferência de dados entre as camadas

---

## Stack Tecnológica

* Java 17 ou 21
* Spring Boot 3.x
* Spring Data JPA e Hibernate
* Spring Security
* MySQL 8.0
* JWT (JSON Web Tokens)
* Maven

---

## Estrutura do Projeto

```text
src/main/java/br/com/sgc/
├── DemoApplication.java
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── ClienteController.java
│   └── ProdutoController.java
├── domain/
│   ├── model/
│   │   ├── Cliente.java
│   │   ├── Produto.java
│   │   └── Usuario.java
│   └── repository/
│       ├── ClienteRepository.java
│       ├── ProdutoRepository.java
│       └── UsuarioRepository.java
├── dto/
│   ├── AuthRequestDTO.java
│   ├── ClienteDTO.java
│   └── ProdutoDTO.java
├── exception/
│   ├── BusinessException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── security/
│   └── JwtAuthenticationFilter.java
└── service/
    ├── ClienteService.java
    ├── JwtService.java
    └── ProdutoService.java
```

---

## Como Executar o Projeto

### Pré-requisitos
* Java 17 ou superior instalado
* Maven 3.8 ou superior instalado
* Servidor MySQL rodando na porta 3306

### Passos para Execução

#### 1. Compilar o projeto
```
mvn clean install
```

#### 2. Iniciar a aplicação
```
mvn spring-boot:run
```

#### 3. Endpoint base
```text
http://localhost:8080
```

---

## Endpoints da API

### Autenticação

* `POST /api/auth/register` - cadastrar um novo usuário
* `POST /api/auth/login` - realizar login e retornar o token JWT

### Clientes

* `POST /api/clientes` - cadastrar novo cliente (requer token)
* `GET /api/clientes` - listar todos os clientes (requer token)
* `GET /api/clientes/{id}` - buscar cliente por id (requer token)
* `PUT /api/clientes/{id}` - atualizar dados do cliente (requer token)
* `DELETE /api/clientes/{id}` - remover cliente do sistema (requer token)

### Produtos

* `POST /api/produtos` - cadastrar novo produto (requer token)
* `GET /api/produtos` - listar todos os produtos (requer token)
* `GET /api/produtos/{id}` - buscar produto por id (requer token)
* `PUT /api/produtos/{id}` - atualizar dados do produto (requer token)
* `DELETE /api/produtos/{id}` - remover produto do sistema (requer token)

### Vendas

* `POST /api/vendas` - registrar uma nova venda e abater estoque (requer token)
* `GET /api/vendas` - listar todas as vendas (requer token)
* `GET /api/vendas/relatorio?inicio=YYYY-MM-DD&fim=YYYY-MM-DD` - gerar relatório de vendas em um período (requer token)

---

## Configuração do Banco de Dados

Configuração localizada no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/papelaria_guara?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

O banco de dados e as tabelas são gerados automaticamente pelo Hibernate a partir das entidades mapeadas no código assim que a aplicação é iniciada.

---

## Testando a API via Terminal

### 1. Registrar usuário de teste

```
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"senha123"}'
```

### 2. Realizar o login para pegar o token

```
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"senha123"}'
```

### 3. Cadastrar um cliente utilizando o token obtido

```
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer INSIRA_O_TOKEN_AQUI" \
  -d '{"nome":"João","email":"joao@mail.com","telefone":"912345678","endereco":"Rua A","cpf":"12345678901"}'
```

---

## Validações e Regras Negócio

* Bloqueio de cadastros com CPF duplicado lançando exceção personalizada
* Impedimento de cadastro de produtos com preço menor que zero
* **Controle de Estoque:** Não permite a venda se a quantidade do produto for insuficiente, e abate do estoque automaticamente na finalização da venda.
* Validação de formato de e-mail e verificação de campos obrigatórios via anotações do Jakarta Validation
* Retorno padronizado em JSON para qualquer erro interceptado pelo handler global