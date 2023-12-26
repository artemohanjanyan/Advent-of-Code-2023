import kotlin.io.path.Path
import kotlin.io.path.outputStream
import kotlin.math.max

fun main() {
    fun startAndFinish(grid: List<String>): Pair<IntPoint, IntPoint> {
        val startPoint = IntPoint(1, 0)
        val finishPoint = IntPoint(grid[0].length - 2, grid.size - 1)
        return Pair(startPoint, finishPoint)
    }

    fun part1(grid: List<String>): Long {
        val (startPoint, finishPoint) = startAndFinish(grid)

        var ans = 0L
        val visited = HashSet<IntPoint>()
        var length = -1L
        fun dfs(point: IntPoint) {
            visited.add(point)
            ++length

            if (point == finishPoint) {
                ans = max(ans, length)
            } else {
                val directions = when (getGridChar(grid, point)) {
                    '.' -> Direction.entries
                    'v' -> listOf(Direction.Down)
                    '>' -> listOf(Direction.Right)
                    else -> throw RuntimeException("(((")
                }

                for (direction in directions) {
                    val nextPoint = moveOnGrid(grid, point, direction)
                    if (nextPoint == null || getGridChar(grid, nextPoint) == '#' || visited.contains(nextPoint)) {
                        continue
                    }
                    dfs(nextPoint)
                }
            }

            --length
            visited.remove(point)
        }
        dfs(startPoint)

        return ans
    }

    fun makeGraph(grid: List<String>): Map<IntPoint, List<Pair<IntPoint, Int>>> {
        val (startPoint, finishPoint) = startAndFinish(grid)

        val graph = HashMap<IntPoint, ArrayList<Pair<IntPoint, Int>>>()
        graph[startPoint] = ArrayList()

        val visited = HashSet<IntPoint>()
        visited.add(startPoint)

        fun makeGraphDfs(point: IntPoint, from: IntPoint, prev: IntPoint, length: Int) {
            visited.add(point)

            var directionsFromHere = 0
            for (direction in Direction.entries) {
                val nextPoint = moveOnGrid(grid, point, direction) ?: continue
                if (getGridChar(grid, nextPoint) != '#') {
                    ++directionsFromHere
                }
            }

            var nextLength = length + 1
            var nextFrom = from
            if (directionsFromHere >= 3 || point == finishPoint) {
                nextLength = 1
                nextFrom = point
                graph[point] = ArrayList()
                graph[point]!!.add(Pair(from, length))
                graph[from]!!.add(Pair(point, length))
            }

            for (direction in Direction.entries) {
                val nextPoint = moveOnGrid(grid, point, direction)
                if (nextPoint == null || getGridChar(grid, nextPoint) == '#' || nextPoint == prev) {
                    continue
                }
                if (visited.contains(nextPoint)) {
                    (graph[nextPoint] ?: continue).add(Pair(from, nextLength))
                    graph[from]!!.add(Pair(nextPoint, nextLength))
                    continue
                }
                makeGraphDfs(nextPoint, nextFrom, point, nextLength)
            }
        }
        makeGraphDfs(IntPoint(1, 1), startPoint, startPoint, 1)

        return graph
    }

    fun printGraphDot(graph: Map<IntPoint, List<Pair<IntPoint, Int>>>, path: String) {
        val visited = HashSet<IntPoint>()
        Path(path).outputStream().bufferedWriter().use {
            it.write("digraph G {\n")
            for ((node, edges) in graph) {
                visited.add(node)
                for ((to, length) in edges) {
                    if (!visited.contains(to)) {
                        it.write("\tx${node.first}y${node.second} -> x${to.first}y${to.second} [ label=\"${length}\" ];\n")
                    }
                }
            }
            it.write("}\n")
        }
    }

    fun part2(grid: List<String>): Long {
        val (startPoint, finishPoint) = startAndFinish(grid)
        val graph = makeGraph(grid)
//        printGraphDot(graph, "src/graph.dot")

        var ans = 0
        val visited = HashSet<IntPoint>()
        var length = 0
        fun dfs(point: IntPoint) {
            visited.add(point)

            if (point == finishPoint) {
                ans = max(ans, length)
            } else {
                for (edge in graph[point]!!) {
                    if (!visited.contains(edge.first)) {
                        length += edge.second
                        dfs(edge.first)
                        length -= edge.second
                    }
                }
            }

            visited.remove(point)
        }
        dfs(startPoint)

        return ans.toLong()
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 94L)
    check(part2(testInput) == 154L)

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
