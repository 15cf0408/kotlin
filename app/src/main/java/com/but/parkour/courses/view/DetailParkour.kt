package com.but.parkour.courses.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.concurrents.view.ListeConcurrentsParkour
import com.but.parkour.obstacles.view.ListeObstacles
import com.but.parkour.courses.viewmodel.ParkourViewModel
import com.but.parkour.ui.theme.ParkourTheme
import androidx.lifecycle.viewmodel.compose.viewModel


class DetailParkour : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val course = intent.getSerializableExtra("course") as Course
        val competition = intent.getSerializableExtra("competition") as Competition

        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DetailParkourPage(
                        modifier = Modifier.padding(innerPadding),
                        course = course,
                        competition = competition
                    )
                }
            }
        }
    }
}

@Composable
fun DetailParkourPage(modifier: Modifier = Modifier, course: Course, competition: Competition) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header()
        CourseCard(course)
        Spacer(modifier = Modifier.height(16.dp))
        CourseActions(course, competition)
    }
}

@Composable
fun Header() {
    Text(
        text = "Détail de la course",
        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(bottom = 16.dp),
        color = Color(0xFF333333)
    )
}

@Composable
fun CourseCard(course: Course, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = course.name ?: "Nom inconnu",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Position: ${course.position ?: "Non définie"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Durée max: ${course.maxDuration ?: "Non définie"} sec",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (course.isOver == true) "Terminée" else "En cours",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun CourseActions(course: Course, competition: Competition) {
    val competitionStatus = competition.status
    val context = LocalContext.current

    when (competitionStatus) {
        Competition.Status.not_ready -> {
            ObstacleButton(context = context, course = course, competitionStatus = competition.status)
            if (EditionMode.isEnable.value) {
                DeleteButton(context = context, competition = competition, course = course)
            }
        }
        Competition.Status.not_started -> {
            ObstacleButton(context = context, course = course, competitionStatus = competition.status)
        }
        Competition.Status.started -> {
            ObstacleButton(context = context, course = course, competitionStatus = competition.status)
            ConcurrentButton(context = context, course = course, competition = competition)
        }
        Competition.Status.finished -> { }
        null -> {}
    }
}

@Composable
private fun ObstacleButton(context: Context, course: Course, competitionStatus: Competition.Status?) {
    Button(
        onClick = {
            val intent = Intent(context, ListeObstacles::class.java)
            intent.putExtra("parkour", course)
            intent.putExtra("competitionStatus", competitionStatus)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
    ) {
        Text("Obstacles", color = Color.White)
    }
}

@Composable
private fun ConcurrentButton(context: Context, course: Course, competition: Competition) {
    Button(
        onClick = {
            val intent = Intent(context, ListeConcurrentsParkour::class.java)
            intent.putExtra("course", course)
            intent.putExtra("competition", competition)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
    ) {
        Text("Chronometrer les concurrents", color = Color.White)
    }
}

@Composable
private fun DeleteButton(context: Context, competition: Competition, course: Course) {
    var showDialog by remember { mutableStateOf(false) }

    val courseViewModel: ParkourViewModel = viewModel()

    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text("Supprimer la course", color = Color.White)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cette course ?") },
            confirmButton = {
                Button(
                    onClick = {
                        course.id?.let {
                            courseViewModel.removeCourse(it, competition.id!!)
                            val intent = Intent(context, ListeParkours::class.java)
                            intent.putExtra("competition", competition)
                            context.startActivity(intent)
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Oui")
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
