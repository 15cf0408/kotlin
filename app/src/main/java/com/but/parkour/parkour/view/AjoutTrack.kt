package com.but.parkour.parkour.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CourseCreate
import com.but.parkour.parkour.ui.theme.ParkourTheme
import com.but.parkour.parkour.viewmodel.ParkourViewModel

class AjoutTrack : ComponentActivity() {
    private val parkourViewModel: ParkourViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                val competition = intent.getSerializableExtra("competition") as? Competition
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    competition?.let {
                        AjtTrackPage(
                            modifier = Modifier.padding(innerPadding),
                            competition = it,
                            onTrackAdded = { track ->
                                parkourViewModel.ajoutCourse(track)
                                navigateToListeParkours(this@AjoutTrack, it)
                            }
                        )
                    } ?: run {
                        ErrorMessage("Aucune compétition trouvée")
                    }
                }
            }
        }
    }
}

@Composable
fun AjtTrackPage(modifier: Modifier = Modifier, competition: Competition, onTrackAdded: (CourseCreate) -> Unit) {
    var nom by remember { mutableStateOf("") }
    var dureeMax by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Ajouter un parcours", modifier = Modifier.padding(32.dp))

        InputField(label = "Nom", value = nom, onValueChange = { nom = it })
        InputField(label = "Durée Maximum", value = dureeMax, onValueChange = { dureeMax = it }, isNumeric = true)

        if (errorMessage.isNotEmpty()) {
            ErrorMessage(errorMessage)
        }

        ActionButtons(
            onAddClick = {
                validateAndSubmit(nom, dureeMax, competition, onTrackAdded, { errorMessage = it })
            },
            onCancelClick = { navigateToListeParkours(context, competition) }
        )
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, isNumeric: Boolean = false) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = if (isNumeric) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun ActionButtons(onAddClick: () -> Unit, onCancelClick: () -> Unit) {
    Row {
        Button(onClick = onAddClick, modifier = Modifier.padding(top = 32.dp)) {
            Text("Ajouter")
        }
        Button(onClick = onCancelClick, modifier = Modifier.padding(top = 32.dp, start = 64.dp)) {
            Text("Annuler")
        }
    }
}

fun validateAndSubmit(nom: String, dureeMax: String, competition: Competition, onTrackAdded: (CourseCreate) -> Unit, onError: (String) -> Unit) {
    if (nom.isEmpty() || dureeMax.isEmpty()) {
        onError("Tous les champs sont obligatoires")
        return
    }
    val dureeMaxInt = dureeMax.toIntOrNull()
    if (dureeMaxInt == null) {
        onError("La durée doit être un nombre entier")
        return
    }
    onError("")
    val track = CourseCreate(
        name = nom,
        maxDuration = dureeMaxInt,
        competitionId = competition.id ?: -1
    )
    onTrackAdded(track)
}

fun navigateToListeParkours(context: Context, competition: Competition) {
    context.startActivity(Intent(context, ListeParkours::class.java).apply {
        putExtra("competition", competition)
    })
}
