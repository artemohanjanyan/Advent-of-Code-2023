fun main() {
    val dx = arrayOf(-1, 0, 1)
    val dc: List<Pair<Int, Int>> = dx
            .flatMap { listOf(it, it, it).zip(dx) }
            .filter { it != 0 to 0 }

    fun part1(input: List<String>): Int {
        val rows = input.size
        val columns = input[0].length
        val used = Array(rows) { BooleanArray(columns) }
        fun checkI(i: Int) = i in 0 until rows
        fun checkJ(j: Int) = j in 0 until columns
        var result = 0
        for (i in 0..<rows) {
            for (j in 0..<columns) {
                if (input[i][j] != '.' && !input[i][j].isDigit()) {
                    for ((di, dj) in dc) {
                        val ii = i + di
                        val jj = j + dj
                        if (checkI(ii) && checkJ(jj) && !used[ii][jj] && input[ii][jj].isDigit()) {
                            var j1 = jj
                            var j2 = jj
                            while (checkJ(j1 - 1) && input[ii][j1 - 1].isDigit()) {
                                --j1
                            }
                            while (checkJ(j2 + 1) && input[ii][j2 + 1].isDigit()) {
                                ++j2
                            }
                            for (jjj in j1..j2) {
                                used[ii][jjj] = true
                            }
                            result += input[ii].substring(j1..j2).toInt()
                        }
                    }
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val rows = input.size
        val columns = input[0].length
        fun checkI(i: Int) = i in 0 until rows
        fun checkJ(j: Int) = j in 0 until columns
        var result = 0
        for (i in 0..<rows) {
            for (j in 0..<columns) {
                if (input[i][j] != '.' && !input[i][j].isDigit()) {
                    var currentRatio = 1
                    var currentNumberN = 0
                    val used = Array(rows) { BooleanArray(columns) }
                    for ((di, dj) in dc) {
                        val ii = i + di
                        val jj = j + dj
                        if (checkI(ii) && checkJ(jj) && !used[ii][jj] && input[ii][jj].isDigit()) {
                            var j1 = jj
                            var j2 = jj
                            while (checkJ(j1 - 1) && input[ii][j1 - 1].isDigit()) {
                                --j1
                            }
                            while (checkJ(j2 + 1) && input[ii][j2 + 1].isDigit()) {
                                ++j2
                            }
                            for (jjj in j1..j2) {
                                used[ii][jjj] = true
                            }
                            currentRatio *= input[ii].substring(j1..j2).toInt()
                            ++currentNumberN
                        }
                    }
                    if (currentNumberN == 2) {
                        result += currentRatio
                    }
                }
            }
        }
        return result
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
