import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

typealias Node17 = Pair<IntPoint, Direction>

fun oppositeDirection(direction: Direction): Direction {
    return when(direction) {
        Direction.Up -> Direction.Down
        Direction.Down -> Direction.Up
        Direction.Left -> Direction.Right
        Direction.Right -> Direction.Left
    }
}

fun main() {
    fun moveOnGridN(grid: List<String>, point: IntPoint, direction: Direction, n: Int): IntPoint? {
        return (1..n).fold<Int, IntPoint?>(point) { currentPoint, _ ->
            currentPoint?.let { moveOnGrid(grid, it, direction) }
        }
    }

    fun part1(grid: List<String>): Long {
        val visited = HashSet<Node17>()
        val distance = HashMap<Node17, Long>()
        val from = HashMap<Node17, Node17>()
        val allDistances = TreeSet(
            compareBy<Pair<Long, Node17>> { it.first }
                .thenBy { it.second.first.first }
                .thenBy { it.second.first.second }
                .thenBy { it.second.second }
        )
        val startNodes = setOf(
            Node17(IntPoint(0, 0), Direction.Up),
            Node17(IntPoint(0, 0), Direction.Left)
        )
        val finishNodes = setOf(
            Node17(IntPoint(grid[0].length - 1, grid.size - 1), Direction.Down),
            Node17(IntPoint(grid[0].length - 1, grid.size - 1), Direction.Right)
        )
        startNodes.forEach {
            distance[it] = 0
            allDistances.add(Pair(0, it))
        }
        while (!finishNodes.any { visited.contains(it) }) {
            val (currentDistance, currentNode) = allDistances.pollFirst()!!
            visited.add(currentNode)
            for (direction in Direction.entries) {
                if (direction != currentNode.second && direction != oppositeDirection(currentNode.second)) {
                    for (stepN in 1..3) {
                        val newPoint = moveOnGridN(grid, currentNode.first, direction, stepN)
                        if (newPoint != null && !visited.contains(Node17(newPoint, direction))) {
                            val newNode = Node17(newPoint, direction)
                            val possibleNewDistance = currentDistance + (1..stepN).sumOf {
                                val pointInTheMiddle = moveOnGridN(grid, currentNode.first, direction, it)!!
                                getGridChar(grid, pointInTheMiddle).toString().toInt()
                            }
                            val newNodeDistance = distance[newNode]
                            if (newNodeDistance != null) {
                                allDistances.remove(Pair(newNodeDistance, newNode))
                            }
                            if (newNodeDistance == null || possibleNewDistance < newNodeDistance) {
                                distance[newNode] = possibleNewDistance
                                from[newNode] = currentNode
                            }
                            allDistances.add(Pair(distance[newNode]!!, newNode))
                        }
                    }
                }
            }
        }
        return finishNodes.mapNotNull { distance[it] }.min()
    }

    fun part2(grid: List<String>): Long {
        val visited = HashSet<Node17>()
        val distance = HashMap<Node17, Long>()
        val from = HashMap<Node17, Node17>()
        val allDistances = TreeSet(
            compareBy<Pair<Long, Node17>> { it.first }
                .thenBy { it.second.first.first }
                .thenBy { it.second.first.second }
                .thenBy { it.second.second }
        )
        val startNodes = setOf(
            Node17(IntPoint(0, 0), Direction.Up),
            Node17(IntPoint(0, 0), Direction.Left)
        )
        val finishNodes = setOf(
            Node17(IntPoint(grid[0].length - 1, grid.size - 1), Direction.Down),
            Node17(IntPoint(grid[0].length - 1, grid.size - 1), Direction.Right)
        )
        startNodes.forEach {
            distance[it] = 0
            allDistances.add(Pair(0, it))
        }
        while (!finishNodes.any { visited.contains(it) }) {
            val (currentDistance, currentNode) = allDistances.pollFirst()!!
            visited.add(currentNode)
            for (direction in Direction.entries) {
                if (direction != currentNode.second && direction != oppositeDirection(currentNode.second)) {
                    for (stepN in 4..10) {
                        val newPoint = moveOnGridN(grid, currentNode.first, direction, stepN)
                        if (newPoint != null && !visited.contains(Node17(newPoint, direction))) {
                            val newNode = Node17(newPoint, direction)
                            val possibleNewDistance = currentDistance + (1..stepN).sumOf {
                                val pointInTheMiddle = moveOnGridN(grid, currentNode.first, direction, it)!!
                                getGridChar(grid, pointInTheMiddle).toString().toInt()
                            }
                            val newNodeDistance = distance[newNode]
                            if (newNodeDistance != null) {
                                allDistances.remove(Pair(newNodeDistance, newNode))
                            }
                            if (newNodeDistance == null || possibleNewDistance < newNodeDistance) {
                                distance[newNode] = possibleNewDistance
                                from[newNode] = currentNode
                            }
                            allDistances.add(Pair(distance[newNode]!!, newNode))
                        }
                    }
                }
            }
        }
        return finishNodes.mapNotNull { distance[it] }.min()
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput) == 102L)
    check(part2(testInput) == 94L)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
