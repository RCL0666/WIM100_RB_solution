package cl.duoc.wim100.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.wim100.model.Candidate
import cl.duoc.wim100.ui.components.LinkedInProfileCard
import cl.duoc.wim100.ui.components.MetricCard

@Composable
fun HomeScreen(
    candidates: List<Candidate>,
    userEmail: String,
    userRut: String = "",
    userProfession: String = "",
    role: String,
    onNavigateToCandidates: () -> Unit,
    onNavigateToEvaluation: () -> Unit,
    onNavigateToResults: () -> Unit,
    onAddCandidate: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        LinkedInProfileCard(
            userEmail = userEmail,
            userRut = userRut,
            userProfession = userProfession,
            role = role,
            onLogout = onLogout,
            onAddCandidate = onAddCandidate
        )

        Text(
            text = "Métricas del proceso de selección",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("Candidatas", candidates.size.toString(), Icons.Default.People, Modifier.weight(1f))
            MetricCard("Evaluadas", candidates.count { it.status != "Pendiente" }.toString(), Icons.Default.Assessment, Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("Seleccionadas", candidates.count { it.status == "Seleccionada" }.toString(), Icons.Default.CheckCircle, Modifier.weight(1f))
            MetricCard("Pendientes", candidates.count { it.status == "Pendiente" }.toString(), Icons.Default.Schedule, Modifier.weight(1f))
        }

        Text("Accesos rápidos de gestión", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        OutlinedButton(onClick = onNavigateToCandidates, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.People, null)
            Spacer(Modifier.width(8.dp))
            Text("Ver candidatas registradas")
        }
        OutlinedButton(onClick = onNavigateToEvaluation, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Assessment, null)
            Spacer(Modifier.width(8.dp))
            Text("Realizar nueva evaluación")
        }
        OutlinedButton(onClick = onNavigateToResults, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.EmojiEvents, null)
            Spacer(Modifier.width(8.dp))
            Text("Consultar ranking y decisiones")
        }
    }
}
