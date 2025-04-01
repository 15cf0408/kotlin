package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.concurrents.view.GestionConcurrents
import com.but.parkour.ui.theme.ParkourTheme
import kotlinx.coroutines.delay

class ListeCompetitions : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                val competitionViewModel: CompetitionViewModel = viewModel()
                val competitions by competitionViewModel.competitions.observeAsState(initial = emptyList())

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CompetitionPage(
                        modifier = Modifier.padding(innerPadding),
                        competitions = competitions
                    )
                }
            }
        }
    }
}

@Composable
fun CompetitionPage(
    modifier: Modifier = Modifier,
    competitions: List<Competition>,
) {
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(500)
        isLoading = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color(0xFFFAFAFA)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitreCompetition()
        EditionMode()

        if (isLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFD32F2F))
            }
        } else if (competitions.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aucune compétition disponible",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                )
            }
        } else {
            ListCompetitions(
                items = competitions,
                modifier = Modifier.weight(1f)
            )
        }

        if (EditionMode.isEnable.value) {
            EditionModeEnable(context)
        }
    }
}

@Composable
fun EditionMode() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "édition",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD32F2F)
        )
        Spacer(modifier = Modifier.width(8.dp))
        RadioButton(
            selected = EditionMode.isEnable.value,
            onClick = {
                EditionMode.isEnable.value = !EditionMode.isEnable.value
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFD32F2F),
                unselectedColor = Color.Gray
            )
        )
    }
}

@Composable
fun TitreCompetition() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Les Guerriers Ninjas !",
            modifier = Modifier.padding(bottom = 24.dp),
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color(0xFFD32F2F),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun EditionModeEnable(context: Context) {
    Column {
        Button(
            onClick = { onClickAjouterCompetition(context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text(
                text = "Ajouter une compétition",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { onGestionConcurrents(context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text(
                text = "Gérer les concurrents",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ListCompetitions(
    items: List<Competition>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()

    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()

        ) {
            items(items) { competition ->
                CompetitionCard(
                    competition = competition,
                )
            }
        }
    }
}

@Composable
fun CompetitionCard(
    competition: Competition,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()



    ) {
        Column(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = competition.name ?: "Nom inconnu",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    val genre = when (competition.gender) {
                        Competition.Gender.H -> "Homme"
                        Competition.Gender.F -> "Femme"
                        else -> "Non défini"
                    }
                    Text(
                        text = "Âge minimal: ${competition.ageMin} ans",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFFD32F2F))
                    )
                    Text(
                        text = "Âge maximal: ${competition.ageMax} ans",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFFD32F2F))
                    )
                    Text(
                        text = "Genre: $genre",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFFD32F2F))
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    val status = when (competition.status) {
                        Competition.Status.not_ready -> "Non prête"
                        Competition.Status.not_started -> "Non commencée"
                        Competition.Status.started -> "Commencée"
                        Competition.Status.finished -> "Terminée"
                        else -> "Non défini"
                    }
                    Text(
                        text = "Statut: $status",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFFD32F2F))
                    )
                    Text(
                        text = "Chute" + if (competition.hasRetry == true) " autorisée" else " non autorisée",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFFD32F2F))
                    )
                }
            }

            Button(
                onClick = { onItemClickDetails(competition, context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Voir détails", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun onItemClickDetails(competition: Competition, context: Context) {
    val intent = Intent(context, DetailsCompetition::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}

fun onClickAjouterCompetition(context: Context) {
    val intent = Intent(context, AjoutCompetition::class.java)
    context.startActivity(intent)
}

fun onGestionConcurrents(context: Context) {
    val intent = Intent(context, GestionConcurrents::class.java)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun CompetitionPreview() {
    ParkourTheme {
        CompetitionPage(
            competitions = listOf(
                Competition(name = "Competition 1"),
                Competition(name = "Competition 2")
            )
        )
    }
}