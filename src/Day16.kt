import kotlin.math.max

enum class Direction(val dx: Int, val dy: Int) {
    Up(0, -1),
    Down(0, 1),
    Left(-1, 0),
    Right(1, 0)
}

fun main() {
    fun move(grid: List<String>, point: IntPoint, direction: Direction): IntPoint? {
        val newPoint = IntPoint(point.first + direction.dx, point.second + direction.dy)
        return if (newPoint.first in grid[0].indices && newPoint.second in grid.indices)
            newPoint
        else
            null
    }

    fun getChar(grid: List<String>, point: IntPoint): Char {
        return grid[point.second][point.first]
    }

    fun energizedCount(grid: List<String>, startingPoint: IntPoint, startingDirection: Direction): Long {
        val visited = HashSet<IntPoint>().apply { add(startingPoint) }
        val visitedWithDirections = HashSet<Pair<IntPoint, Direction>>().apply { add(Pair(startingPoint, startingDirection)) }
        val lightQueue = ArrayDeque<Pair<IntPoint, Direction>>().apply { add(Pair(startingPoint, startingDirection)) }
        while (lightQueue.isNotEmpty()) {
            val (point, direction) = lightQueue.removeFirst()
            fun addNextPoint(direction: Direction) {
                val newPoint = move(grid, point, direction)
                if (newPoint != null && !visitedWithDirections.contains(Pair(newPoint, direction))) {
                    visited.add(newPoint)
                    visitedWithDirections.add(Pair(newPoint, direction))
                    lightQueue.add(Pair(newPoint, direction))
                }
            }
            when (getChar(grid, point)) {
                '.' -> addNextPoint(direction)
                '/' ->
                    addNextPoint(when(direction) {
                        Direction.Up -> Direction.Right
                        Direction.Left -> Direction.Down
                        Direction.Right -> Direction.Up
                        Direction.Down -> Direction.Left
                    })
                '\\' ->
                    addNextPoint(when(direction) {
                        Direction.Up -> Direction.Left
                        Direction.Right -> Direction.Down
                        Direction.Left -> Direction.Up
                        Direction.Down -> Direction.Right
                    })
                '-' ->
                    if (direction == Direction.Left || direction == Direction.Right) {
                        addNextPoint(direction)
                    } else {
                        addNextPoint(Direction.Left)
                        addNextPoint(Direction.Right)
                    }
                '|' ->
                    if (direction == Direction.Up || direction == Direction.Down) {
                        addNextPoint(direction)
                    } else {
                        addNextPoint(Direction.Up)
                        addNextPoint(Direction.Down)
                    }
            }
        }
//        for (i in grid.indices) {
//            for (j in grid.indices) {
//                if (visited.contains(Pair(j, i))) {
//                    print("#")
//                } else {
//                    print(".")
//                }
//            }
//            println()
//        }
        return visited.size.toLong()
    }

    fun part1(grid: List<String>): Long {
        return energizedCount(grid, IntPoint(0, 0), Direction.Right)
    }

    fun part2(grid: List<String>): Long {
        return max(
            max(
                grid[0].indices.maxOf { energizedCount(grid, IntPoint(it, 0), Direction.Down) },
                grid[0].indices.maxOf { energizedCount(grid, IntPoint(it, grid.size - 1), Direction.Up) }
            ),
            max(
                grid.indices.maxOf { energizedCount(grid, IntPoint(0, it), Direction.Right) },
                grid.indices.maxOf { energizedCount(grid, IntPoint(grid[0].length - 1, it), Direction.Left) }
            )
        )
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46L)
    check(part2(testInput) == 51L)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
