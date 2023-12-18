import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.inputStream
import kotlin.math.abs

//fun intPointMinus(a: IntPoint, b: IntPoint): IntPoint {
//    return IntPoint(a.first - b.first, a.second - b.second)
//}

fun move(point: IntPoint, direction: Direction, stepN: Int): IntPoint {
    return IntPoint(point.first + direction.dx * stepN, point.second + direction.dy * stepN)
}

fun crossProduct(a: IntPoint, b: IntPoint): Int {
    return a.first * b.second - a.second * b.first
}

data class LongPoint(val x: Long, val y: Long)

fun move(point: LongPoint, direction: Direction, stepN: Int): LongPoint {
    return LongPoint(point.x + direction.dx * stepN, point.y + direction.dy * stepN)
}

fun crossProduct(a: LongPoint, b: LongPoint): Long {
    return a.x * b.y - a.y * b.x
}

data class Day18Step(val direction: Direction, val stepN: Int, val color: String) {
    fun toRealInstruction(): Day18Step {
        return Day18Step(
            when (color[7]) {
                '0' -> Direction.Right
                '1' -> Direction.Down
                '2' -> Direction.Left
                '3' -> Direction.Up
                else -> throw RuntimeException(":(")
            },
            Integer.parseInt(color.substring(2..6), 16),
            color
        )
    }
}

fun main() {
    fun readInput(name: String): List<Day18Step> {
        Scanner(Path("src/$name.txt").inputStream()).use { scanner ->
            val steps = ArrayList<Day18Step>()
            while (scanner.hasNext()) {
                val direction = scanner.next("[UDLR]").let {
                    when (it) {
                        "R" -> Direction.Right
                        "L" -> Direction.Left
                        "U" -> Direction.Up
                        "D" -> Direction.Down
                        else -> throw RuntimeException(":(")
                    }
                }
                val stepN = scanner.next("[0-9]+").toInt()
                val color = scanner.next("\\(#[a-z0-9]{6}\\)")
                steps.add(Day18Step(direction, stepN, color))
            }
            return steps
        }
    }

    fun part1(steps: List<Day18Step>): Int {
        var currentPoint = IntPoint(0, 0)
        var totalArea = 0
        var totalStepN = 0
        for (step in steps) {
            val nextPoint = move(currentPoint, step.direction, step.stepN)
            totalArea += crossProduct(currentPoint, nextPoint)
            currentPoint = nextPoint
            totalStepN += step.stepN
        }
        return (abs(totalArea) + 6 + (totalStepN - 4)) / 2
    }

    fun part2(steps: List<Day18Step>): Long {
        var currentPoint = LongPoint(0, 0)
        var totalArea = 0L
        var totalStepN = 0L
        for (step in steps.map { it.toRealInstruction() }) {
            val nextPoint = move(currentPoint, step.direction, step.stepN)
            totalArea += crossProduct(currentPoint, nextPoint)
            currentPoint = nextPoint
            totalStepN += step.stepN
        }
        return (abs(totalArea) + 6 + (totalStepN - 4)) / 2
    }

    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62)
    check(part2(testInput) == 952408144115L)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
