# Bank Meet - Infraestructure Frontend / Backend

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
![1A](./assets/img/13A.png)
Backend / Hexagonal Architecture
![1A](./assets/img/13B.png)
---
## Instrucciones de Ejecución

Sigue estos pasos para levantar el entorno de desarrollo:

Abre una terminal y ejecuta:
git clone https://github.com/ANDERSOUNDZ/DOCKER_TEST1.git

cd DOCKER_TEST1

Levantar los Servicios
---
Ejecuta el comando completo para construir las imágenes y levantar los contenedores en segundo plano:
---
docker-compose up --build

Servicios Desplegados

Una vez que Docker termine el proceso, podrás acceder a los siguientes servicios:

| Servicio      | URL / Acceso                                      | Puerto | Descripción                       |
|---------------|---------------------------------------------------|--------|-----------------------------------|
| Frontend      | http://localhost:4200/                            | 4200   | Interfaz de Usuario (Angular)     |
| BackendSwagger| http://localhost:9090/swagger-ui/index.html       | 9090   | API                               |
| pgAdmin       | http://localhost:5050/                            | 5050   | Panel de Gestión de BD            |
| Database      | localhost 5432                                    | 5432   | Motor PostgreSQL                  |

---

---
Configuración de pgAdmin (Visualizar BD)
---

![14](./assets/img/14A.png)

---
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

-------
BANKFY TEST 1
-------



Funcionalidades Front End:

1. Los valores cuando son crédito son positivos, y los débitos son negativos. Debe almacenarse el saldo disponible en cada transacción dependiendo del tipo de movimiento. (suma o resta)

![1A](./assets/img/1A.png)

![1B](./assets/img/1B.png)

2. Si el saldo es cero, y va a realizar una transacción débito, debe desplegar mensaje "Saldo no disponible".
![2A](./assets/img/2A.png)
3. Se debe tener un parámetro de límite diario de retiro (valor tope 1000$)
![3A](./assets/img/3A.jpg)
4. Si el cupo disponible ya se cumplió no debe permitir realizar un débito y debe desplegar el mensaje "Cupo diario Excedido"
![4A](./assets/img/4A.png)
5. Generar reporte (Estado de cuenta) especificando un rango de fechas y un cliente, visualice las cuentas asociadas con sus respectivos saldos y el total de débitos y créditos realizados durante las fechas de ese cliente. Tomar en consideración que también se debe obtener los resultados del reporte en formato base64 (PDF) y Json. Por ejemplo: (/reportes?fecha rango fechas)
![5A](./assets/img/11A.png)
![5A](./assets/img/5A.png)

Funcionalidades Backend

1. Creacion de usuarios:
![6A](./assets/img/6A.png)
![6B](./assets/img/6B.png)
![6B](./assets/img/6C.png)

2. Creacion de Cuentas:
![7A](./assets/img/7A.png)
![7B](./assets/img/7B.png)
![7C](./assets/img/7C.png)

3. Creacion Cuenta corriente para Jose Lema:
![8B](./assets/img/8B.png)

4. Realizar los movimientos:
![9A](./assets/img/9A.png)
![9B](./assets/img/9B.png)

5. Listado de Movimiento por fechas y por usuario descarga de pdf (vista)
![10A](./assets/img/10B.png)
![10B](./assets/img/10A.png)
![5B](./assets/img/5B.png)

---
EndPoints
---

Cliente
---
![5B](./assets/img/12A.png)

Cuenta
---
![5B](./assets/img/12B.png)

Movimiento
---
![5B](./assets/img/12C.png)

