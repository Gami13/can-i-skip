package com.gami.can_i_skip

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.github.wulkanowy.sdk.Sdk
import io.github.wulkanowy.sdk.pojo.RegisterUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import com.gami.can_i_skip.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logInButton = findViewById(R.id.log_in_button) as Button
        logInButton.setOnClickListener {
            Log.d("TEST", "deez nuts");
            GlobalScope.launch {
                loginUser()
            }
            Log.d("nut", "deez nuts");

        }

    }

    suspend fun loginUser() {
        val sdk = Sdk()

        val email = Constants().EMAIL
        val password = Constants().PASSWORD
        val scrapperBaseUrl = Constants().SCRAPPER_BASE_URL
        val symbol = Constants().SYMBOL

        Log.d("EMAIL", email);

        val user: RegisterUser = sdk.getUserSubjectsFromScrapper(
            email = email,
            password = password,
            scrapperBaseUrl = scrapperBaseUrl,
            symbol =symbol,
        )
        Log.d("TEST", user.toString());
    }


}