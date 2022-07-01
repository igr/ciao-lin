package ac.obl.shaolin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FundusTest {

    @Test
    fun `fundus_created_correctly_from_raw_words`() {
        val rawWords = RawWords.list1
        val fundus = createFundusFromRawWords(rawWords)

        assertEquals(6, fundus.words.size)
        assertEquals(5, fundus.subs.size)

        assertEquals("aa", fundus.subs[0].value)
        assertEquals("bb", fundus.subs[1].value)
        assertEquals("cc", fundus.subs[2].value)
        assertEquals("dd", fundus.subs[3].value)
        assertEquals("ee", fundus.subs[4].value)

        var word = fundus.words[0]
        assertEquals("aa0bb", word.value)
        assertEquals(0, word.start)
        assertEquals(1, word.end)

        word = fundus.words[1]
        assertEquals("bb1cc", word.value)
        assertEquals(1, word.start)
        assertEquals(2, word.end)

        word = fundus.words[2]
        assertEquals("bb2dd", word.value)
        assertEquals(1, word.start)
        assertEquals(3, word.end)

        word = fundus.words[3]
        assertEquals("bb3cc", word.value)
        assertEquals(1, word.start)
        assertEquals(2, word.end)

        word = fundus.words[4]
        assertEquals("cc4ee", word.value)
        assertEquals(2, word.start)
        assertEquals(4, word.end)

        word = fundus.words[5]
        assertEquals("dd", word.value)
        assertEquals(3, word.start)
        assertEquals(3, word.end)

        // FUNDUS X == 0

        var cell = fundus[0, 0]
        assertNull(cell)

        cell = fundus[0, 1]!! // aa..bb
        fundus.assertValidCell(cell, 0, 1, listOf("aa0bb"))

        cell = fundus[0, 2]
        assertNull(cell)

        cell = fundus[0, 3]
        assertNull(cell)

        cell = fundus[0, 4]
        assertNull(cell)

        // FUNDUS X == 1

        cell = fundus[1, 0]
        assertNull(cell)

        cell = fundus[1, 1]
        assertNull(cell)

        cell = fundus[1, 2]!!
        fundus.assertValidCell(cell, 1, 2, listOf("bb1cc", "bb3cc"))

        cell = fundus[1, 3]!!
        fundus.assertValidCell(cell, 1, 3, listOf("bb2dd"))

        cell = fundus[1, 4]
        assertNull(cell)

        // FUNDUS X == 2

        cell = fundus[2, 0]
        assertNull(cell)

        cell = fundus[2, 1]
        assertNull(cell)

        cell = fundus[2, 2]
        assertNull(cell)

        cell = fundus[2, 3]
        assertNull(cell)

        cell = fundus[2, 4]!!
        fundus.assertValidCell(cell, 2, 4, listOf("cc4ee"))

        // FUNDUS X == 3

        cell = fundus[3, 0]
        assertNull(cell)

        cell = fundus[3, 1]
        assertNull(cell)

        cell = fundus[3, 2]
        assertNull(cell)

        cell = fundus[3, 3]!!
        fundus.assertValidCell(cell, 3, 3, listOf("dd"))

        cell = fundus[3, 4]
        assertNull(cell)

        // FUNDUS X == 4

        cell = fundus[4, 0]
        assertNull(cell)

        cell = fundus[4, 1]
        assertNull(cell)

        cell = fundus[4, 2]
        assertNull(cell)

        cell = fundus[4, 3]
        assertNull(cell)

        cell = fundus[4, 4]
        assertNull(cell)
    }

    private fun Fundus.assertValidCell(cell: Cell, x: Int, y: Int, words: List<String>) {
        assertEquals(x, cell.x)
        assertEquals(y, cell.y)
        assertEquals(this.subs[x], cell.start)
        assertEquals(this.subs[y], cell.end)
        assertEquals(words.size, cell.count)
        for (i in 0 until cell.count) {
            assertEquals(words[i], cell.words[i].value)
        }
    }

    @Test
    fun `test_iterator_of_fundus`() {
        val rawWords = RawWords.list1
        val fundus = createFundusFromRawWords(rawWords)

        val list = fundus.cells().asSequence().toList()

        assertEquals(5, list.size)
        assertEquals("aa", list[0].start.value)
        assertEquals("bb", list[0].end.value)
        assertEquals("bb", list[1].start.value)
        assertEquals("cc", list[1].end.value)
        assertEquals("bb", list[2].start.value)
        assertEquals("dd", list[2].end.value)
        assertEquals("dd", list[3].start.value)
        assertEquals("dd", list[3].end.value)
        assertEquals("cc", list[4].start.value)
        assertEquals("ee", list[4].end.value)
    }

    @Test
    fun `test_forEachCell_of_fundus`() {
        val rawWords = RawWords.list1
        val fundus = createFundusFromRawWords(rawWords)

        val list = mutableListOf<Cell>()
        fundus.forEachCell { list.add((it)) }

        assertEquals(5, list.size)
        assertEquals("aa", list[0].start.value)
        assertEquals("bb", list[0].end.value)
        assertEquals("bb", list[1].start.value)
        assertEquals("cc", list[1].end.value)
        assertEquals("bb", list[2].start.value)
        assertEquals("dd", list[2].end.value)
        assertEquals("dd", list[3].start.value)
        assertEquals("dd", list[3].end.value)
        assertEquals("cc", list[4].start.value)
        assertEquals("ee", list[4].end.value)
    }

    @Test
    fun `test_column_iterator_of_funds`() {
        val rawWords = RawWords.list1
        val fundus = createFundusFromRawWords(rawWords)

        var list = fundus.columnCells(0).asSequence().toList()

        assertEquals(1, list.size)
        assertEquals("aa", list[0].start.value)
        assertEquals("bb", list[0].end.value)

        list = fundus.columnCells(1).asSequence().toList()
        assertEquals(2, list.size)
        assertEquals("bb", list[0].start.value)
        assertEquals("cc", list[0].end.value)
        assertEquals("bb", list[1].start.value)
        assertEquals("dd", list[1].end.value)

        list = fundus.columnCells(2).asSequence().toList()
        assertEquals(1, list.size)
        assertEquals("cc", list[0].start.value)
        assertEquals("ee", list[0].end.value)

        list = fundus.columnCells(3).asSequence().toList()
        assertEquals(1, list.size)
        assertEquals("dd", list[0].start.value)
        assertEquals("dd", list[0].end.value)

    }
}