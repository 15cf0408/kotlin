package com.but.parkour.obstacles.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
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
        val parkourId = parkour?.id
        Log.d("ListeParkour", "Parkour: $parkour")
        setContent {
            ParkourTheme {
                val obstacleViewModel: ObstaclesViewModel = viewModel()
                parkourId?.let {
                    LaunchedEffect(it) {
                        obstacleViewModel.fetchCoursesObstacles(it)
                    }
                }
                val obstaclesCourse by obstacleViewModel.obstaclesCourse.observeAsState(initial = emptyList())

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ObstaclesPage(
                        obstacles = obstaclesCourse,
                        modifier = Modifier.padding(innerPadding),
                        parkour = parkour,
                        obstacleViewModel = obstacleViewModel,
                        competitionStatus = competitionStatus
                    )
                }
            }
        }
    }
}

@Composable
fun ObstaclesPage(
    obstacles: List<CourseObstacle>,
    modifier: Modifier = Modifier,
    parkour: Course?,
    obstacleViewModel: ObstaclesViewModel,
    competitionStatus: Competition.Status
) {
    var obstaclesList by remember { mutableStateOf(obstacles) }
    LaunchedEffect(obstacles) {
        obstaclesList = obstacles
    }
    if (parkour == null) {
        DisplayMessage("Aucune course dans ce parkour")
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HeaderText("Obstacles")
            ObstaclesList(
                obstacles = obstaclesList,
                modifier = modifier.weight(1f),
                obstacleViewModel = obstacleViewModel,
                competitionStatus = competitionStatus
            ) {
                obstacleViewModel.fetchCoursesObstacles(parkour.id!!)
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (EditionMode.isEnable.value) {
                CreateObstacleButton(parkour, competitionStatus)
            }
        }
    }
}

@Composable
fun ObstaclesList(
    obstacles: List<CourseObstacle>,
    modifier: Modifier = Modifier,
    obstacleViewModel: ObstaclesViewModel,
    competitionStatus: Competition.Status,
    onObstacleDeleted: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedObstacle by remember { mutableStateOf<CourseObstacle?>(null) }
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(obstacles) { item ->
            ObstacleCard(
                item = item,
                onDeleteClick = {
                    selectedObstacle = item
                    showDialog = true
                },
                isDeletable = EditionMode.isEnable.value && competitionStatus == Competition.Status.not_ready
            )
        }
    }

    if (showDialog && selectedObstacle != null) {
        ConfirmDeleteDialog(
            onConfirm = {
                selectedObstacle?.let { onObstacleDeleted() }
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun ObstacleCard(item: CourseObstacle, onDeleteClick: () -> Unit, isDeletable: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(3.dp, Color.Black, shape = MaterialTheme.shapes.medium)
            .padding(4.dp)
    ) {
        Text(
            text = item.obstacleName ?: "Unknown",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (isDeletable) {
            DeleteButton(onClick = onDeleteClick)
        }
    }
}

@Composable
fun DeleteButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text("Supprimer")
    }
}

@Composable
fun ConfirmDeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmation") },
        text = { Text("Êtes-vous sûr de vouloir supprimer cet obstacle ?") },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Oui")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Non")
            }
        }
    )
}

@Composable
fun CreateObstacleButton(parkour: Course, competitionStatus: Competition.Status) {
    if (competitionStatus == Competition.Status.not_ready) {
        ObstacleDropdownMenu(parkour.id)
    }
    CreateNewObstacleButton(parkour)
}

@Composable
fun ObstacleDropdownMenu(parkourId: Int?) {
    val obstaclesViewModel: ObstaclesViewModel = viewModel()
    obstaclesViewModel.fetchAllObstacles()
    val obstacles by obstaclesViewModel.allObstacles.observeAsState(initial = emptyList())

    var expanded by remember { mutableStateOf(false) }
    var selectedObstacle by remember { mutableStateOf<Obstacle?>(null) }

    Column {
        Text(
            text = selectedObstacle?.name ?: "Sélectionner un obstacle",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            obstacles.forEach { obstacle ->
                DropdownMenuItem(
                    onClick = {
                        selectedObstacle = obstacle
                        expanded = false
                    },
                    text = { Text(text = obstacle.name ?: "Unknown") }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (parkourId != null) {
            AddObstacleButton(
                isEnabled = selectedObstacle != null,
                parkourId = parkourId,
                selectedObstacle = selectedObstacle,
                onObstacleAdded = { selectedObstacle = null }
            )
        }
    }
}

@Composable
fun AddObstacleButton(
    isEnabled: Boolean,
    parkourId: Int,
    selectedObstacle: Obstacle?,
    onObstacleAdded: () -> Unit
) {
    val obstaclesViewModel: ObstaclesViewModel = viewModel()
    Button(
        onClick = {
            selectedObstacle?.id?.let { obstacleId ->
                obstaclesViewModel.addObstacleCourse(parkourId, AddCourseObstacleRequest(obstacleId))
            }
            onObstacleAdded()
        },
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Ajouter l'obstacle")
    }
}

@Composable
fun CreateNewObstacleButton(course: Course) {
    val context = LocalContext.current
    Button(
        onClick = { onCreateObstacleClick(context, course) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Créer un obstacle")
    }
}

fun onCreateObstacleClick(context: Context, course: Course) {
    val intent = Intent(context, AjoutObstacles::class.java)
    intent.putExtra("course", course)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun PreviewObstaclesPage() {
    ParkourTheme {
        //Preview content here if necessary
    }
}
