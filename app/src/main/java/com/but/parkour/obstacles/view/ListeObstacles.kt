package com.but.parkour.obstacles.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.*
import com.but.parkour.obstacles.ui.theme.ParkourTheme
import com.but.parkour.obstacles.viewmodel.ObstaclesViewModel

class ListeObstacles : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val parkour = intent.getSerializableExtra("parkour") as? Course
        val competitionStatus = intent.getSerializableExtra("competitionStatus") as Competition.Status

        setContent {
            ParkourTheme {
                val obstacleViewModel: ObstaclesViewModel = viewModel()
                parkour?.id?.let { LaunchedEffect(it) { obstacleViewModel.fetchCoursesObstacles(it) } }
                val obstacles by obstacleViewModel.obstaclesCourse.observeAsState(emptyList())

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ObstaclesPage(
                        obstacles = obstacles,
                        parkour = parkour,
                        competitionStatus = competitionStatus,
                        obstacleViewModel = obstacleViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ObstaclesPage(
    obstacles: List<CourseObstacle>,
    parkour: Course?,
    competitionStatus: Competition.Status,
    obstacleViewModel: ObstaclesViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("Obstacles", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        if (parkour == null) {
            Text("Aucune course dans ce parkour")
            return
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(obstacles) { obstacle ->
                ObstacleCard(obstacle, obstacleViewModel, competitionStatus)
            }
        }

    }
}

@Composable
fun ObstacleCard(
    obstacle: CourseObstacle,
    obstacleViewModel: ObstaclesViewModel,
    competitionStatus: Competition.Status
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(obstacle.obstacleName ?: "Unknown")

            if (EditionMode.isEnable.value && competitionStatus == Competition.Status.not_ready) {
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Supprimer")
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer cet obstacle ?") },
            confirmButton = {
                Button(
                    onClick = {
                        obstacleViewModel.removeObstacle(obstacle.courseObstacleId!!)
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



@Composable
fun DropDownMenuObstacle(parkourId: Int?) {
    val obstaclesViewModel: ObstaclesViewModel = viewModel()
    val obstacles by obstaclesViewModel.allObstacles.observeAsState(emptyList())

    var expanded by remember { mutableStateOf(false) }
    var selectedObstacle by remember { mutableStateOf<Obstacle?>(null) }

    Column {
        Text(
            text = selectedObstacle?.name ?: "Sélectionner un obstacle",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp)
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            obstacles.forEach { obstacle ->
                DropdownMenuItem(
                    onClick = {
                        selectedObstacle = obstacle
                        expanded = false
                    },
                    text = { Text(obstacle.name ?: "Unknown") }
                )
            }
        }

        parkourId?.let {
            Button(
                onClick = {
                    selectedObstacle?.id?.let { obstacleId ->
                        obstaclesViewModel.addObstacleCourse(it, AddCourseObstacleRequest(obstacleId))
                    }
                    selectedObstacle = null
                },
                enabled = selectedObstacle != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ajouter l'obstacle")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewObstaclesPage() {
    ParkourTheme {
        ObstaclesPage(emptyList(), null, Competition.Status.not_ready, viewModel())
    }
}
