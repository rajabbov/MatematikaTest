package com.example.matematikatest

data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val difficulty: String // "easy", "medium", "hard"
)
