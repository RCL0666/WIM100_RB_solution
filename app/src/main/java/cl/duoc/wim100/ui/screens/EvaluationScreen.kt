package cl.duoc.wim100.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.wim100.model.Candidate
import cl.duoc.wim100.ui.components.CandidateCard

@Composable
fun EvaluationScreen(
    candidates: List<Candidate>,
    onSelect: (Candidate) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Text("Evaluaciones", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Selecciona una candidata para calificar sus competencias.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(12.dp))
        if (candidates.isEmpty()) {
            EmptyState("Sin candidatas", "Registra una candidata primero.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(candidates, key = { it.id }) { candidate ->
                    CandidateCard(candidate, onClick = { onSelect(candidate) })
                }
            }
        }
    }
}

@Composable
fun EvaluationDialog(
    candidate: Candidate,
    onDismiss: () -> Unit,
    onSave: (Int, Int, Int, String) -> Unit
) {
    var technical by remember { mutableFloatStateOf(candidate.score.toFloat().coerceIn(0f, 100f)) }
    var communication by remember { mutableFloatStateOf(75f) }
    var experience by remember { mutableFloatStateOf(70f) }
    var note by remember { mutableStateOf(candidate.note) }
    val total = technical * .5f + communication * .2f + experience * .3f

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Evaluar: ${candidate.name}") },
        text = {
            Column(
                Modifier
                    .heightIn(max = 520.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ScoreSlider("Competencias técnicas (50%)", technical) { technical = it }
                ScoreSlider("Comunicación (20%)", communication) { communication = it }
                ScoreSlider("Experiencia (30%)", experience) { experience = it }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Puntaje ponderado final", Modifier.weight(1f), fontWeight = FontWeight.Medium)
                        Text("${"%.1f".format(total)} / 100", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                }

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Observaciones / Feedback") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "El puntaje final se calcula en tiempo real según las ponderaciones del estándar WIM100.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(technical.toInt(), communication.toInt(), experience.toInt(), note) }) {
                Text("Guardar evaluación")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun ScoreSlider(label: String, score: Float, onChange: (Float) -> Unit) {
    Column {
        Row {
            Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Text("${score.toInt()}/100", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        }
        Slider(value = score, onValueChange = onChange, valueRange = 0f..100f)
    }
}
