package ac.obl.shaolin

interface Algo {
    fun setup(): Algo
    operator fun invoke(): List<String>
}
