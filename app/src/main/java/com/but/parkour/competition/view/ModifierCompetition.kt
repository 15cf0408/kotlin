package com.but.parkour.competition.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CompetitionUpdate
import com.but.parkour.clientkotlin.models.CompetitionUpdate.Gender
import com.but.parkour.clientkotlin.models.CompetitionUpdate.Status
import com.but.parkour.ui.theme.ParkourTheme
import com.but.parkour.competition.viewmodel.CompetitionViewModel

class ModifierCompetition : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val competition = intent.getSerializableExtra("competition") as? Competition
        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    competition?.let {
                        ModifierCompetitionForm(
                            modifier = Modifier.padding(innerPadding),
                            oldCompetition = it
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModifierCompetitionForm(modifier: Modifier = Modifier, oldCompetition: Competition) {
    var name by remember { mutableStateOf(oldCompetition.name ?: "") }
    var ageMin by remember { mutableStateOf(oldCompetition.ageMin?.toString() ?: "") }
    var ageMax by remember { mutableStateOf(oldCompetition.ageMax?.toString() ?: "") }
    var gender by remember { mutableStateOf(oldCompetition.gender?.value ?: "") }
    var hasRetry by remember { mutableStateOf(oldCompetition.hasRetry ?: false) }
    var status by remember { mutableStateOf(oldCompetition.status?.value ?: "") }
    var errorMessage by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
            .background(Color(0xFFFFEBEE)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Modifier la compétition ${oldCompetition.name}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom", color = Color(0xFFD32F2F)) },
            textStyle = LocalTextStyle.current.copy(color = Color(0xFFD32F2F)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = ageMin,
            onValueChange = { ageMin = it },
            label = { Text("Âge Min", color = Color(0xFFD32F2F)) },
            textStyle = LocalTextStyle.current.copy(color = Color(0xFFD32F2F)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = ageMax,
            onValueChange = { ageMax = it },
            label = { Text("Âge Max", color = Color(0xFFD32F2F)) },
            textStyle = LocalTextStyle.current.copy(color = Color(0xFFD32F2F)),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Genre", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row {
            RadioButton(
                selected = gender == "H",
                onClick = { gender = "H" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFFD32F2F),
                    unselectedColor = Color.Gray
                )
            )
            Text("Homme", color = Color(0xFFD32F2F))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = gender == "F",
                onClick = { gender = "F" },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFFD32F2F),
                    unselectedColor = Color.Gray
                )
            )
            Text("Femme", color = Color(0xFFD32F2F))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Status", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            OutlinedTextField(
                value = status,
                onValueChange = { },
                label = { Text("Status", color = Color(0xFFD32F2F)) },
                textStyle = LocalTextStyle.current.copy(color = Color(0xFFD32F2F)),
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Status.values().forEach { statusOption ->
                    DropdownMenuItem(
                        onClick = {
                            status = statusOption.value
                            expanded = false
                        },
                        text = { Text(statusOption.value) }
                    )
                }
            }
        }
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(top = 16.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val validationResult = validateFields(name, ageMin, ageMax, gender, status)
                if (validationResult == null) {
                    val newCompetition = CompetitionUpdate(
                        name = name,
                        ageMin = ageMin.toIntOrNull(),
                        ageMax = ageMax.toIntOrNull(),
                        gender = Gender.values().find { it.value == gender },
                        hasRetry = hasRetry,
                        status = Status.values().find { it.value == status }
                    )
                    oldCompetition.id?.let {
                        modifCompetition(context, newCompetition, it)
                    }
                } else {
                    errorMessage = validationResult
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text("Modifier la compétition", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

fun validateFields(name: String, ageMin: String, ageMax: String, gender: String, status: String): String? {
    if (name.isBlank() || ageMin.isBlank() || ageMax.isBlank() || gender.isBlank() || status.isBlank()) {
        return "Tous les champs sont obligatoires"
    }
    if (gender != "H" && gender != "F") {
        return "Le genre doit être Homme ou Femme"
    }
    if (status !in listOf("not_ready", "not_started", "started", "finished")) {
        return "Le status doit être un des suivants : 'not_ready', 'not_started', 'started', 'finished'"
    }
    return null
}

fun modifCompetition(context: Context, competition: CompetitionUpdate, competitionId: Int) {
    val competitionViewModel = CompetitionViewModel()
    competitionViewModel.updateCompetition(competitionId, competition)

    val intent = Intent(context, ListeCompetitions::class.java)
    context.startActivity(intent)
}