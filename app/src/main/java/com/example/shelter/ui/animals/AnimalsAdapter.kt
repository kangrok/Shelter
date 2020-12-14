package com.example.shelter.ui.animals

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shelter.R
import com.example.shelter.models.Animal
import kotlinx.android.synthetic.main.animal_item.view.*

class AnimalsAdapter(private var listener: AnimalsAdapterListener) :
    RecyclerView.Adapter<AnimalsAdapter.AnimalViewHolder>() {

    interface AnimalsAdapterListener {
        fun onAnimalClick(animal: Animal)
    }

    var data = arrayOf<Animal>()

    inner class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.animal_item, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = data[position]

        holder.itemView.apply {
            textview_animal_name.text = animal.name
            when (animal.gender) {
                "M" -> imageview_gender.setImageResource(R.drawable.ic_gender_male)
                "F" -> imageview_gender.setImageResource(R.drawable.ic_gender_female)
            }
            Log.i(animal.id, "${animal.imgs.size}")
            imageview_animal_item.setImageBitmap(animal.imgs[0])
            setOnClickListener { listener.onAnimalClick(animal) }
        }
    }

    override fun getItemCount(): Int = data.size


}