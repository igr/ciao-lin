package ac.obl.shaolin

val cellMetaCount: (Cell) -> Int = { it.meta.count }
val cellMetaRatio: (Cell) -> Float = { it.meta.ratio }

fun maxCell(fundus: Fundus, valueSupplier: (Cell) -> Number): Cell? {
    var max = 0f
    var maxCell: Cell? = null

    fundus.forEachCell {
        val value = valueSupplier(it).toFloat()
        if (value > max) {
            max = value
            maxCell = it
        }
    }
    return maxCell
}

fun maxCellForStart(fundus: Fundus, x: Int, valueSupplier: (Cell) -> Number): Cell? {
    var max = 0f
    var maxCell: Cell? = null

    fundus.forEachCellInColumn(x) {
        val value = valueSupplier(it).toFloat()
        if (value > max) {
            max = value
            maxCell = it
        }
    }

    return maxCell
}