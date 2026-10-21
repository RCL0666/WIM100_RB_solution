# INSTRUCCIONES Y GUÍA DE EVALUACIÓN WIM100

Este documento describe la estructura y funcionamiento de la aplicación WIM100 para la revisión docente e instruccional.

---

## 🔑 Datos de Acceso
* **Correo de Prueba:** `usuario@demo.cl`
* **Contraseña:** Cualquier valor o botón de auto-completar.

---

## 📂 Organización Modular del Código

```text
cl.duoc.wim100
├── MainActivity.kt                      (Punto de entrada de la app)
├── data                                 (Persistencia local Room y SharedPreferences)
│   ├── AppDatabase.kt
│   ├── CandidateDao.kt
│   ├── HistoryDao.kt
│   ├── CandidateRepository.kt
│   └── SessionPreferences.kt
├── domain                               (Lógica de negocio y validadores desacoplados)
│   ├── FormValidators.kt
│   ├── NativeActionsHelper.kt
│   └── NotificationHelper.kt
├── model                                (Modelos de datos y entidades Room)
│   ├── Candidate.kt
│   ├── HistoryEntry.kt
│   └── UserSession.kt
└── ui                                   (Capa de presentación MVVM / Jetpack Compose)
    ├── components
    │   ├── CandidateCard.kt
    │   ├── LinkedInProfileCard.kt
    │   ├── MetricCard.kt
    │   └── ValidatedTextField.kt
    ├── screens
    │   ├── AddCandidateDialog.kt
    │   ├── CandidateDetailDialog.kt
    │   ├── CandidatesScreen.kt
    │   ├── EvaluationScreen.kt
    │   ├── HistoryScreen.kt
    │   ├── HomeScreen.kt
    │   ├── LoginScreen.kt
    │   └── ResultsScreen.kt
    ├── theme
    │   └── Theme.kt
    └── viewmodel
        └── SelectionViewModel.kt
```

---

## 🧪 Pasos para probar cada requisito en la App:

1. **Prueba de Login y Validación Visual:**
   * Ingresa a la app. Si intentas ingresar con un correo distinto a `usuario@demo.cl`, verás el ícono de error ❌ y el mensaje de retroalimentación en rojo.
   * Presiona el botón *"Auto-completar usuario@demo.cl"* y el ícono cambiará automáticamente a ✔️ verde.

2. **Prueba del Perfil estilo LinkedIn en Inicio:**
   * Al iniciar sesión, verás en la pantalla de Inicio la tarjeta con el diseño característico de LinkedIn (Banner azul, Foto de perfil flotante, Titular, Ubicación, Abierto a trabajo, Acerca de, Aptitudes principales y Botones de acción).

3. **Prueba de Persistencia Local (Room DB):**
   * Ve a la pestaña **Candidatas** y agrega un nuevo registro.
   * Cierra la aplicación completamente y vuelve a abrirla. Los registros y el historial se mantendrán guardados localmente gracias a **Room SQLite**.

4. **Prueba de Recursos Nativos:**
   * **Notificaciones Nativas:** Al agregar o evaluar una candidata, se emite una notificación del sistema.
   * **Llamadas e Intent de Compartir:** En la tarjeta de cada candidata o en el perfil, presiona el ícono del teléfono para abrir el marcador telefónico nativo o el ícono de compartir para enviar la ficha técnica.
   * **Document Picker:** Al registrar una candidata, presiona *"Adjuntar currículum PDF"* para abrir el selector nativo de documentos de Android.
