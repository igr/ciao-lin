package ac.obl.shaolin.four

import ac.obl.shaolin.ShaolinException
import java.nio.file.Files
import java.nio.file.Path

class GlpkModel(private val graph: Graph, private val allVertices: List<Vertex>) {

    private val filePath = Path.of("glpk_model.mod")

    fun generateModelFile(constraints: List<String>) {

        // # IJ is within {V,V}
        // # IJ_ST is within {V_ST,V_ST}

        val modelDefinition = """
        param n := ${graph.vertices.size()};
        param s := n+1;
        param t := n+2;

        set V := 1..n;
        set V_ST := 1..n+2;

        var x{V_ST,V_ST} >=0;

        # OBJECTIVE: Maximize
        maximize Z: sum{i in V} (sum{j in V} (x[i,j]) ) + sum{j in V} (x[s,j]) + sum{i in V} (x[i,t]);

        # CONSTRAINTS
        s.t. START: sum{j in V} (x[s,j]) = 1;
        s.t. END: sum{i in V} (x[i,t]) = 1;
        s.t. EQ_FLOW {i in V}: sum{j in V} (x[i,j])  + x[i,t] - sum{j in V} (x[j,i]) - x[s,i] = 0;

        # CONSTRAINTS (VALUE RANGE)
        s.t. START_RANGE {j in V}: 0 <= x[s,j] <= 1;
        s.t. END_RANGE {i in V}: 0 <= x[i,t] <= 1;
    """.trimIndent() + "\n"

        Files.newBufferedWriter(filePath, Charsets.UTF_8).use {
            it.write(modelDefinition)

            allVertices.forEachIndexed { i, vi ->
                allVertices.forEachIndexed { j, vj ->
                    val size = graph[vi, vj]?.size ?: 0
                    it.write("s.t. F_${i}_${j}: 0 <= x[${i + 1},${j + 1}] <= $size;\n")
                }
            }

            for (constraint in constraints) {
                it.write(constraint)
                it.write("\n")
            }
            it.write("end;\n")
        }
    }

    operator fun invoke(): String {
        val result = runCommand("glpsol -m glpk_model.mod -o /dev/stdout", timeoutAmount = 0)!!

        Files.delete(filePath)

        return result
    }

    fun parseResult(result: String): MutableMap<VxPair, Int> {
        checkError(result)

        val x = mutableMapOf<VxPair, Int>()
        val v = allVertices + listOf(Vertex.START, Vertex.END)

        val regex = "x\\[(\\d+),(\\d+)](\\s+)(\\w+)(\\s+)(\\d+)".toRegex()

        result.lines().forEach { line ->
            val matchResults = regex.find(line)
            if (matchResults != null) {
                val (m1, m2, _, _, _, m3) = matchResults.destructured
                val vxPair = VxPair(v[m1.toInt() - 1], v[m2.toInt() - 1])
                val count = m3.toInt()
                if (count > 0)
                    x[vxPair] = count
            }
        }
        return x
    }

    private fun checkError(result: String) {
        if (result.contains("error")) {
            print(result)
            throw ShaolinException("GLPK error")
        }
    }

    fun createConstraints(
        allVertices: List<Vertex>,
        linkedVertices: Set<Vertex>,
        iteration: Int,
    ): String {
        val detachedVertices = allVertices - linkedVertices

        val items = mutableListOf<String>()
        linkedVertices.forEach { linkedVx ->
            var i = allVertices.indexOfFirst { it == linkedVx } + 1
            if (i == 0) {
                i = allVertices.size + 1
            }

            detachedVertices.forEach { detachedVx ->
                val j = allVertices.indexOfFirst { it == detachedVx } + 1
                if (i == 0) {
                    i = allVertices.size + 1
                }
                items.add("x[${i},${j}]")
            }
        }

        return "s.t. ITERATION_${iteration}: ${items.joinToString("+")} >= 1;"
    }

}
