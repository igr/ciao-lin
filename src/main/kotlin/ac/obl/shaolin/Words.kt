package ac.obl.shaolin

data class WordSub(val value: String) {
    override fun toString() = value
}

class WordSubs(private val wordSubs: Array<WordSub>) {
    val size = wordSubs.size
    operator fun get(index: Int) = wordSubs[index]
    fun indexOf(wordSub: WordSub): Int? = when (val ndx = wordSubs.indexOf(wordSub)) {
        -1 -> null
        else -> ndx
    }

    fun wordOf(value: String): Word {
        val start = startOf(value)
        val startIndex = indexOf(WordSub(start))
        val end = endOf(value)
        val endIndex = indexOf(WordSub(end))
        if (startIndex == null || endIndex == null) throw ShaolinException("Word $value is out of this world")
        return Word(value, startIndex, endIndex)
    }
}

data class Word(val value: String, val start: Int, val end: Int)

class Words(private val words: Array<Word>) {
    val size = words.size
    operator fun get(index: Int): Word = words[index]
    fun forEach(consumer: (Word) -> Unit) = words.forEach(consumer)
}

private fun startOf(word: String): String = word.substring(0, 2)
private fun endOf(word: String): String = word.substring(word.length - 2)
private fun splitWordToStartAndEnd(word: String): List<String> = listOf(startOf(word), endOf(word))

fun collectUniqueWordSubs(rawWords: List<String>): WordSubs {
    return rawWords
        .asSequence()
        .map { splitWordToStartAndEnd(it) }
        .flatten()
        .distinct()
        .sorted()
        .map { WordSub(it) }
        .toList()
        .toTypedArray()
        .let {
            WordSubs(it)
        }
}

fun prepareWords(rawWords: List<String>, wordSubs: WordSubs): Words {
    return rawWords
        .sorted()
        .map { wordSubs.wordOf(it) }
        .toTypedArray()
        .let {
            Words(it)
        }
}
