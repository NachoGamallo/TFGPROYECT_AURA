# 🧠 AURA Backend – Especificación Técnica y API REST (TFG)

Este repositorio contiene el código fuente del **Backend** de **AURA**, un sistema de gestión, análisis y gamificación del rendimiento deportivo. La aplicación está construida sobre una arquitectura robusta en capas utilizando **Java 21** y **Spring Boot 4.0.3** , exponiendo una API RESTful protegida criptográficamente y conectada de forma relacional a **PostgreSQL**.

---

## 🏗️ 1. Arquitectura y Patrones de Diseño

El backend se ha diseñado bajo el patrón de **Arquitectura en Capas (Layered Architecture)**, lo que garantiza el desacoplamiento, la facilidad de pruebas unitarias y la mantenibilidad del software.

### Capas del Sistema:
1.  **Capa de Presentación / Controladores (`com.example.Aura.controller`):** Intercepta las solicitudes HTTP. Recibe la información estructurada en **DTOs (Data Transfer Objects)**, valida las reglas básicas de entrada y delega la ejecución a la capa de servicios.

2.  **Capa de Lógica de Negocio / Servicios (`com.example.Aura.services`):** Es el núcleo de la aplicación. Aquí se procesan las reglas de entrenamiento, se calculan ecuaciones metabólicas en tiempo real y se aplican transacciones atómicas (`@Transactional`) para garantizar la consistencia de los datos.

3.  **Capa de Acceso a Datos / Repositorios (`com.example.Aura.repository`):** Interfaces que heredan de `JpaRepository`. Spring Data JPA genera automáticamente las consultas SQL en base al nombre del método (ej: `findByUserIdOrderByCreatedAtAsc`) o mediante consultas personalizadas escritas en JPQL utilizando la anotación `@Query`.

4.  **Capa de Modelos / Entidades Relacionales (`com.example.Aura.model`):** Clases Java que mapean uno a uno las tablas de PostgreSQL mediante anotaciones de Jakarta Persistence (`@Entity`, `@Table`, `@Id`, `@ManyToOne`, etc.).

