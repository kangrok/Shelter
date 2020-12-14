package com.example.shelter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.shelter.R
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.*

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        setUpSpinner(view)
        setUpThemeToggle(view)
        return view
    }

    private fun setUpThemeToggle(view: View) {
        val toggle: MaterialButtonToggleGroup = view.findViewById(R.id.toggle_theme)

        if (currentTheme() == AppCompatDelegate.MODE_NIGHT_YES) {
            toggle.check(R.id.button_dark_theme)
        } else {
            toggle.check(R.id.button_light_theme)
        }

        toggle.addOnButtonCheckedListener { group, checkId, isChecked ->
            val theme: Int
            if (isChecked) {
                theme = if (checkId == R.id.button_dark_theme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    AppCompatDelegate.MODE_NIGHT_NO
                }
                activity?.let {
                    val editor = it.getSharedPreferences("settings", 0).edit()
                    editor.putInt("theme", theme)
                    editor.apply()
                }
            } else if (-1 == group.checkedButtonId) {
                group.check(checkId)
            }
        }
    }

    private fun currentTheme(): Int {
        return AppCompatDelegate.getDefaultNightMode()
    }

    private fun setUpSpinner(view: View) {
        val spinner: Spinner = view.findViewById(R.id.spinner_language)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languages_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(adapter.getPosition(if (currentLang() == "est") "Eesti" else "English"))

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, i: Int, l: Long) {
                val selectedLang = if (adapter.getItem(i).toString() == "Eesti") "est" else "en"
                if (currentLang() != selectedLang) {
                    Locale.setDefault(Locale(selectedLang))
                    activity?.let {
                        val editor = it.getSharedPreferences("settings", 0).edit()
                        editor.putString("locale", selectedLang)
                        editor.apply()
                        it.recreate()
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                val currentSelection = if (Locale.getDefault().language == "est") 0 else 1
                adapterView?.setSelection(currentSelection)
            }
        }
    }

    private fun currentLang(): String {
        return Locale.getDefault().language
    }

}