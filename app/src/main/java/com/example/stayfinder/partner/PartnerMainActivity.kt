package com.example.stayfinder.partner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.stayfinder.R
import com.example.stayfinder.partner.property.PartnerPropertiesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class PartnerMainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.partner_activity__main)

        bottomNavigationView = findViewById(R.id.bottomPartnerNavigationView)
        supportActionBar?.title = "StayFinder for Partner"
        replaceFragment(PartnerBookingFragment())

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.partner_bookings -> {
                    replaceFragment(PartnerBookingFragment())
                }

                R.id.partner_mess -> {
                    replaceFragment(PartnerMessageFragment())
                }

                R.id.partner_properties -> {
                    replaceFragment(PartnerPropertiesFragment())
                }

                R.id.partner_more -> {
                    replaceFragment(PartnerMoreFragment())
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.partner_frameLayout, fragment)
        fragmentTransaction.commit()
    }
}