package cl.duoc.wim100.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.wim100.domain.NativeActionsHelper
import cl.duoc.wim100.model.Candidate
import cl.duoc.wim100.ui.components.CandidateCard

@Composable
fun CandidatesScreen(
    candidates: List<Candidate>,
    onAdd: () -> Unit,
    onSelect: (Candidate) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val filteredCandidates = remember(candidates, searchQuery) {
        if (searchQuery.isBlank()) candidates
        else candidates.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.role.contains(searchQuery, ignoreCase = true) ||
                    it.email.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Candidatas", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("${candidates.size} registros en base local")
            }
            Button(onClick = onAdd) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(4.dp))
                Text("Agregar")
            }
        }

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar por nombre, correo o cargo...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        if (filteredCandidates.isEmpty()) {
            EmptyState("No se encontraron candidatas", "Intenta con otro término de búsqueda o agrega un nuevo registro.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredCandidates, key = { it.id }) { candidate ->
                    CandidateCard(
                        c = candidate,
                        onClick = { onSelect(candidate) },
                        onCallClick = { NativeActionsHelper.dialPhoneNumber(context, candidate.phone) },
                        onShareClick = { NativeActionsHelper.shareCandidateInfo(context, candidate.name, candidate.role, candidate.score, candidate.status) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(title: String, subtitle: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Search, null, modifier = Modifier.size(42.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
