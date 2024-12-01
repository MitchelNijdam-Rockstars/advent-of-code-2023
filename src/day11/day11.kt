package day11

import kotlin.math.abs
import printOutput
import printTestOutput
import readInput
import readTestInput

fun main() {
    val day = 11
    println("\n\n")


    fun parseInput(input: List<String>): List<List<Char>> {
        return input.map { line ->
            line.toCharArray().toList()
        }
    }

    fun expandUniverse(universe: List<List<Char>>, expandAmount: Int = 1): List<List<Char>> {
        val expandedUniverse = universe.map { row ->
            row.toMutableList()
        }.toMutableList()

        var expandedRows = 0
        var expandedColumns = 0
        universe.forEachIndexed { index, row ->
            if (row.all { it == '.' }) {
                repeat(expandAmount) {
                    val extraRow = universe.first().map { '.' }.toMutableList()
                    expandedUniverse.add(index + 1 + expandedRows, extraRow)
                    expandedRows++
                }
            }
        }

        universe.first().forEachIndexed { index, _ ->
            if (universe.all { it[index] == '.' }) {
                repeat(expandAmount) {
                    val toUseIndex = index + 1 + expandedColumns

                    expandedUniverse.forEach { it.add(toUseIndex, '.') }
                    expandedColumns++
                }
            }
        }

        return expandedUniverse
    }

    fun getCoordinates(
        galaxy: List<List<Char>>,
        expandedRows: List<Int>,
        expandedColumns: List<Int>,
        expandFactor: Int = 1
    ): List<Pair<Long, Long>> {
        val coordinates = mutableListOf<Pair<Long, Long>>()
        galaxy.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, point ->
                if (point == '#') {
                    val x = rowIndex + (expandedRows.count { it < rowIndex }) * (expandFactor - 1)
                    val y = columnIndex + (expandedColumns.count { it < columnIndex  }) * (expandFactor - 1)
                    coordinates.add(x.toLong() to y.toLong())
                }
            }
        }

        return coordinates
    }

    fun <T> getGalaxyPairs(galaxies: List<T>): List<Pair<T, T>> {
        return galaxies.flatMapIndexed { index, galaxy ->
            galaxies.drop(index + 1).map { it to galaxy }
        }
    }

    fun calculatePathLengths(galaxyPairs: List<Pair<Pair<Long, Long>, Pair<Long, Long>>>): List<Long> {
        return galaxyPairs.map { (galaxy1, galaxy2) ->
            val (x1, y1) = galaxy1
            val (x2, y2) = galaxy2
            (abs(x1 - x2) + abs(y1 - y2))
        }
    }

    fun getExpandedRows(universe: List<List<Char>>): List<Int> {
        return universe.mapIndexedNotNull { index, row ->
            if (row.all { it == '.' }) index
            else null
        }
    }

    fun getExpandedColumns(universe: List<List<Char>>): List<Int> {
        return universe.first().mapIndexed { index, _ ->
            if (universe.all { it[index] == '.' }) index
            else null
        }.filterNotNull()
    }

    fun part1(input: List<String>): Long {
        val universe = parseInput(input)
        val expandedRows = getExpandedRows(universe)
        val expandedColumns = getExpandedColumns(universe)
        val galaxyCoordinates = getCoordinates(universe, expandedRows, expandedColumns, 1)
        val galaxyPairs = getGalaxyPairs(galaxyCoordinates)

        val pathLengths = calculatePathLengths(galaxyPairs)

        return pathLengths.sum()
    }

    fun part2(input: List<String>): Long {
        val universe = parseInput(input)
        val expandedRows = getExpandedRows(universe)
        val expandedColumns = getExpandedColumns(universe)
        val galaxyCoordinates = getCoordinates(universe, expandedRows, expandedColumns, 1_000_000)
        val galaxyPairs = getGalaxyPairs(galaxyCoordinates)

        val pathLengths = calculatePathLengths(galaxyPairs)

        return pathLengths.sum()
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

private fun List<List<Char>>.print() {
    println("--")
    this.forEach { row ->
        println(row.joinToString(""))
    }
    println("--\n")
}
