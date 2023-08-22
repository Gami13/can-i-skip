package com.gami.can_i_skip

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.wulkanowy.sdk.pojo.AttendanceSummary
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DayFragment:Fragment(R.layout.fragment_day) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navbar = activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
        navbar.visibility = View.VISIBLE





    }

}