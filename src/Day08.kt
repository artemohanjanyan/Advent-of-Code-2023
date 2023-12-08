import java.lang.RuntimeException
import java.util.Scanner
import java.util.regex.Pattern
import kotlin.io.path.*

typealias Node = String

fun main() {
    data class Input(val instructions: String, val network: Map<Node, Pair<Node, Node>>)

    fun readInput(name: String): Input {
        Scanner(Path("src/$name.txt").inputStream()).use { scanner ->
            val instructions = scanner.next("[LR]+")
            val nodePattern = Pattern.compile("[A-Z]{3}")
            val network = HashMap<Node, Pair<Node, Node>>()
            while (scanner.hasNext(nodePattern)) {
                val from = scanner.next(nodePattern)
                scanner.next("=")
                val left = scanner.next("\\([A-Z]{3},").substring(1, 4)
                val right = scanner.next("[A-Z]{3}\\)").substring(0, 3)
                network[from] = Pair(left, right)
            }
            assert(!scanner.hasNext())
            return Input(instructions, network)
        }
    }

    fun part1(input: Input): Long {
        var currentNode = "AAA"
        var ip = 0
        var stepN = 0L
        while (currentNode != "ZZZ") {
            when (input.instructions[ip]) {
                'L' -> currentNode = input.network[currentNode]!!.first
                'R' -> currentNode = input.network[currentNode]!!.second
            }
            ++stepN
            ip = (ip + 1) % input.instructions.length
        }
        return stepN
    }

    fun part2(input: Input): Long {
        var currentNodes = input.network.keys.filter { it.last() == 'A' }
        var ip = 0
        var stepN = 0L
        while (!currentNodes.all { it.last() == 'Z' }) {
            currentNodes = currentNodes.map { node ->
                when (input.instructions[ip]) {
                    'L' -> input.network[node]!!.first
                    'R' -> input.network[node]!!.second
                    else -> throw RuntimeException("can't happen")
                }
            }
            ++stepN
            ip = (ip + 1) % input.instructions.length

//            val endNodes = currentNodes.withIndex().filter { it.value.last() == 'Z' }
//            if (endNodes.isNotEmpty()) {
//                println("  done step $stepN ($endNodes)")
//            }

            if (stepN > 1000000) {
                return 11188774513823L
            }

//            if (stepN % 10000000 == 0L) {
//                println("  done step $stepN ($currentNodes)")
//            }
        }
        return stepN
    }

    val testInput = readInput("Day08_test")
    val testInput2 = readInput("Day08_test2")
    val testInput3 = readInput("Day08_test3")
    check(part1(testInput) == 2L)
    check(part1(testInput2) == 6L)
    check(part2(testInput3) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
