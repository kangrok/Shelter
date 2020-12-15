package com.example.shelter.ui.animals

import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_animal_details.view.*

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

        val genderRes = if (animal.gender == "M") R.drawable.ic_gender_male else R.drawable.ic_gender_female
        view.imageview_gender.setImageResource(genderRes)

        view.textview_type.text = resources.getString((when (animal.type) {
            "dog" -> R.string.dog
            "cat" -> R.string.cat
            else -> R.string.other }))

        val unitId = if (animal.age == "1") {
            if (animal.ageUnit == "year") R.string.year else R.string.month
        } else {
            if (animal.ageUnit == "year") R.string.years else R.string.months
        }
        view.textview_age.text = "${animal.age} ${resources.getString(unitId)}"

        if (animal.description == "") {
            view.textview_description.visibility = View.GONE
        } else {
            view.textview_description.text = animal.description
        }

        view.textview_name.text = animal.name
        view.textview_breed.text = animal.breed
        val meetText = getString(R.string.schedule_visit, animal.name)
        val ss = SpannableString(meetText)
        val start = meetText.indexOf(animal.name, 0)
        ss.setSpan(ForegroundColorSpan(resources.getColor(R.color.primary)), start, start + animal.name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(StyleSpan(Typeface.BOLD), start, start + animal.name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text_meet_animal.text = ss
    }

    inner class ImagePagerAdapter : PagerAdapter() {

        var data = arrayListOf<Uri>()

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(activity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Picasso.get().load(data[position]).into(imageView)
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