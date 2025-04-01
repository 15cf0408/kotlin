package com.but.parkour.courses.view

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.but.parkour.EditionMode
import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseUpdate
import com.but.parkour.courses.viewmodel.ParkourViewModel
import com.but.parkour.ui.theme.ParkourTheme
import org.burnoutcrew.reorderable.*

class ListeParkours : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val competition = intent.getSerializableExtra("competition") as? Competition
        val competitionId = competition?.id
        Log.d("ListeParkour", "Competition: $competition")
        setContent {
            ParkourTheme {
                val parkourViewModel : ParkourViewModel = viewModel()
                competitionId?.let {
                    LaunchedEffect(it) {
                        parkourViewModel.fetchCourses(it)
                    }
                }
                val courses by parkourViewModel.coursesList.observeAsState(initial = emptyList())
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ParkoursPage(
                        modifier = Modifier.padding(innerPadding),
                        competitionName = competition?.name ?: "Inconnue",
                        courses = courses,
                        competition = competition
                    )
                }
            }
        }
    }
}

@Composable
fun ParkoursPage(
    modifier: Modifier = Modifier,
    competitionName: String,
    courses: List<Course>,
    competition: Competition?
) {
    if (competition == null) {
        Text("Aucune compétition trouvée", color = Color.Red, style = MaterialTheme.typography.bodyLarge)
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 32.dp)
        ) {
            Text(
                text = competitionName,
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(color = Color.DarkGray, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ListParkours(
                courses = courses,
                modifier = Modifier.weight(1f),
                competition = competition
            )
        }
    }
}

@Composable
fun ListParkours(
    courses: List<Course>,
    modifier: Modifier = Modifier,
    competition: Competition
) {
    Log.d("ListeParkours", "Competition: $competition")
    val context = LocalContext.current
    val competitionStatus = competition.status
    val parkourViewModel: ParkourViewModel = viewModel()
    val editionEnable = EditionMode.isEnable.value
    var reorderedCourses by remember { mutableStateOf(courses) }

    LaunchedEffect(courses) {
        reorderedCourses = courses
    }

    Log.d("ListeParkours", "Courses: $courses")
    Log.d("ListeParkours", "Reordered Courses: $reorderedCourses")

    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            if (editionEnable && competitionStatus == Competition.Status.not_ready) {
                reorderedCourses = reorderedCourses.toMutableList().apply {
                    add(to.index, removeAt(from.index))
                }
                parkourViewModel.fetchCourses(competition.id!!)
            }
        },
        onDragEnd = { fromIndex, toIndex ->
            if (editionEnable && competitionStatus == Competition.Status.not_ready) {
                reorderedCourses.forEachIndexed { index, updatedCourse ->
                    val courseUpdate = CourseUpdate(position = index + 1)
                    parkourViewModel.updateCourse(updatedCourse.id!!, courseUpdate)
                }
                parkourViewModel.fetchCourses(competition.id!!)
            }
        }
    )

    LazyColumn(
        state = state.listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .then(
                if (editionEnable && competitionStatus == Competition.Status.not_ready) {
                    Modifier.reorderable(state)
                } else {
                    Modifier
                }
            )
    ) {
        items(
            items = reorderedCourses,
            key = { it.id!! }
        ) { course ->
            ReorderableItem(state, key = course.id!!) { isDragging ->
                CourseCard(
                    course = course,
                    onDetailsClick = { onCourseDetailsClick(context, course, competition) },
                    modifier = Modifier
                        .then(
                            if (editionEnable && competitionStatus == Competition.Status.not_ready) {
                                Modifier
                                    .detectReorder(state)
                            } else {
                                Modifier
                            }
                        )
                )
            }
        }
    }

    if (EditionMode.isEnable.value && competitionStatus == Competition.Status.not_ready) {
        Button(
            onClick = { onItemClickAddCourse(context, competition) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Ajouter une course", color = Color.White)
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    onDetailsClick: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()


    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = course.name ?: "Nom inconnu",
                style = MaterialTheme.typography.titleMedium,

            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Position: ${course.position ?: "Non définie"}",

                    )
                    Text(
                        text = "Durée max: ${course.maxDuration ?: "Non définie"} sec",

                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (course.isOver == true) "Terminée" else "En cours",

                    )
                }
            }

            Button(
                onClick = { onDetailsClick(course) },
                modifier = Modifier
                    .fillMaxWidth()
                    ,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(" détails", color = Color.White)
            }
        }
    }
}

fun onCourseDetailsClick(context: Context, course: Course, competition: Competition) {
    val intent = Intent(context, DetailParkour::class.java)
    intent.putExtra("course", course)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}

fun onItemClickAddCourse(context: Context, competition: Competition) {
    val intent = Intent(context, AjoutParkour::class.java)
    intent.putExtra("competition", competition)
    context.startActivity(intent)
}
