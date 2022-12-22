package aoc.day19

import kotlin.math.max

private var testFile: String = "input/day19_test.txt";
private var inputFile: String = "input/day19_input.txt";

private var timeToEnd = 24

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
        for(i in robotInventory.indices){
            oreInventory[i] += robotInventory[i]
        }
    }

    fun createRobots(blueprint: Blueprint): List<State> {
        val newStates = mutableListOf<State>()
        for(robotCostIndex in GEODE downTo ORE) {
            if(robotInventory[robotCostIndex] + 1 > blueprint.maxRobotsNeeded[robotCostIndex]) continue
            val newOreState = oreInventory.copyOf()
            for (oreIndex in ORE .. OBSIDIAN) {
                newOreState[oreIndex] = newOreState[oreIndex] - blueprint.robotCosts[robotCostIndex][oreIndex]
            }
            if(newOreState.all { oreCount -> oreCount >= 0 }) {
                val newRobotState = robotInventory.copyOf()
                newRobotState[robotCostIndex]++
                val newState = State(currentTurn, robotInventory, newOreState)
                newState.robotsGatherOre()
                newState.robotInventory = newRobotState
                newStates.add(newState)
                if(robotCostIndex >= OBSIDIAN) break
            }
        }
        return newStates
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
            if(oreIndex == GEODE) return@oreNeeded Integer.MAX_VALUE
            robotCosts.maxOf { robotCost -> robotCost[oreIndex] }
        }.toIntArray()
        Blueprint(numbers[0], robotCosts, maxOreNeeded)
    }
    val maxScore = blueprints.sumOf { blueprint ->
        var result = findBestGeodeScore(blueprint)

        println("Max Geodes $result")
        blueprintQualityLevel(blueprint.id, result)
    }
    println("Max score is $maxScore")
}

private fun findBestGeodeScore(blueprint: Blueprint): Int {
    println("New blueprint ${blueprint.id}")
    val initialState = State(0, intArrayOf(1, 0, 0, 0), intArrayOf(0, 0, 0, 0))
    val statesToProcess = mutableListOf(initialState)
    var maxGeodes = 0
    var maxGeodesSeen = 0
    while(statesToProcess.isNotEmpty()){
        val state = statesToProcess.removeFirst();
        maxGeodesSeen = max(state.oreInventory[GEODE], maxGeodesSeen)
        if(state.oreInventory[GEODE] < maxGeodesSeen) continue
        println("${statesToProcess.size} ${state.currentTurn} ${state.robotInventory[0]}")
//        println(state)
        if (state.currentTurn >= timeToEnd) {
            maxGeodes = max(maxGeodes, state.oreInventory[GEODE])
            continue
        }
        state.currentTurn++
        val newStates = state.createRobots(blueprint)
        statesToProcess.addAll(newStates)
        state.robotsGatherOre()
        statesToProcess.add(state)
        // state.turn ++
    }
    return maxGeodes
}

private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
}

fun main() {
//    findSolutions(testFile)
    findSolutions(inputFile)
}
