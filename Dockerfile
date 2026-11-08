FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY src ./src
COPY lib/mysql-connector-j-26.10.0.jar ./lib/mysql-connector-j-26.10.0.jar
RUN mkdir -p out && javac -cp lib/mysql-connector-j-26.10.0.jar -d out src/*.java

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/out ./out
COPY --from=build /app/lib/mysql-connector-j-26.10.0.jar ./lib/mysql-connector-j-26.10.0.jar
COPY web ./web
EXPOSE 10000
CMD ["sh", "-c", "java -cp out:lib/mysql-connector-j-26.10.0.jar Server"]
