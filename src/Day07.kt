import java.util.Scanner
import java.util.regex.Pattern
import kotlin.io.path.*

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD,
}

fun main() {
    data class Hand(val cards: String, val bid: Long) {
        val cardsSorted = cards.toCharArray().sorted()

        private fun isFiveOfAKind() =
            cardsSorted[0] == cardsSorted[1] &&
                    cardsSorted[0] == cardsSorted[2] &&
                    cardsSorted[0] == cardsSorted[3] &&
                    cardsSorted[0] == cardsSorted[4]

        private fun isFourOfAKind() =
            cardsSorted[0] == cardsSorted[1] &&
                    cardsSorted[0] == cardsSorted[2] &&
                    cardsSorted[0] == cardsSorted[3] ||
                    cardsSorted[1] == cardsSorted[2] &&
                    cardsSorted[1] == cardsSorted[3] &&
                    cardsSorted[1] == cardsSorted[4]

        private fun isFullHouse() =
            cardsSorted[0] == cardsSorted[1] &&
                    cardsSorted[0] == cardsSorted[2] &&
                    cardsSorted[3] == cardsSorted[4] ||
                    cardsSorted[0] == cardsSorted[1] &&
                    cardsSorted[2] == cardsSorted[3] &&
                    cardsSorted[2] == cardsSorted[4]

        private fun isThreeOfAKind() =
            cardsSorted[0] == cardsSorted[1] && cardsSorted[0] == cardsSorted[2] ||
                    cardsSorted[1] == cardsSorted[2] && cardsSorted[1] == cardsSorted[3] ||
                    cardsSorted[2] == cardsSorted[3] && cardsSorted[2] == cardsSorted[4]

        private fun isTwoPair() =
            cardsSorted[0] == cardsSorted[1] && cardsSorted[2] == cardsSorted[3] ||
                    cardsSorted[0] == cardsSorted[1] && cardsSorted[3] == cardsSorted[4] ||
                    cardsSorted[1] == cardsSorted[2] && cardsSorted[3] == cardsSorted[4]

        private fun isOnePair() =
            cardsSorted[0] == cardsSorted[1] ||
                    cardsSorted[1] == cardsSorted[2] ||
                    cardsSorted[2] == cardsSorted[3] ||
                    cardsSorted[3] == cardsSorted[4]

        val handType: HandType = when(true) {
            isFiveOfAKind() -> HandType.FIVE_OF_A_KIND
            isFourOfAKind() -> HandType.FOUR_OF_A_KIND
            isFullHouse() -> HandType.FULL_HOUSE
            isThreeOfAKind() -> HandType.THREE_OF_A_KIND
            isTwoPair() -> HandType.TWO_PAIR
            isOnePair() -> HandType.ONE_PAIR
            else -> HandType.HIGH_CARD
        }
    }

    data class Input(val hands: List<Hand>)

    fun readInput(name: String): Input {
        Scanner(Path("src/$name.txt").inputStream()).use { scanner ->
            val cardsPattern = Pattern.compile("[AKQJT98765432]{5}")
            val hands = ArrayList<Hand>()
            while (scanner.hasNext(cardsPattern)) {
                val cards = scanner.next(cardsPattern)
                val bid = scanner.nextLong()
                hands.add(Hand(cards, bid))
            }
            return Input(hands)
        }
    }

    val allCards = "AKQJT98765432"
    val cardComparator: Comparator<Char> = compareBy { allCards.indexOf(it) }
    val cardsComparator: Comparator<String> = Comparator { str1, str2 ->
        str1.zip(str2).map { cardComparator.compare(it.first, it.second) }.firstOrNull { it != 0 } ?: 0
    }
    val handComparator: Comparator<Hand> = compareBy(Hand::handType).thenBy(cardsComparator, Hand::cards)

    fun part1(input: Input): Long {
        val sorted = input.hands.sortedWith(handComparator)
        return sorted.withIndex().sumOf { it.value.bid * (sorted.size - it.index) }
    }

    fun handType2(hand: Hand): HandType =
        "AKQT98765432".map { c ->
            Hand(hand.cards.replace('J', c), hand.bid).handType
        }.min()
    val allCards2 = "AKQT98765432J"
    val cardComparator2: Comparator<Char> = compareBy { allCards2.indexOf(it) }
    val cardsComparator2: Comparator<String> = Comparator { str1, str2 ->
        str1.zip(str2).map { cardComparator2.compare(it.first, it.second) }.firstOrNull { it != 0 } ?: 0
    }
    val handComparator2: Comparator<Hand> = compareBy(::handType2).thenBy(cardsComparator2, Hand::cards)

    fun part2(input: Input): Long {
        val sorted = input.hands.sortedWith(handComparator2)
        return sorted.withIndex().sumOf { it.value.bid * (sorted.size - it.index) }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
