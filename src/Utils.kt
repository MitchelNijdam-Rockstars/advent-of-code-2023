import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readTestInput(day: Int) = Path("src/day${day.toDayString()}/Day${day.toDayString()}_test.txt").readLines()
fun readInput(day: Int) = Path("src/day${day.toDayString()}/Day${day.toDayString()}.txt").readLines()

private fun Int.toDayString(): String = this.toString().padStart(2, '0')

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun printTestOutput(testInput: List<String>, part1Test: Int, part2Test: Int) {
    println("\n\n\n")
    println("------------------------------------------------------")
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

fun printOutput(part1: Int, part2: Int) {
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

fun Regex.getFirstGroup(line: String): String = this.find(line)!!.groupValues[1]