package com.example.matematikatest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        val resultTextView = findViewById<TextView>(R.id.resultTextView)
        val resultProgressBar = findViewById<ProgressBar>(R.id.resultProgressBar)
        val restartButton = findViewById<Button>(R.id.restartButton)

        val score = intent.getIntExtra("SCORE", 0)
        val total = intent.getIntExtra("TOTAL", 0)
        val percentage = if (total > 0) (score * 100) / total else 0

        resultTextView.text = "Sizning natijangiz: $percentage%"
        resultProgressBar.progress = percentage

        restartButton.setOnClickListener {
            val intent = Intent(this, WelcomActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}