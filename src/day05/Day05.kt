package day05

import findAllNumbers
import printOutput
import printTestOutput
import readInput
import readTestInput


fun main() {
    val day = 5
    println("\n\n")

    // PART 1
    fun getFirstIndex(input: List<String>, contains: String): Int {
        input.forEachIndexed { index, s ->
            if (s.contains(contains)) return index
        }

        return -1
    }

    fun getFoodMappingLines(input: List<String>, startIndex: Int): List<String> {
        val foodMappingLines = mutableListOf<String>()
        (startIndex + 1..<input.size).forEach { lineIndex ->
            val line = input[lineIndex]
            if (line.isEmpty()) return foodMappingLines
            foodMappingLines.add(line)
        }
        return foodMappingLines
    }


    fun getMappings(input: List<String>) = FoodMapName.entries.associateWith { foodMapName ->
        val mapIndex = getFirstIndex(input, foodMapName.mapName)
        getFoodMappingLines(input, mapIndex).map { line ->
            val numbers = findAllNumbers(line)
            val destinationStart = numbers[0]
            val sourceStart = numbers[1]
            val rangeLength = numbers[2]
            FoodMapping(
                sourceStart..(sourceStart + rangeLength),
                destinationStart..(destinationStart + rangeLength),
                rangeLength
            )
        }
    }

    fun getAlmanac(input: List<String>): Almanac1 {
        val seeds = findAllNumbers(input[0])
        val mappings = getMappings(input)

        return Almanac1(seeds, mappings)
    }

    fun translateNumber(number: Long, mappings: List<FoodMapping>): Long {
        val mapping = mappings.find { it.sourceRange.contains(number) }
        if (mapping == null) {
            return number
        }

        val offset = number - mapping.sourceRange.first
        return mapping.destinationRange.first + offset
    }

    fun part1(input: List<String>): Long {
        val almanac = getAlmanac(input)

        val seedToLocation = almanac.seeds.associateWith { seed ->
            var currentNumber = seed
            FoodMapName.entries.forEach { foodMapName ->
                currentNumber = translateNumber(currentNumber, almanac.mappings[foodMapName]!!)
            }
            currentNumber
        }

        return seedToLocation.values.min()
    }


    fun getAlmanac2(input: List<String>): Almanac2 {
        val seeds = findAllNumbers(input[0]).windowed(2, 2) { pairs ->
            val start = pairs[0]
            val length = pairs[1]
            (start..(start + length))
        }

        val mappings = getMappings(input)

        return Almanac2(seeds, mappings)
    }

    fun getLocation(seed: Long, almanac: Almanac2) =
        FoodMapName.entries.fold(seed) { number, (foodMapName) ->
            translateNumber(number, almanac.mappings[foodMapName]!!)
        }

    fun part2(input: List<String>): Long {
        val almanac = getAlmanac2(input)

        val lowestLocations = almanac.seedRanges.map { seedRange ->
            println("seedRange: $seedRange")
            seedRange.windowed(500_000, 500_000, true) { seeds ->
                val minOf = seeds.minOf { getLocation(it, almanac) }
                minOf
            }
        }

        return lowestLocations.flatten().min()
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

enum class FoodMapName(val mapName: String) {
    SEED_TO_SOIL("seed-to-soil"),
    SOIL_TO_FERTILIZER("soil-to-fertilizer"),
    FERTILIZER_TO_WATER("fertilizer-to-water"),
    WATER_TO_LIGHT("water-to-light"),
    LIGHT_TO_TEMPERATURE("light-to-temperature"),
    TEMPERATURE_TO_HUMIDITY("temperature-to-humidity"),
    HUMIDITY_TO_LOCATION("humidity-to-location");

    operator fun component1(): FoodMapName {
        return this
    }
}

data class Almanac1(
    val seeds: List<Long>,
    val mappings: Map<FoodMapName, List<FoodMapping>>
)

data class Almanac2(
    val seedRanges: List<LongRange>,
    val mappings: Map<FoodMapName, List<FoodMapping>>
)

data class FoodMapping(
    val sourceRange: LongRange,
    val destinationRange: LongRange,
    val rangeLength: Long
)

