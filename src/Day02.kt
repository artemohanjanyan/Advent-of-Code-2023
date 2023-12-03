fun main() {
    data class Grab(val red: Int, val green: Int, val blue: Int)
    data class Game(val id: Int, val grabs: List<Grab>)

    fun parseGame(str: String): Game {
        var s = str
        s = s.drop("Game ".length)
        val id = s.takeWhile { it.isDigit() }.let {
            s.drop(it.length)
            it.toInt()
        }
        s = s.drop(": ".length)
        val grabs = s.split(";").map { grabStr ->
            fun findColor(color: String): Int =
                    Regex("\\d+ $color").find(grabStr)?.value?.takeWhile { it.isDigit() }?.toInt() ?: 0
            Grab(
                    red = findColor("red"),
                    green = findColor("green"),
                    blue = findColor("blue"),
            )
        }
        return Game(id, grabs)
    }

    fun part1(games: List<Game>): Int {
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14
        var result = 0
        for (game in games) {
            if (game.grabs.all { it.red <= maxRed && it.green <= maxGreen && it.blue <= maxBlue }) {
                result += game.id
            }
        }
        return result
    }

    fun part2(games: List<Game>): Int {
        return games.sumOf { game ->
            game.grabs.maxOf { it.red } * game.grabs.maxOf { it.green } * game.grabs.maxOf { it.blue }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test").map { parseGame(it) }
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02").map { parseGame(it) }
    part1(input).println()
    part2(input).println()
}
