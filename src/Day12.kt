fun main() {
    data class Input(val springs: String, val intervals: List<Int>)

    fun parseInput(str: String): Input {
        val parts = str.split(" ")
        return Input(parts[0], parts[1].split(",").map { it.toInt() })
    }

    fun part1(inputs: List<Input>): Long {
        return inputs.sumOf { input ->
            val dp = Array(input.springs.length + 1) { LongArray(input.intervals.size + 1) { 0 } }
            val firstHashI = input.springs.indexOf('#').let {
                if (it == -1) input.springs.length else it
            }
            for (i in 0..firstHashI) {
                dp[i][0] = 1
            }
            if (input.springs.substring(0, input.intervals[0]).all { it == '#' || it == '?' }) {
                dp[input.intervals[0]][1] = 1
            }
            for (i in 1..input.springs.length) {
                for (j in 1..input.intervals.size) {
                    fun checkFailedStreak(): Boolean =
                        input.springs.substring(i - input.intervals[j - 1], i).all { it == '#' || it == '?'}
                    fun failedStreak(): Long =
                        if (i > input.intervals[j - 1] &&
                            checkFailedStreak() &&
                            input.springs[i - input.intervals[j - 1] - 1].let { it == '?' || it == '.' })
                            dp[i - input.intervals[j - 1] - 1][j - 1]
                        else
                            0L
                    dp[i][j] = dp[i][j] + when (input.springs[i - 1]) {
                        '.' -> dp[i - 1][j]
                        '#' -> failedStreak()
                        '?' -> failedStreak() + dp[i - 1][j]
                        else -> throw RuntimeException("can't happen")
                    }
                }
            }
            dp.last().last()
        }
    }

    fun part2(input: List<Input>): Long {
        return part1(input.map { row ->
            Input(
                row.springs + "?" + row.springs + "?" + row.springs + "?" + row.springs + "?" + row.springs,
                row.intervals + row.intervals + row.intervals + row.intervals + row.intervals
            )
        })
    }

    val testInputYevhen = readInput("Day12_test2", ::parseInput)
    println(part1(testInputYevhen))

    val testInput = readInput("Day12_test", ::parseInput)
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12", ::parseInput)
    part1(input).println()
    part2(input).println()
}
