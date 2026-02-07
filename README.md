Bank Meet - Infraestructure Frontend / Backend

## Tecnologías Utilizadas

* **Frontend:** Angular 18+ (Node 22)
* **Backend:** Spring Boot 3 (Java 21 / Maven)
* **Base de Datos:** PostgreSQL 15
* **Gestión de BD:** pgAdmin 4
* **Orquestación:** Docker Compose
---

## Arquitectura de Contenedores

Applicacion Web Bancaria.
Frontend / Clean Architecture
Backend / Hexagonal Architecture

---

## Instrucciones de Ejecución

Sigue estos pasos para levantar el entorno de desarrollo:

Abre una terminal y ejecuta:
git clone https://github.com/ANDERSOUNDZ/DOCKER_TEST1.git
cd DOCKER_TEST1

Levantar los Servicios
Ejecuta el comando completo para construir las imágenes y levantar los contenedores en segundo plano:
docker-compose up --build

Servicios Desplegados

Una vez que Docker termine el proceso, podrás acceder a los siguientes servicios:

ServicioURL / AccesoPuerto Descripción
Frontend http://localhost:4200/ Interfaz de Usuario (Angular)
BackendSwagger http://localhost:9090/swagger-ui/index.html API
pgAdmin http://localhost:5050/ Panel de Gestión de BD 
Database localhost 5432 Motor PostgreSQL

Configuración de pgAdmin (Visualizar BD)

Para ver las tablas y datos en la interfaz visual, sigue estos pasos dentro de pgAdmin:

Inicia sesión con el correo y clave configurados en el compose
(admin@gmail.com / BankMeet2024!).

Haz clic derecho en Servers > Register > Server...

En la pestaña General:

Name: BankMeetDB

En la pestaña Connection:

Host name/address: db

Port: 5432

Maintenance database: db_bank

Username: bank_meet

Password: BankMeet2024!

Save Password: (Marcado)
