package com.example.digidentitytestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.digidentitytestapp.presentation.screen.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("MyLogTag activity on create")
        println("MyLogTag activity ${hashCode()}")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_view, MainFragment.newInstance())
            .commit()
    }
}