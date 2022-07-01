package ac.obl.shaolin

import ac.obl.shaolin.four.Algo4
import java.nio.file.Files
import java.nio.file.Path
import kotlin.random.Random
import kotlin.system.measureTimeMillis

const val ALGO = 4
const val LOG = true

//const val FILE = "small.txt"
const val FILE = "rijeci.txt"

fun main() {
    val rawWords = loadWords(FILE)
    val fundus = createFundusFromRawWords(rawWords)

    println("Total words: ${fundus.words.size}")
    println("Total subs: ${fundus.subs.size}")
    println("Algorithm: $ALGO")
    println("-----------")

    val maxChain: List<String>
    val elapsed = measureTimeMillis {
        maxChain = when (ALGO) {
            1 -> Algo1(fundus, LOG)
            2 -> Algo2(fundus, LOG)
            3 -> Algo3(fundus, LOG, List(24000) { Random.nextInt(2) })
            4 -> Algo4(rawWords, LOG)
            else -> {
                throw ShaolinException("No algo $ALGO")
            }
        }.setup().invoke()
    }

    println("-----------")

    val words = maxChain
    words.forEach { println(it) }
    println("Chain length: ${maxChain.size} / ${words.size}")
    println("Done in ${elapsed}ms.")

    val result = words.joinToString(" ")
    Files.writeString(Path.of("out/${words.size}.txt"), result)
}
