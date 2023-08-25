package com.gami.can_i_skip

import android.credentials.Credential
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ViewCompositionStrategy


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


        App.loadCredentialsFromFile()
        App.loadAttendanceFromFile()
        App.loadTimetableFromFile()
        App.sortAttendanceAlphabetically()
        Log.d("Timetable" , App.timetable.toString())

        App.topBar = findViewById(R.id.topAppBar)
        val navMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val settingsFragment = SettingsFragment();
        val loginFragment = LoginFragment();
        val subjectFragment = SubjectFragment();
        val dayFragment = DayFragment();




        replaceFragment(loginFragment, getString(R.string.topbar_login))





        navMenu.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.nav_day -> {

                    replaceFragment(dayFragment, getString(R.string.topbar_by_day));true
                }

                R.id.nav_subject -> {

                    replaceFragment(subjectFragment, getString(R.string.topbar_by_subject))


                    true
                }
                R.id.nav_settings -> {
                    replaceFragment(settingsFragment, getString(R.string.topbar_settings))
                    true
                }


                else -> false

            }


        }


    }

    fun replaceFragment(fragment: Fragment, title: String = "Can I Skip?") {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentHost, fragment).commit()
        val TopBar =
            findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        App.topBar?.setTitle(title)

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

