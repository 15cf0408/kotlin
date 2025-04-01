package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.competition.viewmodel.CompetitionViewModel
import com.but.parkour.ui.theme.ParkourTheme

class AjoutCompetition : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1E1E1E))
                ) { innerPadding ->
                    AjtCompetPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AjtCompetPage(modifier: Modifier = Modifier) {
    var nom by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Plusieurs essais ?") }
    var expanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("Genre") }
    var genderExpanded by remember { mutableStateOf(false) }
    var ageMin by remember { mutableStateOf("") }
    var ageMax by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color(0xFF2D2D2D)),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ajouter une compétition",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom", color = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(Color(0xFF3C3C3C))
        )

        TextField(
            value = ageMin,
            onValueChange = { ageMin = it },
            label = { Text("Âge Minimum", color = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(Color(0xFF3C3C3C))
        )

        TextField(
            value = ageMax,
            onValueChange = { ageMax = it },
            label = { Text("Âge Maximum", color = Color.White) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .background(Color(0xFF3C3C3C))
        )

        DropdownSelector("Genre", selectedGender, genderExpanded) { genderExpanded = it }
        DropdownSelector("Plusieurs essais ?", selectedOption, expanded) { expanded = it }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { handleAjouter(nom, ageMin, ageMax, selectedGender, selectedOption, context) { errorMessage = it } },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Ajouter", color = Color.White)
            }
            Button(
                onClick = { onClickAnnuler(context) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Annuler", color = Color.White)
            }
        }
    }
}
@Composable
fun DropdownSelector(title: String, selected: String, expanded: Boolean, onExpandChange: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { onExpandChange(true) }
    ) {
        Text(
            text = selected,
            color = Color.White
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandChange(false) }
        ) {
            DropdownMenuItem(
                text = { Text("Homme") },
                onClick = { onExpandChange(false) }
            )
            DropdownMenuItem(
                text = { Text("Femme") },
                onClick = { onExpandChange(false) }
            )
        }
    }
}

fun handleAjouter(nom: String, ageMin: String, ageMax: String, gender: String, multipleAttempts: String, context: Context, setError: (String) -> Unit) {
    if (nom.isEmpty() || ageMin.isEmpty() || ageMax.isEmpty() || gender == "Genre" || multipleAttempts == "Plusieurs essais ?") {
        setError("Tous les champs sont obligatoires")
        return
    }

    val ageMinInt = ageMin.toIntOrNull()
    val ageMaxInt = ageMax.toIntOrNull()
    if (ageMinInt == null || ageMaxInt == null || ageMinInt > ageMaxInt) {
        setError("L'âge minimum doit être inférieur à l'âge maximum")
        return
    }

    setError("")
    val competition = CompetitionCreate(
        name = nom,
        ageMin = ageMinInt,
        ageMax = ageMaxInt,
        gender = if (gender == "Homme") CompetitionCreate.Gender.H else CompetitionCreate.Gender.F,
        hasRetry = multipleAttempts == "Oui"
    )
    CompetitionViewModel().addCompetition(competition)
    context.startActivity(Intent(context, ListeCompetitions::class.java))
}

fun onClickAnnuler(context: Context) {
    context.startActivity(Intent(context, ListeCompetitions::class.java))
}

@Preview(showBackground = true)
@Composable
fun PreviewAjtCompetPage() {
    ParkourTheme { AjtCompetPage() }
}