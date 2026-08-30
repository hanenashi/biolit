package cz.hanenashi.biolit

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SettingsView(this))
    }
}

private class SettingsView(context: Context) : ScrollView(context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val labels = resources.getStringArray(R.array.search_area_labels).toList()
    private val values = resources.getStringArray(R.array.search_area_values).toList()
    private val palette = Palette(context)

    init {
        setBackgroundColor(palette.background)
        isFillViewport = true

        addView(
            LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(20), dp(18), dp(20), dp(20))
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )

                addView(title(context.getString(R.string.settings_title)))
                addView(subtitle(context.getString(R.string.settings_subtitle)))
                addView(searchPanel())
                addView(aboutPanel())
            }
        )
    }

    private fun searchPanel(): LinearLayout {
        val currentPreset = SearchConfig.presetForPreferenceValue(
            prefs.getString(SearchConfig.PREF_SEARCH_AREA, SearchConfig.DEFAULT_PRESET.preferenceValue)
        )
        val currentIndex = values.indexOf(currentPreset.preferenceValue).takeIf { it >= 0 } ?: 0

        return panel().apply {
            addView(sectionLabel(context.getString(R.string.settings_search_type_title)))

            val spinner = AppCompatSpinner(context).apply {
                adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    labels
                ).also {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                setSelection(currentIndex)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(52)
                ).apply {
                    topMargin = dp(8)
                }
                background = roundedDrawable(palette.field, palette.stroke, dp(8))
                setPadding(dp(12), 0, dp(12), 0)
            }

            val summary = bodyText(context.getString(R.string.settings_search_type_summary)).apply {
                setPadding(0, dp(10), 0, 0)
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    prefs.edit()
                        .putString(SearchConfig.PREF_SEARCH_AREA, values[position])
                        .apply()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }

            addView(spinner)
            addView(summary)
        }
    }

    private fun aboutPanel(): LinearLayout {
        return panel().apply {
            addView(sectionLabel(context.getString(R.string.settings_about_title)))
            addView(row(
                context.getString(R.string.settings_destination_title),
                SearchConfig.SEARCH_URL
            ))
            addView(row(
                context.getString(R.string.settings_app_version_title),
                appVersionSummary()
            ))
        }
    }

    private fun title(text: String): TextView {
        return TextView(context).apply {
            this.text = text
            setTextColor(palette.primaryText)
            textSize = 28f
            typeface = Typeface.DEFAULT_BOLD
            includeFontPadding = false
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun subtitle(text: String): TextView {
        return bodyText(text).apply {
            textSize = 15f
            setPadding(0, dp(8), 0, dp(18))
        }
    }

    private fun panel(): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = roundedDrawable(palette.card, palette.stroke, dp(12))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(12)
            }
        }
    }

    private fun sectionLabel(text: String): TextView {
        return TextView(context).apply {
            this.text = text
            setTextColor(palette.primaryText)
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            includeFontPadding = false
        }
    }

    private fun row(label: String, value: String): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(12), 0, 0)
            addView(TextView(context).apply {
                text = label
                setTextColor(palette.secondaryText)
                textSize = 13f
                includeFontPadding = false
            })
            addView(TextView(context).apply {
                text = value
                setTextColor(palette.primaryText)
                textSize = 15f
                setPadding(0, dp(4), 0, 0)
            })
        }
    }

    private fun bodyText(text: String): TextView {
        return TextView(context).apply {
            this.text = text
            setTextColor(palette.secondaryText)
            textSize = 14f
            setLineSpacing(0f, 1f)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun appVersionSummary(): String {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }

        return context.getString(
            R.string.app_version_summary,
            packageInfo.versionName ?: BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )
    }

    private fun roundedDrawable(fill: Int, stroke: Int, radius: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radius.toFloat()
            setColor(fill)
            setStroke(dp(1), stroke)
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    private class Palette(context: Context) {
        private val isNight = (context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        val background = Color.parseColor(if (isNight) "#10130f" else "#f3f6ef")
        val card = Color.parseColor(if (isNight) "#1b2018" else "#ffffff")
        val field = Color.parseColor(if (isNight) "#252b22" else "#f8faf5")
        val stroke = Color.parseColor(if (isNight) "#343c31" else "#dce4d6")
        val primaryText = Color.parseColor(if (isNight) "#f1f5ec" else "#182015")
        val secondaryText = Color.parseColor(if (isNight) "#b8c2b1" else "#5d6758")
    }
}
