import java.math.BigInteger

data class Vector(val x: BigInteger, val y: BigInteger, val z: BigInteger)

data class Line(val n: Vector, val b: BigInteger)

data class Hail(val p: Vector, val v: Vector) {
    val line2d: Line =
        Line(Vector(v.y, -v.x, BigInteger.ZERO), v.x * p.y - v.y * p.x)
}

fun main() {
    fun parseHail(str: String): Hail {
        val parts = str.split(Regex("[, @]+"))
        val p = Vector(parts[0].toBigInteger(), parts[1].toBigInteger(), parts[2].toBigInteger())
        val v = Vector(parts[3].toBigInteger(), parts[4].toBigInteger(), parts[5].toBigInteger())
        return Hail(p, v)
    }

    fun part1(input: List<Hail>, minC: BigInteger, maxC: BigInteger): Long {
        var ans = 0L
        for (i in input.indices) {
            for (j in (i + 1)..<input.size) {
                val l1 = input[i].line2d
                val l2 = input[j].line2d
                val denominator = l1.n.x * l2.n.y - l1.n.y * l2.n.x
                if (denominator == BigInteger.ZERO) {
                    continue
                }
                val enumeratorX = l1.n.y * l2.b - l2.n.y * l1.b
                val enumeratorY = l2.n.x * l1.b - l1.n.x * l2.b
                if (!(denominator > BigInteger.ZERO &&
                    minC * denominator <= enumeratorX &&
                    enumeratorX <= maxC * denominator &&
                    minC * denominator <= enumeratorY &&
                    enumeratorY <= maxC * denominator ||
                    denominator < BigInteger.ZERO &&
                    minC * denominator >= enumeratorX &&
                    enumeratorX >= maxC * denominator &&
                    minC * denominator >= enumeratorY &&
                    enumeratorY >= maxC * denominator)) {
                    continue
                }

                val p1 = input[i]
                val p2 = input[j]
                val denominator2 = p1.v.x * p2.v.y - p1.v.y * p2.v.x
                val enumerator1 = (p2.p.x - p1.p.x) * p2.v.y - (p2.p.y - p1.p.y) * p2.v.x
                val enumerator2 = (p2.p.x - p1.p.x) * p1.v.y - (p2.p.y - p1.p.y) * p1.v.x
                if (denominator2 > BigInteger.ZERO && enumerator1 >= BigInteger.ZERO && enumerator2 >= BigInteger.ZERO ||
                    denominator2 < BigInteger.ZERO && enumerator1 <= BigInteger.ZERO && enumerator2 <= BigInteger.ZERO) {
                    ++ans
                }
            }
        }
        return ans
    }

    fun part2(input: List<Hail>): Long {
        TODO()
    }

    val testInput = readInput("Day24_test", ::parseHail)
    check(part1(testInput, BigInteger("7"), BigInteger("27")) == 2L)
//    check(part2(testInput) == 154L)

    val input = readInput("Day24", ::parseHail)
    part1(input, BigInteger("200000000000000"), BigInteger("400000000000000")).println()
//    part2(input).println()
}
