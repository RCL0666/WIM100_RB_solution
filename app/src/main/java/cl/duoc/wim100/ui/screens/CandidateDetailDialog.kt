package cl.duoc.wim100.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cl.duoc.wim100.model.Candidate

@Composable
fun CandidateDetailDialog(
    candidate: Candidate,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(candidate.name) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                DetailLine("Cargo al que postula", candidate.role)
                DetailLine("Correo electrónico", candidate.email)
                DetailLine("Teléfono", candidate.phone.ifBlank { "No informado" })
                DetailLine("Experiencia", candidate.experience.ifBlank { "No informada" })
                DetailLine("Estado del proceso", candidate.status)
                DetailLine("Puntaje acumulado", "${"%.1f".format(candidate.score)} / 100")
                DetailLine("Currículum PDF", if (candidate.cvUri.isBlank()) "No adjuntado" else "Documento PDF adjuntado")
                if (candidate.note.isNotBlank()) {
                    DetailLine("Observaciones de evaluación", candidate.note)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        },
        dismissButton = {
            TextButton(
                onClick = onDelete,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar registro")
            }
        }
    )
}

@Composable
private fun DetailLine(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
