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


class SubjectFragment : Fragment(R.layout.fragment_subject) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navbar = activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
        navbar.visibility = View.VISIBLE
        App.topBar?.visibility = View.VISIBLE
        
        val composeView =
            view.findViewById<androidx.compose.ui.platform.ComposeView>(R.id.compose_view)

        composeView.setContent {

            Mdc3Theme {


                Column(
                    modifier = Modifier

                        .verticalScroll(rememberScrollState())

                ) {
                    prettyAttendance.forEach {
                        if (it.totalClasses != 0) {
                            val ratio = it.howCloseToSafetySubject()

                            if (ratio == -1.0) {
                                ListItem(
                                    headlineContent = { Text(it.name) },
                                    supportingContent = {
                                        Text(
                                            getString(
                                                R.string.current_attendance,
                                                it.getAttendancePercentage().toString()
                                            ), fontSize = 12.sp
                                        )
                                    },


                                    trailingContent = {
                                        Text(
                                            it.howManyClassesCanSkip().toString(),
                                            fontSize = 20.sp
                                        )
                                    }
                                )
                            } else {

                                val color = it.getSafetyColorSubject(ratio)

                                ListItem(
                                    headlineContent = { Text(it.name) },
                                    supportingContent = {
                                        Text(
                                            getString(
                                                R.string.current_attendance,
                                                it.getAttendancePercentage().toString()
                                            ), fontSize = 12.sp
                                        )
                                    },


                                    trailingContent = {
                                        Text(
                                            it.howManyClassesCanSkip().toString(),
                                            fontSize = 20.sp,
                                            color = color,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                )
                            }

                            HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))
                        }

                    }



                }


            }




        }


        Log.d(
            "DATA",
            App.prettyAttendance.first().toString() + " " + App.prettyAttendance.first()
                .howManyClassesCanSkip().toString()
        )


    }

}
