package day10

import day10.Observation.*
import day10.RelativePosition.*
import printTestOutput
import readTestInput

fun main() {
    val day = 10
    println("\n\n")

    fun mapGrid(input: List<String>): Pair<Grid, Location> {
        var location: Location? = null
        val observations = input.mapIndexed { indexY, line ->
            line.mapIndexed { indexX, char ->
                val observation = Observation.entries.first { char == it.char }
                if (observation == STARTING_POSITION) {
                    location = Location(x = indexX, y = indexY)
                }
                observation
            }
        }

        return Grid(observations) to location!!
    }

    fun traverseGrid(grid: Grid, startingLocation: Location): Long {
        var pipeLength = 0L
        var currentLocation = startingLocation
        var currentObservation = grid.getObservation(currentLocation)

        println("startLoc: $startingLocation")
        var count = 0L

        do {
            count++
            if (count == 10L) return 0
            println("currentLocation: $currentLocation")

            loop@ for (relativePos in currentObservation!!.possibleRelativePositions) {
                println("testing relativePos: $relativePos")
                println(RelativePosition.entries.map { it.connectedObservations })
                val relativeLocation = Location(
                    x = currentLocation.x + relativePos.deltaX,
                    y = currentLocation.y + relativePos.deltaY
                )
                currentObservation = grid.getObservation(relativeLocation)
                println("current observation = $currentObservation")
                if (currentObservation == null) {
                    println("invalid location")
                    continue@loop
                }
                if (relativePos.connectedObservations.contains(currentObservation)) {
                    println("connected observation found")
                    pipeLength++
                    currentLocation = relativeLocation
                    break@loop
                }
            }
        } while (grid.getObservation(currentLocation) != STARTING_POSITION)

        return pipeLength
    }

    fun part1(input: List<String>): Int {
        val (grid, startingLocation) = mapGrid(input)
        val totalPipeLength = traverseGrid(grid, startingLocation)
        println(totalPipeLength)
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput(day)
    val part1Test = part1(testInput)
    val part2Test = part2(testInput)

    printTestOutput(testInput, part1Test, part2Test)

//    val input = readInput(day)
//    val part1 = part1(input)
//    val part2 = part2(input)
//
//    printOutput(part1, part2)
}

data class Grid(
    val observations: List<List<Observation>>
) {
    fun getObservation(location: Location) = observations.getOrNull(location.y)?.getOrNull(location.x)
}

enum class Observation(val char: Char, val possibleRelativePositions: Set<RelativePosition>) {
    VERTICAL_PIPE('|', setOf(TOP, BOTTOM)),
    HORIZONTAL_PIPE('-', setOf(RIGHT, LEFT)),
    TOP_RIGHT_PIPE('L', setOf(TOP, RIGHT)),
    TOP_LEFT_PIPE('J', setOf(TOP, LEFT)),
    BOTTOM_LEFT_PIPE('7', setOf(BOTTOM, LEFT)),
    BOTTOM_RIGHT_PIPE('F', setOf(BOTTOM, RIGHT)),
    GROUND('.', emptySet()),
    STARTING_POSITION('S', setOf(TOP, RIGHT, BOTTOM, LEFT))
}

enum class RelativePosition(val connectedObservations: Set<Observation>, val deltaX: Int, val deltaY: Int) {
    TOP(setOf(VERTICAL_PIPE, BOTTOM_LEFT_PIPE, BOTTOM_RIGHT_PIPE), 0, -1),
    RIGHT(setOf(HORIZONTAL_PIPE, TOP_LEFT_PIPE, BOTTOM_LEFT_PIPE), 1, 0),
    BOTTOM(setOf(VERTICAL_PIPE, TOP_RIGHT_PIPE, TOP_LEFT_PIPE), 0, 1),
    LEFT(setOf(HORIZONTAL_PIPE, TOP_RIGHT_PIPE, BOTTOM_RIGHT_PIPE), -1, 0)
}

data class Location(
    val x: Int,
    val y: Int
)