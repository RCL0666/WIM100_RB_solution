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
