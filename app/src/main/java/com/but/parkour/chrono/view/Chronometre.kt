package com.but.parkour.chrono.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseObstacle
import com.but.parkour.parkour.viewmodel.ChronometreViewModel
import com.but.parkour.ui.theme.ParkourTheme
import kotlinx.coroutines.delay

class Chronometre : ComponentActivity() {
    private val viewModel: ChronometreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val competition = intent.getSerializableExtra("competition") as? Competition
            val course = intent.getSerializableExtra("course") as? Course

            ParkourTheme {
                competition?.hasRetry?.let { retryAllowed ->
                    course?.id?.let { courseId ->
                        ChronometreScreen(viewModel, courseId, retryAllowed)
                    }
                }
            }
        }
    }
}

@Composable
fun ChronometreScreen(viewModel: ChronometreViewModel, parkourId: Int, hasRetry: Boolean) {
    val obstacles by viewModel.obstacles.collectAsState(emptyList())
    var isRunning by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf(0L) }
    var lastLapTime by remember { mutableStateOf(0L) }
    var currentObstacleIndex by remember { mutableStateOf(0) }
    var hasFallen by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }
    val laps = remember { mutableStateListOf<Pair<String, String>>() }

    LaunchedEffect(isRunning) {
        val startTime = System.currentTimeMillis() - currentTime
        while (isRunning) {
            currentTime = System.currentTimeMillis() - startTime
            delay(10L)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Obstacle: ${obstacles.getOrNull(currentObstacleIndex)?.obstacleName ?: "N/A"}", style = MaterialTheme.typography.headlineMedium)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(formatTime(currentTime), style = MaterialTheme.typography.displayLarge)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { isRunning = !isRunning }) {
                Text(if (isRunning) "Pause" else "Démarrer")
            }
            Button(onClick = {
                if (isRunning) {
                    val lapTime = currentTime - lastLapTime
                    laps.add(obstacles.getOrNull(currentObstacleIndex)?.obstacleName.orEmpty() to formatTime(lapTime))
                    lastLapTime = currentTime
                    if (currentObstacleIndex == obstacles.lastIndex) {
                        isRunning = false
                        isFinished = true
                    } else {
                        currentObstacleIndex++
                    }
                } else {
                    currentTime = 0L
                    lastLapTime = 0L
                    laps.clear()
                    currentObstacleIndex = 0
                    isFinished = false
                    hasFallen = false
                }
            }) {
                Text(if (isRunning) "Tour" else "Réinitialiser")
            }
        }

        if (hasRetry && !isRunning) {
            Button(onClick = { hasFallen = true; currentTime = laps.sumOf { parseTime(it.second) } }) {
                Text("Recommencer obstacle")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
            items(laps) { (obstacleName, lapTime) ->
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(obstacleName, style = MaterialTheme.typography.bodyLarge)
                        Text(lapTime, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

fun formatTime(time: Long): String {
    val minutes = (time / 60000) % 60
    val seconds = (time / 1000) % 60
    val milliseconds = time % 1000
    return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds)
}

fun parseTime(timeString: String): Long {
    val parts = timeString.split(":").map { it.toIntOrNull() ?: 0 }
    return if (parts.size == 3) (parts[0] * 60000L) + (parts[1] * 1000L) + parts[2] else 0L
}
