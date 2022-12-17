package aoc.day16

import java.lang.IllegalArgumentException

// DAY 16

private var testFile: String = "input/day16_test.txt";
private var inputFile: String = "input/day16_input.txt";

private data class Valve(val valveName: String, val pressureValue: Int, val valveDistances: MutableMap<String, Int>) {

}

private data class SoloPath(val valvesToVisit: List<Valve>, val valvesTravelled: List<Valve>, var cost: Int) {

}

private data class State(
    val valvesToVisit: List<Valve>,
    val humanValveTravelled: List<Valve>,
    val elephantValveTravelled: List<Valve>,
    var humanCost: Int,
    var elephantCost: Int
) {}

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private fun findSolutions(givenFile: String) {
//    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}

private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val valveMap: HashMap<String, Valve> = createValveMap(inputLines)
    calculateValveDistances(valveMap)
    val allPossibleStates: List<State> = findStates(
        State(
            valveMap.values.filter { valve -> valve.pressureValue > 0 },
            mutableListOf(valveMap["AA"]!!),
            mutableListOf(valveMap["AA"]!!),
            0,
            0
        ),
        valveMap
    )
    var maxStatePressure = allPossibleStates.maxOf { state ->
        countStatePressure(state, valveMap)
    }
    println(allPossibleStates.first { state -> countStatePressure(state, valveMap) == maxStatePressure })
//    println(countStatePressure(allPossibleStates.first { state -> countStatePressure(state, valveMap) == maxStatePressure }, valveMap, true))
//    allPossibleDuoPaths.forEach { e ->
//        println("NEW LINE")
//        println("${e.elephantCost} ${e.elephantValveTravelled.map { e -> e.valveName }}")
//        println("${e.humanCost} ${e.humanValveTravelled.map { e -> e.valveName }}")
//    }
}

private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val valveMap: HashMap<String, Valve> = createValveMap(inputLines)
    calculateValveDistances(valveMap)
    val allPossibleSoloPaths: List<SoloPath> = findSoloPaths(
        SoloPath(
            valveMap.values.filter { valve -> valve.pressureValue > 0 },
            mutableListOf(valveMap["AA"]!!),
            0
        )
    )
    val uniquePaths =
        allPossibleSoloPaths.map { e -> e.valvesTravelled }.map { f -> f.map { e -> e.valveName } }.distinct()
    val maxPressure = uniquePaths.maxOf { path ->
        var sumPressureReleased = 0
        var currentReleaseRate = 0
        var timePassed = 0
        var currentValve = "AA"
        path.subList(1, path.size).forEach { valve ->
            var distanceTraveled = valveMap[currentValve]!!.valveDistances[valve]!! + 1
            timePassed += distanceTraveled
            sumPressureReleased += (distanceTraveled * currentReleaseRate)
            currentReleaseRate += valveMap[valve]!!.pressureValue
            currentValve = valve
        }
        sumPressureReleased += ((30 - timePassed) * currentReleaseRate)
        sumPressureReleased
    }
    println(maxPressure)
}

private fun countStatePressure(state: State, valveMap: HashMap<String, Valve>, print: Boolean? = false): Int{
    var sumPressure = 0
    var pressurePerTurn = 0
    var humanTravelled = ArrayDeque<Valve>()
    humanTravelled.addAll(state.humanValveTravelled)
    var elephantTravelled = ArrayDeque<Valve>()
    elephantTravelled.addAll(state.elephantValveTravelled)
    humanTravelled.removeFirst();
    elephantTravelled.removeFirst()
    var elephant = Pair(0, "AA")
    var human = Pair(0, "AA")
    for(i in 0.. 26){
        val nextHumanStop = human.first.plus(valveMap[human.second]!!.valveDistances[humanTravelled.firstOrNull()?.valveName]?:0) + 1
        val nextElephantStop = elephant.first.plus(valveMap[elephant.second]!!.valveDistances[elephantTravelled.firstOrNull()?.valveName]?:0) + 1
        sumPressure += pressurePerTurn
        if(i == nextElephantStop && !elephantTravelled.isEmpty()){
            var valveStop = elephantTravelled.removeFirst()
            pressurePerTurn += valveStop.pressureValue
            elephant = Pair(nextElephantStop, valveStop.valveName)
            if(print == true) println("Elephant opens ${valveStop.valveName} at $i")
        }
        if(i == nextHumanStop && !humanTravelled.isEmpty()){
            var valveStop = humanTravelled.removeFirst()
            pressurePerTurn += valveStop.pressureValue
            human = Pair(nextHumanStop, valveStop.valveName)
            if(print == true) println("Human opens ${valveStop.valveName} at $i")
        }
    }
    return sumPressure
}

private var maxPressure = 0

