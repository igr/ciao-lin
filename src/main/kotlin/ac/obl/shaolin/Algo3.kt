package ac.obl.shaolin

/**
 * Length: 24585
 * Time: 12s (!)
 */
class Algo3(
    fundus: Fundus,
    log: Boolean,
    private val choices: List<Int>,
) : Algo1(fundus, log) {

    override fun setup(): Algo3 {
        super.setup()
        setupCellMetaRatioAsThroughput(fundus)
        return this
    }

    override fun firstCell(): Cell {
        return fundus
            .cells()
            .sortedByDescending(cellMetaCount)
            .elementAt(if (choices.isNotEmpty()) choices[0] else 0)
    }

    override fun nextCell(cell: Cell): Cell? {
        return fundus
            .columnCells(cell.y)
            .filter { it.meta.count != 0 }
            .sortedByDescending { calcThroughputForCell(fundus, it) }
            .elementAtOrNull(if (choices.size > chain.size) choices[chain.size] else 0)
    }

}
