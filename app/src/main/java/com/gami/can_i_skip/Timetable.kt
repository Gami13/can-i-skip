package com.gami.can_i_skip

import android.util.Log
import androidx.compose.ui.graphics.Color
import io.github.wulkanowy.sdk.pojo.Timetable
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
class Timetable(

) {
    var monday: List<Subject> = listOf();
    var tuesday: List<Subject> = listOf();
    var wednesday: List<Subject> = listOf();
    var thursday: List<Subject> = listOf();
    var friday: List<Subject> = listOf();
    var subjectUnique: List<String> = listOf();
    var subjectOccurence: Map<String, Int> = mapOf();
    var mondayOccurence: Map<String, Int> = mapOf();
    var tuesdayOccurence: Map<String, Int> = mapOf();
    var wednesdayOccurence: Map<String, Int> = mapOf();
    var thursdayOccurence: Map<String, Int> = mapOf();
    var fridayOccurence: Map<String, Int> = mapOf();


    override fun toString(): String {
        return "Timetable(m=$monday, t=$tuesday, w=$wednesday, t=$thursday, f=$friday, subjectUnique=$subjectUnique, subjectOccurence=$subjectOccurence)"
    }

    fun reset() {
        monday = listOf();
        tuesday = listOf();
        wednesday = listOf();
        thursday = listOf();
        friday = listOf();
        subjectUnique = listOf();
        subjectOccurence = mapOf();
        mondayOccurence = mapOf();
        tuesdayOccurence = mapOf();
        wednesdayOccurence = mapOf();
        thursdayOccurence = mapOf();
        fridayOccurence = mapOf();

    }

    suspend fun build() {
        reset()
        var lastMonday = App.getLastMonday();
        var lastFriday = App.getLastFriday();
        //convert to java local date
        Log.d("MONDAY", lastMonday.toString())
        Log.d("FRIDAY", lastFriday.toString())

        if (!App.isSdkPrepared) {
            App.prepareSDK()
        }
        var timeTable: Timetable = App.sdk.getTimetable(lastMonday, lastFriday);
        var canLeaveLoop = false;

        while (!canLeaveLoop) {

            Log.d("Looped", timeTable.lessons.size.toString())
            //find week with lessons and no changes
            if (timeTable.lessons.size > 0) {
                val lessonsWithChange = timeTable.lessons.filter { it.changes == true }
                val uniqueDates = timeTable.lessons.map { it.date }.distinct()


                if (lessonsWithChange.size == 0 && uniqueDates.size == 5) {
                    canLeaveLoop = true;
                    Log.d("LOOP", "Can leave loop")
                }
            }
            if (!canLeaveLoop) {
                lastMonday = lastMonday.minusDays(7);
                lastFriday = lastFriday.minusDays(7);
                timeTable = App.sdk.getTimetable(lastMonday, lastFriday);
            }


        }

        var uniqueSubjects = mutableSetOf<String>()
        var subjectOccurence = mutableMapOf<String, Int>()
        timeTable.lessons.forEach {

            val dayOfWeek = it.date.dayOfWeek;

            Log.d(it.subject, it.toString() + " " + dayOfWeek.toString())

            when (dayOfWeek) {
                DayOfWeek.MONDAY -> monday = monday + Subject(it.subject, it.teacher)
                DayOfWeek.TUESDAY -> tuesday = tuesday + Subject(it.subject, it.teacher)
                DayOfWeek.WEDNESDAY -> wednesday = wednesday + Subject(it.subject, it.teacher)
                DayOfWeek.THURSDAY -> thursday = thursday + Subject(it.subject, it.teacher)
                DayOfWeek.FRIDAY -> friday = friday + Subject(it.subject, it.teacher)
                else -> Log.d("TimeTable", "Unknown day of week")
            }
            uniqueSubjects.add(it.subject)
            if (subjectOccurence.containsKey(it.subject)) {
                subjectOccurence[it.subject] = subjectOccurence[it.subject]!! + 1
            } else {
                subjectOccurence[it.subject] = 1
            }

        }
        subjectUnique = uniqueSubjects.toList()
        this.subjectOccurence = subjectOccurence.toMap()
        this.mondayOccurence = monday.groupingBy { it.name }.eachCount()
        this.tuesdayOccurence = tuesday.groupingBy { it.name }.eachCount()
        this.wednesdayOccurence = wednesday.groupingBy { it.name }.eachCount()
        this.thursdayOccurence = thursday.groupingBy { it.name }.eachCount()
        this.fridayOccurence = friday.groupingBy { it.name }.eachCount()



        Log.d("SUBJECTS", uniqueSubjects.toString())
        Log.d("OCCURENCE", subjectOccurence.toString())
        Log.d("MONDAY", mondayOccurence.toString())


        Log.d("MONDAY", monday.toString())
        Log.d("TUESDAY", tuesday.toString())
        Log.d("WEDNESDAY", wednesday.toString())
        Log.d("THURSDAY", thursday.toString())
        Log.d("FRIDAY", friday.toString())


    }
    fun getDayAttendance(dayOfWeek: DayOfWeek):Double
    {
        var subjects : Map<String,Int> = mapOf()
        when (dayOfWeek){
            DayOfWeek.MONDAY -> subjects = monday.groupingBy { it.name }.eachCount()
            DayOfWeek.TUESDAY -> subjects = tuesday.groupingBy { it.name }.eachCount()
            DayOfWeek.WEDNESDAY -> subjects = wednesday.groupingBy { it.name }.eachCount()
            DayOfWeek.THURSDAY -> subjects = thursday.groupingBy { it.name }.eachCount()
            DayOfWeek.FRIDAY -> subjects = friday.groupingBy { it.name }.eachCount()
            else -> Log.d("TimeTable", "Unknown day of week")
        }

        val correspondingAttendancePretty =
            App.prettyAttendance.filter { subjects.containsKey(it.name) }

        var attended = 0
        var total = 0
        correspondingAttendancePretty.forEach {
            attended += it.presence + it.lateness + it.latenessExcused
            total += it.totalClasses
        }
        return (attended.toDouble()/total.toDouble() * 10000.0).toInt()/100.0

    }
    fun howManyDaysCanSkip(dayOfWeek: DayOfWeek):Int {
        var subjects : Map<String,Int> = mapOf()
        when (dayOfWeek){
            DayOfWeek.MONDAY -> subjects = monday.groupingBy { it.name }.eachCount()
            DayOfWeek.TUESDAY -> subjects = tuesday.groupingBy { it.name }.eachCount()
            DayOfWeek.WEDNESDAY -> subjects = wednesday.groupingBy { it.name }.eachCount()
            DayOfWeek.THURSDAY -> subjects = thursday.groupingBy { it.name }.eachCount()
            DayOfWeek.FRIDAY -> subjects = friday.groupingBy { it.name }.eachCount()
            else -> Log.d("TimeTable", "Unknown day of week")
        }
        val correspondingAttendancePretty =
            App.prettyAttendance.filter { subjects.containsKey(it.name) }
        var skipPerSubject = mutableMapOf<String, Int>()
        correspondingAttendancePretty.forEach {

            skipPerSubject[it.name] = it.howManyClassesCanSkip()
        }
        var skips = 0
        var howManyDaysOfSubjectCanSkip = mutableMapOf<String, Int>()
        skipPerSubject.forEach {
            val subject = it.key
            val skip = it.value

            var howManyInWeek :Int;
            when(dayOfWeek)
            {
                DayOfWeek.MONDAY -> howManyInWeek = mondayOccurence[subject]!!
                DayOfWeek.TUESDAY -> howManyInWeek = tuesdayOccurence[subject]!!
                DayOfWeek.WEDNESDAY -> howManyInWeek = wednesdayOccurence[subject]!!
                DayOfWeek.THURSDAY -> howManyInWeek = thursdayOccurence[subject]!!
                DayOfWeek.FRIDAY -> howManyInWeek = fridayOccurence[subject]!!
                else -> howManyInWeek = 0
            }
            val howManyDaysCanSkip = skip / howManyInWeek!!
            howManyDaysOfSubjectCanSkip[subject] = howManyDaysCanSkip
        }
        skips = howManyDaysOfSubjectCanSkip.values.minOrNull()!!


        return skips


    }

    fun getSafetyColorDay(skips: Int): Color {

        val ratio = skips.toDouble()/App.safetyAfterWeeks.toDouble()
        val amountOfSteps = App.steps.size
        val step = 1.0/amountOfSteps
        var index = 0
        var currentStep = 0.0
        while(currentStep < ratio){
            currentStep += step
            index++
        }
        if(index == 0) index = 1
        if(index > amountOfSteps) index = amountOfSteps
        return App.steps[index-1]
    }



}