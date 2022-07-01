package ac.obl.shaolin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class MaxTest {

    @Test
    fun `find_max_element_in_fundus`() {
        val rawWords = RawWords.list1
        val fundus = createFundusFromRawWords(rawWords)
        setupCellMetaCountAsWordsCount(fundus)

        val cell = maxCell(fundus, cellMetaCount)!!
        assertEquals(1, cell.x)
        assertEquals(2, cell.y)
        assertEquals(2, cell.count)
    }

    @Test
    fun `find_max_element_in_fundus_column`() {
        val rawWords = RawWords.list1
        val fundus = createFundusFromRawWords(rawWords)
        setupCellMetaCountAsWordsCount(fundus)

        var cell = maxCellForStart(fundus, 0, cellMetaCount)!!
        assertEquals(0, cell.x)
        assertEquals(1, cell.y)
        assertEquals(1, cell.count)

        cell = maxCellForStart(fundus, 1, cellMetaCount)!!
        assertEquals(1, cell.x)
        assertEquals(2, cell.y)
        assertEquals(2, cell.count)

        cell = maxCellForStart(fundus, 2, cellMetaCount)!!
        assertEquals(2, cell.x)
        assertEquals(4, cell.y)
        assertEquals(1, cell.count)

        cell = maxCellForStart(fundus, 3, cellMetaCount)!!
        assertEquals(3, cell.x)
        assertEquals(3, cell.y)
        assertEquals(1, cell.count)

        assertNull(maxCellForStart(fundus, 4, cellMetaCount))
    }
}