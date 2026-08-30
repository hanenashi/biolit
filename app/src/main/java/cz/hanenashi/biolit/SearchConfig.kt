package cz.hanenashi.biolit

object SearchConfig {
    const val PREF_SEARCH_AREA = "search_area"
    const val PREF_APP_VERSION = "app_version"

    // BioLib searcharea values observed on the homepage search form.
    const val TAXA_CZECH = "1"
    const val TAXA_ALL_LANGUAGES = "6"
    const val IMAGES = "2"
    const val LINKS_AND_LITERATURE = "3"
    const val TERMS = "5"
    const val TERMS_ALL_LANGUAGES = "9"
    const val BIOTOPES = "7"
    const val LOCALITIES = "8"
    const val EVERYWHERE = "100"

    const val DEFAULT_SEARCH_AREA = TAXA_CZECH
    const val SEARCH_URL = "https://www.biolib.cz/cz/formsearch/"

    val SEARCH_AREAS = setOf(
        TAXA_CZECH,
        TAXA_ALL_LANGUAGES,
        IMAGES,
        LINKS_AND_LITERATURE,
        TERMS,
        TERMS_ALL_LANGUAGES,
        BIOTOPES,
        LOCALITIES,
        EVERYWHERE
    )

    fun normalizedSearchArea(value: String?): String {
        return value?.takeIf { it in SEARCH_AREAS } ?: DEFAULT_SEARCH_AREA
    }
}
