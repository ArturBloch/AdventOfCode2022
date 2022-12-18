package aoc.day17

import kotlin.math.max

// DAY 17

private var testFile: String = "input/day17_test.txt";
private var inputFile: String = "input/day17_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private var shapes = mutableListOf(
    mutableListOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0)),
    mutableListOf(Pair(1, 0), Pair(0, 1), Pair(1, 1), Pair(2, 1), Pair(1, 2)),
    mutableListOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(2, 1), Pair(2, 2)),
    mutableListOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3)),
    mutableListOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1)),
)

private fun findSolutions(givenFile: String) {
    findSolution(givenFile)
}

private fun isViableRockPosition(
    tetrisArena: Array<IntArray>,
    currentX: Int,
    currentY: Int,
    pointList: MutableList<Pair<Int, Int>>
): Boolean {
    val isWithinMapBounds = { x: Int, y: Int -> x >= 0 && x < tetrisArena[0].size && y >= 0 }
    return pointList.all { (x, y) ->
        var rockX = x + currentX
        var rockY = y + currentY
        isWithinMapBounds(rockX, rockY) && tetrisArena[rockY][rockX] == 0
    }
}

private var heights = mutableListOf<Int>()

private fun findSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    var jetStream = inputLines[0]
    var tetrisArena: Array<IntArray> = Array(100000) { IntArray(7) }
    var jetIndex = 0
    var fallenRocks = 0L
    var highestY = 0
    var cycleCount = 0
    while (fallenRocks < 2022) {
        var nextRock = fallenRocks % shapes.size
        var blockPointList = shapes[nextRock.toInt()]
        var currentX = 2
        var currentY = highestY + 3
        var rockFalling = true
        var isPushed = true
        var starterJetIndex = jetIndex % jetStream.length
        while (rockFalling) {
            jetIndex %= jetStream.length
            if (isPushed) {
                // is being pushed this turn
                val deltaX = if (jetStream[jetIndex] == '>') 1 else -1
                val newX = currentX + deltaX
                val newY = currentY
                if (isViableRockPosition(tetrisArena, newX, newY, blockPointList)) {
                    currentX = newX
                    currentY = newY
                }
                jetIndex++
            } else {
                // is falling down this turn
                val newX = currentX
                val newY = currentY - 1
                if (isViableRockPosition(tetrisArena, newX, newY, blockPointList)) {
                    currentX = newX
                    currentY = newY
                } else {
                    rockFalling = false
                }
            }
            isPushed = !isPushed
        }
        var newPotentialY = drawRockInArena(tetrisArena, currentX, currentY, blockPointList)
        highestY = max(highestY, newPotentialY)
        heights.add(highestY)
        fallenRocks++
    }
    println(highestY)
}

fun drawRockInArena(
    tetrisArena: Array<IntArray>,
    currentX: Int,
    currentY: Int,
    pointList: MutableList<Pair<Int, Int>>
): Int {
    var maxY = 0
    pointList.forEach { (x, y) ->
        var blockY = y + currentY
        tetrisArena[blockY][x + currentX] = 1
        if (blockY > maxY) maxY = blockY
    }
    return maxY + 1
}

fun main() {
//    findSolutions(testFile)
    findSolutions(inputFile)
}