private fun findStates(state: State, valveMap: HashMap<String, Valve>): List<State> {
    if (state.valvesToVisit.isEmpty() || (state.humanCost >= 26 && state.elephantCost >= 26)) {
        var pressure = countStatePressure(state, valveMap)
        if(pressure > maxPressure) {
            maxPressure = pressure
            println(maxPressure)
            return mutableListOf(state)
        }
        return mutableListOf()
    }
    return state.valvesToVisit.flatMap { nextValve ->
        val valvesToVisit = state.valvesToVisit.filter { valve -> valve != nextValve }.toMutableList()
        val elephantValvesTravelled = state.elephantValveTravelled.toMutableList()
        val humanValvesTravelled = state.humanValveTravelled.toMutableList()
        if (state.elephantCost < state.humanCost) {
            // move elephant
            val currentValve = state.elephantValveTravelled.last()
            val distance = currentValve.valveDistances[nextValve.valveName]!! + 1
            val nextElephantCost = state.elephantCost + distance
            if (nextElephantCost > 26) {
                // nowhere to move but maybe human can
                findStates(
                    State(state.valvesToVisit, humanValvesTravelled, elephantValvesTravelled, state.humanCost, 26),
                    valveMap
                )
            } else {
                // successfully move elephant
                elephantValvesTravelled.add(nextValve)
                findStates(
                    State(valvesToVisit, humanValvesTravelled, elephantValvesTravelled, state.humanCost, nextElephantCost),
                    valveMap
                )
            }
        } else {
            // move human
            val currentValve = state.humanValveTravelled.last()
            val distance = currentValve.valveDistances[nextValve.valveName]!! + 1
            val nextHumanCost = state.humanCost + distance
            if (nextHumanCost > 26) {
                // nowhere to move but maybe elephant can
                findStates(
                    State(state.valvesToVisit, humanValvesTravelled, elephantValvesTravelled, 26, state.elephantCost),
                    valveMap
                )
            } else {
                // successfully move human
                humanValvesTravelled.add(nextValve)
                findStates(
                    State(valvesToVisit, humanValvesTravelled, elephantValvesTravelled, nextHumanCost, state.elephantCost),
                    valveMap
                )
            }
        }
    }
}

private fun findSoloPaths(soloPath: SoloPath): List<SoloPath> {
    if (soloPath.valvesToVisit.isEmpty()) return mutableListOf(soloPath)
    return soloPath.valvesToVisit.flatMap { nextValve ->
        val valvesToVisit = soloPath.valvesToVisit.filter { valve -> valve != nextValve }.toMutableList()
        val valvesTravelled = soloPath.valvesTravelled.toMutableList()
        val currentValve = soloPath.valvesTravelled.last()
        val nextCost = soloPath.cost + currentValve.valveDistances[nextValve.valveName]!! + 1
        if (nextCost > 30) {
            mutableListOf(SoloPath(mutableListOf(), valvesTravelled, nextCost))
        } else {
            valvesTravelled.add(nextValve)
            findSoloPaths(SoloPath(valvesToVisit, valvesTravelled, nextCost))
        }
    }
}

private fun calculateValveDistances(valveMap: HashMap<String, Valve>) {
    valveMap.values.forEach { outerValve ->
        valveMap.values.filter { valve -> valve.valveName != outerValve.valveName }.forEach { innerValve ->
            val distance = findPathDistance(valveMap, outerValve.valveName, innerValve.valveName)
            outerValve.valveDistances[innerValve.valveName] = distance
        }
    }
}

private fun createValveMap(inputLines: List<String>): HashMap<String, Valve> {
    return inputLines.associate { line ->
        val lineSplit = line.split(" ")
        val valveName = lineSplit[1]
        val valveValue = lineSplit[4].removeSuffix(";").split("=")[1].toInt()
        val valveDistances = mutableMapOf<String, Int>()
        lineSplit.subList(9, lineSplit.size).associateByTo(valveDistances, { it.removeSuffix(",") }, { 0 })
        Pair(valveName, Valve(valveName, valveValue, valveDistances))
    } as HashMap<String, Valve>
}

private fun findPathDistance(valveMap: HashMap<String, Valve>, fromValve: String, toValve: String): Int {
    val starterValve = valveMap[fromValve]!!
    val nextDepthValves = mutableListOf<Valve>()
    val valveQueue = ArrayDeque<Valve>()
    valveQueue.add(starterValve)
    var distance = 0
    val visitedValves: MutableList<String> = mutableListOf()
    visitedValves.add(starterValve.valveName)
    while (!valveQueue.isEmpty()) {
        val currentValve = valveQueue.removeFirst()
        if (currentValve.valveName == toValve) {
            return distance;
        }
        nextDepthValves.addAll(currentValve.valveDistances.filter { (k, v) -> v <= 1 && !visitedValves.contains(k) }.keys.map { name ->
            visitedValves.add(valveMap[name]!!.valveName)
            valveMap[name]!!
        })
        if (valveQueue.isEmpty()) {
            distance++
            valveQueue.addAll(nextDepthValves)
            nextDepthValves.clear()
        }
    }
    throw IllegalArgumentException("Path between valves was not found so something is wrong")
}

fun main() {
//    findSolutions(testFile)
    findSolutions(inputFile)
}
