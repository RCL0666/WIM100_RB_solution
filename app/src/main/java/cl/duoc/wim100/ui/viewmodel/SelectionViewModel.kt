package cl.duoc.wim100.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.wim100.data.AppDatabase
import cl.duoc.wim100.data.CandidateRepository
import cl.duoc.wim100.data.SessionPreferences
import cl.duoc.wim100.domain.NotificationHelper
import cl.duoc.wim100.model.Candidate
import cl.duoc.wim100.model.HistoryEntry
import cl.duoc.wim100.model.UserSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SelectionViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = CandidateRepository(db.candidateDao(), db.historyDao())
    private val sessionPreferences = SessionPreferences(application)
    private val notificationHelper = NotificationHelper(application)

    val candidates: StateFlow<List<Candidate>> = repository.allCandidates.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val history: StateFlow<List<HistoryEntry>> = repository.allHistory.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var userSession by mutableStateOf(
        UserSession(
            email = sessionPreferences.getUserEmail(),
            rut = sessionPreferences.getUserRut(),
            profession = sessionPreferences.getUserProfession(),
            role = sessionPreferences.getUserRole(),
            isLoggedIn = sessionPreferences.isLoggedIn()
        )
    )
        private set

    init {
        // Seed initial data into Room DB safely
        viewModelScope.launch {
            try {
                if (repository.getCandidateCount() == 0) {
                    val demoCandidates = listOf(
                        Candidate(name = "Camila Rojas", email = "camila.rojas@mail.com", rut = "18.234.567-8", phone = "+56 9 1111 2222", role = "Desarrolladora Android", experience = "2 años", score = 82.0, status = "En evaluación"),
                        Candidate(name = "Valentina Soto", email = "valentina.soto@mail.com", rut = "19.876.543-2", phone = "+56 9 3333 4444", role = "Analista QA", experience = "3 años", score = 91.0, status = "Seleccionada"),
                        Candidate(name = "Fernanda Díaz", email = "fernanda.diaz@mail.com", rut = "17.654.321-0", phone = "+56 9 5555 6666", role = "Desarrolladora Android", experience = "1 año", score = 68.0, status = "No seleccionada")
                    )
                    demoCandidates.forEach { repository.insertCandidate(it) }
                    repository.addHistory(HistoryEntry(date = today(), text = "Base de datos Room inicializada con postulantes demo"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun login(email: String, role: String = "Evaluadora") {
        try {
            val rut = sessionPreferences.getUserRut()
            val profession = sessionPreferences.getUserProfession()
            sessionPreferences.saveSession(email, rut, profession, role)
            userSession = UserSession(email = email, rut = rut, profession = profession, role = role, isLoggedIn = true)
            log("Sesión iniciada por $email ($role)")
            notificationHelper.showNotification(
                title = "Bienvenido a WIM100",
                message = "Sesión iniciada como $email"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun registerUser(email: String, rut: String, profession: String, role: String = "Evaluadora") {
        try {
            sessionPreferences.saveSession(email = email, rut = rut, profession = profession, role = role)
            userSession = UserSession(email = email, rut = rut, profession = profession, role = role, isLoggedIn = true)
            log("Nuevo usuario registrado: $email | RUT: $rut | Profesión: $profession")
            notificationHelper.showNotification(
                title = "Registro Exitoso",
                message = "Bienvenido a WIM100, $email"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logout() {
        try {
            sessionPreferences.clearSession()
            userSession = UserSession()
            log("Sesión cerrada")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setRole(role: String) {
        try {
            sessionPreferences.saveSession(userSession.email, userSession.rut, userSession.profession, role)
            userSession = userSession.copy(role = role)
            log("Cambio de rol a $role")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addCandidate(name: String, email: String, rut: String, phone: String, role: String, experience: String, cv: String) {
        viewModelScope.launch {
            try {
                val candidate = Candidate(
                    name = name.trim(),
                    email = email.trim(),
                    rut = rut.trim(),
                    phone = phone.trim(),
                    role = role.trim(),
                    experience = experience.trim(),
                    cvUri = cv
                )
                repository.insertCandidate(candidate)
                log("Se registró a ${name.trim()} (RUT: ${rut.trim()})")
                notificationHelper.showNotification(
                    title = "Nueva Candidata Registrada",
                    message = "${name.trim()} postula al cargo $role"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun evaluate(id: Int, technical: Int, communication: Int, experience: Int, note: String) {
        viewModelScope.launch {
            try {
                val total = technical * 0.5 + communication * 0.2 + experience * 0.3
                val currentCandidates = candidates.value
                val candidate = currentCandidates.firstOrNull { it.id == id }
                if (candidate != null) {
                    val updated = candidate.copy(
                        score = total,
                        status = "Evaluada",
                        note = note
                    )
                    repository.updateCandidate(updated)
                    log("Evaluación actualizada: ${candidate.name} (${"%.1f".format(total)} pts)")
                    notificationHelper.showNotification(
                        title = "Evaluación Completada",
                        message = "${candidate.name}: ${"%.1f".format(total)} puntos"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resolve(id: Int, selected: Boolean) {
        viewModelScope.launch {
            try {
                val currentCandidates = candidates.value
                val candidate = currentCandidates.firstOrNull { it.id == id }
                if (candidate != null) {
                    val statusText = if (selected) "Seleccionada" else "No seleccionada"
                    val updated = candidate.copy(status = statusText)
                    repository.updateCandidate(updated)
                    log("Dirección $statusText a ${candidate.name}")
                    notificationHelper.showNotification(
                        title = "Decisión de Selección",
                        message = "${candidate.name} ha sido marcada como: $statusText"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var isApiLoading by mutableStateOf(false)
        private set

    var apiStatusMessage by mutableStateOf<String?>(null)
        private set

    fun fetchExternalCandidates() {
        viewModelScope.launch {
            isApiLoading = true
            apiStatusMessage = null
            try {
                val response = cl.duoc.wim100.network.RetrofitClient.externalApiService.getRandomCandidates(count = 3)
                if (response.isSuccessful && response.body() != null) {
                    val results = response.body()!!.results
                    var importedCount = 0
                    results.forEach { item ->
                        val fullName = "${item.name.first} ${item.name.last}"
                        val candidate = Candidate(
                            name = fullName,
                            email = item.email,
                            rut = "${(10..25).random()}.${(100..999).random()}.${(100..999).random()}-K",
                            phone = item.phone,
                            role = listOf("Desarrolladora Mobile", "Analista QA", "Backend Developer", "Diseñadora UX/UI").random(),
                            experience = "${(1..7).random()} años",
                            score = (60..95).random().toDouble(),
                            status = "Importado de API Externa"
                        )
                        repository.insertCandidate(candidate)
                        importedCount++
                    }
                    apiStatusMessage = "Éxito: $importedCount candidato(s) importado(s) desde RandomUser API"
                    log("Se importaron $importedCount perfiles desde API Externa (RandomUser API)")
                    notificationHelper.showNotification(
                        title = "API Externa Consumida",
                        message = "Se importaron $importedCount candidatos exitosamente"
                    )
                } else {
                    apiStatusMessage = "Error en la API Externa: ${response.code()}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                apiStatusMessage = "Error de red al conectar con API Externa: ${e.message}"
            } finally {
                isApiLoading = false
            }
        }
    }

    fun syncWithBackendMicroservice() {
        viewModelScope.launch {
            isApiLoading = true
            apiStatusMessage = null
            try {
                // Intentar sincronizar candidatos hacia el microservicio en Spring Boot (http://10.0.2.2:8080/api/candidates)
                val currentCandidates = candidates.value
                if (currentCandidates.isEmpty()) {
                    apiStatusMessage = "No hay candidatos locales para enviar al Microservicio"
                    return@launch
                }
                var syncedCount = 0
                currentCandidates.forEach { candidate ->
                    val dto = cl.duoc.wim100.network.CandidateDto(
                        rut = candidate.rut,
                        fullName = candidate.name,
                        email = candidate.email,
                        experienceYears = candidate.experience.replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 1,
                        profession = candidate.role,
                        status = candidate.status,
                        score = candidate.score.toInt(),
                        feedback = candidate.note
                    )
                    val response = cl.duoc.wim100.network.RetrofitClient.backendApiService.createCandidate(dto)
                    if (response.isSuccessful) {
                        syncedCount++
                    }
                }
                apiStatusMessage = "Microservicio Spring Boot: $syncedCount candidato(s) sincronizado(s)"
                log("Sincronización CRUD con Microservicio Spring Boot enviada ($syncedCount registros)")
                notificationHelper.showNotification(
                    title = "Microservicio Spring Boot",
                    message = "Sincronizados $syncedCount registros con el backend"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                apiStatusMessage = "Microservicio Spring Boot no disponible en http://10.0.2.2:8080 (${e.localizedMessage})"
            } finally {
                isApiLoading = false
            }
        }
    }

    fun deleteCandidate(id: Int) {
        viewModelScope.launch {
            try {
                val candidate = candidates.value.firstOrNull { it.id == id }
                val name = candidate?.name ?: "Candidata"
                repository.deleteCandidate(id)
                log("Se eliminó el registro de $name")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun log(message: String) {
        viewModelScope.launch {
            try {
                repository.addHistory(HistoryEntry(date = today(), text = message))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private fun today() = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
    }
}
