# Posts APP

Uma plataforma social full stack desenvolvida para estudos e demonstração de habilidades em desenvolvimento backend e frontend.

## Objetivo

O projeto tem como objetivo simular uma rede social simplificada onde usuários podem criar publicações, comentar, curtir conteúdos e interagir com outros usuários.

Além das funcionalidades de negócio, o foco é aplicar boas práticas de arquitetura, autenticação, autorização, persistência de dados e desenvolvimento full stack.

---

## Tecnologias

### Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Validation
- Spring Security (JWT)
- PostgreSQL
- Flyway
- Maven

### Frontend

- Angular
- TypeScript
- RxJS
- Angular Router
- Tailwind CSS

### Infraestrutura

- Docker
- Docker Compose

---

## Funcionalidades

### Autenticação

- Cadastro
- Login
- Refresh Token
- Logout
- Alteração de senha

### Usuários

- Visualizar perfil
- Atualizar perfil
- Desativar conta

### Posts

- Criar publicação
- Editar publicação
- Excluir publicação
- Listar publicações
- Buscar publicação por ID
- Pesquisa de publicações
- Paginação

### Comentários

- Criar comentário
- Editar comentário
- Excluir comentário
- Listar comentários de uma publicação

### Curtidas

- Curtir publicação
- Remover curtida
- Curtir comentário
- Remover curtida

### Administração

- Listar usuários
- Bloquear usuários
- Auditoria de ações

## Executando o Projeto

### Clonar o repositório

```bash
git clone https://github.com/hendersonkleber/posts-app.git
```

```bash
cd posts-app
```

---

### Subir PostgreSQL

```bash
docker compose up -d
```

---

### Executar Backend

```bash
cd backend
```

```bash
./mvnw spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

---

### Executar Frontend

```bash
cd frontend
```

```bash
npm install
```

```bash
ng serve
```

A aplicação ficará disponível em:

```text
http://localhost:4200
```

---

## Roadmap

### Versão 1

- [ ] Autenticação JWT
- [ ] CRUD de Posts
- [ ] CRUD de Comentários
- [ ] Curtidas
- [ ] Paginação

### Versão 2

- [ ] Upload de imagens
- [ ] Seguir usuários
- [ ] Feed personalizado
- [ ] Notificações

## Licença

Este projeto foi desenvolvido para fins de estudo e portfólio.
