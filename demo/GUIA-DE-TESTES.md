# Guia de Testes - Papelaria Guará SGC

## Base de Dados
- **Host**: localhost:3306
- **Database**: papelaria_guara (criada automaticamente)
- **Utilizador**: root
- **Password**: (sem password)

---

## Fluxo de Testes Completo

### 1️⃣ **TESTE 1: Criar Utilizador (Registro)**

**Comando:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "senha123"
  }'
```

**Resposta Esperada (HTTP 201):**
```json
{
  "message": "Utilizador registado com sucesso",
  "username": "admin"
}
```

---

### 2️⃣ **TESTE 2: Login e Obter Token JWT**

**Comando:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "senha123"
  }'
```

**Resposta Esperada (HTTP 200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYyNDAwMDAwMCwiZXhwIjoxNjI0MDg2NDAwfQ.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ",
  "username": "admin"
}
```

**⚠️ Guardar o token acima para usar nos próximos testes!**

---

### 3️⃣ **TESTE 3: Criar Cliente (com Token)**

**Comando:**
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYyNDAwMDAwMCwiZXhwIjoxNjI0MDg2NDAwfQ.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "telefone": "912345678",
    "endereco": "Rua A, Nº 123, Lisboa"
  }'
```

**Resposta Esperada (HTTP 201):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "telefone": "912345678",
  "endereco": "Rua A, Nº 123, Lisboa",
  "dataCriacao": "2026-05-20T22:00:00Z"
}
```

---

### 4️⃣ **TESTE 4: Criar Produto**

**Comando:**
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYyNDAwMDAwMCwiZXhwIjoxNjI0MDg2NDAwfQ.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ" \
  -d '{
    "nome": "Caderno 100 folhas",
    "descricao": "Caderno de boa qualidade",
    "preco": 5.99,
    "quantidade": 50
  }'
```

**Resposta Esperada (HTTP 201):**
```json
{
  "id": 1,
  "nome": "Caderno 100 folhas",
  "descricao": "Caderno de boa qualidade",
  "preco": 5.99,
  "quantidade": 50,
  "dataCriacao": "2026-05-20T22:00:00Z"
}
```

---

### 5️⃣ **TESTE 5: Listar Clientes**

**Comando:**
```bash
curl -X GET http://localhost:8080/api/clientes \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYyNDAwMDAwMCwiZXhwIjoxNjI0MDg2NDAwfQ.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
```

**Resposta Esperada (HTTP 200):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@example.com",
    "telefone": "912345678",
    "endereco": "Rua A, Nº 123, Lisboa",
    "dataCriacao": "2026-05-20T22:00:00Z"
  }
]
```

---

### 6️⃣ **TESTE 6: Listar Produtos**

**Comando:**
```bash
curl -X GET http://localhost:8080/api/produtos \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYyNDAwMDAwMCwiZXhwIjoxNjI0MDg2NDAwfQ.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
```

**Resposta Esperada (HTTP 200):**
```json
[
  {
    "id": 1,
    "nome": "Caderno 100 folhas",
    "descricao": "Caderno de boa qualidade",
    "preco": 5.99,
    "quantidade": 50,
    "dataCriacao": "2026-05-20T22:00:00Z"
  }
]
```

---

### 7️⃣ **TESTE 7: Buscar Cliente por ID**

**Comando:**
```bash
curl -X GET http://localhost:8080/api/clientes/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYyNDAwMDAwMCwiZXhwIjoxNjI0MDg2NDAwfQ.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
```

**Resposta Esperada (HTTP 200):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "telefone": "912345678",
  "endereco": "Rua A, Nº 123, Lisboa",
  "dataCriacao": "2026-05-20T22:00:00Z"
}
```

---

### 8️⃣ **TESTE 8: Atualizar Cliente**

**Comando:**
```bash
curl -X PUT http://localhost:8080/api/clientes/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYyNDAwMDAwMCwiZXhwIjoxNjI0MDg2NDAwfQ.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ" \
  -d '{
    "nome": "João Silva Santos",
    "email": "joao.santos@example.com",
    "telefone": "912345679",
    "endereco": "Rua B, Nº 456, Lisboa"
  }'
```

