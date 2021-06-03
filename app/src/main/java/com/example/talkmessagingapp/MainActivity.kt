package com.example.talkmessagingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.talkmessagingapp.phoneauth.EnterPhoneno
import com.example.talkmessagingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

         binding.Loginpage.setOnClickListener {
             val intent = Intent(this, EnterPhoneno::class.java)
             startActivity(intent)
         }
    }
}