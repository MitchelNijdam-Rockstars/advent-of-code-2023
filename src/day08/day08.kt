package day08

import kotlin.concurrent.thread
import printOutput
import printTestOutput
import readInput
import readTestInput

private val charsRegex = Regex("([a-zA-Z]{3})")

fun main() {
    val day = 8
    println("\n\n")

    fun getInstructions(input: List<String>): Instructions {
        val directions = input[0].map { Direction.fromChar(it) }
        val locationMappings = (2..<input.size).associate { index ->
            val locationMapping = charsRegex.findAll(input[index]).map { it.value }.toList()
            locationMapping[0] to (locationMapping[1] to locationMapping[2])
        }
        return Instructions(directions, locationMappings)
    }

    fun part1(input: List<String>): Int {
        val instructions = getInstructions(input)

        val startPosition = "AAA"
        val endPosition = "ZZZ"

        var currentPosition = startPosition
        var steps = 0
        do {
            steps += 1
            val direction = instructions.directions[(steps - 1) % instructions.directions.size]
            val (left, right) = instructions.locationMappings[currentPosition]!!

            currentPosition = if (direction == Direction.LEFT) left else right
        } while (currentPosition != endPosition)
        return steps
    }

    fun part2(input: List<String>): Long {
        val instructions = getInstructions(input)

        val startPositions = instructions.locationMappings.filterKeys { it.endsWith('A') }.keys

        println("number of startPositions: ${startPositions.size}")
        val currentPositions = startPositions.toMutableList()
        var steps = 0L

        do {
            steps += 1
            val threads = currentPositions.mapIndexed { index, currentPosition ->
                thread {
                    val direction = instructions.directions[((steps - 1) % instructions.directions.size).toInt()]
                    val (left, right) = instructions.locationMappings[currentPosition]!!

                    currentPositions[index] = if (direction == Direction.LEFT) left else right
                }
            }
            threads.forEach { it.join() }
            if (steps % 10_000L == 0L) println("Reached $steps")
        } while (!currentPositions.all { it.endsWith('Z') })

        return steps
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput(day)
    val part1Test = part1(testInput)
    val part2Test = part2(testInput)

    printTestOutput(testInput, part1Test.toLong(), part2Test)

    val input = readInput(day)
    val part1 = part1(input)
    val part2 = part2(input)

    printOutput(part1.toLong(), part2)
}

data class Instructions(
    val directions: List<Direction>,
    val locationMappings: Map<String, Pair<String, String>>
)

enum class Direction(val char: Char) {
    LEFT('L'), RIGHT('R');

    companion object {
        fun fromChar(char: Char) = entries.first { it.char == char }
    }
}
