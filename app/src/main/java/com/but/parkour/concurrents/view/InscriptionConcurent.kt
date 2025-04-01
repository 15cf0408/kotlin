package com.but.parkour.concurrents.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.concurrents.viewmodel.CompetitorViewModel
import com.but.parkour.ui.theme.ParkourTheme

class InscriptionConcurent : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competition = intent.getSerializableExtra("competition") as? Competition
        val competitionId = competition?.id
        Log.d("InscriptionConcurent", "Competition: $competition")
        setContent {
            ParkourTheme {
                val competitorViewModel: CompetitorViewModel = viewModel()
                competitionId?.let {
                    LaunchedEffect(it) {
                        competitorViewModel.fetchCompetitorsInscrit(it)
                        competitorViewModel.fetchUnregisteredCompetitors(it)
                    }
                }
                val competitors by competitorViewModel.competitors.observeAsState(initial = emptyList())
                val unregisteredCompetitors by competitorViewModel.unregisteredCompetitors.observeAsState(initial = emptyList())

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InscriptionPage(
                        modifier = Modifier.padding(innerPadding),
                        competition?.name ?: "Unknown",
                        competitors,
                        unregisteredCompetitors,
                        competitorViewModel,
                        competitionId,
                        competition
                    )
                }
            }
        }
    }
}

@Composable
fun InscriptionPage(
    modifier: Modifier = Modifier,
    compet: String,
    competitorsInscrit: List<Competitor>,
    unregisteredCompetitors: List<Competitor>,
    competitorViewModel: CompetitorViewModel,
    competitionId: Int?,
    competition: Competition?
) {
    Log.d("InscriptionPage", "Competitors inscrit dans la course: $competitorsInscrit")
    var selectedCompetitor by remember { mutableStateOf<Competitor?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val competitionStatus = competition?.status

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2C3E50), Color(0xFF4CA1AF))
                )
            )
            .padding(16.dp)
    ) {
        HeaderText(compet)
        Spacer(modifier = Modifier.height(8.dp))

        ListParticipants(
            concurrents = competitorsInscrit,
            modifier = Modifier.weight(1f),
            competitionId = competitionId,
            competitorViewModel = competitorViewModel,
        )

        if (competitionStatus == Competition.Status.not_ready || competitionStatus == Competition.Status.not_started) {
            CompetitorDropdown(
                selectedCompetitor = selectedCompetitor,
                expanded = expanded,
                searchQuery = searchQuery,
                competitors = unregisteredCompetitors,
                onCompetitorSelected = { competitor ->
                    selectedCompetitor = competitor
                    expanded = false
                },
                onSearchQueryChanged = { query -> searchQuery = query },
                onExpandedChanged = { expanded = it }
            )

            Spacer(modifier = Modifier.height(16.dp))
            InscriptionButton(
                selectedCompetitor = selectedCompetitor,
                onInscription = {
                    selectedCompetitor?.let { competitor ->
                        competitionId?.let { id ->
                            competitor.id?.let {
                                competitorViewModel.registerCompetitor(id, it)
                                selectedCompetitor = null
                                searchQuery = ""
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun HeaderText(compet: String) {
    Text(
        text = compet,
        modifier = Modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.titleLarge.copy(
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun CompetitorDropdown(
    selectedCompetitor: Competitor?,
    expanded: Boolean,
    searchQuery: String,
    competitors: List<Competitor>,
    onCompetitorSelected: (Competitor) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onExpandedChanged: (Boolean) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Button(onClick = { onExpandedChanged(true) }) {
            Text(selectedCompetitor?.firstName ?: "Sélectionner un concurrent")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChanged(false) }) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text("Rechercher un concurrent") },
                modifier = Modifier.padding(8.dp)
            )
            competitors.filter { it.firstName?.contains(searchQuery, true) == true }
                .forEach { competitor ->
                    DropdownMenuItem(
                        text = { Text(competitor.firstName ?: "Inconnu") },
                        onClick = { onCompetitorSelected(competitor) }
                    )
                }
        }
    }
}

@Composable
fun ListParticipants(
    concurrents: List<Competitor>,
    modifier: Modifier = Modifier,
    competitionId: Int?,
    competitorViewModel: CompetitorViewModel?
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(concurrents) { competitor ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .shadow(8.dp, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${competitor.firstName} ${competitor.lastName}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Button(
                        onClick = {
                            competitorViewModel?.unregisterCompetitior(competitionId!!, competitor.id!!)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("❌ Supprimer", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun InscriptionButton(selectedCompetitor: Competitor?, onInscription: () -> Unit) {
    Button(
        onClick = onInscription,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
    ) {
        Text(text = "➕ Inscrire un concurrent", color = Color.White)
    }
}
