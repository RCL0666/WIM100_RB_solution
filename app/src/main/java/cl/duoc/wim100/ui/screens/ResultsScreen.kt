package cl.duoc.wim100.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.wim100.model.Candidate
import cl.duoc.wim100.ui.components.StatusChip

@Composable
fun ResultsScreen(
    candidates: List<Candidate>,
    role: String,
    onResolve: (Int, Boolean) -> Unit
) {
    val sortedCandidates = candidates.sortedByDescending { it.score }

    Column(Modifier.fillMaxSize()) {
        Text("Resultados y ranking", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Ranking oficial ordenado por puntaje de evaluación", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))

        if (candidates.isEmpty()) {
            EmptyState("Sin resultados", "Registra y evalúa candidatas para ver el ranking.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(sortedCandidates, key = { it.id }) { c ->
                    val rankIndex = sortedCandidates.indexOf(c) + 1
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text("#$rankIndex", Modifier.padding(10.dp), fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(c.name, fontWeight = FontWeight.SemiBold)
                                    Text(c.role, style = MaterialTheme.typography.bodySmall)
                                }
                                Text("%.1f".format(c.score), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            StatusChip(c.status)

                            if (role == "Dirección") {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { onResolve(c.id, true) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Check, null)
                                        Spacer(Modifier.width(4.dp))
                                        Text("Seleccionar")
                                    }
                                    OutlinedButton(
                                        onClick = { onResolve(c.id, false) },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Close, null)
                                        Spacer(Modifier.width(4.dp))
                                        Text("No seleccionar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
