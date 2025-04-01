package com.but.parkour.courses.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CourseCreate
import com.but.parkour.courses.ui.theme.ParkourTheme
import com.but.parkour.courses.viewmodel.ParkourViewModel

class AjoutParkour : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competition = intent.getSerializableExtra("competition") as? Competition
        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) { innerPadding ->
                    competition?.let {
                        AjtParkourPage(
                            modifier = Modifier.padding(innerPadding),
                            competition = it
                        )
                    } ?: run {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Aucune compétition trouvée", color = Color.Red, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AjtParkourPage(modifier: Modifier = Modifier, competition: Competition) {
    var nom by remember { mutableStateOf("") }
    var dureeMax by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val parkourViewModel = remember { ParkourViewModel() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Ajouter une course",
            modifier = Modifier.padding(32.dp),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        TextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        TextField(
            value = dureeMax,
            onValueChange = { dureeMax = it },
            label = { Text("Durée Maximum") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (nom.isBlank() || dureeMax.isBlank()) {
                        errorMessage = "Tous les champs sont obligatoires"
                    } else {
                        val dureeMaxInt = dureeMax.toIntOrNull()
                        if (dureeMaxInt == null) {
                            errorMessage = "La durée doit être un nombre entier"
                        } else {
                            errorMessage = ""
                            val course = CourseCreate(name = nom, maxDuration = dureeMaxInt, competitionId = competition.id ?: -1)
                            parkourViewModel.addCourse(course)
                            navigateToListeParkours(context, competition)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Ajouter", color = Color.White)
            }
            Button(
                onClick = { navigateToListeParkours(context, competition) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Annuler", color = Color.White)
            }
        }
    }
}

fun navigateToListeParkours(context: Context, competition: Competition) {
    val intent = Intent(context, ListeParkours::class.java).apply {
        putExtra("competition", competition)
    }
    context.startActivity(intent)
}