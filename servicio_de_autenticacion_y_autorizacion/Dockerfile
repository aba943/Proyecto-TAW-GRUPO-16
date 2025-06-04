# Utiliza una imagen base ligera de OpenJDK 17 con Alpine Linux para reducir el tamaño de la imagen
FROM openjdk:21-jdk
# Establece el directorio de trabajo dentro del contenedor donde se copiarán los  archivos y se ejecutará la app
WORKDIR /app
# Copia el archivo JAR generado por Maven desde la carpeta target del proyecto al  directorio de trabajo del contenedor
COPY target/servicio_de_autenticacion_y_autorizacion-0.0.1-SNAPSHOT.jar app.jar
# Expone el puerto 8080 para que la aplicación sea accesible desde fuera del contenedor EXPOSE 8080
# Define el comando por defecto para ejecutar la aplicación Spring Boot cuando el  contenedor se inicie
ENTRYPOINT ["java", "-jar", "app.jar"]

