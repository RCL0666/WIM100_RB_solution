# WIM100 - Sistema de Gestión de Selección de Talento TI

Aplicación nativa en Android desarrollada en **Kotlin** con **Jetpack Compose** y **Material Design 3** para la gestión, evaluación y selección de candidatas del programa **WIM100**.

---

## 📦 Cumplimiento de Requisitos del Proyecto

### 1. Interfaz visual organizada y navegación clara
* Diseño moderno y responsivo con **Jetpack Compose** y **Material 3**.
* Navegación clara estructurada en 5 secciones principales mediante `NavigationBar` inferior:
  * 🏠 **Inicio:** Perfil estilo LinkedIn con banner, avatar, titular profesional, resumen, aptitudes y métricas clave del proceso.
  * 👥 **Candidatas:** Lista de postulantes con búsqueda en tiempo real e interacciones.
  * 📊 **Evaluaciones:** Ponderación y calificación de competencias técnicas.
  * 🏆 **Resultados:** Ranking ordenado por puntaje acumulado y decisiones de Dirección.
  * 📜 **Historial:** Auditoría de eventos registrados durante la sesión.

### 2. Formularios validados con íconos y mensajes visuales
* Implementación del componente `ValidatedTextField`:
  * Retroalimentación visual inmediata (íconos de validación ✔️ y ❌ en cada campo).
  * Bordes rojos y mensajes de error explicativos animados en tiempo real.
* Formulario de **Login** con validación estricta de correo de prueba (`usuario@demo.cl`).
* Formulario de **Registro de Candidata** con campos obligatorios validados (Nombre, Correo, Cargo, Teléfono) y selección de currículum PDF.

### 3. Validaciones manejadas desde lógica (Desacopladas de la UI)
* La lógica de validación se encuentra desacoplada en el módulo `cl.duoc.wim100.domain.FormValidators`.
* Retorna objetos `ValidationResult` con estado `isValid` y mensajes de error `errorMessage` sin acoplar la interfaz de usuario.

### 4. Animaciones funcionales
* `AnimatedContent` para transiciones fluidas entre pantallas.
* `AnimatedVisibility` para aparición/desaparición progresiva de mensajes de error y diálogos.
* Desplazamiento y renderizado animado de controles interactivos (*Sliders* de evaluación).

### 5. Estructura modular y persistencia local (MVVM)
* **Arquitectura limpia MVVM:**
  * `model`: Entidades de datos (`Candidate`, `HistoryEntry`, `UserSession`).
  * `data`: **Room Database** (`AppDatabase`, `CandidateDao`, `HistoryDao`), `CandidateRepository` y `SessionPreferences` (SharedPreferences).
  * `domain`: Validadores independientes (`FormValidators`), Notificaciones y Acciones nativas.
  * `ui`: Componentes Compose reutilizables, Temas y Pantallas.
  * `viewmodel`: `SelectionViewModel` con `StateFlow` y Corrutinas.
* **Persistencia Local:** Los datos de postulantes e historial se almacenan localmente en la base de datos **Room (SQLite)** y la sesión del usuario se mantiene mediante **SharedPreferences**.

### 6. Acceso a Recursos Nativos del Dispositivo
* 📄 **Document Picker (Storage API):** Selección de currículum PDF mediante `ActivityResultContracts.OpenDocument()`.
* 🔔 **Notificaciones del Sistema (NotificationManager):** Notificaciones nativas al registrar o evaluar postulantes.
* 📞 **Icnos de Acción Directa (Intent.ACTION_DIAL & Intent.ACTION_SEND):**
  * Realización de llamadas telefónicas a postulantes.
  * Compartición de la ficha técnica de la candidata por aplicaciones externas.

---

## 🛠️ Guía para Repositorio en GitHub y Tablero Trello

### Configuración en GitHub
1. Inicializar repositorio git:
   ```bash
   git init
   git add .
   git commit -m "feat: Proyecto completo WIM100 con Room, MVVM y Compose"
   git remote add origin https://github.com/TU_USUARIO/wim100-android.git
   git branch -M main
   git push -u origin main
   ```

### Estructura en Trello
Crea un tablero llamado **WIM100 Android App** con las siguientes columnas:
* 📋 **Backlog / Por Hacer:**
  * Configurar estructura MVVM y Room Database.
  * Implementar Login con validación `usuario@demo.cl`.
  * Diseñar perfil estilo LinkedIn en Pantalla de Inicio.
  * Integrar recursos nativos (Llamadas, Compartir, PDF Picker, Notificaciones).
* ⚙️ **En Proceso:**
  * Pruebas de validación en formularios con `FormValidators`.
* ✅ **Completado:**
  * Compilación y verificación final del proyecto.

---

## 🚀 Compilación y Ejecución
1. Abrir el proyecto en **Android Studio**.
2. Sincronizar Gradle (`Sync Project with Gradle Files`).
3. Ejecutar en emulador o dispositivo físico Android (API 24+).
