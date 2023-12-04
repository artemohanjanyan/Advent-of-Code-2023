import kotlin.math.min
import kotlin.math.pow

fun main() {
    data class Game(val id: Int, val winning: Set<Int>, val taken: List<Int>) {
        fun matches(): Int = taken.filter { winning.contains(it) }.size
    }

    fun parseGame(str: String): Game {
        var s = str
        s = s.drop("Card".length)
        s = s.dropWhile { it == ' ' }
        val parts = s.split(Regex(":\\s+|\\s+\\|\\s+"))
        check(parts.size == 3)
        return Game(
                parts[0].toInt(),
                parts[1].split(Regex("\\s+")).map { it.toInt() }.toSet(),
                parts[2].split(Regex("\\s+")).map { it.toInt() },
        )
    }

    fun part1(input: List<Game>): Int {
        return input.sumOf { game ->
            2.0.pow(game.matches() - 1).toInt()
        }
    }

    fun part2(input: List<Game>): Int {
        val copies = Array(input.size) { 1 }
        for ((i, game) in input.withIndex()) {
            val matches = game.matches()
            for (j in (i+1..min(input.size - 1, i+matches))) {
                copies[j] += copies[i]
            }
        }
        return copies.sum()
    }

    val testInput = readInput("Day04_test", ::parseGame)
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04", ::parseGame)
    part1(input).println()
    part2(input).println()
}
