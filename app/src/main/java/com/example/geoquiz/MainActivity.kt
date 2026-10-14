package com.example.geoquiz

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoquiz.ui.theme.GeoQuizTheme
import kotlinx.coroutines.delay

data class Question(
    val questionText: String,
    val answer: Boolean
)

fun getQuestions(): List<Question> {
    return listOf(
        Question("Canberra is the capital of Australia.", true),
        Question("The Pacific Ocean is larger than the Atlantic Ocean.", true),
        Question("The Suez Canal connects the Red Sea and the Indian Ocean.", false),
        Question("The source of the Nile River is in Egypt.", false),
        Question("The Amazon River is the longest river in the Americas.", true),
        Question("Lake Baikal is the world's oldest and deepest freshwater lake.", true)
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoQuizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DemoScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DemoText(message: String, fontSize: TextUnit) {
    Text(
        text = message,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun DemoScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val questions = getQuestions()

    var currentIndex by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    var quizFinished by remember { mutableStateOf(false) }

    val currentQuestion = questions[currentIndex]
    val isLastQuestion = currentIndex == questions.size - 1

    fun checkAnswer(userAnswer: Boolean) {
        if (!answered) {
            answered = true
            if (userAnswer == currentQuestion.answer) {
                correctAnswers++
                Toast.makeText(context, "Правильно!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Неправильно!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun nextQuestion() {
        if (currentIndex < questions.size - 1) {
            currentIndex++
            answered = false
        }
    }

    LaunchedEffect(answered, isLastQuestion) {
        if (answered && isLastQuestion && !quizFinished) {
            quizFinished = true
            delay(1500)
            Toast.makeText(
                context,
                "Тест завершён! Правильных ответов: $correctAnswers из ${questions.size}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        DemoText(message = "GeoQuiz", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = currentQuestion.questionText,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (!answered) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { checkAnswer(true) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF27A6F5),
                        contentColor = Color.White
                    )
                ) {
                    Text("TRUE", fontSize = 18.sp)
                }

                Button(
                    onClick = { checkAnswer(false) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF268DC),
                        contentColor = Color.White
                    )
                ) {
                    Text("FALSE", fontSize = 18.sp)
                }
            }
        } else {
            Text(
                text = "Правильный ответ: ${currentQuestion.answer}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF27A6F5)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        if (answered && !isLastQuestion) {
            Button(
                onClick = { nextQuestion() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("NEXT", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Вопрос ${currentIndex + 1} из ${questions.size}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Правильных ответов: $correctAnswers",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DemoScreenPreview() {
    GeoQuizTheme {
        DemoScreen()
    }
}