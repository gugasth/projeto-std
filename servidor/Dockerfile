FROM openjdk:17.0.1

# pra onde vai dentro do container
WORKDIR /app 

# copy origem -> destino (java -> container)
COPY bin/servidor.jar servidor.jar

CMD ["java", "-jar", "servidor.jar"]