package day07

import printOutput
import printTestOutput
import readInput
import readTestInput

fun main() {
    val day = 7
    println("\n\n")

    fun getHands(input: List<String>): List<Hand> {
        return input.map { line ->
            val cards = line.substring(0, 5).toCharArray().map { CardValue.fromChar(it) }
            val bid = line.substringAfter(' ').toInt()
            Hand(cards, bid)
        }
    }

    fun classify(hand: Hand): Hand {
        val distinctCardsSize = hand.cards.distinct().size
        val eachCardCount = hand.cards.groupingBy { it }.eachCount()

        val handType = HandType.entries.first { handType ->
            distinctCardsSize == handType.distinctSize && eachCardCount.any { it.value == handType.anyValueCount }
        }

        return hand.copy(type = handType)
    }

    fun classify2(hand: Hand): Hand {
        val eachCardCount = hand.cards.groupingBy { it }.eachCount()

        val highestCardCount = eachCardCount.filter { it.key != CardValue.JACK }.maxByOrNull { it.value }?.key ?: CardValue.ACE
        val cardsWithJokersReplaced = hand.cards.map { card ->
            if (card == CardValue.JACK) {
                return@map highestCardCount
            }
            card
        }
        val distinctCardsSize = cardsWithJokersReplaced.distinct().size
        val eachCardCountAfterReplace = cardsWithJokersReplaced.groupingBy { it }.eachCount()


        val handType = HandType.entries.first { handType ->
            distinctCardsSize == handType.distinctSize && eachCardCountAfterReplace.any { it.value == handType.anyValueCount }
        }

        println("$handType: ${hand.cards}")

        return hand.copy(type = handType)
    }

    fun part1(input: List<String>): Int {
        return getHands(input)
            .map { classify(it) }
            .sorted()
            .foldIndexed(0) { index, acc, hand ->
                acc + (hand.bid * (index + 1))
            }
    }

    fun part2(input: List<String>): Int {
        return getHands(input)
            .map { classify2(it) }
            .sorted()
            .foldIndexed(0) { index, acc, hand ->
                acc + (hand.bid * (index + 1))
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput(day)
    val part1Test = part1(testInput)
    val part2Test = part2(testInput)

    printTestOutput(testInput, part1Test, part2Test)

    val input = readInput(day)
    val part1 = part1(input)
    val part2 = part2(input)

    printOutput(part1, part2)
}

data class Hand(
    val cards: List<CardValue>,
    val bid: Int,
    val type: HandType? = null
) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        if (this.type!!.ordinal > other.type!!.ordinal) {
            return 1
        }
        if (this.type.ordinal < other.type.ordinal) {
            return -1
        }
        this.cards.zip(other.cards) { a, b ->
            if (a == b) return@zip
            if (a.ordinal > b.ordinal) return 1
            return -1
        }
        return 0
    }

}

enum class CardValue(val char: Char) {
    JACK('J'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    companion object {
        fun fromChar(char: Char) = entries.first { it.char == char }
    }
}

enum class HandType(val distinctSize: Int, val anyValueCount: Int) {
    HIGH_CARD(5, 1),
    ONE_PAIR(4, 2),
    TWO_PAIR(3, 2),
    THREE_OF_A_KIND(3, 3),
    FULL_HOUSE(2, 3),
    FOUR_OF_A_KIND(2, 4),
    FIVE_OF_A_KIND(1, 5)
}