package cl.duoc.wim100.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.wim100.domain.FormValidators
import cl.duoc.wim100.ui.components.ValidatedTextField

@Composable
fun RegisterUserDialog(
    onDismiss: () -> Unit,
    onRegister: (String, String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var rutError by remember { mutableStateOf<String?>(null) }
    var professionError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registro de Usuario WIM100")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 500.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Completa tus datos para crear tu cuenta en el sistema de selección.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Field 1: Correo electrónico
                ValidatedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    label = "Correo electrónico *",
                    leadingIcon = Icons.Default.Email,
                    errorMessage = emailError,
                    isValid = emailError == null && email.isNotBlank()
                )

                // Field 2: RUT
                ValidatedTextField(
                    value = rut,
                    onValueChange = {
                        rut = it
                        rutError = null
                    },
                    label = "RUT (ej: 12.345.678-9) *",
                    leadingIcon = Icons.Default.Badge,
                    errorMessage = rutError,
                    isValid = rutError == null && rut.isNotBlank()
                )

                // Field 3: Profesión / Cargo
                ValidatedTextField(
                    value = profession,
                    onValueChange = {
                        profession = it
                        professionError = null
                    },
                    label = "Profesión / Cargo *",
                    leadingIcon = Icons.Default.Work,
                    errorMessage = professionError,
                    isValid = professionError == null && profession.isNotBlank()
                )

                Text(
                    text = "* Todos los campos son obligatorios con validación visual.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val emailVal = FormValidators.validateCandidateEmail(email)
                    val rutVal = FormValidators.validateRut(rut)
                    val profVal = FormValidators.validateProfession(profession)

                    emailError = emailVal.errorMessage
                    rutError = rutVal.errorMessage
                    professionError = profVal.errorMessage

                    if (emailVal.isValid && rutVal.isValid && profVal.isValid) {
                        onRegister(email.trim(), rut.trim(), profession.trim())
                    }
                }
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Registrar e Ingresar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
