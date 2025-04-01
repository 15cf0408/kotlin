package com.but.parkour.concurrents.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.clientkotlin.models.Competitor
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.but.parkour.concurrents.viewmodel.GestionConcurrentViewModel

class GestionConcurrents : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionConcurrentsPage()
        }
    }
}

@Composable
fun GestionConcurrentsPage(
    modifier: Modifier = Modifier,
    viewModel: GestionConcurrentViewModel = viewModel(),
) {
    val competitors by viewModel.competitors.observeAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchAllCompetitors()
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text("Liste des concurrents")
        Button(onClick = {
            val intent = Intent(context, AjoutConcurrent::class.java)
            context.startActivity(intent)
        }) {
            Text("Ajouter un concurrent")
        }
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Rechercher un concurrent") },
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(competitors.filter {
                it.firstName?.contains(searchQuery, ignoreCase = true) == true ||
                        it.lastName?.contains(searchQuery, ignoreCase = true) == true
            }) { competitor ->
                CompetitorCard(competitor = competitor)
            }
        }
    }
}

@Composable
private fun CompetitorCard(
    competitor: Competitor,
    gestionConcurrentViewModel: GestionConcurrentViewModel = viewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("${competitor.firstName} ${competitor.lastName}")
        Text("Email: ${competitor.email ?: "Non renseigné"}")
        Text("Téléphone: ${competitor.phone ?: "Non renseigné"}")
        Text("Genre: ${if (competitor.gender == Competitor.Gender.H) "Homme" else "Femme"}")
        Text("Date de naissance: ${competitor.bornAt ?: "Non renseignée"}")



        Button(onClick = { showDeleteDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
            Text("Supprimer")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr de vouloir supprimer ce concurrent ?") },
            confirmButton = {
                Button(onClick = {
                    competitor.id?.let {
                        gestionConcurrentViewModel.deleteCompetitor(it)
                    }
                    showDeleteDialog = false
                }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}