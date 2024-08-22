FROM openjdk:17.0.1

# pra onde vai dentro do contrainer
WORKDIR /app 

# copy destino -> origem (java -> container)
COPY bin/registro.jar registro.jar

CMD ["java", "-jar", "registro.jar"]