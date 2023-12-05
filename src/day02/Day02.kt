package day02

import getFirstGroup
import printOutput
import printTestOutput
import readInput
import readTestInput

private val gameIdRegex = Regex("Game (\\d+):")
private val cubeRevealsRegex = Regex("Game \\d+: (.*)")
private val cubeCountRegex = Regex("(\\d+) ")
private val cubeRegex = Regex("\\d+ (.*)")

fun main() {
    val day = 2
    println("\n\n")

    fun readGames(input: List<String>): List<Game> {
        return input.map { line ->
            val gameId = gameIdRegex.getFirstGroup(line).toInt()
            val cubeReveals = cubeRevealsRegex.getFirstGroup(line).split(";").map { cubeReveal ->
                val cubeToAmount = cubeReveal.split(",").associate { cubeString ->
                    val count = cubeCountRegex.getFirstGroup(cubeString).toInt()
                    val cube = Cube.valueOf(cubeRegex.getFirstGroup(cubeString))

                    cube to count
                }

                CubeReveal(cubeToAmount = cubeToAmount)
            }


            Game(id = gameId, cubeReveals = cubeReveals)
        }
    }

    fun part1(input: List<String>): Int {
        val games = readGames(input)
        val maximumCountPerCube = mapOf(
            Cube.red to 12,
            Cube.green to 13,
            Cube.blue to 14
        )

        val possibleGames = games.filter { game ->
            game.cubeReveals.all { cubeReveal ->
                cubeReveal.cubeToAmount.all { (cube, amount) ->
                    amount <= maximumCountPerCube.getValue(cube)
                }
            }
        }

        return possibleGames.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        val games = readGames(input)

        val gameToLowestPossibleCubeAmounts = games.associateWith { game ->
            Cube.entries.associateWith { cube ->
                game.cubeReveals.maxBy { it.cubeToAmount[cube] ?: 0 }.cubeToAmount[cube] ?: 0
            }
        }

        val powers = gameToLowestPossibleCubeAmounts.map { (_, cubeAmounts) ->
            cubeAmounts.values.reduce { a, b -> a * b }
        }

        return powers.sum()
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

data class Game(
    val id: Int,
    val cubeReveals: List<CubeReveal>
)

data class CubeReveal(
    val cubeToAmount: Map<Cube, Int>
)

enum class Cube {
    blue, red, green
}
