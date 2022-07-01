package ac.obl.shaolin.four

class Candidate(val scores: Map<VxPair, Int> = emptyMap()) {

    private val linked = linked()
    val score = scores.values.sum()
    val linkedScore = linked.first
    val linkedVertices = linked.second
    val separated = score != linkedScore

    private fun linked(): Pair<Int, Set<Vertex>> {
        var value = 0

        val stack = mutableListOf(Vertex.START)
        val searched = stack.toMutableSet()

        while (stack.isNotEmpty()) {
            val last = stack.removeLast()

            val keys = scores.keys.filter { it.from == last }
            value += keys.sumOf { scores[it] ?: 0 }

            val nextVertices = keys.map { it.to }.filter { !searched.contains(it) }

            stack += nextVertices
            searched += nextVertices
        }

        return Pair(value, searched)
    }

}
