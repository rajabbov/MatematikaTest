package com.example.matematikatest

import android.content.Intent
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var questionCounterTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button

    private val questions = listOf(
        // Oson savollar (10 ta)
        Question("2 + 3 = ?", listOf("4", "5", "6", "7"), 1, "easy"),
        Question("5 - 2 = ?", listOf("3", "4", "2", "1"), 0, "easy"),
        Question("4 * 2 = ?", listOf("6", "8", "10", "12"), 1, "easy"),
        Question("10 / 2 = ?", listOf("3", "4", "5", "6"), 2, "easy"),
        Question("7 + 1 = ?", listOf("6", "7", "8", "9"), 2, "easy"),
        Question("3 * 3 = ?", listOf("6", "9", "12", "15"), 1, "easy"),
        Question("8 - 4 = ?", listOf("2", "3", "4", "5"), 2, "easy"),
        Question("6 + 2 = ?", listOf("7", "8", "9", "10"), 1, "easy"),
        Question("9 / 3 = ?", listOf("2", "3", "4", "5"), 1, "easy"),
        Question("5 + 4 = ?", listOf("7", "8", "9", "10"), 2, "easy"),
        // O‘rtacha savollar (10 ta)
        Question("12 * 3 = ?", listOf("36", "24", "48", "30"), 0, "medium"),
        Question("15 - 7 = ?", listOf("6", "7", "8", "9"), 2, "medium"),
        Question("20 / 4 = ?", listOf("4", "5", "6", "7"), 1, "medium"),
        Question("9 * 4 = ?", listOf("32", "36", "40", "44"), 1, "medium"),
        Question("25 + 13 = ?", listOf("36", "37", "38", "39"), 2, "medium"),
        Question("16 / 2 = ?", listOf("6", "7", "8", "9"), 2, "medium"),
        Question("7 * 5 = ?", listOf("30", "35", "40", "45"), 1, "medium"),
        Question("18 - 9 = ?", listOf("7", "8", "9", "10"), 2, "medium"),
        Question("11 * 3 = ?", listOf("30", "33", "36", "39"), 1, "medium"),
        Question("40 / 5 = ?", listOf("6", "7", "8", "9"), 2, "medium"),
        // Qiyin savollar (10 ta)
        Question("13 * 7 = ?", listOf("91", "84", "98", "77"), 0, "hard"),
        Question("56 / 8 = ?", listOf("6", "7", "8", "9"), 1, "hard"),
        Question("19 + 27 = ?", listOf("44", "45", "46", "47"), 2, "hard"),
        Question("15 * 6 = ?", listOf("80", "85", "90", "95"), 2, "hard"),
        Question("72 / 9 = ?", listOf("7", "8", "9", "10"), 1, "hard"),
        Question("24 - 17 = ?", listOf("5", "6", "7", "8"), 2, "hard"),
        Question("11 * 8 = ?", listOf("88", "80", "96", "84"), 0, "hard"),
        Question("45 / 5 = ?", listOf("7", "8", "9", "10"), 2, "hard"),
        Question("29 + 14 = ?", listOf("41", "42", "43", "44"), 2, "hard"),
        Question("16 * 4 = ?", listOf("60", "62", "64", "66"), 2, "hard")
    )

    private var currentQuestionIndex = 0
    private var score = 0
    private var selectedDifficulty = "easy"
    private var filteredQuestions = emptyList<Question>()
    private var selectedOptionIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        questionCounterTextView = findViewById(R.id.questionCounterTextView)
        questionTextView = findViewById(R.id.questionTextView)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)

        selectedDifficulty = intent.getStringExtra("DIFFICULTY") ?: "easy"
        filteredQuestions = questions.filter { it.difficulty == selectedDifficulty }.shuffled()

        displayQuestion()

        option1.setOnClickListener { selectOption(0, option1) }
        option2.setOnClickListener { selectOption(1, option2) }
        option3.setOnClickListener { selectOption(2, option3) }
        option4.setOnClickListener { selectOption(3, option4) }
    }

    private fun displayQuestion() {
        val question = filteredQuestions[currentQuestionIndex]
        questionCounterTextView.text = "Savol ${currentQuestionIndex + 1}/${filteredQuestions.size}"
        questionTextView.text = question.questionText
        option1.text = question.options[0]
        option2.text = question.options[1]
        option3.text = question.options[2]
        option4.text = question.options[3]
        selectedOptionIndex = -1
        resetOptionButtons()
    }

    private fun selectOption(index: Int, button: Button) {
        selectedOptionIndex = index
        button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        resetOptionButtons()
        button.setBackgroundResource(R.drawable.button_rounded_selected)
        checkAnswer()
        animateToNextQuestion()
    }

    private fun resetOptionButtons() {
        option1.setBackgroundResource(R.drawable.button_rounded)
        option2.setBackgroundResource(R.drawable.button_rounded)
        option3.setBackgroundResource(R.drawable.button_rounded)
        option4.setBackgroundResource(R.drawable.button_rounded)
    }

    private fun checkAnswer() {
        if (selectedOptionIndex != -1) {
            if (selectedOptionIndex == filteredQuestions[currentQuestionIndex].correctAnswerIndex) {
                score++
            }
        }
    }

    private fun animateToNextQuestion() {
        // Tasodifiy animatsiya tanlash
        val animationType = Random.nextInt(4) // 0: Slide, 1: Zoom, 2: Fade, 3: Rotate

        // Animatsiyalar
        val outAnimation: Animation
        val inAnimation: Animation

        when (animationType) {
            0 -> { // Slide
                outAnimation = AnimationSet(true).apply {
                    addAnimation(TranslateAnimation(0f, 300f, 0f, 0f).apply { duration = 300 })
                    addAnimation(AlphaAnimation(1f, 0f).apply { duration = 300 })
                }
                inAnimation = AnimationSet(true).apply {
                    addAnimation(TranslateAnimation(-300f, 0f, 0f, 0f).apply { duration = 300 })
                    addAnimation(AlphaAnimation(0f, 1f).apply { duration = 300 })
                }
            }
            1 -> { // Zoom
                outAnimation = AnimationSet(true).apply {
                    addAnimation(ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply { duration = 300 })
                    addAnimation(AlphaAnimation(1f, 0f).apply { duration = 300 })
                }
                inAnimation = AnimationSet(true).apply {
                    addAnimation(ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply { duration = 300 })
                    addAnimation(AlphaAnimation(0f, 1f).apply { duration = 300 })
                }
            }
            2 -> { // Fade
                outAnimation = AlphaAnimation(1f, 0f).apply { duration = 300 }
                inAnimation = AlphaAnimation(0f, 1f).apply { duration = 300 }
            }
            else -> { // Rotate
                outAnimation = AnimationSet(true).apply {
                    addAnimation(RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply { duration = 300 })
                    addAnimation(AlphaAnimation(1f, 0f).apply { duration = 300 })
                }
                inAnimation = AnimationSet(true).apply {
                    addAnimation(RotateAnimation(-90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply { duration = 300 })
                    addAnimation(AlphaAnimation(0f, 1f).apply { duration = 300 })
                }
            }
        }

        // Animatsiyani qo‘llash
        questionTextView.startAnimation(outAnimation)
        questionCounterTextView.startAnimation(outAnimation)
        option1.startAnimation(outAnimation)
        option2.startAnimation(outAnimation)
        option3.startAnimation(outAnimation)
        option4.startAnimation(outAnimation)

        outAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                // Animatsiya tugagach savolni yangilash
                currentQuestionIndex++
                if (currentQuestionIndex < filteredQuestions.size) {
                    displayQuestion()
                    questionTextView.startAnimation(inAnimation)
                    questionCounterTextView.startAnimation(inAnimation)
                    option1.startAnimation(inAnimation)
                    option2.startAnimation(inAnimation)
                    option3.startAnimation(inAnimation)
                    option4.startAnimation(inAnimation)
                } else {
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra("SCORE", score)
                    intent.putExtra("TOTAL", filteredQuestions.size)
                    startActivity(intent)
                    finish()
                }
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}