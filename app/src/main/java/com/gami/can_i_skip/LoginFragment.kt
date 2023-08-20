package com.gami.can_i_skip

import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.gami.can_i_skip.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

import io.github.wulkanowy.sdk.Sdk
import io.github.wulkanowy.sdk.pojo.RegisterStudent
import io.github.wulkanowy.sdk.pojo.RegisterUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment : Fragment(R.layout.fragment_login) {





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navbar = activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
        navbar.visibility = View.GONE
        val logInButton = view.findViewById<Button>(R.id.log_in_button)
        logInButton.setOnClickListener {
            val inputEmail = view.findViewById<EditText>(R.id.inputEmail).text.toString();
            val inputPassword = view.findViewById<EditText>(R.id.inputPassword).text.toString();

            Log.d("TEST", "$inputEmail $inputPassword");
            GlobalScope.launch {
                loginUser()
            }
            Log.d("nut", "deez nuts");

        }


    }

    suspend fun loginUser() {


        val user: RegisterUser = App().sdk.getUserSubjectsFromScrapper(
            email = App().emailProper,
            password = App().passwordProper,
            scrapperBaseUrl = App().scrapperBaseUrlProper,
            symbol = App().symbolProper,
        )
        val registerSymbol = user.symbols
            .filter { it.schools.isNotEmpty() }
            .first { it.schools.all { school -> school.subjects.isNotEmpty() } }
        val registerUnit = registerSymbol.schools.first()
        val registerStudent = registerUnit.subjects.filterIsInstance<RegisterStudent>().first()
        Log.d("TEST", registerStudent.semesters.toString())
        val semester = registerStudent.semesters.first()
        App().sdk.apply {
            email = App().emailProper
            password = App().passwordProper
            scrapperBaseUrl = App().scrapperBaseUrlProper
            loginType = Sdk.ScrapperLoginType.valueOf(user.loginType?.name!!)

            symbol = registerSymbol.symbol
            schoolSymbol = registerUnit.schoolId
            studentId = registerStudent.studentId
            diaryId = semester.diaryId
        }
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentHost, SubjectFragment())
        transaction?.disallowAddToBackStack()
        transaction?.commit()




    }


}