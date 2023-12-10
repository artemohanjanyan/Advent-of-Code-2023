typealias Point10 = Pair<Int, Int>

fun main() {
    fun pipeDirections(pipe: Char): List<Point10> =
        when (pipe) {
            '|' -> listOf(Pair(-1, 0), Pair(1, 0))
            '-' -> listOf(Pair(0, -1), Pair(0, 1))
            'L' -> listOf(Pair(-1, 0), Pair(0, 1))
            'J' -> listOf(Pair(-1, 0), Pair(0, -1))
            '7' -> listOf(Pair(0, -1), Pair(1, 0))
            'F' -> listOf(Pair(0, 1), Pair(1, 0))
            else -> emptyList()
        }

    fun add(point: Point10, direction: Point10): Point10 =
        Pair(point.first + direction.first, point.second + direction.second)

    fun part1(input: List<String>): Int {
        val start: Point10 = input.flatMapIndexed { i, row ->
            val startJ = row.indexOf('S')
            if (startJ != -1) listOf(Pair(i, startJ)) else emptyList()
        }.first()

        pipeLoop@ for (startPipe in "|L-J7F") {
            fun getPipeAt(point: Point10): Char =
                if (point == start) startPipe else input[point.first][point.second]

            fun pointIsInBounds(point: Point10): Boolean =
                point.first in input.indices && point.second in input[0].indices

            fun adjacentPoints(point: Point10): List<Point10> =
                pipeDirections(getPipeAt(point)).map { add(point, it) }

            fun pipeIsConnected(point: Point10): Boolean =
                adjacentPoints(point).all { adjacent ->
                    pointIsInBounds(adjacent) && adjacentPoints(adjacent).contains(point)
                }

            val visitedPoints = HashSet<Point10>()
            var currentPoints = listOf(start)
            var stepN = 0

            while (currentPoints.isNotEmpty()) {
                if (!currentPoints.all(::pipeIsConnected)) {
                    continue@pipeLoop
                }
                visitedPoints.addAll(currentPoints)
                currentPoints = currentPoints.flatMap { current ->
                    adjacentPoints(current).filter {
                        !visitedPoints.contains(it)
                    }
                }
                ++stepN
            }
            return stepN - 1
        }
        throw RuntimeException(":(")
    }

    fun part2(input: List<String>): Int {
        val start: Point10 = input.flatMapIndexed { i, row ->
            val startJ = row.indexOf('S')
            if (startJ != -1) listOf(Pair(i, startJ)) else emptyList()
        }.first()

        pipeLoop@ for (startPipe in "|L-J7F") {
            fun getPipeAt(point: Point10): Char =
                if (point == start) startPipe else input[point.first][point.second]

            fun pointIsInBounds(point: Point10): Boolean =
                point.first in input.indices && point.second in input[0].indices

            fun adjacentPoints(point: Point10): List<Point10> =
                pipeDirections(getPipeAt(point)).map { add(point, it) }

            fun pipeIsConnected(point: Point10): Boolean =
                adjacentPoints(point).all { adjacent ->
                    pointIsInBounds(adjacent) && adjacentPoints(adjacent).contains(point)
                }

            val visitedPoints = HashSet<Point10>()
            var currentPoints = listOf(start)
            var stepN = 0

            while (currentPoints.isNotEmpty()) {
                if (!currentPoints.all(::pipeIsConnected)) {
                    continue@pipeLoop
                }
                visitedPoints.addAll(currentPoints)
                currentPoints = currentPoints.flatMap { current ->
                    adjacentPoints(current).filter {
                        !visitedPoints.contains(it)
                    }
                }
                ++stepN
            }

            val visited2 = HashSet<Point10>()
            for (i in 1..<input.size) {
                var fl = false
                for (j in input[i].indices) {
                    val a = Point10(i - 1, j)
                    val b = Point10(i, j)
                    fun visit() {
                        visited2.add(a)
                        visited2.add(b)
                    }
                    val isIntersection = visitedPoints.contains(b) && adjacentPoints(b).contains(a)

                    if (!isIntersection) {
                        if (fl) {
                            visit()
                        }
                    } else {
                        visit()
                        fl = !fl
                    }
                }
            }

            return visited2.size - visitedPoints.size
        }
        throw RuntimeException(":(")
    }

    val testInput = readInput("Day10_test")
    val testInput2 = readInput("Day10_test2")
    val testInput3 = readInput("Day10_test3")
    val testInput4 = readInput("Day10_test4")
    check(part1(testInput) == 8)
    check(part2(testInput2) == 4)
    check(part2(testInput3) == 4)
    check(part2(testInput4) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
