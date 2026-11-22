package cl.duoc.wim100.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
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
    isApiLoading: Boolean = false,
    apiStatusMessage: String? = null,
    onAdd: () -> Unit,
    onSelect: (Candidate) -> Unit,
    onImportApi: () -> Unit = {},
    onSyncBackend: () -> Unit = {}
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
                Text("${candidates.size} registros en base local (Room)")
            }
            Button(onClick = onAdd) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(4.dp))
                Text("Agregar")
            }
        }

        Spacer(Modifier.height(8.dp))

        // Botones para integración de API Externa y Microservicio Spring Boot
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onImportApi,
                enabled = !isApiLoading,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("API Externa", style = MaterialTheme.typography.labelMedium)
            }

            OutlinedButton(
                onClick = onSyncBackend,
                enabled = !isApiLoading,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Backend CRUD", style = MaterialTheme.typography.labelMedium)
            }
        }

        AnimatedVisibility(visible = isApiLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
        }

        AnimatedVisibility(visible = !apiStatusMessage.isNullOrBlank()) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(
                    text = apiStatusMessage ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(Modifier.height(6.dp))

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
