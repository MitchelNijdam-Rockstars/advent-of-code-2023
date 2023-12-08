package day04

import kotlin.math.pow
import printOutput
import printTestOutput
import readInput
import readTestInput

fun main() {
    val day = 4
    println("\n\n")

    fun splitToNumbers(part: String) =
        part.split(' ').filterNot { it == "" }.map { it.toInt() }

    fun getAmountOfWinningNumbers(numbersYouHave: List<Int>, winningNumbers: List<Int>) =
        numbersYouHave.count { winningNumbers.contains(it) }

    fun getCards(input: List<String>) = input.mapIndexed { cardIndex, line ->
        val parts = line.split(':', '|')
        val numbersYouHave = splitToNumbers(parts[1])
        val winningNumbers = splitToNumbers(parts[2])
        Card(
            cardIndex,
            numbersYouHave,
            winningNumbers,
            getAmountOfWinningNumbers(numbersYouHave, winningNumbers)
        )
    }

    fun part1(input: List<String>): Int {
        val cards = getCards(input)

        return cards.map { card ->
            if (card.amountOfWinningNumbers == 0) 0 else 2.0.pow(card.amountOfWinningNumbers - 1)
        }.sumOf { it.toInt() }
    }

    fun getCopies(card: Card, allCards: List<Card>): List<Card> {
        val amountOfWinningNumbers = card.amountOfWinningNumbers
        return (card.index + 1..card.index + amountOfWinningNumbers).map { allCards[it] }
    }

    fun part2(input: List<String>): Int {
        val cards = getCards(input)

        val cardToAmountOfCopies = cards.associateWith {
            1
        }.toMutableMap()

        cards.forEach { card ->
            val copies = getCopies(card, cards)
            val amountOfCopiesOfOriginalCard = cardToAmountOfCopies[card]!!
            copies.forEach { copy ->
                cardToAmountOfCopies[copy] = cardToAmountOfCopies[copy]!! + (1 * amountOfCopiesOfOriginalCard)
            }
        }

        return cardToAmountOfCopies.values.sum()
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

data class Card(
    val index: Int,
    val numbersYouHave: List<Int>,
    val winningNumbers: List<Int>,
    val amountOfWinningNumbers: Int
)
