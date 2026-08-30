package cz.hanenashi.biolit

object SearchConfig {
    const val PREF_SEARCH_AREA = "search_area"
    const val PREF_APP_VERSION = "app_version"

    const val SEARCH_URL = "https://www.biolib.cz/cz/formsearch/"
    val DEFAULT_PRESET = SearchPreset.TAXA_CZECH

    fun presetForPreferenceValue(value: String?): SearchPreset {
        return SearchPreset.entries.firstOrNull { it.preferenceValue == value } ?: DEFAULT_PRESET
    }
}

data class SearchParameter(
    val name: String,
    val value: String
)

enum class SearchPreset(
    val preferenceValue: String,
    val parameters: List<SearchParameter>
) {
    TAXA_CZECH(
        preferenceValue = "taxa_czech",
        parameters = listOf(
            SearchParameter("searchrecords", "1"),
            SearchParameter("searchvnames", "1"),
            SearchParameter("searchgallery", "0"),
            SearchParameter("searchsources", "0"),
            SearchParameter("searcharticles", "0"),
            SearchParameter("searchdict", "0"),
            SearchParameter("searchsynonyms", "1"),
            SearchParameter("searchbiotops", "0"),
            SearchParameter("searchlocals", "0"),
            SearchParameter("searchtype", "4")
        )
    )
}
