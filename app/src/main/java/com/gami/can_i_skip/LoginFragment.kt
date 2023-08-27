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
import com.gami.can_i_skip.App.Companion.sdk
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.wulkanowy.sdk.pojo.RegisterStudent
import io.github.wulkanowy.sdk.pojo.RegisterUnit
import io.github.wulkanowy.sdk.pojo.RegisterUser
import io.github.wulkanowy.sdk.scrapper.login.BadCredentialsException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)


        App.topBar?.visibility = View.GONE
        var isLoginStarted = false;


        val navbar = activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
        navbar.visibility = View.GONE
        val logInButton = view.findViewById<Button>(R.id.log_in_button)
        val baseUrls = resources.getStringArray(R.array.hosts_keys)
        val baseUrlsValues = resources.getStringArray(R.array.hosts_values)

        val arrayAdapter = ArrayAdapter(view.context, R.layout.list_item, baseUrls)
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).setAdapter(arrayAdapter)



        logInButton.setOnClickListener {
            if (isLoginStarted) {
                Toast.makeText(view.context, getString(R.string.please_wait), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val inputEmail = view.findViewById<EditText>(R.id.inputEmail).text.toString();
            val inputPassword = view.findViewById<EditText>(R.id.inputPassword).text.toString();
            val inputBaseUrl =
                view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).text.toString();
            if (inputEmail.isEmpty() || inputPassword.isEmpty() || inputBaseUrl.isEmpty()) {
                Toast.makeText(view.context, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val inputBaseUrlValue = baseUrlsValues[baseUrls.indexOf(inputBaseUrl)]


            Log.d("TEST", "$inputEmail, $inputPassword, $inputBaseUrl, $inputBaseUrlValue");




            GlobalScope.launch {
                isLoginStarted = true;
                /*  Doesnt work in API 24      logInButton.isClickable = false;
                        logInButton.setText(R.string.please_wait);*/
                var loginResult = loginUser(inputEmail, inputPassword, inputBaseUrlValue, view)/*   logInButton.isClickable = true;
                   logInButton.setText(R.string.log_in);*/
                if (loginResult) {
                    isLoginStarted = false;
                }
            }

            Log.d("nut", "deez nuts");
        }


    }

    suspend fun loginUser(
        emailI: String, passwordI: String, scrapperBaseUrlI: String, view: View
    ): Boolean {

        try {
            val user: RegisterUser = sdk.getUserSubjectsFromScrapper(
                email = emailI,
                password = passwordI,
                scrapperBaseUrl = scrapperBaseUrlI,

                )


            val registerSymbol = user.symbols.filter { it.schools.isNotEmpty() }
                .first { it.schools.all { school -> school.subjects.isNotEmpty() } }


            val registerUnits = registerSymbol.schools
            val registerUnitNamesArray = registerUnits.map { it.schoolName }
            var registerUnit: RegisterUnit = registerUnits.first();

            var didSelectStudent = false;
            var didSelectSchool = false;
            if (registerUnits.size > 1) {
                didSelectSchool = false;

                val builder = AlertDialog.Builder(view.context)
                builder.setTitle(getString(R.string.choose_school))
                    .setItems(
                        registerUnitNamesArray.toTypedArray(),
                        DialogInterface.OnClickListener { dialog, which ->

                            registerUnit = registerUnits[which]
                            didSelectSchool = true;

                        })


                requireActivity().runOnUiThread {

                    builder.create().show()
                }
            } else {
                didSelectSchool = true;
            }
            while (!didSelectSchool) {

            }

            val registerStudents = registerUnit.subjects.filterIsInstance<RegisterStudent>()
            val registerStudentNamesArray = registerStudents.map { it.studentName }
            var registerStudent: RegisterStudent = registerStudents.first();
            if (registerStudents.size > 1) {
                didSelectStudent = false;
                val builder = AlertDialog.Builder(view.context)
                builder.setTitle(getString(R.string.choose_student))
                    .setItems(
                        registerStudentNamesArray.toTypedArray(),
                        DialogInterface.OnClickListener { dialog, which ->

                            registerStudent = registerStudents[which]
                            didSelectStudent = true;

                        })


                requireActivity().runOnUiThread {

                    builder.create().show()
                }
            } else {
                didSelectStudent = true;
            }



            while (!didSelectStudent) {


            }
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




            App.saveCredentialsToFile()
            App.refresh()
            App.timetable.build()
            App.saveTimetableToFile()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentHost, SubjectFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()

            return true
        } catch (e: BadCredentialsException) {

            AlertDialog.Builder(view.context).setTitle(R.string.login_failed)
                .setMessage(R.string.login_failed_message)
                .setNeutralButton(android.R.string.ok, null)

                .show()
            Toast.makeText(view.context, R.string.login_failed, Toast.LENGTH_SHORT).show()

            return false
        }


    }


}