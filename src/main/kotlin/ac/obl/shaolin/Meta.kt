package ac.obl.shaolin

data class Meta(var count: Int = 0, var ratio: Float = 0f)

fun setupCellMetaCountAsWordsCount(fundus: Fundus) {
    fundus.forEachCell {
        it.meta.count = it.count
    }
}

fun setupCellMetaRatioAsThroughput(fundus: Fundus) {
    fundus.forEachCell {
        it.meta.ratio = calcThroughputForCell(fundus, it)
    }
}

fun calcThroughputForCell(fundus: Fundus, cell: Cell): Float {
    if (cell.meta.count == 0) {
        return 0f
    }

    var nextWordsCount = 0
    fundus.forEachCellInColumn(cell.y) { nextWord -> nextWordsCount += nextWord.count }

    return (nextWordsCount / cell.meta.count.toFloat())

}