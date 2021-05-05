package com.example.ledapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val github: Button = findViewById(R.id.button)
        button.setOnClickListener{
            gitButton()
        }

        val actionBar = supportActionBar

        actionBar!!.title = "Help" // Changes title of second activity to help

        actionBar.setDisplayHomeAsUpEnabled(true) // Creates back button
    }

    fun gitButton(){
        val url = "https://github.com/Sufyaaaaaaaan"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}