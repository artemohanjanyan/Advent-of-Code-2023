import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.inputStream
import kotlin.math.min

fun main() {
    data class Input(val pattern: List<String>) {
        fun getScore(): Long {
            fun checkVertical(jj: Int): Boolean {
                for (i in pattern.indices) {
                    for (j in 0..<min(jj, pattern[0].length - jj)) {
                        if (pattern[i][jj - j - 1] != pattern[i][jj + j]) {
                            return false
                        }
                    }
                }
                return true
            }
            fun checkHorizontal(ii: Int): Boolean {
                for (j in pattern[0].indices) {
                    for (i in 0..<min(ii, pattern.size - ii)) {
                        if (pattern[ii - i - 1][j] != pattern[ii + i][j]) {
                            return false
                        }
                    }
                }
                return true
            }
            for (j in 1..<pattern[0].length) {
                if (checkVertical(j)) {
                    return j.toLong()
                }
            }
            for (i in 1..<pattern.size) {
                if (checkHorizontal(i)) {
                    return i.toLong() * 100
                }
            }
            throw RuntimeException("not symmetrical :(")
        }

        fun getScore2(): Long {
            fun checkVertical(jj: Int): Boolean {
                var errorN = 0
                for (i in pattern.indices) {
                    for (j in 0..<min(jj, pattern[0].length - jj)) {
                        if (pattern[i][jj - j - 1] != pattern[i][jj + j]) {
                            ++errorN
                            if (errorN > 1) {
                                return false
                            }
                        }
                    }
                }
                return errorN == 1
            }
            fun checkHorizontal(ii: Int): Boolean {
                var errorN = 0
                for (j in pattern[0].indices) {
                    for (i in 0..<min(ii, pattern.size - ii)) {
                        if (pattern[ii - i - 1][j] != pattern[ii + i][j]) {
                            ++errorN
                            if (errorN > 1) {
                                return false
                            }
                        }
                    }
                }
                return errorN == 1
            }
            for (j in 1..<pattern[0].length) {
                if (checkVertical(j)) {
                    return j.toLong()
                }
            }
            for (i in 1..<pattern.size) {
                if (checkHorizontal(i)) {
                    return i.toLong() * 100
                }
            }
            throw RuntimeException("not symmetrical :(")
        }
    }

    fun readInput(name: String): List<Input> {
        Scanner(Path("src/$name.txt").inputStream()).useDelimiter("\n\n").use { scanner ->
            val inputs = ArrayList<Input>()
            while (scanner.hasNext()) {
                inputs.add(Input(scanner.next().split("\n")))
            }
            return inputs
        }
    }

    fun part1(inputs: List<Input>): Long {
        return inputs.sumOf(Input::getScore)
    }

    fun part2(inputs: List<Input>): Long {
        return inputs.sumOf(Input::getScore2)
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405L)
    check(part2(testInput) == 400L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
