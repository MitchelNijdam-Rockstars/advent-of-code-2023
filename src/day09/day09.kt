package day09

import findAllNumbers
import kotlin.math.absoluteValue
import printOutput
import printTestOutput
import readInput
import readTestInput

fun main() {
    val day = 9
    println("\n\n")

    fun getOasis(input: List<String>): Oasis {
        val history = input.map { line ->
            findAllNumbers(line)
        }
        return Oasis(history)
    }

    fun part1(input: List<String>): Long {
        val oasis = getOasis(input)

        val differenceTrees = oasis.history.map { historyValues ->
            val differenceTreeValues = mutableListOf(historyValues)
            var currentDifferences = historyValues

            while (!currentDifferences.all { it == 0L }) {
                currentDifferences = currentDifferences.windowed(2, 1) { twoValues ->
                    twoValues[1] - twoValues[0]
                }
                differenceTreeValues.add(currentDifferences)
            }

            DifferenceTree(differenceTreeValues)
        }

        val differenceTreesWithPredictions = differenceTrees.map { tree ->
            val values = tree.values.foldRight(mutableListOf<List<Long>>()) { differences, acc ->
                val mutableDifferences = differences.toMutableList()

                if (differences.all { it == 0L }) {
                    mutableDifferences.add(0L)
                } else {
                    val previousDifferences = acc.first()
                    mutableDifferences.add(previousDifferences.last() + differences.last())
                }
                acc.add(0, mutableDifferences.toList())
                acc
            }

            DifferenceTree(values)
        }

        println("\n\n--WITH PRED--\n${differenceTreesWithPredictions.joinToString("\n---\n")}")

        val map = differenceTreesWithPredictions.map { it.values.first().last() }
        println(map)
        return map.sumOf { it }
    }

    fun part2(input: List<String>): Long {
        return input.size.toLong()
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

data class Oasis(
    val history: List<List<Long>>
)

data class DifferenceTree(
    val values: List<List<Long>>
) {
    override fun toString(): String {
        var i = 0
        return values.joinToString("\n") { differences ->
            val numbers = differences.joinToString(" ")
            numbers.padStart(numbers.length + i++)
        }
    }
}
