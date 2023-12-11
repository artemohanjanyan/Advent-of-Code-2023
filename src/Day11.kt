import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun sumDistances(input: List<String>, multiplier: Long): Long {
        val galaxies = input.flatMapIndexedTo(HashSet()) { i, row ->
            row.mapIndexedNotNull { j, c -> if (c == '#') Pair(i, j) else null }
        }
        val emptyRows = input.withIndex().filter { !it.value.contains('#') }.map { it.index }.toSet()
        val emptyColumns = input[0].indices.filter { j -> input.indices.all { i -> input[i][j] != '#' } }.toSet()
        var ans = 0L
        for (galaxyA in galaxies) {
            for (galaxyB in galaxies) {
                if (galaxyA.first < galaxyB.first || galaxyA.first == galaxyB.first && galaxyA.second < galaxyB.second) {
                    ans += galaxyB.first - galaxyA.first
                    ans += emptyRows.filter { galaxyA.first < it && it < galaxyB.first }.size * (multiplier - 1)
                    ans += abs(galaxyB.second - galaxyA.second)
                    ans += emptyColumns.filter { min(galaxyA.second, galaxyB.second) < it && it < max(galaxyA.second, galaxyB.second) }.size * (multiplier - 1)
                }
            }
        }
        return ans
    }

    fun part1(input: List<String>): Long {
        return sumDistances(input, 2)
    }

    fun part2(input: List<String>): Long {
        return sumDistances(input, 1000000)
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    check(sumDistances(testInput, 10) == 1030L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
