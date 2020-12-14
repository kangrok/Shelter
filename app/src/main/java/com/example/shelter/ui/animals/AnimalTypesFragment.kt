package com.example.shelter.ui.animals

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.shelter.R
import com.example.shelter.ui.home.HomeFragment

class AnimalTypesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout =
            if (parentFragment?.javaClass != HomeFragment::class.java &&
                resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                R.layout.fragment_animal_types
            } else {
                R.layout.fragment_animal_types_land
            }

        val view = inflater.inflate(layout, container, false)

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