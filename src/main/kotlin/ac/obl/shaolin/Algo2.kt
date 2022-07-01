package ac.obl.shaolin

/*

Dužina: 24585
Vreme: 1500ms

*/

class Algo2(fundus: Fundus, log: Boolean) : Algo1(fundus, log) {

    override fun setup(): Algo2 {
        super.setup()
        setupCellMetaRatioAsThroughput(fundus)
        return this
    }

    override fun firstCell(): Cell = maxCell(fundus, cellMetaRatio)!!

    override fun nextCell(cell: Cell): Cell? = maxCellForStart(fundus, cell.y) {
        calcThroughputForCell(fundus, it)
    }
}
