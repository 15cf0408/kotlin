package com.but.parkour.parkour.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseUpdate
import com.but.parkour.parkour.viewmodel.ParkourViewModel
import com.but.parkour.ui.theme.ParkourTheme
import org.burnoutcrew.reorderable.*
import androidx.compose.runtime.livedata.observeAsState


class ListeParkours : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competition = intent.getSerializableExtra("competition") as? Competition
        val competitionId = competition?.id

        setContent {
            ParkourTheme {
                val parkourViewModel: ParkourViewModel = viewModel()
                competitionId?.let { LaunchedEffect(it) { parkourViewModel.chargerCourses(it) } }
                val courses by parkourViewModel.courses.observeAsState(initial = emptyList())

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ParkoursPage(
                        modifier = Modifier.padding(innerPadding),
                        competition?.name ?: "Unknown",
                        courses,
                        competition
                    )
                }
            }
        }
    }
}

@Composable
fun ParkoursPage(modifier: Modifier = Modifier, compet: String, courses: List<Course>, competition: Competition?) {
    competition?.let {
        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = compet,
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))
            ListParkours(courses, Modifier.weight(1f), competition)
        }
    } ?: Text("Aucune compétition trouvée")
}

@Composable
fun ListParkours(courses: List<Course>, modifier: Modifier = Modifier, competition: Competition) {
    val context = LocalContext.current
    val parkourViewModel: ParkourViewModel = viewModel()
    val editionEnabled = remember { mutableStateOf(EditionMode.isEnable.value) }
    var reorderedCourses by remember { mutableStateOf(courses) }

    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            if (editionEnabled.value && competition.status == Competition.Status.not_ready) {
                reorderedCourses = reorderedCourses.toMutableList().apply { add(to.index, removeAt(from.index)) }
                parkourViewModel.chargerCourses(competition.id!!)
            }
        },
        onDragEnd = { _, _ ->
            if (editionEnabled.value && competition.status == Competition.Status.not_ready) {
                reorderedCourses.forEachIndexed { index, updatedCourse ->
                    parkourViewModel.updateCourse(updatedCourse.id!!, CourseUpdate(position = index + 1))
                }
                parkourViewModel.chargerCourses(competition.id!!)
            }
        }
    )

    LazyColumn(
        state = reorderState.listState,
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        items(items = reorderedCourses, key = { it.id!! }) { course ->
            ReorderableItem(reorderState, key = course.id!!) { isDragging ->
                CourseCard(
                    course = course,
                    onDetailsClick = { onCourseDetailsClick(context, course, competition) },
                    modifier = Modifier.detectReorder(reorderState).alpha(if (isDragging) 0.5f else 1f)
                )
            }
        }
    }

    if (editionEnabled.value && competition.status == Competition.Status.not_ready) {
        Button(
            onClick = { onItemClickAddCourse(context, competition) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ajouter une course")
        }
    }
}

@Composable
fun CourseCard(course: Course, onDetailsClick: (Course) -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = course.name ?: "Nom inconnu", style = MaterialTheme.typography.titleMedium)
            Text(text = "Position: ${course.position ?: "Non définie"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Durée max: ${course.maxDuration ?: "Non définie"} sec", style = MaterialTheme.typography.bodyMedium)
            Text(text = if (course.isOver == true) "Terminée" else "En cours", style = MaterialTheme.typography.bodyMedium)

            Button(onClick = { onDetailsClick(course) }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Text("Voir détails")
            }
        }
    }
}

fun onCourseDetailsClick(context: Context, course: Course, competition: Competition) {
    context.startActivity(Intent(context, DetailParkour::class.java).apply {
        putExtra("course", course)
        putExtra("competition", competition)
    })
}

fun onItemClickAddCourse(context: Context, competition: Competition) {
    context.startActivity(Intent(context, AjoutTrack::class.java).apply {
        putExtra("competition", competition)
    })
}
