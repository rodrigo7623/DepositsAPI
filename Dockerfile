# Imagen base de OpenJDK
FROM openjdk:17-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /temp

# Copia el archivo JAR de tu aplicación al directorio de trabajo del contenedor
COPY target/*.jar deposits.jar

# Exponer el puerto en el que se ejecutará la aplicación (opcional)
EXPOSE 8081

# Configura el comando de inicio para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "deposits.jar"]