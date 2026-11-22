# Explicación del Proyecto WIMAPP y la Nueva Etapa (Evaluación Parcial 4 - DSY1105)

---

## 1. ¿Qué se corrigió anteriormente y por qué?

### A. Error de compilación en `AndroidManifest.xml`
* **Problema:** La aplicación no compilaba marcando el error `AAPT: error: resource style/Theme.WIM100 not found`. Esto sucedía porque el archivo `AndroidManifest.xml` intentaba usar una plantilla de estilo visual (`Theme.WIM100`) que **no existía** dentro de las carpetas de recursos (`res/values/themes.xml`).
* **Solución:** Se cambió el atributo `android:theme` en el `AndroidManifest.xml` por un tema nativo estándar de Android (`@android:style/Theme.Material.Light.NoActionBar`). Dado que la interfaz visual de la app se construye íntegramente con **Jetpack Compose** (que maneja sus propios temas en Kotlin con `Theme.kt`), este cambio permitió que el proyecto compilara y ejecutara perfectamente sin requerir archivos XML adicionales.

### B. Error del Emulador (`Medium Phone is already running as process 2732`)
* **Problema:** Android Studio informaba que el emulador virtual estaba bloqueado o ya ejecutándose bajo el proceso número `2732`.
* **Solución:** Se ejecutó un comando de sistema (`taskkill`) para finalizar forzadamente ese proceso "fantasma" que se había quedado colgado en segundo plano, liberando el emulador para poder lanzar la app normalmente.

### C. Sincronización con GitHub (`WIM100_RB_solution.git`)
* **Acción:** Se prepararon los cambios (`git add`), se creó el commit correspondientes (`git commit`) y se subió la actualización (`git push`) a la rama `main` de su repositorio remoto en GitHub.

---

## 2. Arquitectura del Proyecto Actual

El proyecto está diseñado bajo una arquitectura **Monolítica Estructurada** utilizando el patrón **MVVM (Modelo - Vista - ViewModel)** y principios de **Clean Architecture**:

```
cl.duoc.wim100/
├── data/              # CAPA DE DATOS (ROOM DATABASE & PREFERENCES)
│   ├── AppDatabase.kt          # Configuración de SQLite con Room
│   ├── CandidateDao.kt         # Consultas a la base de datos de candidatos
│   ├── CandidateRepository.kt  # Repositorio unificado de datos
│   ├── HistoryDao.kt           # Historial de evaluaciones
│   └── SessionPreferences.kt   # Preferencias de sesión de usuario
├── domain/            # CAPA DE DOMINIO (LÓGICA DE NEGOCIO Y REGLAS)
│   ├── FormValidators.kt       # Validaciones puras de formularios
│   ├── NativeActionsHelper.kt  # Integración con funciones nativas (Vibración, Intent)
│   └── NotificationHelper.kt   # Gestión de notificaciones nativas de Android
├── model/             # MODELOS DE DATOS (ENTIDADES)
│   ├── Candidate.kt            # Objeto de Candidato
│   ├── HistoryEntry.kt         # Objeto de entrada de historial
│   └── UserSession.kt          # Objeto de sesión de usuario
└── ui/                # CAPA VISUAL Y PRESENTACIÓN (JETPACK COMPOSE)
    ├── components/             # Componentes reutilizables (Tarjetas, Campos validados)
    ├── screens/                # Pantallas completas (Login, Home, Evaluación, Historial)
    ├── theme/                  # Tema de colores y tipografía en Compose
    └── viewmodel/              # VIEWMODEL (Estado de la UI y puente entre UI y Datos)
        └── SelectionViewModel.kt
```

* **Monolítico:** Porque todo el código de la app está empaquetado en un solo módulo (`:app`) que genera el APK final.
* **MVVM:** Separa la interfaz (`ui/`) de la lógica de datos (`data/`), usando el `SelectionViewModel` como el intermediario que reacciona a los cambios de estado.

---

## 3. Análisis de la Nueva Etapa (Evaluación Parcial 4 - Encargo DSY1105)

De acuerdo con el documento en PDF entregado (`Ev. Parcial 4_Encargo_Estudiante_ DSY1105.pdf`), para completar el 100% de esta evaluación de 2 semanas (ponderación 6%), se deben implementar los siguientes **7 requerimientos obligatorios**:

1. **Interfaz Visual Completa y Funcional (15%)**
   * Pantallas y formularios validados en Jetpack Compose, sin errores de navegación ni ejecución. *(Gran parte ya está avanzada en su proyecto)*.

2. **Desarrollo de Microservicios en Spring Boot (15%)**
   * Creación de un microservicio propio en Java/Kotlin con Spring Boot (o módulo backend separado) con su propia base de datos (PostgreSQL/MySQL/H2) expuesto con endpoints REST.

3. **Integración App Móvil <-> Microservicios (15%)**
   * La app de Android debe conectarse al microservicio propio mediante **Retrofit** para enviar, recibir, actualizar y eliminar datos (CRUD completo en tiempo real).

4. **Consumo de una API Externa (15%)**
   * Integración de una API REST pública (ej: *RandomUser API* para importar datos de candidatos reales con fotos, o *ReqRes API*) consumida con **Retrofit** y mostrada en la interfaz.

5. **Pruebas Unitarias (15%)**
   * Cobertura de tests unitarios de al menos el **80% de la lógica** en `ViewModel`, `Repository` o `Validators`, implementadas con **JUnit** y **MockK**.

6. **Generación del APK Firmado (10%)**
   * Generación del ejecutable `.apk` firmado mediante una clave digital Keystore (`.jks`), dejando configurado el proyecto en `build.gradle.kts`.

7. **Documentación, GitHub y Trello (15%)**
   * Evidencia de commits distribuidos entre los integrantes (Renato y Bastián), actualización del **README.md** con capturas, endpoints y configuración, y planificación en **Trello**.

---

## 4. Plan de Acción Recomendado

Para abordar esta evaluación de manera ordenada, realizaremos los siguientes pasos:

1. **Configurar Retrofit & API Externa:**
   * Agregar dependencias de `Retrofit` y `GsonConverterFactory` en `app/build.gradle.kts`.
   * Crear la capa de red (`NetworkModule` / API Services) para consumir una API externa de candidatos o usuarios.
2. **Configurar la Integración del Microservicio (Backend Spring Boot):**
   * Crear los endpoints de API Service en Android para conectarse a las operaciones CRUD (Crear, Leer, Actualizar, Borrar) del microservicio.
3. **Implementar Pruebas Unitarias (Tests >= 80%):**
   * Crear el directorio `app/src/test/java/...`
   * Escribir pruebas unitarias con JUnit 5 / MockK para validar `FormValidatorsTest` y `SelectionViewModelTest`.
4. **Firmado del APK:**
   * Generar la clave digital `.jks` y configurar las firmas de la app.
5. **Documentación README.md:**
   * Redactar un `README.md` profesional en la raíz del proyecto para cumplir con la pauta de entrega.
