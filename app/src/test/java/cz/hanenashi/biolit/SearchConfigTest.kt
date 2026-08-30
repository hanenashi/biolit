package cz.hanenashi.biolit

import org.junit.Assert.assertEquals
import org.junit.Test

class SearchConfigTest {
    @Test
    fun presetForPreferenceValueKeepsKnownValues() {
        SearchPreset.entries.forEach { preset ->
            assertEquals(preset, SearchConfig.presetForPreferenceValue(preset.preferenceValue))
        }
    }

    @Test
    fun presetForPreferenceValueFallsBackToDefaultForOldOrUnknownValues() {
        assertEquals(SearchConfig.DEFAULT_PRESET, SearchConfig.presetForPreferenceValue(null))
        assertEquals(SearchConfig.DEFAULT_PRESET, SearchConfig.presetForPreferenceValue(""))
        assertEquals(SearchConfig.DEFAULT_PRESET, SearchConfig.presetForPreferenceValue("bogus"))
        assertEquals(SearchConfig.DEFAULT_PRESET, SearchConfig.presetForPreferenceValue("taxa_czech"))
    }

    @Test
    fun presetsUseExecutedSearchAreaParameters() {
        val expectedSearchAreas = mapOf(
            SearchPreset.TAXA_CZECH to "1",
            SearchPreset.TAXA_ALL_LANGUAGES to "6",
            SearchPreset.IMAGES to "2",
            SearchPreset.LINKS_AND_LITERATURE to "3",
            SearchPreset.TERMS to "5",
            SearchPreset.TERMS_ALL_LANGUAGES to "9",
            SearchPreset.BIOTOPES to "7",
            SearchPreset.LOCALITIES to "8",
            SearchPreset.EVERYWHERE to "100"
        )

        expectedSearchAreas.forEach { (preset, searchArea) ->
            val parameters = preset.parameters.associate { it.name to it.value }

            assertEquals("execute", parameters["action"])
            assertEquals(searchArea, parameters["searcharea"])
            assertEquals(2, parameters.size)
        }
    }
}
