# OLX Anúncios

Projeto Spring Boot para integração, importação e consulta de anúncios OLX.

## Tecnologias
- Java 17
- Spring Boot 3.5.x
- Spring Data JPA
- PostgreSQL
- Lombok
- Jackson
- Springdoc OpenAPI (Swagger)

## Funcionalidades
- Integração com API externa para login e busca de anúncios
- Salvamento de anúncios no banco de dados
- Consulta de anúncios por descrição
- Agendamento automático de importação
- Documentação automática via Swagger UI

## Como rodar
1. Configure o banco de dados em `src/main/resources/application.yml`.
2. Execute:
   ```sh
   mvn clean install -DskipTests
   mvn spring-boot:run
   ```
3. Acesse a documentação em: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---
Desenvolvido por Israel Rodrigues. 