**Resposta Esperada (HTTP 200):**
```json
{
  "id": 1,
  "nome": "João Silva Santos",
  "email": "joao.santos@example.com",
  "telefone": "912345679",
  "endereco": "Rua B, Nº 456, Lisboa",
  "dataCriacao": "2026-05-20T22:00:00Z"
}
```

---

### 9️⃣ **TESTE 9: Apagar Cliente**

**Comando:**
```bash
curl -X DELETE http://localhost:8080/api/clientes/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYyNDAwMDAwMCwiZXhwIjoxNjI0MDg2NDAwfQ.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ"
```

**Resposta Esperada (HTTP 204 - Sem Conteúdo):**
```
(Resposta vazia indica sucesso)
```

---

## ⚠️ Erros Comuns e Respostas

**Erro: Não Autorizado (HTTP 401)**
```json
{
  "error": "Unauthorized",
  "message": "Token inválido ou expirado"
}
```

**Erro: Recurso Não Encontrado (HTTP 404)**
```json
{
  "error": "Not Found",
  "message": "Cliente com ID 999 não encontrado"
}
```

**Erro: Validação (HTTP 400)**
```json
{
  "error": "Bad Request",
  "message": "Nome é obrigatório"
}
```
```

---

## 👥 2. CRUD DE CLIENTES

Use o **token obtido no login** em todos os requests abaixo.

### 2.1️⃣ Criar Cliente

**Endpoint**: `POST http://localhost:8080/clientes`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

**Requisição**:
```json
{
  "nome": "João Silva",
  "cpf": "12345678900",
  "email": "joao@email.com",
  "telefone": "11987654321",
  "endereco": "Rua das Flores, 123, São Paulo"
}
```

**Resposta (201 Created)**:
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "email": "joao@email.com",
  "telefone": "11987654321",
  "endereco": "Rua das Flores, 123, São Paulo"
}
```

**Erro esperado - CPF duplicado (400 Bad Request)**:
```json
{
  "erro": "CPF já cadastrado na base de dados"
}
```

**Erro esperado - Email inválido (400 Bad Request)**:
```json
{
  "email": "Email inválido"
}
```

---

### 2.2️⃣ Listar Todos os Clientes

**Endpoint**: `GET http://localhost:8080/clientes`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Resposta (200 OK)**:
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "cpf": "12345678900",
    "email": "joao@email.com",
    "telefone": "11987654321",
    "endereco": "Rua das Flores, 123, São Paulo"
  }
]
```

---

### 2.3️⃣ Obter Cliente por ID

**Endpoint**: `GET http://localhost:8080/clientes/1`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Resposta (200 OK)**:
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "email": "joao@email.com",
  "telefone": "11987654321",
  "endereco": "Rua das Flores, 123, São Paulo"
}
```

**Erro esperado - Não encontrado (404 Not Found)**:
```json
{
  "erro": "Cliente não encontrado"
}
```

---

### 2.4️⃣ Atualizar Cliente

**Endpoint**: `PUT http://localhost:8080/clientes/1`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

**Requisição**:
```json
{
  "nome": "João Pedro Silva",
  "cpf": "12345678900",
  "email": "joao.new@email.com",
  "telefone": "11988888888",
  "endereco": "Rua Nova, 456"
}
```

**Resposta (200 OK)**: Cliente atualizado

---

### 2.5️⃣ Deletar Cliente

**Endpoint**: `DELETE http://localhost:8080/clientes/1`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

**Resposta (204 No Content)**: (Sem corpo, apenas status)

---

## 📦 3. CRUD DE PRODUTOS

### 3.1️⃣ Criar Produto

**Endpoint**: `POST http://localhost:8080/produtos`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

**Requisição**:
```json
{
  "nome": "Papel A4 Branco",
  "descricao": "Papel sulfite 75g/m², pacote com 500 folhas",
  "preco": 25.50,
  "quantidadeEstoque": 100
}
```

