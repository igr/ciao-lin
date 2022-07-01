package ac.obl.shaolin

fun createFundusFromRawWords(rawWords: List<String>): Fundus {
    val wordSubs = collectUniqueWordSubs(rawWords)
    val words = prepareWords(rawWords, wordSubs)
    return Fundus(words, wordSubs).reset()
}

class Cell(
    val x: Int,
    val y: Int,
    val start: WordSub,
    val end: WordSub,
    val words: Array<Word> = arrayOf(),
    val meta: Meta = Meta(),
) {
    val count: Int = words.size

    fun addWord(it: Word): Cell {
        return Cell(x, y, start, end, words + it)
    }

//    fun clone(): Cell {
//        return Cell(
//            x,
//            y,
//            start,
//            end,
//            words,
//            Meta(meta.count, meta.ratio)
//        )
//    }
}

class Fundus(val words: Words, val subs: WordSubs) {
    private val size = subs.size
    private val matrix = Array(size) { Array<Cell?>(size) { null } }
    operator fun get(x: Int, y: Int): Cell? = matrix[x][y]
    private fun get(x: Int, y: Int, supplier: (x: Int, y: Int) -> Cell): Cell {
        var cell = get(x, y)
        if (cell == null) {
            cell = supplier(x, y)
            set(x, y, cell)
        }
        return cell
    }

    private val newCell: (Int, Int) -> Cell = { x, y ->
        Cell(x, y, subs[x], subs[y])
    }

    private operator fun set(x: Int, y: Int, cell: Cell) {
        matrix[x][y] = cell
    }

    private fun clear(x: Int, y: Int) {
        matrix[x][y] = null
    }

    fun forEachCell(consumer: (Cell) -> Unit) {
        for (y in 0 until size) {
            for (x in 0 until size) {
                val cell = get(x, y)
                if (cell != null) consumer(cell)
            }
        }
    }

    fun cells(): List<Cell> {
        val result = mutableListOf<Cell>()
        forEachCell { result.add(it) }
        return result
    }

    fun columnCells(x: Int): List<Cell> {
        val result = mutableListOf<Cell>()
        forEachCellInColumn(x) { result.add(it) }
        return result
    }

    fun forEachCellInColumn(x: Int, consumer: (Cell) -> Unit) {
        for (y in 0 until size) {
            val cell = get(x, y) ?: continue
            consumer(cell)
        }
    }

    fun forEachCellInRow(y: Int, consumer: (Cell) -> Unit) {
        for (x in 0 until size) {
            val cell = get(x, y) ?: continue
            consumer(cell)
        }
    }

    private fun forEachMatrixPoint(consumer: (Int, Int) -> Unit) {
        for (y in 0 until size) {
            for (x in 0 until size) {
                consumer(x, y)
            }
        }
    }

    fun reset(): Fundus {
        forEachMatrixPoint { x, y -> clear(x, y) }
        words.forEach {
            val cell = get(it.start, it.end, newCell)
            set(it.start, it.end, cell.addWord(it))
        }
        return this
    }

//    fun clone(): Fundus {
//        val newFunds = Fundus(words, subs)
//
//        forEachMatrixPoint { x, y ->
//            val cell = get(x, y)
//            if (cell != null) {
//                newFunds[x, y] = cell.clone()
//            }
//        }
//
//        return newFunds
//    }
}
