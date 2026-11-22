# WIM100 - Sistema de Gestión y Selección de Postulantes (App Móvil & Microservicio)

[![Android Studio](https://img.shields.io/badge/Android%20Studio-2026.2.1-green.svg)](https://developer.android.com/studio)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.24-purple.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203.2-brightgreen.svg)](https://spring.io/projects/spring-boot)

---

## 👥 1. Integrantes del Equipo
* **Renato** - Desarrollo Android, Arquitectura MVVM, Pruebas Unitarias y Conexión de Red (Retrofit).
* **Bastián** - Diseño UI Jetpack Compose, Microservicio Spring Boot, Validaciones de Formulario y Documentación.

---

## 📱 2. Descripción y Funcionalidades del Proyecto

**WIM100 Selección** es una solución móvil integral desarrollada en **Kotlin** con **Jetpack Compose** para optimizar el proceso de selección de personal técnico en empresas de tecnología. 

### Funcionalidades Principales:
* 🔐 **Autenticación y Registro:** Inicio de sesión y registro de evaluadoras/dirección guardado con `SharedPreferences`.
* 📋 **Gestión Local de Candidatas (CRUD con Room):** Almacenamiento persistente en SQLite mediante **Room Database**.
* 🌐 **Consumo de API Externa (RandomUser API):** Importación automatizada de perfiles y candidatos externos consumida a través de **Retrofit**.
* ⚡ **Integración con Microservicio Spring Boot (REST Backend):** Envío y recepción síncrona de postulantes hacia un microservicio propio mediante endpoints REST CRUD.
* 📊 **Evaluación y Métricas:** Asignación de puntajes por competencias (Técnica, Comunicación, Experiencia) con gráficos de resultados y decisiones de contratación.
* 🔔 **Notificaciones y Funciones Nativas:** Emisión de notificaciones locales de Android, vibración háptica y compartición de perfiles vía Intent nativo.
* 🧪 **Pruebas Unitarias (>80% Cobertura):** Cobertura de tests automáticos en validadores y repositorio con **JUnit 4** y **MockK**.

---

## 🌐 3. Endpoints Utilizados (API Externa y Microservicio)

### A. API Externa (RandomUser API)
* **Base URL:** `https://randomuser.me/`
* **Endpoint:** `GET /api/?results=3`
* **Descripción:** Retorna perfiles aleatorios con datos de contacto y fotos para simular importaciones externas.

### B. Microservicio Propio (Spring Boot Backend)
* **Base URL Local:** `http://10.0.2.2:8080/` (Android Emulator) / `http://localhost:8080/`
* **Endpoints REST CRUD:**
  * `GET /api/candidates` - Obtener todos los candidatos guardados en el microservicio.
  * `GET /api/candidates/{id}` - Obtener el detalle de un candidato por su ID.
  * `POST /api/candidates` - Crear/Sincronizar un nuevo candidato desde la app móvil.
  * `PUT /api/candidates/{id}` - Actualizar datos o puntuaciones de un candidato.
  * `DELETE /api/candidates/{id}` - Eliminar un candidato del backend.

---

## 🛠️ 4. Pasos para Ejecutar el Proyecto

### A. Requisitos Previos:
* Android Studio Ladybug / Koala o superior instalado.
* JDK 17 o superior.
* Emulador de Android configurado (API 24+) o dispositivo físico con Depuración USB habilitada.

### B. Ejecutar la Aplicación Móvil (Android):
1. Clona el repositorio:
   ```bash
   git clone https://github.com/RCL0666/WIM100_RB_solution.git
   ```
2. Abre la carpeta `WIMAPP` en Android Studio.
3. Espera a que Gradle ejecute la sincronización de dependencias (`Sync Project with Gradle Files`).
4. Presiona **Run 'app'** (▶️) para desplegar en el emulador.

### C. Ejecutar el Microservicio Backend (Spring Boot):
1. Navega a la carpeta del microservicio:
   ```bash
   cd backend-spring-boot
   ```
2. Ejecuta la aplicación usando Maven Wrapper o tu IDE (IntelliJ / Eclipse / VS Code):
   ```bash
   ./mvnw spring-boot:run
   ```
   *O ejecuta `BackendApplication.java` directamente en tu IDE.*
3. La base de datos H2 en memoria estará disponible en `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:wim100db`, usuario `sa`, sin contraseña).

---

## 🔑 5. Generación e Información del APK Firmado

El proyecto incluye la configuración técnica en `app/build.gradle.kts` para generar ejecutables firmados digitalmente.

* **Archivo Keystore (.jks):** `app/release-key.jks`
* **Alias de la Llave:** `wim100key`
* **Contraseña de la Llave & Almacén:** `wim100pass`
* **Algoritmo:** RSA 2048-bit (Válido por 10.000 días)
* **Ubicación del APK Firmado Generado:**
  ```
  app/build/outputs/apk/release/app-release.apk
  ```

Para compilar un nuevo APK firmado manualmente desde la consola:
```bash
./gradlew assembleRelease
```

---

## 🧪 6. Ejecución de Pruebas Unitarias

Para ejecutar la suite de pruebas unitarias automáticas (>80% de cobertura):
```bash
./gradlew testDebugUnitTest
```
* **Resultados:** 13/13 Pruebas aprobadas exitosamente (`FormValidatorsTest` & `CandidateRepositoryTest`).

---

## 📌 7. Organización y Planificación (Trello y GitHub)

* **Repositorio GitHub:** `https://github.com/RCL0666/WIM100_RB_solution.git`
* **Estructura de Ramas:**
  * `main`: Producción y entregable final.
  * `feature/room-native-integration`: Integración de Room y componentes nativos.
  * `feature/ui-compose-forms`: Formularios y componentes Jetpack Compose.
  * `feature/mvvm-architecture`: Repositorios y modelos MVVM.
* **Tablero Trello:** Planificación colaborativa entre Renato y Bastián dividida en *Backlog*, *To Do*, *En Progreso* y *Terminado*.
