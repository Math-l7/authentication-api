# Authentication REST API (Spring Boot + JWT)

> API RESTful de autenticação desenvolvida em Java com Spring Boot, utilizando JWT para segurança e PostgreSQL como banco de dados. Inclui documentação automática via Swagger.

---

## Tecnologias utilizadas

- Java 21  
- Spring Boot 3.5  
- Spring Data JPA  
- PostgreSQL  
- Spring Security (Crypto + JWT)  
- Swagger (OpenAPI)  
- Maven

---

## Funcionalidades

- Registro e login de usuários  
- Geração de token JWT para autenticação  
- Proteção de endpoints via roles  
- Consulta de usuários e permissões  
- Documentação interativa via Swagger UI  

---

## Endpoints principais

- `POST /auth/login` → Login de usuário  
- `POST /auth/register` → Registro de usuário  
- `GET /users` → Lista todos os usuários (protegido)  
- `GET /users/{id}` → Consulta usuário por ID (protegido)  

> A documentação completa está disponível no Swagger UI: `/swagger-ui/index.html`

---

## Estrutura do projeto

src/main/java
├── controller → endpoints REST
├── service → regras de negócio
├── repository → acesso ao banco de dados
├── models → entidades JPA
└── exceptions → tratamento de erros customizados

---

## Como rodar o projeto

1. Clone o repositório  
2. Configure o banco PostgreSQL e atualize o `application.properties`  
3. Rode `mvn spring-boot:run`  
4. Acesse o Swagger UI: `http://localhost:8080/swagger-ui/index.html`  

---

## Diferenciais

- Projeto pronto para deploy  
- Estrutura organizada e seguindo boas práticas  
- Código comentado e de fácil manutenção  
- Testes unitários planejados para endpoints críticos  