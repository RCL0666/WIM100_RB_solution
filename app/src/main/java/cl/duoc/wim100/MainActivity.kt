package cl.duoc.wim100

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.wim100.model.Candidate
import cl.duoc.wim100.ui.screens.*
import cl.duoc.wim100.ui.theme.WIM100Theme
import cl.duoc.wim100.ui.viewmodel.SelectionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WIM100Theme {
                WIM100App()
            }
        }
    }
}

private enum class Page(val label: String) {
    HOME("Inicio"),
    CANDIDATES("Candidatas"),
    EVALUATION("Evaluaciones"),
    RESULTS("Resultados"),
    HISTORY("Historial")
}

@Composable
fun WIM100App(vm: SelectionViewModel = viewModel()) {
    val candidates by vm.candidates.collectAsState()
    val history by vm.history.collectAsState()
    val userSession = vm.userSession

    var page by remember { mutableStateOf(Page.HOME) }
    var showAdd by remember { mutableStateOf(false) }
    var selectedCandidate by remember { mutableStateOf<Candidate?>(null) }

    if (!userSession.isLoggedIn) {
        LoginScreen(
            onLoginSuccess = { email ->
                vm.login(email)
            },
            onRegisterUser = { newEmail, newRut, newProfession ->
                vm.registerUser(email = newEmail, rut = newRut, profession = newProfession)
            }
        )
    } else {
        Scaffold(
            topBar = {
                Surface(shadowElevation = 2.dp, color = MaterialTheme.colorScheme.surface) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(
                                Icons.Default.Groups,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("WIM100", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Gestión de selección", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        var roleMenu by remember { mutableStateOf(false) }
                        Box {
                            TextButton(onClick = { roleMenu = true }) {
                                Icon(Icons.Default.AccountCircle, null)
                                Spacer(Modifier.width(4.dp))
                                Text(userSession.role)
                            }
                            DropdownMenu(expanded = roleMenu, onDismissRequest = { roleMenu = false }) {
                                DropdownMenuItem(
                                    text = { Text("Evaluadora") },
                                    onClick = {
                                        vm.setRole("Evaluadora")
                                        roleMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Dirección") },
                                    onClick = {
                                        vm.setRole("Dirección")
                                        roleMenu = false
                                    }
                                )
                            }
                        }

                        IconButton(onClick = { vm.logout() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Cerrar sesión",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar {
                    Page.entries.forEach { item ->
                        NavigationBarItem(
                            selected = page == item,
                            onClick = { page = item },
                            icon = {
                                Icon(
                                    when (item) {
                                        Page.HOME -> Icons.Default.Home
                                        Page.CANDIDATES -> Icons.Default.People
                                        Page.EVALUATION -> Icons.Default.Assessment
                                        Page.RESULTS -> Icons.Default.EmojiEvents
                                        Page.HISTORY -> Icons.Default.History
                                    },
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        ) { padding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                AnimatedContent(
                    targetState = page,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "PageTransition"
                ) { targetPage ->
                    when (targetPage) {
                        Page.HOME -> HomeScreen(
                            candidates = candidates,
                            userEmail = userSession.email,
                            userRut = userSession.rut,
                            userProfession = userSession.profession,
                            role = userSession.role,
                            onNavigateToCandidates = { page = Page.CANDIDATES },
                            onNavigateToEvaluation = { page = Page.EVALUATION },
                            onNavigateToResults = { page = Page.RESULTS },
                            onAddCandidate = { showAdd = true },
                            onLogout = { vm.logout() }
                        )
                        Page.CANDIDATES -> CandidatesScreen(
                            candidates = candidates,
                            onAdd = { showAdd = true },
                            onSelect = { selectedCandidate = it }
                        )
                        Page.EVALUATION -> EvaluationScreen(
                            candidates = candidates,
                            onSelect = { selectedCandidate = it }
                        )
                        Page.RESULTS -> ResultsScreen(
                            candidates = candidates,
                            role = userSession.role,
                            onResolve = { id, selected -> vm.resolve(id, selected) }
                        )
                        Page.HISTORY -> HistoryScreen(history = history)
                    }
                }
            }
        }

        if (showAdd) {
            AddCandidateDialog(
                onDismiss = { showAdd = false },
                onSave = { name, email, rut, phone, role, experience, cv ->
                    vm.addCandidate(name, email, rut, phone, role, experience, cv)
                    showAdd = false
                }
            )
        }

        selectedCandidate?.let { candidate ->
            if (page == Page.CANDIDATES) {
                CandidateDetailDialog(
                    candidate = candidate,
                    onDismiss = { selectedCandidate = null },
                    onDelete = {
                        vm.deleteCandidate(candidate.id)
                        selectedCandidate = null
                    }
                )
            } else {
                EvaluationDialog(
                    candidate = candidate,
                    onDismiss = { selectedCandidate = null },
                    onSave = { tech, comm, exp, note ->
                        vm.evaluate(candidate.id, tech, comm, exp, note)
                        selectedCandidate = null
                    }
                )
            }
        }
    }
}
