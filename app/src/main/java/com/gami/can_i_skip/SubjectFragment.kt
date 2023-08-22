package com.gami.can_i_skip

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.gami.can_i_skip.App.Companion.attendance
import com.gami.can_i_skip.App.Companion.sdk
import com.gami.can_i_skip.App.Companion.subjects
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.wulkanowy.sdk.pojo.AttendanceSummary
import kotlinx.coroutines.runBlocking

class SubjectFragment : Fragment(R.layout.fragment_subject) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navbar = activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
        navbar.visibility = View.VISIBLE
        val testingButton = view.findViewById<View>(R.id.testingButton)

        testingButton.setOnClickListener {


            runBlocking {
                var prettySubjects = listOf<SubjectPretty>();
                val subjects = sdk.getSubjects();
                var attendances: List<AttendanceSummary> = listOf()
                App.subjects = subjects

                subjects.forEach {
                    val attendance = sdk.getAttendanceSummary(it.id)
                    Log.d("ATTENDANCE", "${it.name}, ${attendance}")
                    App.attendance += attendance
                    var totalClasses = 0;
                    var presence = 0;
                    var absence = 0;
                    var absenceExcused = 0;
                    var absenceForSchoolReasons = 0;
                    var lateness = 0;
                    var latenessExcused = 0;
                    var exemption = 0;
                    attendance.forEach {
                        totalClasses += it.presence + it.absence + it.absenceExcused + it.absenceForSchoolReasons + it.lateness + it.latenessExcused + it.exemption
                        presence += it.presence
                        absence += it.absence
                        absenceExcused += it.absenceExcused
                        absenceForSchoolReasons += it.absenceForSchoolReasons
                        lateness += it.lateness
                        latenessExcused += it.latenessExcused
                        exemption += it.exemption
                    }
                    prettySubjects += SubjectPretty(
                        it.id,
                        it.name,
                        totalClasses,
                        presence,
                        absence,
                        absenceExcused,
                        absenceForSchoolReasons,
                        lateness,
                        latenessExcused,
                        exemption,
                        attendance
                    )


                }
                App.prettySubjects = prettySubjects
                Log.d("ATTENDANCE", "${attendance}")
                Log.d("SUBJECTS", "${subjects}")
                Log.d("PRETTY SUBJECTS", "${prettySubjects}")
            }


        }


    }

}