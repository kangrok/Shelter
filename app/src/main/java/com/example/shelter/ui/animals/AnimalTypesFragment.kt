package com.example.shelter.ui.animals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shelter.R

class AnimalTypesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animal_types, container, false)

        val dogsButton = view.findViewById<ImageButton>(R.id.button_dogs)
        dogsButton.setOnClickListener {
            displayAnimalsFragment(view, "dog")
        }

        val catsButton = view.findViewById<ImageButton>(R.id.button_cats)
        catsButton.setOnClickListener {
            displayAnimalsFragment(view, "cat")
        }

        val othersButton = view.findViewById<ImageButton>(R.id.button_others)
        othersButton.setOnClickListener {
            displayAnimalsFragment(view, "other")
        }

        return view
    }

    private fun displayAnimalsFragment(view: View, type: String) {
        val action = AnimalTypesFragmentDirections.navigateToAnimals(type)
        Navigation.findNavController(view).navigate(action)
    }

}