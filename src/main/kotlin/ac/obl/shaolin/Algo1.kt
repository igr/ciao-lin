package ac.obl.shaolin

/**
 * Length: 22668
 * Time: 320ms
 * MAX-MAX (?) algoritam. Počinje od ćelije (para) koja ima najviše reči. Nastavlja se sa ćelijom koja takođe ima maksimalan broj reči.
 * Ukoliko ćelija nema ni jednu slobodnu preostalu reč, markira se kao potrošena,
i radi se rollback.
 */
open class Algo1(val fundus: Fundus, val log: Boolean = false) : Algo {

    protected val chain = mutableListOf<Cell>()
    private val maxChain = mutableListOf<Cell>()

    override fun setup(): Algo1 {
        setupCellMetaCountAsWordsCount(fundus)
        return this
    }

    private fun addCellToChain(chain: MutableList<Cell>, cell: Cell) {
        // mark usage
        cell.meta.count--
        if (cell.meta.count < 0) {
            throw ShaolinException("Too much, Mark")
        }

        // add to chain
        chain.add(cell)

        if (log) println("${chain.size}. ${cell.start}..${cell.end}")
    }

    private fun saveMaxChain() {
        if (chain.size > maxChain.size) {
            maxChain.clear()
            maxChain.addAll(chain)
        }
    }

    protected open fun firstCell() = maxCell(fundus, cellMetaCount)!!
    protected open fun nextCell(cell: Cell) = maxCellForStart(fundus, cell.y, cellMetaCount)

    protected open fun disableCell(cell: Cell) {
        cell.meta.count = 0
    }

    override operator fun invoke(): List<String> {
        var cell = firstCell()
        addCellToChain(chain, cell)

        while (true) {
            val nextCell = nextCell(cell)

            if (nextCell == null) {
                // rollback
                saveMaxChain()

                // označi je kao potrošenu i izbaci je iza dalje analize
                disableCell(cell)
                val removedCell = chain.removeLastOrNull() ?: break
                if (cell != removedCell) throw ShaolinException("Removed is not last")

                val prevCell = chain.lastOrNull() ?: break
                cell = prevCell
                continue
            }

            cell = nextCell

            addCellToChain(chain, cell)
        }

        return toWords(maxChain)
    }

    private fun toWords(maxChain: List<Cell>): List<String> {
        val solution = Solution(fundus, maxChain)

        return solution().map { it.value }
    }
}
