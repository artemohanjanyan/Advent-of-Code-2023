typealias Line9 = List<Long>

fun main() {
    fun readLine(line: String): Line9 {
        return line.split(' ').map { it.toLong() }
    }

    fun makePascal(input: List<Line9>): Array<LongArray> {
        val maxLength = input.maxOf { it.size } + 1
        val pascal = Array(maxLength) { LongArray(maxLength) }
        for (i in 0..<maxLength) {
            pascal[i][0] = 1
            pascal[i][i] = 1
            for (j in 1..<i) {
                pascal[i][j] = pascal[i - 1][j - 1] + pascal[i - 1][j]
            }
        }
        return pascal
    }

    fun findStepN(line: Line9): Int {
        var currentLine = line
        var stepN = 0
        while (!currentLine.all { it == 0L }) {
            currentLine = currentLine.zipWithNext { a, b -> b - a }
            ++stepN
        }
        return stepN
    }

    fun part1(input: List<Line9>): Long {
        val pascal = makePascal(input)
        return input.sumOf { line ->
            val stepN = findStepN(line)
            var ans = 0L
            for (i in 0..<stepN) {
                ans += line[line.size - 1 - i] * pascal[stepN][i + 1] * (if (i % 2 == 0) 1 else -1)
            }
            ans
        }
    }

    fun part2(input: List<Line9>): Long {
        val pascal = makePascal(input)
        return input.sumOf { line ->
            val stepN = findStepN(line)
            var ans = 0L
            for (i in 0..<stepN) {
                ans += line[i] * pascal[stepN][i + 1] * (if (i % 2 == 0) 1 else -1)
            }
            ans
        }
    }

    val testInput = readInput("Day09_test", ::readLine)
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09", ::readLine)
    part1(input).println()
    part2(input).println()
}
