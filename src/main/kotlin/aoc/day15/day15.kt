package aoc.day15

import kotlin.math.abs
import kotlin.math.min

// DAY 15

private var testFile: String = "input/day15_test.txt";
private var inputFile: String = "input/day15_input.txt";
private var targetY: Long = 2000000
private var borderDeltas: List<Pair<Int, Int>> = listOf(Pair(1, 1), Pair(-1, -1), Pair(-1, 1), Pair(1, -1))

private data class Entity(val x: Long, val y: Long, var range: Long? = null) {
}

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private fun manhattan(fromX: Long, fromY: Long, toX: Long, toY: Long): Long {
    return abs(toX - fromX) + abs(toY - fromY);
}

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}

private fun calculateRadarRanges(radarsToBeacons: HashMap<Entity, Entity>) {
    radarsToBeacons.forEach { (radar, beacon) ->
        radar.range = manhattan(radar.x, radar.y, beacon.x, beacon.y)
    }
}

private fun insideAllowedBounds(x: Long, y: Long): Boolean {
    return x < 4000000 && y < 4000000 && x >= 0 && y >= 0
}

private fun findBorderStartingPoint(
    deltaMove: Pair<Int, Int>,
    radar: Entity,
    radarOuterBorder: Long
): Pair<Long, Long> {
    var deltaSum = abs(deltaMove.second + deltaMove.first)
    var currentX = if (deltaSum == 2) radar.x else (radar.x - deltaMove.second * radarOuterBorder)
    var currentY = if (deltaSum == 2) (radar.y - deltaMove.second * radarOuterBorder) else radar.y
    return Pair(currentX, currentY)
}

private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val radarToBeaconMap: HashMap<Entity, Entity> = inputLines.associate { line ->
        val numbers = Regex("(-?\\d+)").findAll(line).map { it.value.toLong() }.toMutableList()
        Pair(Entity(numbers[0], numbers[1]), Entity(numbers[2], numbers[3]))
    } as HashMap<Entity, Entity>
    calculateRadarRanges(radarToBeaconMap)
    var resultX = 0L
    var resultY = 0L
    radarToBeaconMap.keys.forEach outerEach@{ radar ->
        var radarOuterBorder = radar.range!! + 1L
        borderDeltas.forEach { moveDelta ->
            var (currentX, currentY) = findBorderStartingPoint(moveDelta, radar, radarOuterBorder)
            var manhattan = radarOuterBorder
            while (manhattan == radarOuterBorder) {
                if (insideAllowedBounds(currentX, currentY)) {
                    if (radarToBeaconMap.keys.all { otherRadar ->
                            var distance = manhattan(otherRadar.x, otherRadar.y, currentX, currentY)
                            var outsideRadarRange = distance > otherRadar.range!!
                            outsideRadarRange
                        }) {
                        resultX = currentX
                        resultY = currentY
                        return@outerEach
                    }
                }
                currentX += moveDelta.first
                currentY += moveDelta.second
                manhattan = manhattan(currentX, currentY, radar.x, radar.y)
            }
        }
    }
    var result = (resultX * 4000000) + resultY
    println(result)
}

private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val radarToBeaconMap: HashMap<Entity, Entity> = inputLines.associate { line ->
        val numbers = Regex("(-?\\d+)").findAll(line).map { it.value.toLong() }.toMutableList()
        Pair(Entity(numbers[0], numbers[1]), Entity(numbers[2], numbers[3]))
    } as HashMap<Entity, Entity>
    calculateRadarRanges(radarToBeaconMap)
    val minX = radarToBeaconMap.minOf { entry -> min(entry.value.x, entry.key.x) } - radarToBeaconMap.keys.maxOf{it.range!!}
    val maxX = radarToBeaconMap.maxOf { entry -> kotlin.math.max(entry.value.x, entry.key.x) } + radarToBeaconMap.keys.maxOf{it.range!!}
    println("$minX $maxX")
    var impossibleSpots = (minX..maxX).sumOf maxSpots@{ x ->
        if (radarToBeaconMap.values.any { beacon -> beacon.x == x && beacon.y == targetY }) return@maxSpots 0L
        var result = if (radarToBeaconMap.any { entry ->
                var radarBeaconManhattan = manhattan(entry.value.x, entry.value.y, entry.key.x, entry.key.y)
                var radarSpotManhattan = manhattan(entry.key.x, entry.key.y, x, targetY)
                radarSpotManhattan <= radarBeaconManhattan
            }) 1L else 0L
        result
    }
    println("Impossible spots $impossibleSpots")
}

fun main() {
//    findSolutions(testFile)
    findSolutions(inputFile)
}
