package com.example.pexeso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

lateinit var startBtn : Button
lateinit var leadBtn : Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn = findViewById(R.id.btnStart)
        leadBtn = findViewById(R.id.btnLead)

        startBtn.setOnClickListener()
        {
            val intent = Intent(this, startGame::class.java)
            startActivity(intent)
        }
        leadBtn.setOnClickListener()
        {
            val intent = Intent(this, Leaderboards::class.java)
            startActivity(intent)
        }

    }
}