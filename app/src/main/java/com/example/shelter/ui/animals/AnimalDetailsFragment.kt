package com.example.shelter.ui.animals

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.shelter.MainActivity
import com.example.shelter.R
import kotlinx.android.synthetic.main.fragment_animal_details.view.*
import java.util.*

class AnimalDetailsFragment : Fragment() {

    private val args: AnimalDetailsFragmentArgs by navArgs()
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animal_details, container, false)

        val titleId = when (args.animal.type) {
            "dog" -> R.string.dogs
            "cat" -> R.string.cats
            else -> R.string.others
        }
        (activity as MainActivity).supportActionBar?.title = resources.getString(titleId)

        displayAnimalDetails(view)

        viewPager = view.findViewById(R.id.viewpager_animal)
        val imagePagerAdapter = ImagePagerAdapter()
        imagePagerAdapter.data = args.animal.imgs
        viewPager.adapter = imagePagerAdapter
        viewPager.currentItem = 0

        val callButton = view.findViewById<Button>(R.id.button_call)
        callButton.setOnClickListener {
            (activity as MainActivity).dialPhoneNumber(callButton.text.toString())
        }

        return view
    }

    private fun displayAnimalDetails(view: View) {
        val animal = args.animal

        view.textview_name.text = animal.name

        if (animal.gender == "M") {
            view.imageview_gender.setImageResource(R.drawable.ic_gender_male)
        } else {
            view.imageview_gender.setImageResource(R.drawable.ic_gender_female)
        }

        val unitId = if (animal.age == "1") {
            if (animal.ageUnit == "year") R.string.year else R.string.month
        } else {
            if (animal.ageUnit == "year") R.string.years else R.string.months
        }
        view.textview_age.text = "${animal.age} ${resources.getString(unitId)}"
        view.textview_type.text = animal.type.capitalize(Locale.ROOT)
        view.textview_breed.text = animal.breed
        view.textview_description.text = animal.description
        view.text_meet_name.text = animal.name

    }

    inner class ImagePagerAdapter : PagerAdapter() {

        var data = arrayListOf<Bitmap>()

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(activity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setImageBitmap(args.animal.imgs[position])
            (container as ViewPager).addView(imageView)
            return imageView
        }

        override fun getCount(): Int {
            return data.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object` as View
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            (container as ViewPager).removeView(view)
        }
    }
}