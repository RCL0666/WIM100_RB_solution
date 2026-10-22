package cl.duoc.wim100.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.wim100.model.Candidate

@Composable
fun CandidateCard(
    c: Candidate,
    onClick: () -> Unit,
    onCallClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.large
                ) {
                    Icon(
                        Icons.Default.Person,
                        null,
                        modifier = Modifier.padding(12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(c.name, fontWeight = FontWeight.SemiBold)
                    Text(c.role, style = MaterialTheme.typography.bodySmall)
                    Text(c.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(4.dp))
                    StatusChip(c.status)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("${"%.1f".format(c.score)} pts", fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.ChevronRight, null)
                }
            }

            if (onCallClick != null || onShareClick != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (onCallClick != null && c.phone.isNotBlank()) {
                        IconButton(onClick = onCallClick) {
                            Icon(Icons.Default.Phone, contentDescription = "Llamar candidata", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    if (onShareClick != null) {
                        IconButton(onClick = onShareClick) {
                            Icon(Icons.Default.Share, contentDescription = "Compartir información", tint = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val color = when (status) {
        "Seleccionada" -> Color(0xFFD8F3E5)
        "No seleccionada" -> Color(0xFFFFE0E0)
        "Evaluada", "En evaluación" -> Color(0xFFFFEFC7)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    Surface(color = color, shape = MaterialTheme.shapes.small) {
        Text(
            status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
