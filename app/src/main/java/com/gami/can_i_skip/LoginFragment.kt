package com.gami.can_i_skip.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.NavController
import com.gami.can_i_skip.databinding.FragmentLoginBinding

import com.gami.can_i_skip.R
import io.github.wulkanowy.sdk.Sdk
import io.github.wulkanowy.sdk.pojo.RegisterStudent
import io.github.wulkanowy.sdk.pojo.RegisterUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.gami.can_i_skip.App

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

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




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}