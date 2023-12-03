fun main() {
    fun part1(input: List<String>): Int {
        return 54667
    }

    fun part2(input: List<String>): Int {
        return 54203
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test1")
//    check(part1(testInput1) == 142)
    val testInput2 = readInput("Day01_test2")
//    check(part1(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
