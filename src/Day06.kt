import java.util.Scanner
import java.util.regex.Pattern
import kotlin.io.path.*
import kotlin.math.min

fun main() {
    data class Race(val time: Long, val distance: Long) {
        fun distance(buttonTime: Long): Long = buttonTime * (time - buttonTime)
    }

    data class Input(val races: List<Race>)

    fun readInput(name: String): Input {
        Scanner(Path("src/$name.txt").inputStream()).use { scanner ->
            scanner.next("Time:")
            val times = ArrayList<Long>()
            while (scanner.hasNextLong()) {
                times.add(scanner.nextLong())
            }
            scanner.next("Distance:")
            val distances = ArrayList<Long>()
            while (scanner.hasNextLong()) {
                distances.add(scanner.nextLong())
            }
            return Input(times.zip(distances).map { Race(it.first, it.second) })
        }
    }

    fun winN(race: Race): Long {
        var l = 0L
        var r = race.time
        while (l + 2 < r) {
            val mid1 = l + (r - l) / 3
            val mid2 = r - (r - l) / 3
            if (race.distance(mid1) > race.distance(mid2)) {
                r = mid2
            } else {
                l = mid1
            }
        }
        val maxDistance = l + 1

        l = 0L
        r = maxDistance
        while (l + 1 < r) {
            val mid = l + (r - l) / 2
            if (race.distance(mid) > race.distance) {
                r = mid
            } else {
                l = mid
            }
        }
        val min = r

        l = maxDistance
        r = race.time
        while (l + 1 < r) {
            val mid = l + (r - l) / 2
            if (race.distance(mid) > race.distance) {
                l = mid
            } else {
                r = mid
            }
        }
        val max = l

        return max - min + 1
    }

    fun part1(input: Input): Long {
        return input.races.map(::winN).fold(1L) { a, b -> a * b }
    }

    fun merge(a: Long, b: Long): Long = (a.toString() + b.toString()).toLong()

    fun part2(input: Input): Long {
        val race = Race(
            input.races.map { it.time }.fold(0L, ::merge),
            input.races.map { it.distance }.fold(0L, ::merge),
        )
        return winN(race)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
