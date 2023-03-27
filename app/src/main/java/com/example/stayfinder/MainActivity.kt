package com.example.stayfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.stayfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var myActionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarSetup()
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                }

                R.id.saved -> {
                    replaceFragment(SavedFragment())
                }

                R.id.bookings -> {
                    replaceFragment(BookingFragment())
                }

                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_msg -> {
                Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show()
            }

            R.id.ic_notice -> {
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSetup() {
        myActionBar = supportActionBar
        myActionBar?.title = "StayFinder"

        myActionBar?.setDisplayShowHomeEnabled(true)
        myActionBar?.setDisplayUseLogoEnabled(true)
    }
}