package cl.duoc.wim100.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
fun AddCandidateDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var cvUri by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var rutError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var roleError by remember { mutableStateOf<String?>(null) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        cvUri = uri?.toString().orEmpty()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar candidata") },
        text = {
            Column(
                Modifier
                    .heightIn(max = 520.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ValidatedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = null
                    },
                    label = "Nombre completo *",
                    leadingIcon = Icons.Default.Person,
                    errorMessage = nameError,
                    isValid = nameError == null && name.isNotBlank()
                )

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

                ValidatedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = null
                    },
                    label = "Teléfono",
                    leadingIcon = Icons.Default.Phone,
                    errorMessage = phoneError,
                    isValid = phoneError == null && phone.isNotBlank()
                )

                ValidatedTextField(
                    value = role,
                    onValueChange = {
                        role = it
                        roleError = null
                    },
                    label = "Profesión / Cargo al que postula *",
                    leadingIcon = Icons.Default.Work,
                    errorMessage = roleError,
                    isValid = roleError == null && role.isNotBlank()
                )

                OutlinedTextField(
                    value = experience,
                    onValueChange = { experience = it },
                    label = { Text("Experiencia") },
                    leadingIcon = { Icon(Icons.Default.Timeline, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = { picker.launch(arrayOf("application/pdf")) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PictureAsPdf, null)
                    Spacer(Modifier.width(6.dp))
                    Text(if (cvUri.isBlank()) "Adjuntar currículum PDF" else "PDF seleccionado")
                }

                Text("* Campos obligatorios con validación", style = MaterialTheme.typography.labelSmall)
            }
        },
        confirmButton = {
            Button(onClick = {
                val nameVal = FormValidators.validateCandidateName(name)
                val emailVal = FormValidators.validateCandidateEmail(email)
                val rutVal = FormValidators.validateRut(rut)
                val phoneVal = FormValidators.validateCandidatePhone(phone)
                val roleVal = FormValidators.validateCandidateRole(role)

                nameError = nameVal.errorMessage
                emailError = emailVal.errorMessage
                rutError = rutVal.errorMessage
                phoneError = phoneVal.errorMessage
                roleError = roleVal.errorMessage

                if (nameVal.isValid && emailVal.isValid && rutVal.isValid && phoneVal.isValid && roleVal.isValid) {
                    onSave(name, email, rut, phone, role, experience, cvUri)
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
