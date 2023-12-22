fun main() {
    fun isGardenPlot(grid: List<String>, intPoint: IntPoint): Boolean =
        getGridChar(grid, intPoint) != '#'

    fun findStartingPoint(grid: List<String>): IntPoint {
        for ((y, row) in grid.withIndex()) {
            for ((x, c) in row.withIndex()) {
                if (c == 'S') {
                    return IntPoint(x, y)
                }
            }
        }
        throw RuntimeException("O_O")
    }

    fun makeStep(grid: List<String>, points: Set<IntPoint>): Set<IntPoint> =
        points.flatMap { point ->
            Direction.entries.toSet()
                .mapNotNull { moveOnGrid(grid, point, it) }
                .filter { isGardenPlot(grid, it) }
        }.toSet()

    fun makeNStepsFrom(grid: List<String>, startingPoint: IntPoint, stepN: Int): Set<IntPoint> =
        (1..stepN).fold(setOf(startingPoint)) { points, _ ->
            makeStep(grid, points)
        }

    fun part1(grid: List<String>, stepN: Int = 64): Long {
        return makeNStepsFrom(grid, findStartingPoint(grid), stepN).size.toLong()
    }

    val stepN2 = 26501365
//    val stepN2 = 65 + 131
//    val stepN2 = 65 + 131 * 6

    fun part2Brute(grid: List<String>): Long {
        val bigGrid: List<String> = (1..20).flatMap { grid.map { line -> (1..20).fold("") { acc, _ -> acc + line } } }
        val points = makeNStepsFrom(bigGrid, IntPoint(65 + 131 * 8, 65 + 131 * 8), stepN2)
//        Path("src/qqq.txt").outputStream().bufferedWriter().use {
//            for (y in bigGrid.indices) {
//                for (x in bigGrid[0].indices) {
//                    it.write(if (points.contains(IntPoint(x, y))) "S" else ".")
//                }
//                it.write("\n")
//            }
//        }
        return points.size.toLong()
    }

    fun part2(grid: List<String>): Long {
        val startingPoint = IntPoint(65, 65)
        check(startingPoint == findStartingPoint(grid))

        val garden1 = makeNStepsFrom(grid, startingPoint, 131)
        val garden2 = makeNStepsFrom(grid, startingPoint, 132)

        fun topLeft(point: IntPoint): Boolean =
            point.first + point.second < 65

        fun bottomRight(point: IntPoint): Boolean =
            point.first + point.second > 195

        fun topRight(point: IntPoint): Boolean =
            point.first - point.second > 65

        fun bottomLeft(point: IntPoint): Boolean =
            point.second - point.first > 65

        fun centerPoint(point: IntPoint): Boolean =
            !topLeft(point) && !topRight(point) && !bottomLeft(point) && !bottomRight(point)

        val center1 = garden1.count(::centerPoint)
        val center2 = garden2.count(::centerPoint)
        val sidesTogether = garden1.count { !centerPoint(it) } + garden2.count { !centerPoint(it) }

        val bigStepN = (stepN2 - 65) / 131

        fun square(n: Long): Long = n * n

        return sidesTogether.toLong() * bigStepN * (bigStepN + 1) +
                center1.toLong() * square(bigStepN.toLong() / 2 * 2 + 1) +
                center2.toLong() * square((bigStepN.toLong() + 1) / 2 * 2) - bigStepN // I have no idea why
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput, 6) == 16L)

    val input = readInput("Day21")
    part1(input).println()
//    part2Brute(input).println()
    // 622926942173582 too high
    part2(input).println()
}
