package aoc.day14

import kotlin.math.max
import kotlin.math.min

// DAY 14

private var testFile: String = "input/day14_test.txt";
private var inputFile: String = "input/day14_input.txt";

private val AIR = 0
private val SAND = 1
private val WALL = 2

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private fun findMaximumY(inputLines: List<String>): Int{
    return inputLines.maxOf { line ->
        val lineParts = line.split(" -> ")
        lineParts.maxOf { part -> part.split(",")[1].toInt() }
    }
}

private fun findSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val maxY = findMaximumY (inputLines)
    val cave: Array<IntArray> = Array(maxY+3) { e -> IntArray(1000) }
    drawFloor(cave)
    drawWalls(inputLines, cave)
    var simulateSand = true
    var sandCounter = 0
    while (simulateSand) {
        var sandX = 500
        var sandY = 0
        if(cave[sandY][sandX] == SAND) {
            simulateSand = false
            break
        }
        while(true){
            if(sandY + 1 >= cave.size) {
                simulateSand = false
                break;
            }
            if(cave[sandY + 1][sandX] == AIR) {
                sandY = sandY + 1
                continue
            } else if(cave[sandY + 1][sandX - 1] == AIR){
                sandY = sandY + 1
                sandX = sandX - 1
                continue
            } else if(cave[sandY + 1][sandX + 1] == AIR){
                sandY = sandY + 1
                sandX = sandX + 1
                continue
            } else{
                cave[sandY][sandX] = SAND
                sandCounter++
                break
            }
        }
    }
    println("Sand stuck $sandCounter")
}

fun drawWalls(inputLines: List<String>, cave: Array<IntArray>) {
    inputLines.forEach { line ->
        val lineParts = line.split(" -> ")
        var currentX = -1
        var currentY = -1
        for (linePart in lineParts) {
            val newX = linePart.split(",")[0].toInt()
            val newY = linePart.split(",")[1].toInt()
            if (currentX != -1) {
                (min(currentX, newX)..max(currentX, newX)).forEach { x -> cave[currentY][x] = WALL }
                (min(currentY, newY)..max(currentY, newY)).forEach { y -> cave[y][currentX] = WALL }
            }
            currentX = newX
            currentY = newY
        }
    }

}

fun drawFloor(cave: Array<IntArray>) {
    for(i in 0 until cave[0].size){
        cave[cave.size - 1][i] = WALL
    }
}

private fun findSolutions(givenFile: String) {
    findSolution(givenFile)
}


fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}

