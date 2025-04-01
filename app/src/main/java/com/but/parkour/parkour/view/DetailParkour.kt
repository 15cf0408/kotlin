package com.but.parkour.parkour.view

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.concurrents.view.ListeConcurrentsParkour
import com.but.parkour.obstacles.view.ListeObstacles
import com.but.parkour.parkour.viewmodel.ParkourViewModel
import com.but.parkour.ui.theme.ParkourTheme

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
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        CenteredTitle("Détail de la course")
        CourseCard(course)
        Spacer(modifier = Modifier.height(16.dp))
        CourseActions(course, competition)
    }
}

@Composable
private fun CenteredTitle(title: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun CourseCard(course: Course, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = course.name ?: "Nom inconnu", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            CourseDetails(course)
        }
    }
}

@Composable
fun CourseDetails(course: Course) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("Position: ${course.position ?: "Non définie"}", style = MaterialTheme.typography.bodyMedium)
            Text("Durée max: ${course.maxDuration ?: "Non définie"} sec", style = MaterialTheme.typography.bodyMedium)
        }
        Text(
            text = if (course.isOver == true) "Terminée" else "En cours",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CourseActions(course: Course, competition: Competition) {
    val context = LocalContext.current
    when (competition.status) {
        Competition.Status.not_ready -> {
            ObstacleButton(context, course, competition.status)
            if (EditionMode.isEnable.value) DeleteCourseButton(context, competition, course)
        }
        Competition.Status.not_started -> ObstacleButton(context, course, competition.status)
        Competition.Status.started -> {
            ObstacleButton(context, course, competition.status)
            ConcurrentButton(context, course, competition)
        }
        else -> {}
    }
}

@Composable
private fun ObstacleButton(context: Context, course: Course, competitionStatus: Competition.Status?) {
    ActionButton("Obstacles") {
        context.startActivity(Intent(context, ListeObstacles::class.java).apply {
            putExtra("parkour", course)
            putExtra("competitionStatus", competitionStatus)
        })
    }
}

@Composable
private fun ConcurrentButton(context: Context, course: Course, competition: Competition) {
    ActionButton("Chronométrer les concurrents") {
        context.startActivity(Intent(context, ListeConcurrentsParkour::class.java).apply {
            putExtra("course", course)
            putExtra("competition", competition)
        })
    }
}

@Composable
private fun ActionButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(label)
    }
}

@Composable
private fun DeleteCourseButton(context: Context, competition: Competition, course: Course) {
    var showDialog by remember { mutableStateOf(false) }
    val courseViewModel: ParkourViewModel = viewModel()

    ActionButton(label = "Supprimer la course", onClick = { showDialog = true })

    if (showDialog) {
        ConfirmationDialog(
            onConfirm = {
                course.id?.let {
                    courseViewModel.supprimerCourse(it, competition.id!!)
                    context.startActivity(Intent(context, ListeParkours::class.java).apply {
                        putExtra("competition", competition)
                    })
                }
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
private fun ConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmation") },
        text = { Text("Êtes-vous sûr de vouloir supprimer cette course ?") },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Oui")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Non") }
        }
    )
}
