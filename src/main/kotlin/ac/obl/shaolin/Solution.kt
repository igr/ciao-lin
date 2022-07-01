package ac.obl.shaolin

class Solution(private val fundus: Fundus, private val chain: List<Cell>) {

    operator fun invoke(): List<Word> {
        setupCellMetaCountAsWordsCount(fundus)
        return chain
            .map {
                it.words[it.count - (it.meta.count--)]
            }
            .toList()
    }

}