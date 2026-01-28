# E-Commerce API REST

Uma API REST completa para gerenciamento de e-commerce desenvolvida com **Spring Boot**, **PostgreSQL**, **JPA/Hibernate**, **Spring Security** e **Docker**. O projeto segue padrões REST e boas práticas de desenvolvimento.

## 📋 Descrição do Projeto

Esta é uma solução backend robusta para um sistema de e-commerce que oferece funcionalidades essenciais como:

- **Gerenciamento de Usuários**: Registro, login e controle de acesso
- **Gerenciamento de Produtos**: CRUD completo com busca por nome
- **Gerenciamento de Pedidos**: Criar, atualizar e rastrear pedidos
- **Validação de Dados**: Todas as entradas são validadas com regras de negócio
- **Segurança**: Implementação de Spring Security para proteger endpoints
- **Documentação Automática**: Swagger/OpenAPI integrado

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| **Java** | 21 | Linguagem de programação |
| **Spring Boot** | 4.0.1 | Framework web e aplicação |
| **Spring Data JPA** | - | Persistência de dados e ORM |
| **Spring Security** | - | Autenticação e autorização |
| **PostgreSQL** | 18 | Banco de dados relacional |
| **Hibernate** | - | Mapeamento Objeto-Relacional |
| **Lombok** | - | Redução de código boilerplate |
| **SpringDoc OpenAPI** | 2.6.0 | Documentação Swagger/OpenAPI |
| **Docker** | - | Containerização da aplicação |
| **Maven** | - | Gerenciador de dependências e build |

## 📦 Estrutura do Projeto

```
e-commerce-java/
├── src/main/java/com/leonardo/ecommerce_api/
│   ├── controller/           # Endpoints REST
│   │   ├── UserController.java
│   │   ├── ProductController.java
│   │   └── OrderController.java
│   ├── service/              # Lógica de negócio
│   │   ├── UserService.java
│   │   ├── ProductService.java
│   │   ├── OrderService.java
│   │   └── OrderItemService.java
│   ├── model/                # Entidades JPA
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Role.java
│   │   └── OrderStatus.java
│   ├── dto/                  # Data Transfer Objects
│   │   ├── UserRequestDTO.java
│   │   ├── UserResponseDTO.java
│   │   ├── ProductRequestDTO.java
│   │   ├── ProductResponseDTO.java
│   │   ├── OrderRequestDTO.java
│   │   ├── OrderResponseDTO.java
│   │   ├── OrderItemRequestDTO.java
│   │   ├── OrderItemResponseDTO.java
│   │   └── LoginRequestDTO.java
│   ├── repository/           # Acesso a dados
│   │   ├── UserRepository.java
│   │   ├── ProductRepository.java
│   │   ├── OrderRepository.java
│   │   └── OrderItemRepository.java
│   ├── security/             # Configuração de segurança
│   │   └── SecurityConfig.java
│   └── EcommerceApiApplication.java
├── src/main/resources/
│   ├── application.yml       # Configuração principal
│   ├── application.properties
│   ├── static/               # Arquivos estáticos
│   └── templates/            # Templates Thymeleaf
├── db.config/
│   └── init.sql              # Script inicial do banco de dados
├── docker-compose.yml        # Orquestração de containers
├── Dockerfile                # Definição da imagem Docker
└── pom.xml                   # Configuração Maven

```

## 🚀 Como Executar o Projeto

### Pré-requisitos

- **Docker** e **Docker Compose** instalados
- Ou alternativamente:
  - Java 21+
  - Maven 3.8+
  - PostgreSQL 18+

### Opção 1: Usando Docker Compose (Recomendado)

1. **Clonar o repositório**
   ```bash
   git clone https://github.com/seu-usuario/e-commerce-java.git
   cd e-commerce-java
   ```

2. **Construir e iniciar os containers**
   ```bash
   docker-compose up --build
   ```

3. **Verificar se tudo está funcionando**
   ```bash
   docker-compose ps
   ```

A aplicação estará disponível em: `http://localhost:8080`

### Opção 2: Execução Local (Sem Docker)

1. **Configurar banco de dados PostgreSQL**
   
   Edite `src/main/resources/application.yml`:
   ```yaml
   datasource:
     url: jdbc:postgresql://localhost:5432/ecommerce_db
     username: seu_usuario
     password: sua_senha
   ```

2. **Criar banco de dados e schema**
   ```bash
   psql -U postgres
   CREATE DATABASE ecommerce_db;
   \c ecommerce_db
   \i db.config/init.sql
   ```

3. **Compilar e executar**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## 📚 Documentação da API

Após iniciar o projeto, acesse a documentação interativa do Swagger:

```
http://localhost:8080/swagger-ui.html
```

### Principais Endpoints

#### 👤 Usuários

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/register` | Criar novo usuário |
| POST | `/api/login` | Realizar login |
| DELETE | `/api/users/{id}` | Deletar usuário |

**Exemplo - Registrar usuário:**
```json
POST /api/register
{
  "fullName": "João Silva",
  "email": "joao@email.com",
  "password": "senha123",
  "role": "USER"
}
```

#### 📦 Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/products` | Listar todos os produtos |
| GET | `/api/products/{id}` | Obter produto por ID |
| GET | `/api/products/name/{name}` | Buscar produtos por nome |
| POST | `/api/products` | Criar novo produto |
| PUT | `/api/products/{id}` | Atualizar produto |
| DELETE | `/api/products/{id}` | Deletar produto |

**Exemplo - Criar produto:**
```json
POST /api/products
{
  "name": "Notebook Dell",
  "description": "Notebook de alta performance",
  "price": 3500.00,
  "stockQuantity": 10
}
```

