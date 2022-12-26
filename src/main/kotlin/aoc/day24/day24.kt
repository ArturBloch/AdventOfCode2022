package aoc.day24

import java.lang.IllegalArgumentException

private var testFile: String = "input/day24_test.txt";
private var inputFile: String = "input/day24_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}

private data class Blizzard(var x: Int, var y: Int, var deltaX: Int, var deltaY: Int) {
    fun advanceBlizzard(grid: Array<CharArray>) {
        this.x += deltaX
        this.y += deltaY
        if (grid[this.y][this.x] == '#') {
            if (this.y == grid.size - 1) this.y = 1
            if (this.y == 0) this.y = grid.size - 2
            if (this.x == grid[0].size - 1) this.x = 1
            if (this.x == 0) this.x = grid[0].size - 2
        }
    }
}

private data class Pos(var x: Int = 0, var y: Int = 0) {}

private val neighbours = mutableListOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0), Pair(0, 0))

private var blizzards: MutableList<Blizzard> = mutableListOf()

private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    var startPos: Pos = Pos()
    var endPos: Pos = Pos()
    var grid: Array<CharArray> = Array(inputLines.size) { CharArray(inputLines[0].length) }
    inputLines.forEachIndexed { y, line ->
        if (y == 0) startPos = Pos(line.indexOf('.'), y)
        if (y == grid.size - 1) endPos = Pos(line.indexOf('.'), y)
        line.forEachIndexed { x, char ->
            grid[y][x] = '.'
            when (char) {
                '<' -> blizzards.add(Blizzard(x, y, -1, 0))
                '>' -> blizzards.add(Blizzard(x, y, 1, 0))
                '^' -> blizzards.add(Blizzard(x, y, 0, -1))
                'v' -> blizzards.add(Blizzard(x, y, 0, 1))
                '#' -> grid[y][x] = '#'
            }
        }
    }

    var startToEnd = findPath(startPos, endPos, grid, blizzards)
    var endToStart = findPath(endPos, startPos, grid, blizzards)
    var startToEndAgain = findPath(startPos, endPos, grid, blizzards)

    println("Distance to exit is $startToEnd")
    println("Total distance of the trip is ${startToEnd + endToStart + startToEndAgain}")
}

private fun findPath(startPos: Pos, endPos: Pos, grid: Array<CharArray>, blizzards: MutableList<Blizzard>): Int {
    blizzards.forEach { b -> b.advanceBlizzard(grid) }
    var visited: Array<IntArray> = Array(grid.size) { IntArray(grid[0].size) }
    visited[startPos.y][startPos.x] = 1
    var path: ArrayDeque<Pos> = ArrayDeque(mutableListOf(startPos))
    var nextDepth: MutableList<Pos> = mutableListOf()
    var depth = 0
    while (path.isNotEmpty()) {
        var currentPos = path.removeFirst()
        for (neighbour in neighbours) {
            var newPos = Pos(currentPos.x + neighbour.first, currentPos.y + neighbour.second)
            if (outsideBounds(newPos, grid) || grid[newPos.y][newPos.x] == '#') continue
            if (blizzards.any { blizz -> blizz.x == newPos.x && blizz.y == newPos.y }) continue
            if (newPos == endPos) return depth + 1
            nextDepth.add(newPos)
        }

        if (path.isEmpty()) {
            depth++
            path.addAll(nextDepth.distinct())
            nextDepth.clear()
            blizzards.forEach { b -> b.advanceBlizzard(grid) }
        }
    }
    throw IllegalArgumentException("There is no path to the exit!")
}

private fun outsideBounds(pos: Pos, grid: Array<CharArray>): Boolean {
    return pos.x < 0 || pos.y < 0 || pos.y >= grid.size || pos.x >= grid[0].size;
}

private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)

}

fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}
