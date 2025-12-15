# Sistema Bancario (Java + MySQL)

Este proyecto es una aplicación de escritorio desarrollada en **Java** utilizando **Swing** para la interfaz gráfica y **MySQL** para la gestión y persistencia de datos. El sistema simula las operaciones bancarias fundamentales, permitiendo el registro de usuarios, gestión de saldos en tiempo real e historial de transacciones seguro.

**Integrantes del Proyecto:**
* Michael Carrillo
* Kevin Amagua


## Configuración de la Base de Datos

Para el correcto funcionamiento del sistema, es necesario ejecutar el siguiente script en **MySQL Workbench** (o su gestor de preferencia) para crear la estructura de datos:

```sql
CREATE DATABASE sistema_bancario;

USE sistema_bancario;

-- Tabla para almacenar la información de los clientes
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    saldo DECIMAL(10, 2) DEFAULT 0.00
);

-- Tabla para almacenar el historial de transacciones
CREATE TABLE movimientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    tipo VARCHAR(50), -- Ejemplo: 'Depósito', 'Retiro', 'Transferencia'
    monto DECIMAL(10, 2),
    fecha VARCHAR(50),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
````

-----

## Descripción de Módulos y Lógica del Sistema

A continuación se detalla el funcionamiento técnico y lógico de cada formulario incluido en el proyecto:

### 1\. Inicio de Sesión (`Login.java`)

Es la interfaz de entrada y seguridad del sistema.

  * **Función:** Autenticar a los usuarios registrados en la base de datos.
  * **Datos de Entrada:** Usuario (Nombre o Email) y Contraseña.
  * **Lógica Backend:** \* Utiliza el método `BaseDatos.validarLogin()`.
      * Ejecuta una consulta `SELECT` segura. Si las credenciales coinciden, el sistema instancia un objeto `Usuario` con el ID y saldo real recuperados de MySQL y abre el panel principal.

### 2\. Registro de Usuario (`Registro.java`)

Permite la creación de nuevos clientes en la entidad bancaria.

  * **Función:** Validar datos y almacenar nuevos registros.
  * **Validaciones:** Verifica que no existan campos vacíos y comprueba la igualdad de contraseñas.
  * **Lógica Backend:** \* Utiliza `BaseDatos.registrarUsuario()`.
      * Ejecuta un `INSERT` en la tabla `usuarios`. Cada cuenta nueva se inicializa obligatoriamente con un saldo de **$0.00**.

### 3\. Panel Principal (`Banco.java`)

Es el "Dashboard" central de la aplicación.

  * **Función:** Visualizar el estado financiero y facilitar la navegación.
  * **Características Técnicas:**
      * **Visualización:** Muestra el nombre del usuario y su saldo actual formateado.
      * **Historial Dinámico:** Carga una tabla (`JTable`) con los movimientos traídos desde la BD mediante `BaseDatos.cargarHistorial()`.
      * **Refresco Automático:** Implementa `WindowListeners` en los botones de acción. Al cerrar una ventana de operación (Depósito/Retiro), el dashboard actualiza el saldo y la tabla automáticamente sin reiniciar el programa.

### 4\. Depósito (`Deposito.java`)

Módulo para ingresar dinero a la cuenta propia.

  * **Lógica de Negocio:**
    1.  Valida que el monto ingresado sea numérico y mayor a 0.
    2.  Calcula el nuevo saldo (`saldoActual + monto`).
    3.  Actualiza el saldo en la BD mediante `UPDATE`.
    4.  Registra la operación en la tabla `movimientos` (`INSERT`) con la fecha actual.

### 5\. Retiro (`Retiro.java`)

Módulo para extraer dinero de la cuenta.

  * **Validación Crítica:** Impide retirar más dinero del disponible (`if monto > saldoActual`).
  * **Lógica de Negocio:**
    1.  Resta el monto del saldo actual.
    2.  Actualiza el registro del usuario en MySQL (`BaseDatos.actualizarSaldo`).
    3.  Guarda el movimiento como "Retiro" en el historial, registrando el monto en negativo para control interno.

### 6\. Transferencia (`Transferencia.java`)

Módulo avanzado para enviar dinero entre usuarios del mismo banco.

  * **Función:** Mover fondos de la cuenta local a una cuenta de terceros.
  * **Lógica Backend:**
    1.  **Búsqueda:** Utiliza `BaseDatos.buscarUsuario()` para validar que el destinatario (buscado por Email o Nombre) exista realmente en la BD.
    2.  **Validación:** Asegura que el emisor tenga fondos suficientes y que no se transfiera dinero a sí mismo.
    3.  **Transacción Atómica (Simulada):**
          * Descuenta el saldo al emisor (`UPDATE`).
          * Suma el saldo al receptor (`UPDATE`).
    4.  **Auditoría:** Genera dos registros en la tabla `movimientos`: uno de "Transferencia enviada" para el usuario logueado y otro de "Transferencia recibida" para el destinatario.

-----

## 🔐 Configuración de Conexión (BaseDatos.java)

La conexión a la base de datos se gestiona dentro de la clase `BaseDatos.java`. Para ejecutar el proyecto en un entorno local, asegúrese de configurar las constantes con las credenciales de su servidor MySQL.

A continuación, se muestra un ejemplo del código de configuración (sin credenciales reales):

```java
public class BaseDatos {
    
    // URL de conexión (Host: localhost, Puerto: 3306, Base: sistema_bancario)
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_bancario";
    
    // Usuario por defecto de MySQL
    private static final String USER = "root"; 
    
    // IMPORTANTE: Reemplace "TU_CLAVE_AQUI" por la contraseña real de su MySQL
    private static final String PASSWORD = "TU_CLAVE_AQUI"; 
}
````

-----

## Tecnologías Utilizadas

  * **Lenguaje de Programación:** Java (JDK 21)
  * **Entorno de Desarrollo:** IntelliJ IDEA
  * **Interfaz Gráfica:** Java Swing
  * **Base de Datos:** MySQL
  * **Conectividad:** JDBC (MySQL Connector J)
  * **Control de Versiones:** Git
