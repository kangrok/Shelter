package com.example.shelter.ui.animals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.shelter.models.Animal

class AnimalsViewModel(application: Application) : AndroidViewModel(application) {

    var animals: Array<Animal> = arrayOf()
}