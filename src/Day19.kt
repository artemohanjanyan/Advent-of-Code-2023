import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.io.path.Path
import kotlin.io.path.inputStream

fun main() {
    data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun total(): Int = x + m + a + s
        fun get(c: Char): Int =
            when (c) {
                'x' -> x
                'm' -> m
                'a' -> a
                's' -> s
                else -> throw RuntimeException(":<")
            }
    }
    data class Rule(val property: Char, val operator: Char, val value: Int, val ifTrue: String) {
        fun test(part: Part): Boolean {
            val propertyValue = part.get(property)
            return when (operator) {
                '<' -> propertyValue < value
                '>' -> propertyValue > value
                else -> throw RuntimeException("._.")
            }
        }
    }
    data class Workflow(val name: String, val rules: List<Rule>, val lastRule: String)
    data class Input(val workflows: List<Workflow>, val parts: List<Part>) {
        val workflowMap: Map<String, Workflow> = workflows.associateBy { it.name }

        fun test(part: Part): Boolean {
            var currentWorkflow = "in"
            outer@ while (currentWorkflow != "R" && currentWorkflow != "A") {
                val workflow = workflowMap[currentWorkflow]!!
                for (rule in workflow.rules) {
                    if (rule.test(part)) {
                        currentWorkflow = rule.ifTrue
                        continue@outer
                    }
                }
                currentWorkflow = workflow.lastRule
            }
            return currentWorkflow == "A"
        }
    }

    fun readInput(fileName: String): Input {
        Scanner(Path("src/$fileName.txt").inputStream()).use { scanner ->
            val workflows = ArrayList<Workflow>()
            while (scanner.hasNext("[a-z]+\\{.*}")) {
                val workflowStr = scanner.next()
                val (name, rest1) = workflowStr.dropLast(1).split('{').let { Pair(it[0], it[1]) }
                val rules = ArrayList<Rule>()
                for (ruleStr in rest1.split(',')) {
                    if (ruleStr.length == 1 || ruleStr[1] != '<' && ruleStr[1] != '>') {
                        workflows.add(Workflow(name, rules, ruleStr))
                        break
                    }
                    val property = ruleStr[0]
                    val operator = ruleStr[1]
                    val (value, ifTrue) = ruleStr.drop(2).split(':').let { Pair(it[0].toInt(), it[1]) }
                    rules.add(Rule(property, operator, value, ifTrue))
                }
            }
            val parts = ArrayList<Part>()
            while (scanner.hasNext("\\{.*}")) {
                val partStr = scanner.next().drop(1).dropLast(1)
                val properties = partStr.split(',')
                fun find(propertyChar: Char): Int =
                    properties.find { it[0] == propertyChar }!!.drop(2).toInt()
                parts.add(Part(
                    x = find('x'),
                    m = find('m'),
                    a = find('a'),
                    s = find('s'),
                ))
            }
            return Input(workflows, parts)
        }
    }

    fun part1(input: Input): Long {
        return input.parts.sumOf { part ->
            if (input.test(part)) part.total().toLong() else 0
        }
    }

    fun part2(input: Input): Long {
        val ruleValues = HashMap<Char, ArrayList<Int>>()
        for (c in listOf('x', 'm', 'a', 's')) {
            ruleValues[c] = ArrayList()
            ruleValues[c]!!.add(1)
            ruleValues[c]!!.add(4001)
        }
        for (rule in input.workflows.flatMap { it.rules }) {
            ruleValues[rule.property]!!.add(
                when (rule.operator) {
                    '<' -> rule.value
                    '>' -> rule.value + 1
                    else -> throw RuntimeException("?_?")
                }
            )
        }
        val xValues = ruleValues['x']!!
        val mValues = ruleValues['m']!!
        val aValues = ruleValues['a']!!
        val sValues = ruleValues['s']!!
        xValues.sort()
        mValues.sort()
        aValues.sort()
        sValues.sort()
        var combinations = 0L
        for ((x1, x2) in xValues.zipWithNext()) {
            if (x1 == x2) continue
            println("yo $x1")
            for ((m1, m2) in mValues.zipWithNext()) {
                if (m1 == m2) continue
                for ((a1, a2) in aValues.zipWithNext()) {
                    if (a1 == a2) continue
                    for ((s1, s2) in sValues.zipWithNext()) {
                        if (s1 == s2) continue
                        if (input.test(Part(x1, m1, a1, s1))) {
                            combinations += (x2 - x1).toLong() * (m2 - m1) * (a2 - a1) * (s2 - s1)
                        }
                    }
                }
            }
        }
        return combinations
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 19114L)
    check(part2(testInput) == 167409079868000L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
