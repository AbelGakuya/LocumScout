package com.locums.locumscout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.locums.locumscout.other.Constants.ACTION_SHOW_APPLICANTS_DETAIL_FRAGMENT

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToDoctorDetailsIfneeded(intent)

//        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
      //  setSupportActionBar(toolbar)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView) as BottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        bottomNavigationView.setOnNavigationItemReselectedListener { /* No Operation*/ }

        navHostFragment.findNavController()
            .addOnDestinationChangedListener{
                    _,destination,_ ->
                when(destination.id){
                    R.id.homeFragment, R.id.shiftDetailsFragment, R.id.shiftListFragment, R.id.userProfileFragment ->
                        bottomNavigationView.visibility = View.VISIBLE

                    else -> bottomNavigationView.visibility = View.GONE
                }
            }
    }

    private fun navigateToDoctorDetailsIfneeded(intent: Intent?) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        if (intent?.action == ACTION_SHOW_APPLICANTS_DETAIL_FRAGMENT){
            navHostFragment.findNavController().navigate(R.id.action_global_notifications_details_Fragment)
        }

    }
}