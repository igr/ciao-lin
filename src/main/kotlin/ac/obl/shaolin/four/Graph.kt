package ac.obl.shaolin.four

// Vertex length (number of chars from the word)
private const val LEN = 2

data class Vertex constructor(val name: String) {
    operator fun plus(to: Vertex): VxPair {
        return VxPair(this, to)
    }

    init {
        if (name.length != LEN) {
            throw IllegalArgumentException("Vertex name '$name' must be $LEN characters long")
        }
    }

    companion object {
        val START = Vertex(">".repeat(LEN))
        val END = Vertex(".".repeat(LEN))
    }

    override fun toString(): String {
        return "($name)"
    }
}

data class VxPair(val from: Vertex, val to: Vertex) {
    constructor(from: String, to: String) : this(Vertex(from), Vertex(to))

    override fun toString(): String {
        return "$from->$to"
    }
}

data class Edge(val from: Vertex, val to: Vertex, val value: String) {
    override fun toString(): String {
        return "$from->$to <$value>"
    }
}

// ---------------------------------------------------------------- containers

class Vertices() {
    private val vertices = mutableSetOf<Vertex>()
    fun size() = vertices.size
    fun sorted() = vertices.sortedBy { it.name }.toList()
    fun add(vertex: Vertex) {
        vertices.add(vertex)
    }
}

class VxPath(array: Array<Vertex> = arrayOf()) {
    private val vertices = array
    fun length() = vertices.size
    fun first(): Vertex = vertices.first()
    fun last(): Vertex = vertices.last()
    fun sub(index: Int): VxPath {
        return VxPath(vertices.sliceArray(index..vertices.lastIndex))
    }

    fun contains(v: Vertex): Boolean = vertices.contains(v)
    operator fun plus(v: Vertex): VxPath {
        return VxPath(vertices + v)
    }

    operator fun plus(path: VxPath): VxPath {
        return VxPath(vertices + path.vertices)
    }

    fun indexOf(v: Vertex): Int = vertices.indexOf(v)
    operator fun get(index: Int) = vertices[index]
}

class Graph() {
    private val matrix = mutableMapOf<VxPair, MutableList<Edge>>()
    val vertices = Vertices()

    fun addEdge(value: String) {
        val from = Vertex(value.substring(0, LEN))
        val to = Vertex(value.substring(value.length - LEN))

        val edge = Edge(from, to, value)
        matrix
            .getOrPut(from + to) { mutableListOf() }
            .add(edge)

        vertices.add(from)
        vertices.add(to)
    }

    operator fun get(from: Vertex, to: Vertex): MutableList<Edge>? = matrix[from + to]

    operator fun get(vertices: VxPair): MutableList<Edge>? = matrix[vertices]
    fun removeLast(xx: VxPair): Edge? = get(xx)?.removeLast()


}
