package cl.duoc.wim100.domain

import android.util.Patterns

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

object FormValidators {

    fun validateLoginEmail(email: String): ValidationResult {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult(false, "El correo no puede estar vacío.")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()) {
            return ValidationResult(false, "Ingresa un correo electrónico válido.")
        }
        return ValidationResult(true)
    }

    fun validateRut(rut: String): ValidationResult {
        val trimmed = rut.trim().replace(".", "").replace("-", "")
        if (trimmed.isEmpty()) {
            return ValidationResult(false, "El RUT es obligatorio.")
        }
        if (trimmed.length < 8 || trimmed.length > 9) {
            return ValidationResult(false, "RUT inválido (ejemplo: 12.345.678-9).")
        }
        val body = trimmed.substring(0, trimmed.length - 1)
        val dv = trimmed.substring(trimmed.length - 1).uppercase()
        if (!body.all { it.isDigit() }) {
            return ValidationResult(false, "El RUT debe contener solo números y dígito verificador.")
        }
        var sum = 0
        var multiplier = 2
        for (i in body.length - 1 downTo 0) {
            sum += (body[i] - '0') * multiplier
            multiplier = if (multiplier == 7) 2 else multiplier + 1
        }
        val expectedDvNum = 11 - (sum % 11)
        val expectedDv = when (expectedDvNum) {
            11 -> "0"
            10 -> "K"
            else -> expectedDvNum.toString()
        }
        if (dv != expectedDv) {
            return ValidationResult(false, "El dígito verificador del RUT es incorrecto.")
        }
        return ValidationResult(true)
    }

    fun validateProfession(profession: String): ValidationResult {
        val trimmed = profession.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult(false, "La profesión es obligatoria.")
        }
        if (trimmed.length < 3) {
            return ValidationResult(false, "La profesión debe tener al menos 3 caracteres.")
        }
        return ValidationResult(true)
    }

    fun validateCandidateName(name: String): ValidationResult {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult(false, "El nombre completo es obligatorio.")
        }
        if (trimmed.length < 3) {
            return ValidationResult(false, "El nombre debe tener al menos 3 caracteres.")
        }
        return ValidationResult(true)
    }

    fun validateCandidateEmail(email: String): ValidationResult {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult(false, "El correo electrónico es obligatorio.")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()) {
            return ValidationResult(false, "Formato de correo inválido (ejemplo@mail.com).")
        }
        return ValidationResult(true)
    }

    fun validateCandidateRole(role: String): ValidationResult {
        val trimmed = role.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult(false, "El cargo o profesión es obligatorio.")
        }
        return ValidationResult(true)
    }

    fun validateCandidatePhone(phone: String): ValidationResult {
        val trimmed = phone.trim()
        if (trimmed.isNotEmpty() && trimmed.length < 8) {
            return ValidationResult(false, "El teléfono debe tener al menos 8 dígitos.")
        }
        return ValidationResult(true)
    }
}
