package com.gami.can_i_skip

import android.content.Context
import io.github.wulkanowy.sdk.pojo.AttendanceSummary
import kotlinx.serialization.Serializable
import kotlinx.serialization.*

import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.io.File
@Serializable
data class SubjectPretty(
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
    fun getAttendancePercentage(): Double {
        return (presence.toDouble() / totalClasses.toDouble()) * 100
    }
    fun howManyClassesCanSkip(): Int {
        return ((presence.toDouble() / App.targetAttendance) - totalClasses).toInt()
    }


}