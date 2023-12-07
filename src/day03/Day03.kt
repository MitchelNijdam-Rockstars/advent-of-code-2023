package day03

import printOutput
import printTestOutput
import readInput
import readTestInput

private val numberRegex = Regex("(\\d+)")

fun main() {
    val day = 3
    println("\n\n")

    fun readPartNumbers(input: List<String>): List<PartNumber> {
        return input.mapIndexed { index, line ->
            val partNumbers = numberRegex.findAll(line).toList().map { result ->
                val match = result.groups[0]!!

                PartNumber(
                    number = match.value.toInt(),
                    row = index,
                    indexRange = match.range
                )
            }

            partNumbers
        }.flatten()
    }

    // PART 1

    fun touchesSymbol(row: Int, partNumber: PartNumber, input: List<String>): Boolean {
        val relevantLine = input.getOrNull(row) ?: return false
        val start = if (partNumber.indexRange.first - 1 < 0) 0 else partNumber.indexRange.first - 1
        val end =
            if (partNumber.indexRange.last + 2 > relevantLine.length) relevantLine.length else partNumber.indexRange.last + 2
        val relevantCharacters = relevantLine.substring(start, end)
        return relevantCharacters.any { it.isSymbol() }
    }

    fun isAdjacentToSymbol(input: List<String>, partNumber: PartNumber): Boolean {
        return listOf(partNumber.row - 1, partNumber.row, partNumber.row + 1).any { row ->
            touchesSymbol(row, partNumber, input)
        }
    }

    fun part1(input: List<String>): Int {
        val partNumbers = readPartNumbers(input)

        return partNumbers.filter { partNumber ->
            isAdjacentToSymbol(input, partNumber)
        }.sumOf { it.number }
    }

    // PART 2

    fun getGear(row: Int, partNumber: PartNumber, input: List<String>): Gear? {
        val relevantLine = input.getOrNull(row) ?: return null
        val start = if (partNumber.indexRange.first - 1 < 0) 0 else partNumber.indexRange.first - 1
        val end =
            if (partNumber.indexRange.last + 2 > relevantLine.length) relevantLine.length else partNumber.indexRange.last + 2
        val relevantCharacters = relevantLine.substring(start, end)
        val gearIndex = relevantCharacters.indexOf('*')
        val realGearIndex = relevantLine.indexOf('*', start)

        return if (gearIndex != -1) Gear(row, realGearIndex) else null
    }

    fun getAllGears(input: List<String>, partNumber: PartNumber): List<Gear> {
        return listOf(partNumber.row - 1, partNumber.row, partNumber.row + 1).mapNotNull { row ->
            getGear(row, partNumber, input)
        }
    }

    fun part2(input: List<String>): Int {
        val gearToPartNumber = mutableMapOf<Gear, MutableList<PartNumber>>()

        val partNumbers = readPartNumbers(input)

        partNumbers.forEach { partNumber ->
            getAllGears(input, partNumber).forEach { gear ->
                gearToPartNumber.putIfAbsent(gear, mutableListOf())
                gearToPartNumber.getValue(gear).add(partNumber)
            }
        }

        return gearToPartNumber.filterValues { it.size == 2 }.mapValues { (_, partNumbers) ->
            partNumbers.map { it.number }.reduce { a, b -> a * b }
        }.values.sum()
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

private fun Char.isSymbol() = !this.isDigit() && this != '.'

data class PartNumber(
    val number: Int,
    val row: Int,
    val indexRange: IntRange
)

data class Gear(
    val row: Int,
    val index: Int
)
