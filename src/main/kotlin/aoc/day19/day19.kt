package aoc.day19

import kotlin.math.max
import kotlin.math.min

private var testFile: String = "input/day19_test.txt";
private var inputFile: String = "input/day19_input.txt";

private var timeToEndPart1 = 24
private var timeToEndPart2 = 32

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private val ORE = 0;
private val CLAY = 1;
private val OBSIDIAN = 2;
private val GEODE = 3;

private data class State(
    var currentTurn: Int,
    var robotInventory: IntArray,
    var oreInventory: IntArray,
) {
    fun robotsGatherOre() {
        for (i in robotInventory.indices) {
            oreInventory[i] += robotInventory[i]
        }
    }

    fun canAffordGeodeBot(blueprint: Blueprint): Boolean {
        return (ORE..OBSIDIAN).all { oreIndex -> oreInventory[oreIndex] >= blueprint.robotCosts[GEODE][oreIndex] }
    }

    fun createRobot(blueprint: Blueprint, oreIndex: Int): State? {
        if (robotInventory[oreIndex] + 1 > blueprint.maxRobotsNeeded[oreIndex]) return null
        val newOreState = oreInventory.copyOf()
        val newRobotState = robotInventory.copyOf()
        newRobotState[oreIndex]++
        for (oreCostIndex in ORE..OBSIDIAN) {
            newOreState[oreCostIndex] = newOreState[oreCostIndex] - blueprint.robotCosts[oreIndex][oreCostIndex]
        }
        if (newOreState.any { oreCount -> oreCount < 0 }) return null
        var newState = this.copy(
            currentTurn = currentTurn + 1,
            oreInventory = newOreState
        )
        newState.robotsGatherOre()
        newState.robotInventory = newRobotState
        return newState
    }
}

private data class Blueprint(
    val id: Int,
    val robotCosts: List<IntArray>,
    val maxRobotsNeeded: IntArray
)

private fun blueprintQualityLevel(blueprintId: Int, openedGeodesCount: Int): Int {
    return blueprintId * openedGeodesCount;
}

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}

private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val blueprints = inputLines.map { line ->
        val numbers = Regex("(-?\\d+)").findAll(line).map { it.value.toInt() }.toMutableList()
        val oreRobotCost = intArrayOf(numbers[1], 0, 0)
        val clayRobotCost = intArrayOf(numbers[2], 0, 0)
        val obsidianRobotCost = intArrayOf(numbers[3], numbers[4], 0)
        val geodeRobotCost = intArrayOf(numbers[5], 0, numbers[6])
        val robotCosts = listOf(oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost)
        val maxOreNeeded = (ORE..GEODE).map oreNeeded@{ oreIndex ->
            if (oreIndex == GEODE) return@oreNeeded Integer.MAX_VALUE
            robotCosts.maxOf { robotCost -> robotCost[oreIndex] }
        }.toIntArray()
        Blueprint(numbers[0], robotCosts, maxOreNeeded)
    }
    val maxScorePart1 = blueprints.sumOf { blueprint ->
        println("New blueprint ${blueprint.id}")
        val initialState = State(0, intArrayOf(1, 0, 0, 0), intArrayOf(0, 0, 0, 0))
        var result = findBestGeodeScore(blueprint, initialState, -1, timeToEndPart1)

        println("Max Geodes $result")
        blueprintQualityLevel(blueprint.id, result)
    }
    println("Answer to part 1 is $maxScorePart1")

}


private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val blueprints = inputLines.subList(0, min(inputLines.size, 3)).map { line ->
        val numbers = Regex("(-?\\d+)").findAll(line).map { it.value.toInt() }.toMutableList()
        val oreRobotCost = intArrayOf(numbers[1], 0, 0)
        val clayRobotCost = intArrayOf(numbers[2], 0, 0)
        val obsidianRobotCost = intArrayOf(numbers[3], numbers[4], 0)
        val geodeRobotCost = intArrayOf(numbers[5], 0, numbers[6])
        val robotCosts = listOf(oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCost)
        val maxOreNeeded = (ORE..GEODE).map oreNeeded@{ oreIndex ->
            if (oreIndex == GEODE) return@oreNeeded Integer.MAX_VALUE
            robotCosts.maxOf { robotCost -> robotCost[oreIndex] }
        }.toIntArray()
        Blueprint(numbers[0], robotCosts, maxOreNeeded)
    }
    val maxScorePart2 = blueprints.map { blueprint ->
        println("New blueprint ${blueprint.id}")
        val initialState = State(0, intArrayOf(1, 0, 0, 0), intArrayOf(0, 0, 0, 0))
        var result = findBestGeodeScore(blueprint, initialState, -1, timeToEndPart2)
        println("Max Geodes $result")
        result
    }.reduce { x, acc -> x * acc }
    println("Answer to part 2 is $maxScorePart2")
}

private fun robotEveryTurn(num: Int): Int {
    if (num == 0) return 0
    return (1..num).reduce(Integer::sum)
}

private fun maxObtainableGeodes(fromState: State, timeToEnd: Int): Int {
    var turnsLeft = timeToEnd - fromState.currentTurn
    return fromState.oreInventory[GEODE] + (fromState.robotInventory[GEODE] * turnsLeft) + robotEveryTurn(turnsLeft)
}

private fun findBestGeodeScore(blueprint: Blueprint, currentState: State, maxGeodes: Int, timeToEnd: Int): Int {
    // if reached the end update maxGeodes and return
    var maxGeodesSoFar = maxGeodes
    if (currentState.currentTurn >= timeToEnd) {
        maxGeodesSoFar = max(maxGeodesSoFar, currentState.oreInventory[GEODE])
        return currentState.oreInventory[GEODE];
    }
    if (maxObtainableGeodes(currentState, timeToEnd) < maxGeodesSoFar && maxGeodesSoFar != -1) {
        return maxGeodesSoFar
    }
    // if cannot reach higher amount then just return
    var fromOre = ORE
    if (currentState.canAffordGeodeBot(blueprint)) fromOre = GEODE
    maxGeodesSoFar = max(maxGeodesSoFar, (fromOre..GEODE).mapNotNull { ore ->
        currentState.createRobot(blueprint, ore)
    }.maxOfOrNull { state -> findBestGeodeScore(blueprint, state, maxGeodesSoFar, timeToEnd) } ?: -1)
    currentState.currentTurn++
    currentState.robotsGatherOre()
    return max(maxGeodesSoFar, findBestGeodeScore(blueprint, currentState, maxGeodesSoFar, timeToEnd))
}

fun main() {
//    findSolutions(testFile)
    findSolutions(inputFile)
}
