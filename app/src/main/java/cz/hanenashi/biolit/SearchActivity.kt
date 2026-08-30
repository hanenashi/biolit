package cz.hanenashi.biolit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.PreferenceManager

class SearchActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.action != Intent.ACTION_PROCESS_TEXT && intent.action != Intent.ACTION_SEND) {
            finish()
            return
        }

        val selectedText = textFromIntent(intent)

        if (selectedText.isNotEmpty()) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val preset = prefs.getString(
                SearchConfig.PREF_SEARCH_AREA,
                SearchConfig.DEFAULT_PRESET.preferenceValue
            ).let(SearchConfig::presetForPreferenceValue)

            val uriBuilder = Uri.parse(SearchConfig.SEARCH_URL)
                .buildUpon()
                .appendQueryParameter("string", selectedText)

            preset.parameters.forEach { parameter ->
                uriBuilder.appendQueryParameter(parameter.name, parameter.value)
            }

            startActivity(Intent(Intent.ACTION_VIEW, uriBuilder.build()))
        }

        finish()
    }

    private fun textFromIntent(intent: Intent): String {
        val extra = when (intent.action) {
            Intent.ACTION_PROCESS_TEXT -> Intent.EXTRA_PROCESS_TEXT
            Intent.ACTION_SEND -> Intent.EXTRA_TEXT
            else -> return ""
        }

        return intent
            .getCharSequenceExtra(extra)
            ?.toString()
            ?.trim()
            .orEmpty()
    }
}
