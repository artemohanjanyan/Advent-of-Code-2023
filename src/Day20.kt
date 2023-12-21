import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.io.path.Path
import kotlin.io.path.inputStream

enum class ModuleType {
    Untyped, Broadcast, FlipFlop, Conjunction
}

fun main() {
    data class Module(val type: ModuleType, val name: String, val destinations: List<String>)
    data class Input(val modules: List<Module>) {
        val moduleMap = modules.associateBy { it.name }
    }
    data class Pulse(val source: String, val destination: String, val isHigh: Boolean)

    fun readInput(fileName: String): Input {
        Scanner(Path("src/$fileName.txt").inputStream()).use { scanner ->
            val modules = ArrayList<Module>()
            while (scanner.hasNext()) {
                val s1 = scanner.next()
                val typeStr = s1.takeWhile { !it.isLetter() }
                val moduleName = s1.drop(typeStr.length)
                val type =
                    if (typeStr == "" && moduleName == "broadcaster")
                        ModuleType.Broadcast
                    else if (typeStr == "&")
                        ModuleType.Conjunction
                    else if (typeStr == "%")
                        ModuleType.FlipFlop
                    else if (typeStr == "")
                        ModuleType.Untyped
                    else
                        throw RuntimeException("%_&")
                scanner.next("->")
                val destinations = ArrayList<String>()
                while (scanner.hasNext("[a-z]+,")) {
                    destinations.add(scanner.next().dropLast(1))
                }
                destinations.add(scanner.next())
                modules.add(Module(type, moduleName, destinations))
            }
            return Input(modules)
        }
    }

    fun part1(input: Input): Long {
        var low = 0L
        var high = 0L
        val flipFlops = HashMap<String, Boolean>()
        for (module in input.modules) {
            if (module.type == ModuleType.FlipFlop) {
                flipFlops[module.name] = false
            }
        }
        val conjunctions = HashMap<String, HashMap<String, Boolean>>()
        for (module in input.modules) {
            if (module.type == ModuleType.Conjunction) {
                val rememberedValues = HashMap<String, Boolean>()
                for (inputModule in input.modules) {
                    if (inputModule.destinations.contains(module.name)) {
                        rememberedValues[inputModule.name] = false
                    }
                }
                conjunctions[module.name] = rememberedValues
            }
        }

        for (pressN in 1..1000) {
            val pulseQueue = ArrayDeque<Pulse>()
            fun enqueuePulse(pulse: Pulse) {
                pulseQueue.addFirst(pulse)
                if (pulse.isHigh) {
                    ++high
                } else {
                    ++low
                }
//                println("${pulse.source} -${if (pulse.isHigh) "high" else "low"}-> ${pulse.destination}")
            }
            enqueuePulse(Pulse("button", "broadcaster", false))
            while (!pulseQueue.isEmpty()) {
                val pulse = pulseQueue.removeLast()
                val module = input.moduleMap[pulse.destination] ?: continue
                fun sendPulses(isHigh: Boolean) {
                    for (destination in module.destinations) {
                        enqueuePulse(Pulse(module.name, destination, isHigh))
                    }
                }
                when (module.type) {
                    ModuleType.Untyped -> {}
                    ModuleType.Broadcast -> sendPulses(pulse.isHigh)
                    ModuleType.FlipFlop ->
                        if (!pulse.isHigh) {
                            val newValue = !flipFlops.getValue(module.name)
                            flipFlops[module.name] = newValue
                            sendPulses(newValue)
                        }

                    ModuleType.Conjunction -> {
                        val rememberedValues = conjunctions.getValue(module.name)
                        rememberedValues[pulse.source] = pulse.isHigh
                        sendPulses(!rememberedValues.all { it.value })
                    }
                }
            }
        }
        return low * high
    }

    fun part2(input: Input): Long {
        val flipFlops = HashMap<String, Boolean>()
        for (module in input.modules) {
            if (module.type == ModuleType.FlipFlop) {
                flipFlops[module.name] = false
            }
        }
        val conjunctions = HashMap<String, HashMap<String, Boolean>>()
        for (module in input.modules) {
            if (module.type == ModuleType.Conjunction) {
                val rememberedValues = HashMap<String, Boolean>()
                for (inputModule in input.modules) {
                    if (inputModule.destinations.contains(module.name)) {
                        rememberedValues[inputModule.name] = false
                    }
                }
                conjunctions[module.name] = rememberedValues
            }
        }

        for (pressN in 1..2_000_000_000) {
            if (pressN % 1_000_000 == 0) {
                println("  step $pressN")
            }
            val pulseQueue = ArrayDeque<Pulse>()
            fun enqueuePulse(pulse: Pulse) {
                pulseQueue.addFirst(pulse)
            }
            enqueuePulse(Pulse("button", "broadcaster", false))
            while (!pulseQueue.isEmpty()) {
                val pulse = pulseQueue.removeLast()
                if (pulse.destination == "rs" && pulse.isHigh) {
                    println("${pulse.source} sent high pulse at press $pressN!")
                }
                if (pulse.destination == "rx" && !pulse.isHigh) {
                    return pressN.toLong()
                }
                val module = input.moduleMap[pulse.destination] ?: continue
                fun sendPulses(isHigh: Boolean) {
                    for (destination in module.destinations) {
                        enqueuePulse(Pulse(module.name, destination, isHigh))
                    }
                }
                when (module.type) {
                    ModuleType.Untyped -> {}
                    ModuleType.Broadcast -> sendPulses(pulse.isHigh)
                    ModuleType.FlipFlop ->
                        if (!pulse.isHigh) {
                            val newValue = !flipFlops.getValue(module.name)
                            flipFlops[module.name] = newValue
                            sendPulses(newValue)
                        }

                    ModuleType.Conjunction -> {
                        val rememberedValues = conjunctions.getValue(module.name)
                        rememberedValues[pulse.source] = pulse.isHigh
                        sendPulses(!rememberedValues.all { it.value })
                    }
                }
            }
        }
        return -1
    }

    val testInput = readInput("Day20_test")
    val testInput2 = readInput("Day20_test2")
    check(part1(testInput) == 32000000L)
    check(part1(testInput2) == 11687500L)
//    check(part2(testInput) == 167409079868000L)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
