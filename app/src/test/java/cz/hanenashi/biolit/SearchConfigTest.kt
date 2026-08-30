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
        assertEquals(SearchConfig.DEFAULT_PRESET, SearchConfig.presetForPreferenceValue("1"))
    }

    @Test
    fun czechTaxaPresetUsesExecutedSearchParameters() {
        val parameters = SearchPreset.TAXA_CZECH.parameters.associate { it.name to it.value }

        assertEquals("1", parameters["searchrecords"])
        assertEquals("1", parameters["searchvnames"])
        assertEquals("0", parameters["searchgallery"])
        assertEquals("0", parameters["searchsources"])
        assertEquals("0", parameters["searcharticles"])
        assertEquals("0", parameters["searchdict"])
        assertEquals("1", parameters["searchsynonyms"])
        assertEquals("0", parameters["searchbiotops"])
        assertEquals("0", parameters["searchlocals"])
        assertEquals("4", parameters["searchtype"])
        assertEquals(10, parameters.size)
    }
}