### El porqué de los DTOs y el uso de Lombok
Para evitar la sobreexposición de datos sensibles (como los hashes de las contraseñas o problemas de recursividad cíclica en las relaciones de la base de datos, toda la comunicación exterior se realiza mediante **DTOs** (`com.example.Aura.dto`). Además, se utiliza **Lombok** (`@Data`, `@AllArgsConstructor`) para autogenerar en tiempo de compilación los métodos getters, setters y constructores, manteniendo el código limpio y legible.

---

## 🔐 2. Seguridad, CORS y Ciclo de Vida del Token (JWT)

AURA implementa seguridad avanzada **Stateless (sin estado)** para garantizar la velocidad y la compatibilidad con Flutter.

### Configuración del Filtro de Seguridad (`SecurityConfig`)
La clase `SecurityConfig.java` configura un pipeline de seguridad (`SecurityFilterChain`) donde se desactivan las vulnerabilidades por falsificación de solicitudes (`csrf().disable()`) ya que la API no maneja cookies de sesión. Se configuran políticas estrictamente sin estado (`SessionCreationPolicy.STATELESS`), y se definen los accesos permitidos:
* **Endpoints Públicos:** Registro y Login (`/api/auth/**`), consultas al catálogo de ejercicios, creación y consulta de rutinas de manera temporal para facilitar pruebas.
* **Endpoints Privados:** Información del perfil, estadísticas y registro de peso (`/api/user/**`, `/api/record/**`) requieren autenticación obligatoria.

### Flujo de Trabajo del Token JWT:
1. **Petición de Acceso:** El usuario envía su correo y contraseña en texto plano al controlador `AuthController`.

2. **Validación y Cifrado:** El servicio `AuthService` busca al usuario en el repositorio, recupera el hash en BCrypt de la base de datos y utiliza `passwordEncoder.matches()` para validar de forma segura que las credenciales coincidan sin revelar el texto plano.

3. **Generación del Token:** Si la validación es correcta, `JwtService` genera un token firmado digitalmente con el algoritmo criptográfico **HS256** utilizando una clave secreta codificada internamente. El token incrusta el correo electrónico del usuario como *Subject* y se le asigna un tiempo de expiración de 24 horas.

4. **Intercepción por Filtro (`JwtAuthenticationFilter`):** En cada petición posterior a endpoints protegidos, la app móvil añade la cabecera `Authorization: Bearer <token>`. Nuestro filtro personalizado intercepta la solicitud antes de llegar al controlador , extrae el token , valida que no haya expirado , recupera al usuario e inyecta un objeto `UsernamePasswordAuthenticationToken` en el contexto global de Spring Security (`SecurityContextHolder`). A partir de este momento, el usuario se considera "Autenticado" durante la vida útil de esa petición HTTP.

### Configuración de CORS y WebConfig
Para permitir que entornos de desarrollo híbridos, navegadores web y la plataforma de Flutter (especialmente webview o FlutterFlow) realicen peticiones sin ser bloqueados por la política del mismo origen (Same-Origin Policy), se configura CORS de manera global tanto en `SecurityConfig` como en `WebConfig`. Se exponen explícitamente los métodos HTTP requeridos (`GET, POST, PUT, DELETE, PATCH, OPTIONS`) permitiendo el comodín `*` en los orígenes y cabeceras para un despliegue ágil en desarrollo.

---

## 📊 3. Desglose Teórico-Práctico de los Servicios (Lógica de Negocio)

### A. Servicio de Nutrición Automática (`NutritionService`)

Cuando un usuario se registra en la plataforma, AURA calcula dinámicamente sus necesidades energéticas iniciales basándose en la ciencia del metabolismo deportivo.

1.  **Cálculo de la Tasa Metabólica Basal (BMR):** Implementa la **Ecuación de Harris-Benedict revisada** (Mifflin-St Jeor), diferenciando matemáticamente por género biológico:
    * *Hombres:* $\text{BMR} = (10 \times \text{peso}) + (6.25 \times \text{altura}) - (5 \times \text{edad}) + 5$.
    * *Mujeres:* $\text{BMR} = (10 \times \text{peso}) + (6.25 \times \text{altura}) - (5 \times \text{edad}) - 161$.
      
2.  **Cálculo del Gasto Energético Total Diario (TDEE):** Multiplica el BMR obtenido por el factor de actividad física del usuario extraído de la enumeración `ActivityLevel` (`SEDENTARY: 1.2`, `LIGHT: 1.375`, `MODERATE: 1.55`, `HIGH: 1.725`, `ATHLETE: 1.9`).

3.  **Ajuste por Objetivo:** * Si el objetivo es `FAT_LOSS`, aplica un **déficit calórico** restando exactamente $500\text{ kcal}$ al TDEE.
    * Si el objetivo es `MUSCLE_GAIN`, aplica un **superávit calórico** sumando $300\text{ kcal}$.
    * Si es `MAINTENANCE`, mantiene las calorías del TDEE intactas.
    
4.  **Reparto de Macronutrientes:** * Asigna fijos $2.2\text{ gramos de proteína}$ por cada kilo de peso corporal del usuario.
    * Asigna fijo $1.0\text{ gramo de grasa}$ por cada kilo de peso corporal.
    * Sabiendo que 1g de proteína aporta $4\text{ kcal}$ y 1g de grasa aporta $9\text{ kcal}$, calcula las calorías restantes y las divide entre $4$ para obtener los gramos exactos de carbohidratos necesarios (`carbGrams`). El plan se guarda automáticamente de forma activa (`isActive = true`).

### B. Servicio de Rutinas (`RoutineService`)

Permite automatizar el entrenamiento modular.
* Al invocar `createRoutine`, el servicio asocia automáticamente al usuario autenticado, extrae su perfil físico para heredar de forma transparente el nivel (`UserLevel`) y el objetivo deportivo (`Goal`) a la rutina. 

* Itera los ejercicios enviados en la petición JSON y, mediante identificadores compuestos binarios (`RoutineExerciceID`), asocia de manera relacional las entidades `Routine` y `Exercise` en la tabla intermedia `RoutineExercice`, guardando datos cruciales como el orden de los ejercicios (`position`), series, repeticiones y el tiempo de descanso en segundos.

### C. Servicio de Sesiones de Entrenamiento (`TrainingSessionService`)
Es el motor dinámico de la ejecución en tiempo real.

* **Inicialización (`getWorkoutDataForRoutine`):** Cuando el usuario va a iniciar un entrenamiento, la API busca en su historial si existe una sesión previa finalizada de esa misma rutina. Si existe, extrae los registros de series y repeticiones de la última sesión (`ExerciseRecord`) y los inyecta en la respuesta como "Pesos y Repeticiones Previas" (`setDTO.setPreviousWeight`, `setPreviousReps`). Si es la primera vez que la realiza, inyecta los valores base configurados por defecto al crear la rutina. Esto le permite al usuario ver en su pantalla qué peso levantó la última vez para aplicar de forma real el principio de **sobrecarga progresiva**.

* **Finalización (`saveCompletedSession`):** Al finalizar el entrenamiento, el servicio intercepta el JSON con la duración total en segundos, calcula el tiempo real en minutos, crea la entidad `TrainingSession` y guarda de forma atómica en la tabla `exercise_record` cada serie realizada con los kilogramos y repeticiones ejecutados por el usuario.

### D. Servicio de Usuarios (`UserService`)

Orquesta la analítica general de la aplicación.
* **`getHomeData`:** Construye el DTO unificado del Dashboard principal. Ejecuta consultas agregadas en la base de datos para sumar la duración en minutos de todos los entrenamientos del día de hoy, cuenta el número de sesiones finalizadas dentro de la semana actual (calculando dinámicamente los rangos de lunes a domingo), extrae el último peso corporal registrado en la tabla `body_record` para pintar la evolución del peso, e inyecta las calorías diarias objetivo calculadas por el servicio nutricional.

---

## 🛠️ 4. Guía de Despliegue para Usuarios Externos

Cualquier persona que descargue este repositorio puede levantar la API localmente siguiendo estos pasos secuenciales:

### Requisitos Técnicos en la Máquina:
* **Java Development Kit (JDK):** Versión 21 instalada de forma global en las variables de entorno.
* **PostgreSQL Server:** Corriendo localmente en el puerto estándar (`5432`).
* **NGROK:** Instalado en el sistema operativo.

### Pasos para Arrancar:
1.  **Montar la base de datos:** Entra en tu consola de PostgreSQL o pgAdmin. Ejecuta la instrucción `CREATE DATABASE "AuraDatabase";`. Abre el archivo `auraDataBase.sql` incluido en la raíz de este proyecto y copia/ejecuta todo el script SQL. Esto generará los enums, las tablas relacionales y poblará el sistema con los datos de ejercicios básicos (Press de banca, Sentadillas, Dominadas, etc.) con sus respectivas instrucciones estructuradas.

2.  **Configurar credenciales:** Edita el archivo `src/main/resources/application.properties` y modifica las propiedades de conexión reemplazando `spring.datasource.username` y `spring.datasource.password` con las credenciales de tu base de datos PostgreSQL local.

3.  **Compilar y Ejecutar:** Abre una terminal dentro del directorio donde se encuentra el archivo `pom.xml`.
    * *En Windows:* Ejecuta el comando `mvnw.cmd spring-boot:run`.
    * *En Linux/macOS:* Otorga permisos de ejecución con `chmod +x mvnw` y arranca con `./mvnw spring-boot:run`.
    * *Resultado:* El backend compilará sus dependencias y levantará el servidor Tomcat escuchando en el puerto local `8080`.

4.  **Exposición a Internet vía NGROK:** Como la app móvil desarrollada en Flutter necesita hacer peticiones HTTP a este backend y `localhost` solo es accesible dentro de la misma máquina, abre una nueva terminal secundaria y escribe:

    ```bash
    ngrok http 8080
    ```

   NGROK abrirá un túnel seguro y te proporcionará una URL pública bajo protocolo seguro HTTPS (ejemplo: `https://abcd-123-45.ngrok-free.dev`). **Copia esa dirección completa**, ya que es la que se debe configurar como punto de entrada en el código fuente de la aplicación móvil.
