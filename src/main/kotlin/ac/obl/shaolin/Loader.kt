package ac.obl.shaolin

private fun loadResourceAsText(fileName: String): String? =
    object {}.javaClass.getResource("/$fileName")?.readText()

fun loadWords(fileName: String): List<String> {
    return loadResourceAsText(fileName)!!
        .lines()
        .map { it.trim() }
        .filter { it.length >= 2 }
        .filter { it.isNotEmpty() }
}

private val otherWordsAndKalodont: (String) -> Boolean = {
    if (!it.startsWith("ka")) {
        true
    } else
        it.equals("kalodont")
}
