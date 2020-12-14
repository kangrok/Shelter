package com.example.shelter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shelter.utils.LangContextWrapper
import com.google.android.material.navigation.NavigationView
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener: NavController.OnDestinationChangedListener
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        updateSettings()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.fragment_container)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.animalTypesFragment,
                R.id.newsFragment,
                R.id.contactFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                navController.navigate(R.id.settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LangContextWrapper.wrap(newBase, Locale.getDefault().language))
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || return super.onSupportNavigateUp()
    }

    fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun updateSettings() {
        val sharedPreferences = getSharedPreferences("settings", 0)

        val theme = sharedPreferences.getInt("theme", -1)
        if (theme != -1 && AppCompatDelegate.getDefaultNightMode() != theme) {
            AppCompatDelegate.setDefaultNightMode(theme)
        }

        val locale = sharedPreferences.getString("locale", null)
        if (locale != null && Locale.getDefault().language != locale) {
            Locale.setDefault(Locale(locale))
            recreate()
        }
    }
}