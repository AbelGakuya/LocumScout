package com.locums.locumscout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
      //  setSupportActionBar(toolbar)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        bottomNavigationView.setOnNavigationItemReselectedListener { /* No Operation*/ }
    }
}