# Usando a imagem oficial do Maven com OpenJDK 21
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copiando o arquivo pom.xml pra baixar as dependências do projeto
COPY locadoraLivro/locadoraLivro/pom.xml ./
RUN mvn dependency:go-offline

# Copiando o código-fonte e fazendo o build da aplicação
COPY locadoraLivro/locadoraLivro/src ./src
RUN mvn clean package -DskipTests

# Agora vamos usar a imagem do OpenJDK 21 pra rodar o app em produção
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiando o .jar gerado no build pra dentro do container
COPY --from=build /app/target/*.jar app.jar

# Expondo a porta 8040, que é a porta padrão do Spring Boot
EXPOSE 8040

# Comando pra rodar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]