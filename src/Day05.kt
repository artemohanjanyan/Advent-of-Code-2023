import java.util.Scanner
import java.util.regex.Pattern
import kotlin.io.path.*
import kotlin.math.min

fun main() {
    data class Input(val seeds: List<Long>, val mappings: List<List<Triple<Long, Long, Long>>>) {
        fun locateSeed(seed: Long): Long {
            var s = seed
            for (mapping in mappings) {
                for ((dst, src, len) in mapping) {
                    if (s in src until src + len) {
                        s = dst + s - src
                        break
                    }
                }
            }
            return s
        }

        fun seedRanges(): List<Pair<Long, Long>> {
            val ranges = ArrayList<Pair<Long, Long>>()
            for (i in (0 until seeds.size / 2)) {
                ranges.add(Pair(seeds[i * 2], seeds[i * 2] + seeds[i * 2 + 1] - 1))
            }
            return ranges
        }

        fun locateSeedRanges(ranges: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
            var currentRanges = ranges
            for (mapping in mappings) {
                val nextRanges = ArrayList<Pair<Long, Long>>()
                for (range in currentRanges) {
                    var currentRange = range
                    for ((dst, src, len) in mapping) {
                        if (src + len - 1 < currentRange.first) {
                            continue
                        }
                        if (src > currentRange.first) {
                            val nextCoordinate = min(src, currentRange.second + 1)
                            nextRanges.add(Pair(currentRange.first, nextCoordinate))
                            currentRange = Pair(nextCoordinate + 1, currentRange.second)
                            if (currentRange.first > currentRange.second) {
                                break
                            }
                        }
                        assert(src > currentRange.second)
                        val nextCoordinate = min(currentRange.second, src + len - 1)
                        nextRanges.add(Pair(currentRange.first - src + dst, nextCoordinate - src + dst))
                        currentRange = Pair(nextCoordinate + 1, currentRange.second)
                        if (currentRange.first > currentRange.second) {
                            break
                        }
                    }
                    if (currentRange.first <= currentRange.second) {
                        nextRanges.add(currentRange)
                    }
                }
                currentRanges = nextRanges
            }
            return currentRanges
        }
    }

    fun readInput(name: String): Input {
        Scanner(Path("src/$name.txt").inputStream()).use { scanner ->
            scanner.next("seeds:")
            val seeds = ArrayList<Long>()
            while (scanner.hasNextLong()) {
                seeds.add(scanner.nextLong())
            }

            val mappingPattern1 = Pattern.compile("\\p{Lower}+-to-\\p{Lower}+")
            val mappingPattern2 = Pattern.compile("map:")
            val mappings = ArrayList<List<Triple<Long, Long, Long>>>()
            while (scanner.hasNext(mappingPattern1)) {
                scanner.next(mappingPattern1)
                scanner.next(mappingPattern2)
                val mapping = ArrayList<Triple<Long, Long, Long>>()
                while (scanner.hasNextLong()) {
                    val a = scanner.nextLong()
                    val b = scanner.nextLong()
                    val c = scanner.nextLong()
                    mapping.add(Triple(a, b, c))
                }
                mapping.sortBy { it.second }
                mappings.add(mapping)
            }

            return Input(seeds, mappings)
        }
    }

    fun part1(input: Input): Long {
        return input.seeds.minOf(input::locateSeed)
    }

    fun part2(input: Input): Long {
        return input.locateSeedRanges(input.seedRanges()).minOf { it.first }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
