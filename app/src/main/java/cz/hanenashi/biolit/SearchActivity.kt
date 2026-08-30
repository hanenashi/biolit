package cz.hanenashi.biolit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.PreferenceManager

class SearchActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.action != Intent.ACTION_PROCESS_TEXT) {
            finish()
            return
        }

        val selectedText = intent
            .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            ?.toString()
            ?.trim()
            .orEmpty()

        if (selectedText.isNotEmpty()) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
            val searchArea = prefs.getString(
                SearchConfig.PREF_SEARCH_AREA,
                SearchConfig.DEFAULT_SEARCH_AREA
            ).let(SearchConfig::normalizedSearchArea)

            val uri = Uri.parse(SearchConfig.SEARCH_URL)
                .buildUpon()
                .appendQueryParameter("searcharea", searchArea)
                .appendQueryParameter("string", selectedText)
                .build()

            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        finish()
    }
}
