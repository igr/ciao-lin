package ac.obl.shaolin.four

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GraphTest {

    @Test
    fun `graph_is_created`() {
        val graph = Graph()
        graph.addEdge("hello")

        assertEquals("hello", graph[VxPair("h", "o")]!![0].value)
    }
}
