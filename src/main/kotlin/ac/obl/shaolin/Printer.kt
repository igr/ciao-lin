package ac.obl.shaolin

fun printFundusColumn(fundus: Fundus, x: Int) {
    println("---")
    fundus.forEachCellInColumn(x) {
        println("[${it.x}, ${it.y}] ${it.start}..${it.end} <${it.count}>")
    }
}