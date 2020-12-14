package com.example.shelter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.shelter.MainActivity
import com.example.shelter.R


class HomeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<Button>(R.id.button_call).setOnClickListener {
            (activity as MainActivity).dialPhoneNumber(resources.getString(R.string.phone_number_visit))
        }

        view.findViewById<Button>(R.id.button_call_reception).setOnClickListener {
            (activity as MainActivity).dialPhoneNumber(resources.getString(R.string.phone_number_visit))
        }

        return view
    }
}