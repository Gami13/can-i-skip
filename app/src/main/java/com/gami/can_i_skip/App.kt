package com.gami.can_i_skip

import android.app.Application
import android.content.Context
import androidx.compose.ui.graphics.Color

import io.github.wulkanowy.sdk.Sdk
import io.github.wulkanowy.sdk.pojo.AttendanceSummary
import io.github.wulkanowy.sdk.pojo.Subject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import java.io.File
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters


class App : Application() {


    companion object {

        var context: Context? = null
        var sdk = Sdk()
        var isSdkPrepared = false
        var topBar: com.google.android.material.appbar.MaterialToolbar? = null
        var credentials: Credentials = Credentials("", "", "", "", "", "", 0, 0)
        var subjects: List<Subject> = listOf()
        var attendance: List<AttendanceSummary> = listOf()
        var prettyAttendance: List<AttendancePretty> = listOf()
        /*add option to change*/
        var targetAttendance = 0.60
        /*add option to change*/

        var safetyAfterWeeks = 2

        /*add option to change*/
        var steps :Array<Color> = arrayOf(
           Color(210,2,2),
            Color(183,96,6),
            Color(154,156,10),
            Color(69,131,12),
            Color(13,107,16),
        )
        var timetable: com.gami.can_i_skip.Timetable = com.gami.can_i_skip.Timetable()

        fun sortAttendanceAlphabetically() {
            /*"Wszystkie should be at the top*/

            val sorted = prettyAttendance.sortedBy { it.name }
            val wszystkie = sorted.find { it.name == "Wszystkie" }
            val others = sorted.filter { it.name != "Wszystkie" }
            prettyAttendance = listOf(wszystkie!!) + others

            App.saveAttendanceToFile()
        }
        fun saveTimetableToFile() {

            val filesDir = App.context?.filesDir;
            val file = File(filesDir, "timetable")

            file.writeText(Json.encodeToString(App.timetable))
        }
        fun loadTimetableFromFile(){
            val filesDir = App.context?.filesDir;
            val file = File(filesDir, "timetable")
            if (file.exists()) {
                val timetable = Json.decodeFromString<com.gami.can_i_skip.Timetable>(file.readText())
                App.timetable = timetable
            }
        }
        fun saveAttendanceToFile() {

            val filesDir = App.context?.filesDir;
            val file = File(filesDir, "subjectsPretty")

            file.writeText(Json.encodeToString(App.prettyAttendance))
        }

        fun loadAttendanceFromFile() {
            val filesDir = App.context?.filesDir;
            val file = File(filesDir, "subjectsPretty")
            if (file.exists()) {
                val subjectsPretty = Json.decodeFromString<List<AttendancePretty>>(file.readText())
                App.prettyAttendance = subjectsPretty
            }
        }

        fun saveCredentialsToFile() {
            val filesDir = App.context?.filesDir;
            val file = File(filesDir, "credentials")

            file.writeText(Json.encodeToString(App.credentials))


        }

        fun loadCredentialsFromFile() {
            val filesDir = App.context?.filesDir;
            val file = File(filesDir, "credentials")
            if (file.exists()) {
                val credentials = Json.decodeFromString<Credentials>(file.readText())
                App.credentials = credentials
            }
        }

        fun prepareSDK() {
            sdk.apply {
                email = credentials.email
                password = credentials.password
                scrapperBaseUrl = credentials.scrapperBaseUrl
                loginType = Sdk.ScrapperLoginType.valueOf(credentials.loginType)

                symbol = credentials.symbol
                schoolSymbol = credentials.schoolSymbol
                studentId = credentials.studentId
                diaryId = credentials.diaryId
            }
            isSdkPrepared = true

        }



        fun getPreviousMonday(monday: LocalDate):LocalDate {
            return monday.minusDays(7)
        }
        fun getLastMonday():LocalDate {

            val lastFriday = getLastFriday()
            return lastFriday.minusDays(4)

        }

        fun getLastFriday():LocalDate {

                return LocalDate.now(ZoneId.of("Europe/Warsaw")).with(TemporalAdjusters.previous(DayOfWeek.FRIDAY))
        }

        suspend fun updateAttendance() {
            if(!isSdkPrepared)
            {
                prepareSDK()
            }
            var prettyAttendance = listOf<AttendancePretty>();
            val subjects = sdk.getSubjects();
            var attendances: List<AttendanceSummary> = listOf()
            App.subjects = subjects


            subjects.forEach {
                val attendance = sdk.getAttendanceSummary(it.id)

                App.attendance += attendance
                var totalClasses = 0;
                var presence = 0;
                var absence = 0;
                var absenceExcused = 0;
                var absenceForSchoolReasons = 0;
                var lateness = 0;
                var latenessExcused = 0;
                var exemption = 0;
                var months = listOf<AttendanceMonth>()
                attendance.forEach {
                    totalClasses += it.presence + it.absence + it.absenceExcused + it.absenceForSchoolReasons + it.lateness + it.latenessExcused + it.exemption
                    presence += it.presence
                    absence += it.absence
                    absenceExcused += it.absenceExcused
                    absenceForSchoolReasons += it.absenceForSchoolReasons
                    lateness += it.lateness
                    latenessExcused += it.latenessExcused
                    exemption += it.exemption
                    months += AttendanceMonth(
                        it.month,
                        it.presence,
                        it.absence,
                        it.absenceExcused,
                        it.absenceForSchoolReasons,
                        it.lateness,
                        it.latenessExcused,
                        it.exemption,
                    )
                }
                prettyAttendance += AttendancePretty(
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
                    months

                )


            }
            App.prettyAttendance = prettyAttendance

            App.saveAttendanceToFile()
        }


    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }


}