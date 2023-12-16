import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.inputStream

fun main() {
    fun readInput(name: String): List<String> {
        Scanner(Path("src/$name.txt").inputStream()).useDelimiter(",").use { scanner ->
            val inputs = ArrayList<String>()
            while (scanner.hasNext()) {
                inputs.add(scanner.next())
            }
            return inputs
        }
    }

    fun hash(string: String): Int {
        var hash = 0
        for (c in string) {
            hash += c.code
            hash *= 17
            hash %= 256
        }
        return hash
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { hash(it).toLong() }
    }

    fun part2(input: List<String>): Long {
        val boxes = Array(256) { ArrayList<Pair<Int, String>>() }
        for (step in input) {
            val label = step.takeWhile { it.isLetter() }
            val operation = step[label.length]
            val box = boxes[hash(label)]
            when (operation) {
                '-' -> box.removeIf { it.second == label }
                '=' -> {
                    val focalLength = step.last().toString().toInt()
                    val newLens = Pair(focalLength, label)
                    val existingLensI = box.indexOfFirst { it.second == label }
                    if (existingLensI == -1) {
                        box.add(newLens)
                    } else {
                        box[existingLensI] = newLens
                    }
                }
                else -> throw RuntimeException(":(")
            }
        }
        return boxes.withIndex().sumOf { (i, box) ->
            box.withIndex().sumOf { (j, lens) ->
                (i + 1).toLong() * (j + 1) * lens.first
            }
        }
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320L)
    check(part2(testInput) == 145L)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