#### 🛒 Pedidos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/orders/user/{userId}` | Listar pedidos do usuário |
| GET | `/api/orders/{id}` | Obter detalhes do pedido |
| POST | `/api/orders/create` | Criar novo pedido |
| PUT | `/api/orders/update-paid/{id}` | Marcar como pago |
| PUT | `/api/orders/update-shipped/{id}` | Marcar como enviado |
| PUT | `/api/orders/update-canceled/{id}` | Cancelar pedido |
| PUT | `/api/orders/update-order/{id}` | Atualizar itens do pedido |

**Exemplo - Criar pedido:**
```json
POST /api/orders/create
{
  "userId": "uuid-do-usuario",
  "orderItems": [
    {
      "productId": "uuid-do-produto",
      "quantity": 2,
      "unitValue": 3500.00
    }
  ]
}
```

## 🗄️ Modelo de Dados

### Tabelas do Banco de Dados

**users**
- `id` (UUID) - Identificador único
- `full_name` (TEXT) - Nome completo
- `email` (TEXT UNIQUE) - Email único
- `password` (TEXT) - Senha criptografada
- `role` (TEXT) - Papel do usuário (USER, ADMIN)
- `created_at` (TIMESTAMPTZ) - Data de criação
- `updated_at` (TIMESTAMPTZ) - Data de atualização

**products**
- `id` (UUID) - Identificador único
- `name` (TEXT) - Nome do produto
- `description` (TEXT) - Descrição
- `price` (DECIMAL) - Preço unitário
- `stock_quantity` (INT) - Quantidade em estoque
- `version` (INT) - Controle de versão (otimistic locking)
- `created_at` (TIMESTAMPTZ) - Data de criação
- `updated_at` (TIMESTAMPTZ) - Data de atualização

**orders**
- `id` (UUID) - Identificador único
- `user_id` (UUID FK) - Referência ao usuário
- `status` (TEXT) - Status do pedido (PENDING, PAID, SHIPPED, CANCELED)
- `total_amount` (DECIMAL) - Valor total
- `created_at` (TIMESTAMPTZ) - Data de criação
- `updated_at` (TIMESTAMPTZ) - Data de atualização

**order_items**
- `id` (UUID) - Identificador único
- `order_id` (UUID FK) - Referência ao pedido
- `product_id` (UUID FK) - Referência ao produto
- `quantity` (INT) - Quantidade
- `unit_value` (DECIMAL) - Valor unitário
- `subtotal` (DECIMAL GENERATED) - Calculado automaticamente (quantity × unit_value)

## 🔐 Segurança

### Implementações de Segurança

1. **Spring Security**: Controle de autenticação e autorização
2. **Validação de Entrada**: Todas as DTOs utilizam anotações `@Valid` e validadores customizados
3. **Criptografia de Senha**: Senhas são criptografadas antes de serem armazenadas
4. **UUID**: Uso de UUIDs em vez de IDs sequenciais para maior segurança
5. **Relacionamentos Cascata**: Deleção em cascata garante integridade referencial

## 📝 Configuração

### application.yml

```yaml
spring:
  application:
    name: ecommerce-api
  datasource:
    url: jdbc:postgresql://db:5432/ecommerce_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate  # Usar 'validate' em produção
    properties:
      hibernate:
        format_sql: true
        show_sql: false

server:
  port: 8080

logging:
  level:
    org.springframework.security: INFO
```

## 🧪 Testes

Execute os testes automatizados:

```bash
mvn test
```

## 🐳 Gerenciamento do Docker

### Verificar containers em execução
```bash
docker-compose ps
```

### Parar containers
```bash
docker-compose down
```

### Parar e remover volumes (limpar dados)
```bash
docker-compose down -v
```

### Ver logs
```bash
docker-compose logs -f api
docker-compose logs -f db
```

### Conectar ao banco de dados diretamente
```bash
docker-compose exec db psql -U postgres -d ecommerce_db
```

## 🔧 Desenvolvimento

### Extensões Úteis

1. **Postman** ou **Insomnia**: Para testar endpoints
2. **VS Code Extensions**: 
   - Extension Pack for Java
   - Spring Boot Extension Pack
   - Lombok Annotations Support

### Melhorias Futuras

- [ ] Autenticação JWT
- [ ] Paginação nos endpoints de listagem
- [ ] Filtros e busca avançada
- [ ] Upload de imagens de produtos
- [ ] Sistema de carrinho de compras
- [ ] Notificações por email
- [ ] Integração com gateway de pagamento
- [ ] Testes de integração e unitários completos
- [ ] CI/CD com GitHub Actions
- [ ] Deployment em Kubernetes

## 📄 Licença

Este projeto está licenciado sob a MIT License. Veja o arquivo `LICENSE` para mais detalhes.

## 👨‍💻 Autor

**Leonardo**

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## ⚠️ Troubleshooting

### Erro: "conexão recusada ao banco de dados"
- Certifique-se de que o PostgreSQL está em execução
- Verifique as credenciais em `application.yml`
- Se usar Docker, certifique-se de que o container do banco está iniciado

### Erro: "porta 8080 já está em uso"
```bash
# Mude a porta em application.yml
server:
  port: 8081
```

### Erro: "migration não foi executada"
```bash
# Recrie o banco de dados
docker-compose down -v
docker-compose up --build
```

## 📞 Suporte

Para reportar bugs ou sugerir melhorias, abra uma issue no repositório ou entre em contato com o autor.

---

**Desenvolvido com ❤️ usando Spring Boot e PostgreSQL**
