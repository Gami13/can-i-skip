package com.gami.can_i_skip

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.bottomnavigation.BottomNavigationView

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

class SettingsFragment : Fragment(R.layout.fragment_day) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var safetyAfterWeeks = App.safetyAfterWeeks
        var targetAttendance = App.targetAttendance
        var finalSteps = App.steps

        var currentlyEditingColorIdx = 0
        var stepsAmount = App.steps.size

        fun save() {
            App.safetyAfterWeeks = safetyAfterWeeks
            App.targetAttendance = targetAttendance
            if (finalSteps.size != stepsAmount) {
                finalSteps = finalSteps.copyOfRange(0, stepsAmount)
            }
            App.steps = finalSteps
            Log.d("SAVE", "WeeksSafety: $safetyAfterWeeks, TargetAttendance: $targetAttendance")
            App.saveEverythingToFile()
            Toast.makeText(view.context, "Saved", Toast.LENGTH_SHORT).show()
        }

        val navbar = activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
        navbar.visibility = View.VISIBLE
        App.topBar?.visibility = View.VISIBLE

        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            var showColorPicker by rememberSaveable { mutableStateOf((false)) }
            var changableSteps by rememberSaveable { mutableStateOf((App.steps)) }

            Mdc3Theme {

                /*Needs colors, and rebuild */
                Column(
                    modifier = Modifier

                        .verticalScroll(rememberScrollState())
                ) {


                    var safety by rememberSaveable { mutableStateOf(App.safetyAfterWeeks.toString()) }
                    ListItem(headlineContent = { Text(getString(R.string.set_safety)) },
                        supportingContent = {
                            Text(
                                "",
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        },

                        trailingContent = {
                            TextField(
                                value = safety,
                                onValueChange = {
                                    safety = it;
                                    if (it.isNotEmpty()) {
                                        safetyAfterWeeks = it.toInt()

                                    }
                                },
                                singleLine = true,
                                textStyle = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.width(75.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )


                        })
                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))


                    var attendance by rememberSaveable { mutableStateOf((App.targetAttendance * 100).toString()) }

                    ListItem(headlineContent = { Text(getString(R.string.set_attendance)) }, supportingContent = {
                        Text(
                            getString(R.string.set_attendance_desc),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },

                        trailingContent = {
                            TextField(value = attendance,
                                singleLine = true,
                                onValueChange = {
                                    attendance = it;
                                    targetAttendance = it.toDouble() / 100.0
                                },
                                textStyle = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.width(90.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                suffix = { Text("%") })


                        })
                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))


                    var steps by rememberSaveable { mutableStateOf((App.steps.size).toString()) }

                    ListItem(headlineContent = { Text(getString(R.string.set_steps)) }, supportingContent = {
                        Text(
                            getString(R.string.set_steps_desc),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },

                        trailingContent = {
                            TextField(
                                value = steps,
                                singleLine = true,
                                onValueChange = {
                                    steps = it;
                                    if (steps.isNotEmpty()) {
                                        stepsAmount = it.toInt()

                                    }
                                    Log.d("STEPS", stepsAmount.toString())
                                },
                                textStyle = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.width(90.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                                )


                        })
                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))

                    if (steps.isNotEmpty()) {
                        for (i in 0..steps.toInt() - 1) {
                            if (i >= changableSteps.size) {
                                changableSteps += Color.White
                            }
                            val stepRange = App.getStepRange(i)
                            Column(modifier = Modifier.padding(16.dp, 0.dp)) {
                                ListItem(headlineContent = { Text(getString(R.string.set_step, (i+1).toString())) },

                                    supportingContent = {
                                        Text(
                                            "${stepRange.lower*100}% - ${stepRange.upper*100}%",
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    },
                                    trailingContent = {
                                        Button(
                                            onClick = {
                                                showColorPicker = true
                                                currentlyEditingColorIdx = i

                                            },

                                            colors = ButtonDefaults.buttonColors(containerColor = changableSteps[i]),
                                            modifier = Modifier
                                                .height(40.dp)
                                                .width(40.dp)

                                        ) {


                                        }

                                    })
                                HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))
                            }
                        }
                    }



                    ListItem(headlineContent = { Text(getString(R.string.set_timetable)) }, supportingContent = {
                        Text(
                            getString(R.string.set_timetable_desc),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },

                        trailingContent = {
                            Button(
                                onClick = {
                                    GlobalScope.launch {
                                        App.timetable.build()
                                        App.saveTimetableToFile()
                                    }

                                },
                            ) {
                                Text(getString(R.string.rebuild))
                            }

                        })


                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))
                    ListItem(headlineContent = { Text(getString(R.string.log_out)) }, supportingContent = {
                        Text(
                            getString(R.string.log_out_desc),
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    },

                        trailingContent = {
                            Button(
                                onClick = {
                                    App.logOut()
                                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                                    transaction?.replace(R.id.fragmentHost, LoginFragment())
                                    transaction?.disallowAddToBackStack()
                                    transaction?.commit()



                                },
                            ) {
                                Text(getString(R.string.log_out))
                            }

                        })


                    HorizontalDivider(modifier = Modifier.padding(12.dp, 0.dp))


                }
                Box() {
                    FloatingActionButton(
                        onClick = { save() },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = getString(R.string.save),
                        )


                    }
                }
                if (showColorPicker) {
                    Log.d("COLOR", "SHOW")

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f))
                    ) {
                        val colorController = rememberColorPickerController(

                        )
                        Card(
                            modifier = Modifier
                                .width(320.dp)
                                .height(420.dp)
                                .padding(16.dp)
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = getString(R.string.pick_color), modifier = Modifier
                                    .padding(16.dp)
                                    .align(
                                        Alignment.CenterHorizontally
                                    ),

                                fontSize = 32.sp, textAlign = TextAlign.Center
                            )
                            var chosenColor = Color.Red

                            HsvColorPicker(controller = colorController,
                                modifier = Modifier
                                    .width(240.dp)
                                    .height(240.dp)
                                    .align(Alignment.CenterHorizontally),
                                onColorChanged = {

                                    chosenColor = it.color

                                })
                            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                Button(
                                    onClick = {
                                        showColorPicker = false
                                        changableSteps[currentlyEditingColorIdx] = chosenColor;
                                        finalSteps = changableSteps

                                    }, modifier = Modifier.padding(16.dp)

                                ) {
                                    Text(getString(R.string.done))
                                }
                                Button(
                                    onClick = {
                                        showColorPicker = false

                                    }, modifier = Modifier.padding(16.dp)

                                ) {
                                    Text(getString(R.string.cancel))
                                }
                            }


                        }


                    }
                } else {
                    Log.d("COLOR", "HIDE")
                }


            }
        }

    }

}