**Resposta (201 Created)**:
```json
{
  "id": 1,
  "nome": "Papel A4 Branco",
  "descricao": "Papel sulfite 75g/m², pacote com 500 folhas",
  "preco": 25.50,
  "quantidadeEstoque": 100
}
```

**Erro esperado - Preço negativo (400 Bad Request)**:
```json
{
  "preco": "Preço não pode ser negativo"
}
```

---

### 3.2️⃣ Listar Todos os Produtos

**Endpoint**: `GET http://localhost:8080/produtos`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

---

### 3.3️⃣ Obter Produto por ID

**Endpoint**: `GET http://localhost:8080/produtos/1`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

---

### 3.4️⃣ Atualizar Produto

**Endpoint**: `PUT http://localhost:8080/produtos/1`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

**Requisição**: (Similar ao criar)

---

### 3.5️⃣ Deletar Produto

**Endpoint**: `DELETE http://localhost:8080/produtos/1`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...
```

---

## 🔒 4. ERROS DE AUTENTICAÇÃO

### Sem Token

**Endpoint**: `GET http://localhost:8080/clientes`

**Resposta (403 Forbidden)**: 
```
Access Denied
```

---

### Token Expirado ou Inválido

**Endpoint**: `GET http://localhost:8080/clientes`

**Headers**:
```
Authorization: Bearer TOKEN_INVALIDO_OU_EXPIRADO
```

**Resposta (403 Forbidden)**: 
```
Access Denied
```

---

## 📋 5. VALIDAÇÕES DE ENTRADA (DTO)

### Cliente - Campos Obrigatórios

| Campo | Tipo | Validação | Exemplo |
|-------|------|-----------|---------|
| `nome` | String | @NotBlank | "João Silva" |
| `cpf` | String | @NotBlank, único | "12345678900" |
| `email` | String | @Email, @NotBlank | "joao@email.com" |
| `telefone` | String | @NotBlank | "11987654321" |
| `endereco` | String | @NotBlank | "Rua A, 123" |

### Produto - Campos Obrigatórios

| Campo | Tipo | Validação | Exemplo |
|-------|------|-----------|---------|
| `nome` | String | @NotBlank | "Papel A4" |
| `descricao` | String | Opcional | "Papel sulfite" |
| `preco` | Double | @PositiveOrZero | 25.50 |
| `quantidadeEstoque` | Integer | @PositiveOrZero | 100 |

---

## 🛠️ 6. USANDO COM POSTMAN/INSOMNIA

### Salvar Token como Variável (Automático)

No Postman, após fazer login, você pode:

1. Na aba **Tests** do endpoint `/auth/login`, adicione:
```javascript
var jsonData = pm.response.json();
pm.environment.set("token", jsonData.token);
```

2. Depois, em outros endpoints, use no header:
```
Authorization: Bearer {{token}}
```

---

## ✅ Checklist de Testes

- [ ] Login com usuário válido → Token recebido
- [ ] Login com credenciais inválidas → 401
- [ ] Criar cliente com dados válidos → 201
- [ ] Criar cliente com CPF duplicado → 400 (BusinessException)
- [ ] Criar cliente sem token → 403
- [ ] Listar clientes com token → 200
- [ ] Obter cliente por ID existente → 200
- [ ] Obter cliente por ID inexistente → 404
- [ ] Atualizar cliente → 200
- [ ] Deletar cliente → 204
- [ ] Criar produto com preço válido → 201
- [ ] Criar produto com preço negativo → 400
- [ ] Listar produtos → 200
- [ ] Email inválido em cliente → 400

---

## 📱 Exemplo com cURL (Terminal)

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","senha":"123456"}' | jq -r '.token')

echo "Token: $TOKEN"

# 2. Criar cliente
curl -X POST http://localhost:8080/clientes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome":"João",
    "cpf":"12345678900",
    "email":"joao@email.com",
    "telefone":"11987654321",
    "endereco":"Rua A"
  }'

# 3. Listar clientes
curl -X GET http://localhost:8080/clientes \
  -H "Authorization: Bearer $TOKEN"
```

---

**Status**: ✅ Todos os testes devem passar sem erros  
**Última atualização**: 20 de maio de 2026
