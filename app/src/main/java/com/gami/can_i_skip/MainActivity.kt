package com.gami.can_i_skip

import android.credentials.Credential
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gami.can_i_skip.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.KeyStore


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_main)

        val navMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val loginFragment = LoginFragment();
        val subjectFragment = SubjectFragment();
        val dayFragment = DayFragment();




        replaceFragment(loginFragment)




        Log.d("TEST", navMenu.toString())
        navMenu.setOnItemSelectedListener {
            Log.d("test", it.toString())
            when (it.itemId) {
                R.id.nav_day -> {

                    replaceFragment(dayFragment);true
                }

                R.id.nav_subject -> {

                    replaceFragment(subjectFragment)


                    true
                }

                else -> false

            }


        }


    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentHost, fragment).commit()
    }


}


/*    suspend fun getAttendanceSummary()
    {
        val attendanceSummary = mutableListOf<List<AttendanceSummary>>();
        val subjects = sdk.getSubjects();
        subjects.forEach{
            val attendance = sdk.getAttendanceSummary(it.id)
            attendanceSummary.add(attendance)
            Log.d("ATTENDANCE", "${it.name}, ${attendance}")
        }
    }*/

