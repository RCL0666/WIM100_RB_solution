package cl.duoc.wim100.domain

import org.junit.Assert.*
import org.junit.Test

class FormValidatorsTest {

    @Test
    fun `validateLoginEmail con correo valido retorna true`() {
        val result = FormValidators.validateLoginEmail("evaluadora@wim100.cl")
        assertTrue(result.isValid)
        assertNull(result.errorMessage)
    }

    @Test
    fun `validateLoginEmail con correo vacio retorna false`() {
        val result = FormValidators.validateLoginEmail("")
        assertFalse(result.isValid)
        assertEquals("El correo no puede estar vacío.", result.errorMessage)
    }

    @Test
    fun `validateLoginEmail con formato invalido retorna false`() {
        val result = FormValidators.validateLoginEmail("correo-invalido")
        assertFalse(result.isValid)
        assertEquals("Ingresa un correo electrónico válido.", result.errorMessage)
    }

    @Test
    fun `validateRut con RUT chileno valido retorna true`() {
        // RUTs chilenos matemáticamente válidos
        val result1 = FormValidators.validateRut("18.234.567-9")
        val result2 = FormValidators.validateRut("19.876.543-0")
        assertTrue("RUT 18.234.567-9 debe ser válido", result1.isValid)
        assertTrue("RUT 19.876.543-0 debe ser válido", result2.isValid)
    }

    @Test
    fun `validateRut con digito verificador incorrecto retorna false`() {
        val result = FormValidators.validateRut("18.234.567-0")
        assertFalse(result.isValid)
        assertEquals("El dígito verificador del RUT es incorrecto.", result.errorMessage)
    }

    @Test
    fun `validateRut vacio retorna false`() {
        val result = FormValidators.validateRut("   ")
        assertFalse(result.isValid)
        assertEquals("El RUT es obligatorio.", result.errorMessage)
    }

    @Test
    fun `validateCandidateName con nombre corto retorna false`() {
        val result = FormValidators.validateCandidateName("Ab")
        assertFalse(result.isValid)
        assertEquals("El nombre debe tener al menos 3 caracteres.", result.errorMessage)
    }

    @Test
    fun `validateCandidateName con nombre valido retorna true`() {
        val result = FormValidators.validateCandidateName("Camila Rojas")
        assertTrue(result.isValid)
    }

    @Test
    fun `validateProfession valida correctamente longitud minima`() {
        val invalid = FormValidators.validateProfession("IT")
        val valid = FormValidators.validateProfession("Desarrolladora Mobile")
        assertFalse(invalid.isValid)
        assertTrue(valid.isValid)
    }
}
