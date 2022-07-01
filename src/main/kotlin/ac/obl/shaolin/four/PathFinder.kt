package ac.obl.shaolin.four

import ac.obl.shaolin.ShaolinException

object PathFinder {

    operator fun invoke(
        scores: MutableMap<VxPair, Int>,
        graph: Graph,
        allVertices: List<Vertex>,
    ): MutableList<Edge> {
        val data = extractAllCycle(scores, allVertices)

        var scoresReminding: MutableMap<VxPair, Int> = data.first.filterValues { it > 0 }.toMutableMap()
        val links: MutableList<VxPath> = data.second
        val startingVx = scoresReminding.filterKeys { it.from == Vertex.START }.map { it.key }.first().to
        val answer = mutableListOf(Edge(Vertex.START, startingVx, "START"))
        scoresReminding = scoresReminding.filterKeys { it.from != Vertex.START }.toMutableMap()

        while (true) {
            val nextVx = answer.last().to

            val edges = linkClosedPath(nextVx, links, graph)
            answer.addAll(edges)

            val firstVx: VxPair = scoresReminding.filterKeys { it.from == nextVx }.map { it.key }.first()
            if (firstVx.to == Vertex.END) break

            answer += graph.removeLast(firstVx)!!
            scoresReminding.remove(firstVx)
        }

        answer.removeFirst()

        repeat(answer.size - 1) {
            if (answer[it].to != answer[it + 1].from) {
                throw ShaolinException("Invalid answer $answer")
            }
        }

        return answer
    }


    private fun linkClosedPath(
        nextVx: Vertex,
        links: MutableList<VxPath>,
        graph: Graph,
    ): MutableList<Edge> {

        val availableLinks = links.filter { it.contains(nextVx) }.toList()
        availableLinks.forEach { links.remove(it) }

        val path = mutableListOf<Edge>()

        availableLinks.forEach { link ->
            val linkDup = link + link.sub(1)
            val offset = link.indexOf(nextVx)
            repeat(link.length() - 1) {
                val x = linkDup[it + offset]
                val y = linkDup[it + 1 + offset]
                val w = graph[x, y]!!
                path += w.removeLast()
                path += linkClosedPath(path.last().to, links, graph)
            }
        }
        return path
    }

    private fun extractAllCycle(
        scores: MutableMap<VxPair, Int>,
        allVertices: List<Vertex>,
    ): Pair<Map<VxPair, Int>, MutableList<VxPath>> {

        val d = allVertices.map { vi ->
            allVertices.sumOf { vj ->
                scores[VxPair(vj, vi)] ?: 0
            }
        }.toMutableList()

        val link = mutableListOf<VxPath>()
        var k = 1
        while (d.max() > 1) {
            allVertices.forEachIndexed { _, v ->
                while (true) {
                    val l = findClosedPath(v, scores, k) ?: break
                    link.add(l)
                    repeat(l.length() - 1) { i ->
                        val ij = VxPair(l[i], l[i + 1])
                        scores[ij] = scores[ij]!! - 1
                        val indexOfD = allVertices.indexOfFirst { v -> v == l[i] }
                        d[indexOfD] -= 1
                    }
                }
            }
            k++
        }
        return Pair(scores, link)
    }

    private fun findClosedPath(v: Vertex, x: Map<VxPair, Int>, deep: Int): VxPath? {
        val edges = x.filterValues { it > 0 }.map { it.key }
        return findClosedPathRecursively(VxPath(arrayOf(v)), edges, deep)
    }

    private fun findClosedPathRecursively(path: VxPath, edges: List<VxPair>, maxDepth: Int): VxPath? {

        if (path.length() == maxDepth + 1) {
            return if (path.first() == path.last()) {
                path
            } else null
        }

        edges.forEach { edge ->
            val ok = (path.length() < 2 || !path.sub(1).contains(edge.to))
            if (edge.from == path.last() && ok) {
                val closedPath = findClosedPathRecursively(path + edge.to, edges, maxDepth)
                if (closedPath != null) {
                    return@findClosedPathRecursively closedPath
                }
            }
        }
        return null
    }

}
