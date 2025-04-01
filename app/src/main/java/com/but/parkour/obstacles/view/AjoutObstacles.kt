package com.but.parkour.obstacles.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.ObstacleCreate
import com.but.parkour.obstacles.viewmodel.ObstaclesViewModel
import com.but.parkour.ui.theme.ParkourTheme

class AjoutObstacles : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val course = intent.getSerializableExtra("course") as? Course
        enableEdgeToEdge()
        setContent {
            AjoutObstacleScreen(course = course)
        }
    }
}

@Composable
fun AjoutObstacleScreen(course: Course?) {
    var obstacleName by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (course == null) {
        DisplayMessage("Aucune course sélectionnée")
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HeaderText("Ajouter un obstacle")
            ObstacleNameInput(obstacleName) { obstacleName = it }
            AddObstacleButton {
                handleObstacleAddition(obstacleName, course, context)
            }
        }
    }
}

@Composable
fun HeaderText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun ObstacleNameInput(obstacleName: String, onNameChange: (String) -> Unit) {
    OutlinedTextField(
        value = obstacleName,
        onValueChange = onNameChange,
        label = { Text("Nom de l'obstacle") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AddObstacleButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text("Ajouter")
    }
}

@Composable
fun DisplayMessage(message: String) {
    Text(message)
}

fun handleObstacleAddition(obstacleName: String, course: Course, context: Context) {
    val obstaclesViewModel = ObstaclesViewModel()
    obstaclesViewModel.addObstacle(ObstacleCreate(name = obstacleName))

    val intent = Intent(context, ListeObstacles::class.java)
    intent.putExtra("parkour", course)
    context.startActivity(intent)
}
