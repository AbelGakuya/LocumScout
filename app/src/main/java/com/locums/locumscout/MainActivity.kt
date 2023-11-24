package com.locums.locumscout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.locums.locumscout.other.Constants.ACTION_SHOW_APPLICANTS_DETAIL_FRAGMENT
import com.locums.locumscout.ui.shift_listing_fragments.HomeFragment
import com.locums.locumscout.ui.shift_listing_fragments.OrdersFragment
import com.locums.locumscout.ui.shift_listing_fragments.ShiftListFragment
import com.locums.locumscout.ui.user_profile_fragments.UserProfileFragment

//import com.locums.locumscout.services.LocumBackgroundService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        if (intent.action != null){
//            navigateToDoctorDetailsIfneeded(intent)
//        } else {
//            navigateToSplash()
//        }

//        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
      //  setSupportActionBar(toolbar)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView) as BottomNavigationView

        val navController = findNavController(R.id.navHostFragment)
        bottomNavigationView.setupWithNavController(navController)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment


     //   bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        bottomNavigationView.setOnNavigationItemReselectedListener { /* No Operation*/ }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    // Handle home action
                    loadFragment(HomeFragment())
                    true
                }
                R.id.shiftListFragment -> {

                    // Handle dashboard action
                    loadFragment(ShiftListFragment())
                    true
                }
                R.id.ordersFragment -> {
                    // Handle notifications action
                    loadFragment(OrdersFragment())
                    true
                }
                
                R.id.userProfileFragment -> {
                    loadFragment(UserProfileFragment())
                    true
                }
                else -> false
            }
        }

        loadFragment(HomeFragment())
        navHostFragment.findNavController()
            .addOnDestinationChangedListener{
                    _,destination,_ ->
                when(destination.id){
                    R.id.homeFragment, R.id.shiftDetailsFragment, R.id.shiftListFragment,
                    R.id.userProfileFragment, R.id.ordersFragment,
                    R.id.notificationsFragment, R.id.notificationsDetailsFragment ->
                        bottomNavigationView.visibility = View.VISIBLE

                    else -> bottomNavigationView.visibility = View.GONE
                }
            }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.navHostFragment,fragment)
            .commit()

    }

    private fun navigateToSplash() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navHostFragment.findNavController().navigate(R.id.action_global_splash_Fragment)
    }

    private fun navigateToDoctorDetailsIfneeded(intent: Intent?) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        if (intent?.action == ACTION_SHOW_APPLICANTS_DETAIL_FRAGMENT){
            navHostFragment.findNavController().navigate(R.id.action_global_notifications_details_Fragment)
        }
    }
}