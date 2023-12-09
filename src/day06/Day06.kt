package day06

import findAllNumbers
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import printOutput
import printTestOutput
import readInput
import readTestInput

fun main() {
    val day = 6
    println("\n\n")

    fun getBoatGames(input: List<String>): List<BoatGame> {
        val gameTimes = findAllNumbers(input[0])
        val gameDistances = findAllNumbers(input[1])

        return gameTimes.zip(gameDistances) { time, distance ->
            BoatGame(time, distance)
        }
    }

    fun part1(input: List<String>): Int {
        val games = getBoatGames(input)

        return games.map { game ->
            val distances = (1..game.time).map { pressedTime ->
                val timeLeft = game.time - pressedTime
                timeLeft * pressedTime
            }
            distances.filter { it > game.distanceToBeat }
        }.map { it.count() }.reduce { acc, i -> acc * i }
    }

    fun getBoatGame(input: List<String>): BoatGame {
        return getBoatGames(input.map { it.replace(" ", "") }).first()
    }

    fun part2(input: List<String>): Int {
        val game = getBoatGame(input)

        println(game)

        val timeMin = (-game.time + sqrt(game.time.toDouble().pow(2) - (4 * game.distanceToBeat))) / -2
        val timeMax = (-game.time - sqrt(game.time.toDouble().pow(2) - (4 * game.distanceToBeat))) / -2

        println("min: $timeMin, max: $timeMax = ${(timeMax - timeMin)}")




        return (floor(timeMax) - ceil(timeMin) + 1).roundToInt()
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

data class BoatGame(
    val time: Long,
    val distanceToBeat: Long
)
