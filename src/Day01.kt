fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstNumber = line.find { it.isDigit() }
            val lastNumber = line.findLast { it.isDigit() }

            try {
                "$firstNumber$lastNumber".toInt()
            } catch(e: Exception) {
                0
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val firstNumber = Number.entries.minBy { number ->
                val fullNumber = line.indexOf(number.name)
                val smallNumber = line.indexOf(number.number)

                listOf(fullNumber, smallNumber).minOf { if (it == -1) Int.MAX_VALUE else it }
            }
            val lastNumber = Number.entries.maxBy { number ->
                val fullNumber = line.lastIndexOf(number.name)
                val smallNumber = line.lastIndexOf(number.number)

                listOf(fullNumber, smallNumber).max()
            }

            "${firstNumber.number}${lastNumber.number}".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val part1Test = part1(testInput)
    val part2Test = part2(testInput)

    printTestOutput(testInput, part1Test, part2Test)

    val input = readInput("Day01")
    val part1 = part1(input)
    val part2 = part2(input)

    printOutput(part1, part2)
}

enum class Number(val number: String) {
    one("1"),
    two("2"),
    three("3"),
    four("4"),
    five("5"),
    six("6"),
    seven("7"),
    eight("8"),
    nine("9");
}

private fun printOutput(part1: Int, part2: Int) {
    println("\n\n")
    println(
        """
        +++ Results +++
        
        Part 1
        $part1
        
        Part 2
        $part2
    """.trimIndent()
    )
}

private fun printTestOutput(testInput: List<String>, part1Test: Int, part2Test: Int) {
    println(
        """
        --- Test Results ---
        
        Input: $testInput
        
        Part 1 (test)
        $part1Test
        
        Part 2 (test)
        $part2Test
    """.trimIndent()
    )
}
