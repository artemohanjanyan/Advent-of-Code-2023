data class Point3D(val x: Int, val y: Int, val z: Int) {
    fun drop(): Point3D = Point3D(x, y, z - 1)
}

data class Brick(val a: Point3D, val b: Point3D) {
    val pointsBetween: List<Point3D> =
        if (a.x != b.x) (a.x..b.x).map { Point3D(it, a.y, a.z) }
        else if (a.y != b.y) (a.y..b.y).map { Point3D(a.x, it, a.z) }
        else (a.z..b.z).map { Point3D(a.x, a.y, it) }

    fun drop(): Brick = Brick(a.drop(), b.drop())
}

fun main() {
    fun parseBrick(str: String): Brick {
        val parts = str.split(",", "~").map { it.toInt() }
        assert(parts[0] <= parts[3])
        assert(parts[1] <= parts[4])
        assert(parts[2] <= parts[5])
        return Brick(Point3D(parts[0], parts[1], parts[2]), Point3D(parts[3], parts[4], parts[5]))
    }

    fun getSet(map: HashMap<Int, HashSet<Int>>, i: Int): HashSet<Int> {
        val list = map[i]
        return if (list == null) {
            val newList = HashSet<Int>()
            map[i] = newList
            newList
        } else
            list
    }

    fun makeGraph(bricks: List<Brick>): Pair<HashMap<Int, HashSet<Int>>, HashMap<Int, HashSet<Int>>> {
        val fallenBricks = ArrayList<Brick>()
        val pointToBrick = HashMap<Point3D, Int>()
        for ((i, brick) in bricks.sortedBy { it.a.z }.withIndex()) {
            var currentBrick = brick
            while (currentBrick.a.z > 1) {
                val droppedBrick = currentBrick.drop()
                if (droppedBrick.pointsBetween.any(pointToBrick::contains)) {
                    break
                }
                currentBrick = droppedBrick
            }
            fallenBricks.add(currentBrick)
            currentBrick.pointsBetween.forEach { point ->
                pointToBrick[point] = i
            }
        }

        val bricksBelow = HashMap<Int, HashSet<Int>>()
        val bricksAbove = HashMap<Int, HashSet<Int>>()
        for ((i, brick) in fallenBricks.withIndex()) {
            for (pointBelow in brick.drop().pointsBetween) {
                val brickAtPoint = pointToBrick[pointBelow]
                if (brickAtPoint != null && brickAtPoint != i) {
                    getSet(bricksBelow, i).add(brickAtPoint)
                    getSet(bricksAbove, brickAtPoint).add(i)
                }
            }
        }
        return Pair(bricksBelow, bricksAbove)
    }

    fun part1(bricks: List<Brick>): Long {
        val (bricksBelow, bricksAbove) = makeGraph(bricks)
        return bricks.indices
            .count { i ->
                getSet(bricksAbove, i).all { getSet(bricksBelow, it).size > 1 }
            }
            .toLong()
    }

    fun part2(bricks: List<Brick>): Long {
        val (bricksBelow, bricksAbove) = makeGraph(bricks)
        fun chainReaction(brickI: Int): Long {
            val fallenBricks = HashSet<Int>()
            fun dfs(brickI: Int) {
                fallenBricks.add(brickI)
                for (above in getSet(bricksAbove, brickI)) {
                    if (getSet(bricksBelow, above).count { !fallenBricks.contains(it) } == 0) {
                        dfs(above)
                    }
                }
            }
            dfs(brickI)
            return fallenBricks.size.toLong() - 1
        }
        return bricks.indices.sumOf(::chainReaction)
    }

    val testInput = readInput("Day22_test", ::parseBrick)
    check(part1(testInput) == 5L)
    println(part2(testInput))
    check(part2(testInput) == 7L)

    val input = readInput("Day22", ::parseBrick)
    // 619 too high
    // 595 too high
    part1(input).println()
    part2(input).println()
}
