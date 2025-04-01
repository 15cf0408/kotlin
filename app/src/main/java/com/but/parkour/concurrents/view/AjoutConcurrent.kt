package com.but.parkour.concurrents.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.but.parkour.clientkotlin.models.CompetitorCreate
import com.but.parkour.clientkotlin.models.CompetitorCreate.Gender
import com.but.parkour.concurrents.viewmodel.CompetitorViewModel
import java.time.LocalDate

class AjoutConcurrent : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                AjoutConcurrentForm(
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@Composable
fun AjoutConcurrentForm(modifier: Modifier = Modifier) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Genre") }
    var genderExpanded by remember { mutableStateOf(false) }
    var bornAt by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        Text("Ajouter un concurrent")

        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Prénom") }
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Nom") }
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Numéro de téléphone") }
        )

        Box(modifier = Modifier.clickable { genderExpanded = true }) {
            TextField(
                value = selectedGender,
                onValueChange = { },
                label = { Text("Genre") },
                readOnly = true
            )
            DropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Homme") },
                    onClick = {
                        selectedGender = "Homme"
                        genderExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Femme") },
                    onClick = {
                        selectedGender = "Femme"
                        genderExpanded = false
                    }
                )
            }
        }

        TextField(
            value = bornAt,
            onValueChange = { bornAt = it },
            label = { Text("Date de naissance (YYYY-MM-DD)") }
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = androidx.compose.ui.graphics.Color.Red
            )
        }

        Button(
            onClick = {
                errorMessage = ""
                val verif = verifChamps(
                    firstName,
                    lastName,
                    email,
                    phone,
                    selectedGender,
                    bornAt,
                )
                if (verif.isNotEmpty()) {
                    errorMessage = verif
                } else {
                    onAjoutCompetitorClick(
                        firstName,
                        lastName,
                        email,
                        phone,
                        selectedGender,
                        bornAt,
                        context,
                    )
                }
            }
        ) {
            Text("Ajouter")
        }
    }
}

private fun verifChamps(
    firstName: String,
    lastName: String,
    email: String,
    phone: String,
    gender: String,
    bornAt: String,
): String {
    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
        || phone.isEmpty() || gender == "Genre" || bornAt.isEmpty()) {
        return "Tous les champs sont obligatoires"
    }

    if (phone.toIntOrNull() == null || phone.length != 10) {
        return "Le numéro de téléphone doit être valide"
    }

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$".toRegex()
    if (!email.matches(emailRegex)) {
        return "L'adresse email n'est pas valide"
    }

    var validDate: Boolean
    try {
        LocalDate.parse(bornAt)
        validDate = true
    } catch (e: Exception) {
        validDate = false
    }

    if (!validDate) {
        return "La date doit être valide au format 'YYYY-MM-DD'"
    }

    return ""
}

fun onAjoutCompetitorClick(
    firstName: String,
    lastName: String,
    email: String,
    phone: String,
    gender: String,
    bornAt: String,
    context: Context
) {
    val competitor = CompetitorCreate(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        gender = when (gender) {
            "Homme" -> Gender.H
            "Femme" -> Gender.F
            else -> throw IllegalArgumentException("Genre invalide")
        },
        bornAt = LocalDate.parse(bornAt)
    )

    val competitorViewModel = CompetitorViewModel()
    competitorViewModel.addCompetitor(competitor)

    val intent = Intent(context, GestionConcurrents::class.java)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        AjoutConcurrentForm()
    }
}

@Composable
fun ParkourTheme(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}