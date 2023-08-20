package com.gami.can_i_skip

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class SubjectFragment:Fragment(R.layout.fragment_subject) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navbar = activity?.findViewById(R.id.bottom_navigation) as BottomNavigationView
        navbar.visibility = View.VISIBLE





    }
}