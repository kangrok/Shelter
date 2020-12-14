package com.example.shelter.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.*

class LangContextWrapper private constructor(base: Context) : ContextWrapper(base) {
    companion object {
        private val enLocale = Locale("en")
        private val estLocale = Locale("est")

        fun wrap(baseContext: Context, language: String): ContextWrapper {
            var wrappedContext = baseContext
            val config = Configuration(baseContext.resources.configuration)

            if (language.isNotBlank()) {
                val locale = returnOrCreateLocale(language)
                Locale.setDefault(locale)
                config.setLocale(locale)
                config.setLayoutDirection(locale)
                wrappedContext = baseContext.createConfigurationContext(config)
            }
            return LangContextWrapper(wrappedContext)
        }

        private fun returnOrCreateLocale(language: String): Locale {
            return when (language) {
                "en" -> enLocale
                "est" -> estLocale
                else -> Locale(language)
            }
        }
    }

}