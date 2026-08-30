package cz.hanenashi.biolit

import org.junit.Assert.assertEquals
import org.junit.Test

class SearchConfigTest {
    @Test
    fun normalizedSearchAreaKeepsKnownValues() {
        SearchConfig.SEARCH_AREAS.forEach { searchArea ->
            assertEquals(searchArea, SearchConfig.normalizedSearchArea(searchArea))
        }
    }

    @Test
    fun normalizedSearchAreaFallsBackToDefaultForUnknownValues() {
        assertEquals(SearchConfig.DEFAULT_SEARCH_AREA, SearchConfig.normalizedSearchArea(null))
        assertEquals(SearchConfig.DEFAULT_SEARCH_AREA, SearchConfig.normalizedSearchArea(""))
        assertEquals(SearchConfig.DEFAULT_SEARCH_AREA, SearchConfig.normalizedSearchArea("bogus"))
    }
}
