package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.ui.theme.ParkourTheme
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.CompetitionUpdate
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.concurrents.view.InscriptionConcurent
import com.but.parkour.courses.view.ListeParkours

class DetailsCompetition : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val competition = intent.getSerializableExtra("competition") as Competition

        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DetailsCompetitionPage(
                        competition = competition,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailsCompetitionPage(
    competition: Competition,
    modifier: Modifier = Modifier
) {
    Log.d("DetailsCompetitionPage", "Competition: $competition")

    var currentCompetition by remember { mutableStateOf(competition) }
    val competitionViewModel: CompetitionViewModel = viewModel()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color(0xFFFFEBEE)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageTitle()
        CompetitionDetailsCard(currentCompetition)
        Spacer(modifier = Modifier.height(24.dp))
        CompetitionActions(currentCompetition) { updatedCompetition ->
            currentCompetition = updatedCompetition
        }
    }
}

@Composable
fun PageTitle() {
    Text(
        text = "Détails de la Compétition",
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD32F2F)
        ),
        modifier = Modifier.padding(bottom = 24.dp)
    )
}

@Composable
private fun CompetitionDetailsCard(competition: Competition) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = competition.name ?: "Nom inconnu",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB71C1C)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val genre = when (competition.gender) {
                Competition.Gender.H -> "Homme"
                Competition.Gender.F -> "Femme"
                else -> "Non défini"
            }

            val status = when (competition.status) {
                Competition.Status.not_ready -> "Non prête"
                Competition.Status.not_started -> "Non commencée"
                Competition.Status.started -> "Commencée"
                Competition.Status.finished -> "Terminée"
                else -> "Non défini"
            }

            Text("Âge minimal: ${competition.ageMin} ans", color = Color(0xFFB71C1C))
            Text("Âge maximal: ${competition.ageMax} ans", color = Color(0xFFB71C1C))
            Text("Genre: $genre", color = Color(0xFFB71C1C))
            Text("Statut: $status", color = Color(0xFFB71C1C))
            Text("Chute" + if (competition.hasRetry == true) " autorisée" else " non autorisée", color = Color(0xFFB71C1C))
        }
    }
}

@Composable
private fun CompetitionActions(
    competition: Competition,
    onCompetitionUpdate: (Competition) -> Unit
) {
    val context = LocalContext.current
    val status = competition.status

    when (status) {
        Competition.Status.not_ready -> {
            ConcurrentButton(context, competition)
            ParkoursButton(context, competition)
            ValiderCompetitionButton(competition, onCompetitionUpdate)
            if (EditionMode.isEnable.value) {
                ModifyButton(context, competition)
                DeleteButton(context, competition)
            }
        }
        Competition.Status.not_started -> {
            ConcurrentButton(context, competition)
            ParkoursButton(context, competition)
        }
        Competition.Status.started -> {
            ConcurrentButton(context, competition)
            ParkoursButton(context, competition)
        }
        Competition.Status.finished -> {
            ViewResultsButton()
        }
        null -> {}
    }
}

@Composable
fun ValiderCompetitionButton(
    competition: Competition,
    onCompetitionUpdate: (Competition) -> Unit
) {
    val competitionViewModel: CompetitionViewModel = viewModel()

    val competValid = CompetitionUpdate(
        status = CompetitionUpdate.Status.not_started
    )

    Button(
        onClick = {
            competitionViewModel.updateCompetition(competition.id!!, competValid)
            val updatedCompetition = competition.copy(status = Competition.Status.not_started)
            onCompetitionUpdate(updatedCompetition)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
    ) {
        Text("Valider le parcours", color = Color.White)
    }
}

@Composable
fun ViewResultsButton() {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
    ) {
        Text("Afficher les résultats", color = Color.White)
    }
}

@Composable
private fun ModifyButton(context: Context, competition: Competition) {
    Button(
        onClick = {
            val intent = Intent(context, ModifierCompetition::class.java)
            intent.putExtra("competition", competition)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
    ) {
        Text("Modifier la compétition", color = Color.White)
    }
}

@Composable
private fun ConcurrentButton(context: Context, competition: Competition) {
    Button(
        onClick = {
            val intent = Intent(context, InscriptionConcurent::class.java)
            intent.putExtra("competition", competition)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
    ) {
        Text("Concurrents", color = Color.White)
    }
}

@Composable
private fun ParkoursButton(context: Context, competition: Competition) {
    Button(
        onClick = {
            val intent = Intent(context, ListeParkours::class.java)
            intent.putExtra("competition", competition)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
    ) {
        Text("Courses", color = Color.White)
    }
}

@Composable
private fun DeleteButton(context: Context, competition: Competition) {
    var showDialog by remember { mutableStateOf(false) }

    val competitionViewModel: CompetitionViewModel = viewModel()

    Button(
        onClick = {
            showDialog = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text("Supprimer la compétition", color = Color.White)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cette compétition ?") },
            confirmButton = {
                Button(
                    onClick = {
                        competition.id?.let {
                            competitionViewModel.removeCompetition(it)
                            val intent = Intent(context, ListeCompetitions::class.java)
                            context.startActivity(intent)
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Oui", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Non")
                }
            }
        )
    }
}