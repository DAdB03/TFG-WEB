# Proyecto de Java con SpringBoot: CHATHUB

>[!NOTE]
>## Sistema de comunicaciones internas
>Este proyecto contiene una infraestructura back y front, la cual gestiona una "redsocial" enfocada para los institutos de la comunidad de madrid.

>[!CAUTION]
>## Requisitos previos
> Es necesario tener una cuenta de [Educa Madrid](https://www.educa2.madrid.org/educamadrid/).
> - Estas cuentas solamente se otrogan a personal docente y alumnado.

>[!IMPORTANT]
> Proyecto montado en [Maven](https://maven.apache.org).
>
> Es necesario [Java JDK](https://www.oracle.com/es/java/technologies/downloads/).

>[!IMPORTANT]
> Es necesario crear un archivo application.properties el cual se tiene que situar en la carpeta resources del proyecto:

```bash
  spring.application.name=CHATHUB
  spring.datasource.url=[CONEXIÓN AL GESTOR DE BBDD]
  spring.datasource.username=[USUARIO BBDD]
  spring.datasource.password=[CONTRASEÑA BBDD]
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.properties.hibernate.dialect=[DIALECTO DEL LA BBDD "MariaDB" o "MySQL"]
  spring.main.allow-circular-references=true
  jwt.secret=[CONTRASEÑA EN BASE64 PARA CIFRAR LOS JWT]
  ftp.user=[USUARIO FTP]
  ftp.pass=[CONTRASEÑA FTP]
  spring.servlet.multipart.max-file-size=[TAMAÑO MAXIMO DE ARCHIVOS]
  spring.servlet.multipart.max-request-size=[TAMAÑO MAXIMO DE REQUEST "Se recomienda que sea lo mismo que de archivo"]
```

## Autores

* **Daniel Rodriguez** - *Developer Back-end & Front-end* - [SnakerRB](https://github.com/SnakerRB)
* **Diego Alonso** - *Developer Front & Back-end helper* - [DAdB03](https://github.com/DAdB03)
* **Pedro Cuadrado** - *Documentation Manager & Back-end helper* - [PECULOCU5](https://github.com/PECULOCU5)
