fun main() {
    fun load(platform: List<CharArray>): Long {
        var load = 0L
        for (i in platform.indices) {
            for (j in platform[0].indices) {
                if (platform[i][j] == 'O') {
                    load += platform.size - i
                }
            }
        }
        return load
    }

    fun part1(input: List<String>): Long {
        val platform: List<CharArray> = input.map { it.toCharArray() }
        for (i in platform.indices) {
            for (j in platform[0].indices) {
                if (platform[i][j] == 'O') {
                    for (k in (i - 1) downTo 0) {
                        if (platform[k][j] != '.') {
                            break
                        }
                        platform[k][j] = 'O'
                        platform[k + 1][j] = '.'
                    }
                }
            }
        }
        return load(platform)
    }

    fun part2(input: List<String>): Long {
        val platform = input.map { it.toCharArray() }
        val loadMap = HashMap<Int, Long>()
        val loadReverseMap = HashMap<Long, Int>()
        for (cycle in 1..10000) {
            for (i in platform.indices) {
                for (j in platform[0].indices) {
                    if (platform[i][j] == 'O') {
                        for (k in (i - 1) downTo 0) {
                            if (platform[k][j] != '.') {
                                break
                            }
                            platform[k][j] = 'O'
                            platform[k + 1][j] = '.'
                        }
                    }
                }
            }
            for (j in platform[0].indices) {
                for (i in platform.indices) {
                    if (platform[i][j] == 'O') {
                        for (k in (j - 1)downTo 0) {
                            if (platform[i][k] != '.') {
                                break
                            }
                            platform[i][k] = 'O'
                            platform[i][k + 1] = '.'
                        }
                    }
                }
            }
            for (i in platform.indices.reversed()) {
                for (j in platform[0].indices) {
                    if (platform[i][j] == 'O') {
                        for (k in (i + 1)..<platform.size) {
                            if (platform[k][j] != '.') {
                                break
                            }
                            platform[k][j] = 'O'
                            platform[k - 1][j] = '.'
                        }
                    }
                }
            }
            for (j in platform[0].indices.reversed()) {
                for (i in platform.indices) {
                    if (platform[i][j] == 'O') {
                        for (k in (j + 1)..<platform[0].size) {
                            if (platform[i][k] != '.') {
                                break
                            }
                            platform[i][k] = 'O'
                            platform[i][k - 1] = '.'
                        }
                    }
                }
            }
            val currentLoad = load(platform)
            println("$cycle $currentLoad")
//            loadMap[cycle] = currentLoad
//            if (cycle > 10000 && loadReverseMap.contains(currentLoad)) {
//                val previousSameLoad = loadReverseMap[currentLoad]!!
//                val cycleLength = cycle - previousSameLoad
//                return loadMap[1000000000 % cycleLength + previousSameLoad]!!
//            }
//            loadReverseMap[currentLoad] = cycle
        }
        return load(platform)
    }

    val testInput = readInput("Day14_test")
//    check(part1(testInput) == 136L)
//    check(part2(testInput) == 64L)

    val input = readInput("Day14")
//    part1(input).println()
    part2(input).println()
}
