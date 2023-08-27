package com.gami.can_i_skip

import android.util.Range
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import kotlin.math.round
import kotlin.math.roundToInt

@Serializable
data class AttendancePretty(
    var id: Int,
    var name: String,
    var totalClasses: Int,
    var presence: Int,
    var absence: Int,
    var absenceExcused: Int,
    var absenceForSchoolReasons: Int,
    var lateness: Int,
    var latenessExcused: Int,
    var exemption: Int,
    var perMonth: List<AttendanceMonth>
) {
    override fun toString(): String {
        return "SubjectPretty(id=$id, name='$name', totalClasses=$totalClasses, presence=$presence, absence=$absence, absenceExcused=$absenceExcused, absenceForSchoolReasons=$absenceForSchoolReasons, lateness=$lateness, latenessExcused=$latenessExcused, exemption=$exemption, perMonth=$perMonth)"
    }
    //returns attendance percentage rounded to 2 decimal places
    fun getAttendancePercentage(): Double {
        val presenceTotal = presence + lateness + latenessExcused
        val total= totalClasses

        return (presenceTotal.toDouble() / total.toDouble() * 100_00).roundToInt()/100.0
    }
    fun howManyClassesCanSkip(): Int {
        if (App.targetAttendance == 0.0) return 99999
        var skippable = ((presence.toDouble() / App.targetAttendance) - totalClasses).toInt()
        if(skippable < 0) skippable = 0
        return skippable
    }
    fun howCloseToSafetySubject():Double
    {
        val howManyInWeek = App.timetable.subjectOccurence[name]
        if(name  == "Wszystkie"){
            return -1.0
        }
        if(howManyInWeek == null) return -1.0

        val fullSafetyReq = howManyInWeek.toDouble().times(App.safetyAfterWeeks.toDouble())
        val currently = howManyClassesCanSkip()
        val ratio =  currently.toDouble()/fullSafetyReq

        return ratio
    }

    fun getSafetyColorSubject(ratio: Double): Color {

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