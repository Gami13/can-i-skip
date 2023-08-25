package com.gami.can_i_skip

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.wulkanowy.sdk.Sdk
import io.github.wulkanowy.sdk.pojo.RegisterStudent
import io.github.wulkanowy.sdk.pojo.RegisterUser
import io.github.wulkanowy.sdk.scrapper.login.BadCredentialsException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import com.gami.can_i_skip.App.Companion.sdk

class LoginFragment : Fragment(R.layout.fragment_login) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)




        if (App.credentials.email != "") {

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentHost, SubjectFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()
            App.topBar?.title = getString(R.string.topbar_by_subject)
            return
        }

        val navbar = activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
        navbar.visibility = View.GONE
        val logInButton = view.findViewById<Button>(R.id.log_in_button)
        val baseUrls = resources.getStringArray(R.array.hosts_keys)
        val baseUrlsValues = resources.getStringArray(R.array.hosts_values)

        val arrayAdapter = ArrayAdapter(view.context, R.layout.list_item, baseUrls)
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).setAdapter(arrayAdapter)



        logInButton.setOnClickListener {


            val inputEmail = view.findViewById<EditText>(R.id.inputEmail).text.toString();
            val inputPassword = view.findViewById<EditText>(R.id.inputPassword).text.toString();
            val inputBaseUrl =
                view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).text.toString();
            if (inputEmail.isEmpty() || inputPassword.isEmpty() || inputBaseUrl.isEmpty()) {
                Toast.makeText(view.context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val inputBaseUrlValue = baseUrlsValues[baseUrls.indexOf(inputBaseUrl)]


            Log.d("TEST", "$inputEmail, $inputPassword, $inputBaseUrl, $inputBaseUrlValue");




            runBlocking {

                launch { loginUser(inputEmail, inputPassword, inputBaseUrlValue, view) }
            }

            Log.d("nut", "deez nuts");
        }


    }

    suspend fun loginUser(
        emailI: String,
        passwordI: String,
        scrapperBaseUrlI: String,
        view: View
    ): Boolean {
        /*   DONT KNOW WHY DOESNT WORK
             val logInButton = view.findViewById<Button>(R.id.log_in_button)
             Log.d("TEST", "logging")
             logInButton.isClickable = false;
             logInButton.setText(R.string.please_wait);

             val colorFrom = view.context.getColor(R.color.main)
             val colorTo = view.context.getColor(R.color.mainDisabled)
             val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
             colorAnimation.duration = 250 // milliseconds

             colorAnimation.addUpdateListener { animator -> logInButton.setBackgroundColor(animator.animatedValue as Int) }
             colorAnimation.start()

             logInButton.setBackgroundColor(
                 ContextCompat.getColor(
                     view.context,
                     R.color.mainDisabled
                 )
             )*/
        try {
            val user: RegisterUser = sdk.getUserSubjectsFromScrapper(
                email = emailI,
                password = passwordI,
                scrapperBaseUrl = scrapperBaseUrlI,

                )


            val registerSymbol = user.symbols
                .filter { it.schools.isNotEmpty() }
                .first { it.schools.all { school -> school.subjects.isNotEmpty() } }

            val registerUnit = registerSymbol.schools.first()
            val registerStudent = registerUnit.subjects.filterIsInstance<RegisterStudent>().first()

            val semester = registerStudent.semesters.first()
            App.credentials = Credentials(
                email = emailI,
                password = passwordI,
                scrapperBaseUrl = scrapperBaseUrlI,
                loginType = user.loginType?.name!!,
                symbol = registerSymbol.symbol,
                schoolSymbol = registerUnit.schoolId,
                studentId = registerStudent.studentId,
                diaryId = semester.diaryId,
            )
            App.prepareSDK()



            Log.d("SDK", "Schoold id: ${registerUnit.schoolId}")
            Log.d("SDK", "Student id: ${registerStudent.studentId}")


            App.saveCredentialsToFile()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentHost, SubjectFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()
            return true
        } catch (e: BadCredentialsException) {

            AlertDialog.Builder(view.context)
                .setTitle(R.string.login_failed)
                .setMessage(R.string.login_failed_message)
                .setNeutralButton(android.R.string.ok, null)

                .show()
            Toast.makeText(view.context, R.string.login_failed, Toast.LENGTH_SHORT).show()
            return false
        }


    }


}