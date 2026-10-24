package cl.duoc.wim100.data

import android.content.Context
import android.content.SharedPreferences

class SessionPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("wim100_session_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_RUT = "user_rut"
        private const val KEY_USER_PROFESSION = "user_profession"
        private const val KEY_USER_ROLE = "user_role"
    }

    fun saveSession(email: String, rut: String = "", profession: String = "", role: String = "Evaluadora") {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USER_RUT, rut)
            .putString(KEY_USER_PROFESSION, profession)
            .putString(KEY_USER_ROLE, role)
            .apply()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""

    fun getUserRut(): String = prefs.getString(KEY_USER_RUT, "12.345.678-9") ?: "12.345.678-9"

    fun getUserProfession(): String = prefs.getString(KEY_USER_PROFESSION, "Líder de Selección Tech") ?: "Líder de Selección Tech"

    fun getUserRole(): String = prefs.getString(KEY_USER_ROLE, "Evaluadora") ?: "Evaluadora"
}
