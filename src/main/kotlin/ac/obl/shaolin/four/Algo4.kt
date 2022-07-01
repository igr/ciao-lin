package ac.obl.shaolin.four

import ac.obl.shaolin.Algo
import ac.obl.shaolin.ShaolinException

/**
 * Length: 26552
 * Time: 6688567ms (111 min == 1h50m)
 */
class Algo4(rawWords: List<String>, private val log: Boolean) : Algo {

    private val graph = Graph()
    private val allVertices: List<Vertex>

    init {
        rawWords.forEach { graph.addEdge(it) }
        allVertices = graph.vertices.sorted()
    }

    private fun solve(): Candidate {
        var iteration = 0
        var answer = Candidate()
        val additionalConstraints = mutableListOf<String>()

        while (true) {
            if (log) println("Run GLPK iteration $iteration")

            val glpkModel = GlpkModel(graph, allVertices)

            glpkModel.generateModelFile(additionalConstraints)

            val rawOutput = glpkModel()
            val result = glpkModel.parseResult(rawOutput)
            val candidate = Candidate(result)

            if (log) println("GLPK result parsed OK")

            if (candidate.score == 0) {
                break
            }

            if (!candidate.separated) {
                if (candidate.linkedScore > answer.linkedScore) answer = candidate
                break
            }

            if (answer.linkedScore > candidate.score) {
                break
            }

            if (candidate.linkedScore > answer.score) {
                answer = candidate
            }

            if (log) println("Create GLPK constraints")

            val linkedVertices = candidate.linkedVertices
            val constraint = glpkModel.createConstraints(allVertices, linkedVertices, iteration)

            additionalConstraints.add(constraint)

            iteration++
        }
        return answer
    }

    // ---------------------------------------------------------------- interface

    override fun setup(): Algo4 {
        return this
    }

    override fun invoke(): List<String> {
        val answer = solve()

        if (log) println("Answer found!")

        val route = PathFinder(answer.scores.toMutableMap(), graph, allVertices)

        if (route.size + 2 != answer.linkedScore) {
            throw ShaolinException("Invalid answer $route")
        }

        return route.map { it.value }
    }
}
