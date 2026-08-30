package cz.hanenashi.biolit

object SearchConfig {
    const val PREF_SEARCH_AREA = "search_area"
    const val PREF_APP_VERSION = "app_version"

    const val SEARCH_URL = "https://www.biolib.cz/cz/formsearch/"
    val DEFAULT_PRESET = SearchPreset.TAXA_CZECH

    fun presetForPreferenceValue(value: String?): SearchPreset {
        return SearchPreset.entries.firstOrNull { it.matchesPreferenceValue(value) } ?: DEFAULT_PRESET
    }
}

data class SearchParameter(
    val name: String,
    val value: String
)

enum class SearchPreset(
    val preferenceValue: String,
    val searchArea: String,
    private val legacyPreferenceValues: Set<String> = emptySet()
) {
    TAXA_CZECH(
        preferenceValue = "1",
        searchArea = "1",
        legacyPreferenceValues = setOf("taxa_czech")
    ),
    TAXA_ALL_LANGUAGES("6", "6"),
    IMAGES("2", "2"),
    LINKS_AND_LITERATURE("3", "3"),
    TERMS("5", "5"),
    TERMS_ALL_LANGUAGES("9", "9"),
    BIOTOPES("7", "7"),
    LOCALITIES("8", "8"),
    EVERYWHERE("100", "100");

    val parameters: List<SearchParameter>
        get() = listOf(
            SearchParameter("action", "execute"),
            SearchParameter("searcharea", searchArea)
        )

    fun matchesPreferenceValue(value: String?): Boolean {
        return value == preferenceValue || value in legacyPreferenceValues
    }
}
