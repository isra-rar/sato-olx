# Dockerfile para aplicação Spring Boot OLX Anúncios
# 1. Usa uma imagem base leve do OpenJDK
FROM openjdk:17-jdk-alpine

# 2. Define o diretório de trabalho dentro do container
WORKDIR /app

# 3. Copia o arquivo JAR gerado pelo Maven para dentro do container
# (ajuste o nome do JAR conforme o gerado pelo seu build)
COPY target/sato-olx.jar app.jar

# 4. Expõe a porta padrão do Spring Boot (ajuste se necessário)
EXPOSE 8080

# 5. Define o comando de inicialização do container
ENTRYPOINT ["java", "-jar", "app.jar"] 