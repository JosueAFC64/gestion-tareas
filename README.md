Descripción del Proyecto

Gestión de Tareas es una aplicación web completa diseñada para ayudar a los usuarios a organizar y gestionar sus tareas de manera eficiente. La aplicación permite a los usuarios crear, asignar, priorizar, categorizar y realizar un seguimiento del progreso de sus tareas.

El **backend** está construido utilizando Java y el framework Spring Boot, proporcionando una API robusta y segura para la gestión de datos y la lógica de negocio. Utiliza PostgreSQL como base de datos para el almacenamiento persistente de la información. La autenticación de usuarios se implementa mediante cookies para mantener el estado de la sesión de forma segura.
El **frontend** se desarrolla con Angular, ofreciendo una interfaz de usuario moderna y reactiva para una experiencia de usuario fluida e intuitiva. Se comunica con el backend a través de una API RESTful.

Características Principales
* Gestión de Tareas: Crear, editar, eliminar y visualizar tareas.
* Asignación de Tareas: Asignar tareas a diferentes usuarios (si la funcionalidad lo requiere).
* Priorización: Establecer la prioridad de las tareas (alta, media, baja).
* Categorización: Organizar las tareas en diferentes categorías o proyectos.
* Seguimiento del Progreso: Marcar tareas como completadas y visualizar el progreso.
* Autenticación Segura: Sistema de autenticación de usuarios basado en cookies.
* Interfaz de Usuario Intuitiva: Interfaz moderna y fácil de usar desarrollada con Angular.

Instrucciones de Instalación

Sigue estas instrucciones para configurar y ejecutar la aplicación en tu entorno local.
Requisitos Previos
Asegúrate de tener instaladas las siguientes herramientas:
* Java Development Kit (JDK) 19: Necesario para compilar y ejecutar el backend de Spring Boot. Puedes descargarlo desde [la página de descarga de Oracle](https://www.oracle.com/java/technologies/javase-jdk19-archive-downloads.html) o a través de tu gestor de paquetes.
* Maven: Herramienta de gestión de dependencias y construcción para el backend de Java. Puedes descargarlo desde [la página de descarga de Apache Maven](https://maven.apache.org/download.cgi).
* Node.js (versión LTS recomendada): Entorno de ejecución de JavaScript necesario para Angular CLI y la gestión de dependencias del frontend. Puedes descargarlo desde [la página oficial de Node.js](https://nodejs.org/).
* npm (Node Package Manager): Generalmente se instala con Node.js.
* Angular CLI: Interfaz de línea de comandos para Angular. Puedes instalarlo globalmente con el siguiente comando:
    ```bash
    npm install -g @angular/cli
    ```
* PostgreSQL: Sistema de gestión de bases de datos relacional utilizado por el backend. Puedes descargarlo e instalarlo desde [la página oficial de PostgreSQL](https://www.postgresql.org/download/). Asegúrate de crear una base de datos para la aplicación.

Configuración del Backend
1.  Clonar el Repositorio del Backend:
    ```bash
    git clone <URL_DEL_REPOSITORIO_BACKEND> backend
    cd backend
    ```
2.  Configurar la Base de Datos PostgreSQL:
    * Crea una base de datos en PostgreSQL para la aplicación.
    * Actualiza la configuración de la conexión a la base de datos en el archivo `src/main/resources/application.properties`
3.  Construir y Ejecutar el Backend:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    El backend debería estar ejecutándose en el puerto configurado (por defecto, 8080).

Configuración del Frontend

1.  Clonar el Repositorio del Frontend:
    ```bash
    git clone <URL_DEL_REPOSITORIO_FRONTEND> frontend
    cd frontend
    ```
2.  Instalar las Dependencias del Frontend:
    ```bash
    npm install
    ```
3.  Configurar la URL del Backend:
    * Edita el archivo de configuración del entorno para establecer la URL base de la API del backend.
4.  Ejecutar el Frontend:
    ```bash
    ng serve -o
    ```
    Esto construirá y servirá la aplicación Angular, abriéndola automáticamente en tu navegador (generalmente en `http://localhost:4200`).

Tecnologías Utilizadas

* Backend:
    * Java
    * Spring Boot
    * Spring Data JPA
    * PostgreSQL
    * Spring Security (para autenticación y autorización)
    * Maven
* Frontend:
    * Angular
    * TypeScript
    * HTML
    * CSS (o SCSS)
    * Angular CLI
    * npm
