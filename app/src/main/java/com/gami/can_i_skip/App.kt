package com.gami.can_i_skip

import android.app.Application
import io.github.wulkanowy.sdk.Sdk

class App : Application() {
    var sdk = Sdk()
    var emailProper = Constants().EMAIL
    var passwordProper = Constants().PASSWORD
    var scrapperBaseUrlProper = Constants().SCRAPPER_BASE_URL
    var symbolProper= Constants().SYMBOL
}