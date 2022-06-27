package com.example.sirioecommerceapp.ui.act

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sirioecommerceapp.R
import com.example.sirioecommerceapp.utils.Constants
import com.example.sirioecommerceapp.utils.TextView

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val sharedPreferences =
            getSharedPreferences(Constants.SIRIO_PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        val id_first = findViewById<TextView>(R.id.id_first)
        id_first.text= "Hello $username."


    }
}