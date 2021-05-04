package com.example.ledapp

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import java.io.StringReader
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import okhttp3.RequestBody.Companion.toRequestBody
import com.bumptech.glide.Glide
class MainActivity : AppCompatActivity() {


    private lateinit var tempImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonOn: Button = findViewById(R.id.LED_ON)
        val buttonOff: Button = findViewById(R.id.LED_OFF)
        val buttonRefresh: Button = findViewById(R.id.Refresh)
        val temperature: String = "Temperature:       "
        textView.text = temperature

        tempImage = findViewById(R.id.temp_Image)


        buttonOn.setOnClickListener {
            funButtonOn()
            fanOn()


        }
        buttonOff.setOnClickListener {
            funButtonOff()
            fanOff()
        }
        buttonRefresh.setOnClickListener{
            funButtonRefresh()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    } //For the about me menu

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.add_action) {
            Toast.makeText(this, "By Sufyaan Tanveer",Toast.LENGTH_SHORT).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    } // For the about me menu


    private fun fanOn(){
        Glide.with(this).load(R.drawable.fanon).into(tempImage)
    }
    private fun fanOff(){
        tempImage.setImageResource(R.drawable.fanon)
    }
    private fun funButtonRefresh() { //This button refreshes the data and outputs it to the text view
        println("Attempting to get JSON data!")
        val url = "https://api.thingspeak.com/channels/1029606/feeds.json?results=1"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body) // This is the json body being created

                val gson = GsonBuilder().create()
                val json = gson.fromJson(body, Json::class.java)
                var field1 = (((json as Json).feeds as java.util.ArrayList<*>)[0] as Feed).field1 // THis section of the code breaks down the body and only gives field 1
                val strField1 = field1.toString()
                textView.text = ("Temperature: $strField1°c")

            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request!")
            }

        })



    }


    private fun funButtonOn() { //Sends a HTTP Request to make the field 1
        val payload = "test payload"
        println("Sending data!!!")

        val okHttpClient = OkHttpClient()
        val requestBody = payload.toRequestBody()
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("https://api.thingspeak.com/update?api_key=7VLC2AHIGM8GRKIK&field1=1")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle this
            }
        })

    }
    private fun funButtonOff(){ //Sends http request to make the field 0
        val payload = "test payload"
        println("Sending Data!!!")

        val okHttpClient = OkHttpClient()
        val requestBody = payload.toRequestBody()
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("https://api.thingspeak.com/update?api_key=7VLC2AHIGM8GRKIK&field1=0")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle this
            }
        })
    }
}

// These are the classes which structure the json so that the Gson can break it up
class Json(val feeds: List<Feed>)
class Feed(val field1: Double)

