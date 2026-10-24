package cl.duoc.wim100.model

data class UserSession(
    val email: String = "",
    val rut: String = "",
    val profession: String = "",
    val role: String = "Evaluadora",
    val isLoggedIn: Boolean = false
)
