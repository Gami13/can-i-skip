package com.gami.can_i_skip

import android.app.Application
import android.content.Context
import android.util.Log
import io.github.wulkanowy.sdk.Sdk
import java.io.File

class App : Application() {
    private var context: Context? = null
    var sdk = Sdk()
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }


}