package com.gami.can_i_skip

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.fragment.app.Fragment
import com.gami.can_i_skip.App.Companion.attendance
import com.gami.can_i_skip.App.Companion.prepareSDK
import com.gami.can_i_skip.App.Companion.prettyAttendance
import com.gami.can_i_skip.App.Companion.sdk
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.wulkanowy.sdk.pojo.AttendanceSummary
import kotlinx.coroutines.runBlocking
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import java.time.DayOfWeek
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton

import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.dynamicDarkColorScheme

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.gami.can_i_skip.App.Companion.timetable
import com.google.accompanist.themeadapter.material3.Mdc3Theme


import com.google.android.material.color.DynamicColors
import io.github.wulkanowy.sdk.pojo.Timetable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class DayFragment : Fragment(R.layout.fragment_subject) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navbar = activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
        App.topBar?.visibility = View.VISIBLE
        navbar.visibility = View.VISIBLE
        val composeView =
            view.findViewById<androidx.compose.ui.platform.ComposeView>(R.id.compose_view)


        composeView.setContent {

            Mdc3Theme {


                Column(
                    modifier = Modifier

                        .verticalScroll(rememberScrollState())
                ) {

                    val mondaySkips = timetable.howManyDaysCanSkip(DayOfWeek.MONDAY)
                    val mondayColor = timetable.getSafetyColorDay(mondaySkips)

                    ListItem(
                        headlineContent = { Text(getString(R.string.monday)) },
                        supportingContent = {
                            Text(

                                getString(R.string.current_attendance_rate, timetable.getDayAttendance(DayOfWeek.MONDAY).toString()),
                                fontSize = 12.sp
                            )
                        },
                        trailingContent = {
                            Text(
                                mondaySkips.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = mondayColor
                            )
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))
                    val tuesdaySkips = timetable.howManyDaysCanSkip(DayOfWeek.TUESDAY)
                    val tuesdayColor = timetable.getSafetyColorDay(tuesdaySkips)

                    ListItem(
                        headlineContent = { Text(getString(R.string.tuesday)) },
                        supportingContent = {
                            Text(

                                getString(R.string.current_attendance_rate, timetable.getDayAttendance(DayOfWeek.TUESDAY).toString()),
                                fontSize = 12.sp
                            )
                        },
                        trailingContent = {
                            Text(
                                tuesdaySkips.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = tuesdayColor
                            )
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))
                    val wednesdaySkips = timetable.howManyDaysCanSkip(DayOfWeek.WEDNESDAY)
                    val wednesdayColor = timetable.getSafetyColorDay(wednesdaySkips)

                    ListItem(
                        headlineContent = { Text(getString(R.string.wednesday)) },
                        supportingContent = {
                            Text(

                                getString(R.string.current_attendance_rate, timetable.getDayAttendance(DayOfWeek.WEDNESDAY).toString()),
                                fontSize = 12.sp
                            )
                        },
                        trailingContent = {
                            Text(
                                wednesdaySkips.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = wednesdayColor
                            )
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))
                    val thursdaySkips = timetable.howManyDaysCanSkip(DayOfWeek.THURSDAY)
                    val thursdayColor = timetable.getSafetyColorDay(thursdaySkips)

                    ListItem(
                        headlineContent = { Text(getString(R.string.thursday)) },
                        supportingContent = {
                            Text(

                                getString(R.string.current_attendance_rate, timetable.getDayAttendance(DayOfWeek.THURSDAY).toString()),
                                fontSize = 12.sp
                            )
                        },
                        trailingContent = {
                            Text(
                                thursdaySkips.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = thursdayColor
                            )
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))

                    val fridaySkips = timetable.howManyDaysCanSkip(DayOfWeek.FRIDAY)
                    val fridayColor = timetable.getSafetyColorDay(fridaySkips)

                    ListItem(
                        headlineContent = { Text(getString(R.string.friday)) },
                        supportingContent = {
                            Text(

                                getString(R.string.current_attendance_rate, timetable.getDayAttendance(DayOfWeek.FRIDAY).toString()),
                                fontSize = 12.sp
                            )
                        },
                        trailingContent = {
                            Text(
                                fridaySkips.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = fridayColor
                            )
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))





                }


            }


            /*Column(
                modifier = Modifier

                    .verticalScroll(rememberScrollState())
            ) {


                prettyAttendance.forEach {
                    if (it.totalClasses != 0) {


                        ListItem(

                            headlineContent = { Text(it.name) },
                            trailingContent = { Text(it.howManyClassesCanSkip().toString()) },

                            )

                    }


                }
            }*/

        }


        Log.d(
            "DATA",
            App.prettyAttendance.first().toString() + " " + App.prettyAttendance.first()
                .howManyClassesCanSkip().toString()
        )

        /*     testingButton.setOnClickListener {
                 runBlocking {
                   prepareSDK()
                     val subjects = sdk.getSubjects()
                     Log.d("DATA", subjects.toString())

                 }


             }*/


    }

}
