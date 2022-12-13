package aoc.day9

import kotlin.math.abs
import kotlin.math.max

// DAY 9

private var testFile: String = "input/day9_test.txt";
private var inputFile: String = "input/day9_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()

data class SnakePart(var x: Int, var y: Int, var follows: SnakePart?)

fun chebyshevDistance(snakePart: SnakePart, snakePartTwo: SnakePart): Int{
    return max(abs(snakePart.x - snakePartTwo.x), abs(snakePart.y - snakePartTwo.y))
}

fun fixSnakePartPosition(snakePart: SnakePart, followedPart: SnakePart) {
    if (chebyshevDistance(snakePart, followedPart) <= 1) return
    if (followedPart.x != snakePart.x && followedPart.y != snakePart.y) {
        var deltaY = (followedPart.y - snakePart.y) / abs((followedPart.y - snakePart.y))
        var deltaX = (followedPart.x - snakePart.x) / abs((followedPart.x - snakePart.x))
        snakePart.y += deltaY
        snakePart.x += deltaX
    } else if (followedPart.x == snakePart.x) {
        snakePart.y = (followedPart.y + snakePart.y) / 2
    } else if (followedPart.y == snakePart.y) {
        snakePart.x = (followedPart.x + snakePart.x) / 2
    }
}

private fun findOneStarSolution(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val head = SnakePart(0, 0, null)
    val tail = SnakePart(0, 0, head)
    var uniquePosition: HashSet<Pair<Int, Int>> = hashSetOf()
    givenInput.forEach { line ->
        val (dir, dist) = line.split(" ")
        for (i in 0 until Integer.parseInt(dist)) {
            when (dir) {
                "U" -> head.y++
                "D" -> head.y--
                "L" -> head.x--
                "R" -> head.x++
            }
            fixSnakePartPosition(tail, head)
            uniquePosition.add(Pair(tail.x, tail.y))
        }
    }
    println(uniquePosition.size)
}


private fun findTwoStarSolution(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val head = SnakePart(0, 0, null)
    val tailParts: MutableList<SnakePart> = mutableListOf()
    var prevSnakeTailPart = SnakePart(0, 0, head)
    tailParts.add(prevSnakeTailPart)
    for(i in 2..9){
        val snakeTailPart = SnakePart(0,0, prevSnakeTailPart)
        tailParts.add(snakeTailPart)
        prevSnakeTailPart = snakeTailPart
    }
    var uniquePosition: HashSet<Pair<Int, Int>> = hashSetOf()
    givenInput.forEach { line ->
        val (dir, dist) = line.split(" ")
        for (i in 0 until Integer.parseInt(dist)) {
            when (dir) {
                "U" -> head.y++
                "D" -> head.y--
                "L" -> head.x--
                "R" -> head.x++
            }
            tailParts.forEach{
                snakePart ->
                snakePart.follows?.let { fixSnakePartPosition(snakePart, it) }
            }
            uniquePosition.add(Pair(tailParts.last().x, tailParts.last().y))
        }
    }
    println(uniquePosition.size)
}

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}


fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}

