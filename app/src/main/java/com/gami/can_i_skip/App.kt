package com.gami.can_i_skip

import com.gami.can_i_skip.SubjectPretty
import android.app.Application
import android.content.Context
import android.util.Log
import io.github.wulkanowy.sdk.Sdk
import io.github.wulkanowy.sdk.pojo.AttendanceSummary
import io.github.wulkanowy.sdk.pojo.Subject
import java.io.File

class App : Application() {
    private var context: Context? = null

    companion object {

        var sdk = Sdk()
        var subjects: List<Subject> = listOf()
        var attendance: List<AttendanceSummary> = listOf()
        var prettySubjects: List<SubjectPretty> = listOf()

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }




}