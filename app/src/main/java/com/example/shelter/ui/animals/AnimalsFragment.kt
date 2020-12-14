package com.example.shelter.ui.animals

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shelter.MainActivity
import com.example.shelter.R
import com.example.shelter.models.Animal
import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimalsFragment : Fragment() {

    private val args: AnimalsFragmentArgs by navArgs()
    private lateinit var viewModel: AnimalsViewModel
    private lateinit var animalsAdapter: AnimalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animals, container, false)

        val titleId = when (args.type) {
            "dog" -> R.string.dogs
            "cat" -> R.string.cats
            else -> R.string.others
        }
        (activity as MainActivity).supportActionBar?.title = resources.getString(titleId)

        viewModel = ViewModelProvider(this).get(AnimalsViewModel::class.java)

        animalsAdapter = AnimalsAdapter(object : AnimalsAdapter.AnimalsAdapterListener {
            override fun onAnimalClick(animal: Animal) {
                displayAnimalDetailsFragment(view, animal)
            }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_animals)
        recyclerView.adapter = animalsAdapter

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(activity, 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(activity, 4)
        }

        CoroutineScope(Dispatchers.Default).launch {
            displayAnimals()
        }

        return view
    }

    private fun displayAnimals() {
        val animals = arrayListOf<Animal>()

        Firebase.firestore.collection("animals")
            .whereEqualTo("type", args.type)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    animals.add(
                        Animal(
                            doc.id,
                            doc["name"].toString(),
                            doc["type"].toString(),
                            doc["gender"].toString(),
                            doc["breed"].toString(),
                            doc["age"].toString(),
                            doc["ageUnit"].toString(),
                            doc["description"].toString(),
                            doc["images"] as ArrayList<String>
                        )
                    )
                }
                getImages(animals)
            }
    }

    private fun getImages(animals: ArrayList<Animal>) {
        val lastAnimal = animals.size - 1

        animals.forEachIndexed { i, animal ->
            val lastImg = animal.imgLocations.size - 1

            animal.imgLocations.forEachIndexed { j, location ->
                val imgRef = Firebase.storage.getReference("${animal.id}/$location")

                imgRef.getBytes(1024 * 1024).addOnSuccessListener {
                    animal.addImg(BitmapFactory.decodeByteArray(it, 0, it.size))

                    if (i == lastAnimal && j == lastImg) {
                        viewModel.animals = animals.toTypedArray()
                        animalsAdapter.data = viewModel.animals
                        animalsAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun displayAnimalDetailsFragment(view: View, animal: Animal) {
        val action = AnimalsFragmentDirections.navigateToAnimalDetails(animal)
        Navigation.findNavController(view).navigate(action)
    }
}