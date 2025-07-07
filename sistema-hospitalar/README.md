# Sistema de Gestão Hospitalar (VitalPlus)

## Descrição

O **Sistema de Gestão Hospitalar (VitalPlus)** é uma API RESTful desenvolvida em Java 21 e Spring Boot 3.4.5, destinada a clínicas, hospitais e serviços de home care. Fornece gerenciamento de usuários (administradores, pacientes, profissionais de saúde), consultas, internações, prontuários, receitas digitais e teleconsultas, com segurança baseada em JWT.

## Tecnologias Utilizadas

* Java 21
* Spring Boot 3.4.5
* Spring Security + JWT
* Spring Data JPA (H2 em memória)
* Jakarta Bean Validation
* Springdoc OpenAPI (Swagger UI)
* Lombok

## Pré-requisitos

* JDK 17+
* Maven 3.6+

## Como Executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/SEU_USUARIO/sistema-hospitalar.git
   cd sistema-hospitalar
   ```
2. Build e run:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
3. Acesse a documentação:

   * Swagger UI: `http://localhost:8080/swagger-ui/index.html`
   * OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints Principais

### Autenticação

* **POST** `/autenticacao/login`

  * Gera token JWT.

### Administradores

* **POST** `/administradores`            → Cadastrar administrador
* **PUT**  `/administradores/{id}`       → Atualizar perfil
* **GET**  `/administradores`            → Listar todos
* **GET**  `/administradores/relatorio`  → Relatório geral

### Pacientes

* **POST** `/administradores/cadastrar-paciente`  → Cadastrar paciente
* **PUT**  `/pacientes/{id}`                     → Atualizar perfil
* **GET**  `/pacientes`                          → Listar todos

### Profissionais de Saúde

* **POST** `/administradores/cadastrar-profissional`  → Cadastrar profissional
* **PUT**  `/profissionais/{id}`                       → Atualizar perfil
* **GET**  `/profissionais`                            → Listar todos

### Consultas

* **POST** `/gerenciar-consultas/agendar`  → Agendar consulta
* **PUT**  `/gerenciar-consultas/remarcar` → Remarcar
* **DELETE** `/gerenciar-consultas/cancelar/{id}` → Cancelar
* **PUT**  `/gerenciar-consultas/realizada/{id}` → Marcar realizada
* **GET**  `/gerenciar-consultas/historico/{pacienteId}` → Histórico

### Prontuários

* **POST** `/prontuarios/cadastrar/{pacienteId}` → Cadastrar prontuário
* **GET**  `/prontuarios`                        → Listar todos
* **GET**  `/prontuarios/paciente/{id}`          → Por paciente

### Receitas Médicas

* **POST** `/receitas/emitir`              → Emitir receita
* **GET**  `/receitas/paciente/{id}`       → Listar por paciente
* **GET**  `/receitas/profissional/{id}`   → Listar por profissional

### Internações

* **POST** `/internacoes/internar`         → Internar paciente
* **PUT**  `/internacoes/alta/{id}`        → Dar alta
* **GET**  `/internacoes`                  → Listar todas
* **GET**  `/internacoes/{id}`             → Buscar por id
* **DELETE** `/internacoes/{id}`           → Excluir

### Teleconsultas

* **POST** `/teleconsulta/iniciar`         → Iniciar link de teleconsulta
* **GET**  `/teleconsulta/consulta/{id}`   → Buscar por consulta

## Configurações

As configurações de JWT, banco H2 e logs estão em `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:meubanco
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
jwt.secret=<sua_chave>
jwt.expiration=86400000
```

## Testes

* Utilizar Postman (arquivo `VitalPlus_API_Hospitalar.postman_collection.json`).
* Cenários principais: autenticação, cadastro, agendamento de consultas, internações, teleconsulta.
* Evidências via prints de resposta e códigos HTTP.

## Estrutura do Projeto

```
src/
 ├─ main/
 │   ├─ java/com/vital_plus/sistema_hospitalar/
 │   │   ├─ controller/
 │   │   ├─ model/
 │   │   ├─ dto/
 │   │   ├─ repository/
 │   │   ├─ configuracao/
 │   │   └─ exception/
 │   └─ resources/
 │       └─ application.properties
 └─ test/
```
## 📎 Anexos

- 📘 [Coleção Postman](./postman/VitalPlus-API-Hospitalar.postman_collection.json)
- 📚 [Documentação OpenAPI - Swagger JSON](./swagger/openapi.json)


## Contribuição e License

Este projeto é fornecido como base para estudo.

---

*VitalPlus © 2025*
