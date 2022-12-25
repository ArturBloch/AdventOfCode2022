package aoc.day23

import kotlin.math.abs
import kotlin.math.max

private var testFile: String = "input/day23_test.txt";
private var inputFile: String = "input/day23_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private var directionQueue: ArrayDeque<String> = ArrayDeque(mutableListOf("north", "south", "west", "east"))
private val dirMap: MutableMap<String, List<Pair<Int, Int>>> = mutableMapOf(
    "north" to mutableListOf(Pair(-1, -1), Pair(0, -1), Pair(1, -1)),
    "south" to mutableListOf(Pair(-1, 1), Pair(0, 1), Pair(1, 1)),
    "west" to mutableListOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1)),
    "east" to mutableListOf(Pair(1, -1), Pair(1, 0), Pair(1, 1)),
)

private data class Elf(var x: Int, var y: Int, var action: String = "") {}

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}

private fun chebyshevDistance(elf: Elf, elf2: Elf): Int {
    return max(abs(elf2.x - elf.x), abs(elf2.y - elf.y))
}

private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val elfList: MutableList<Elf> = mutableListOf()
    directionQueue = ArrayDeque(mutableListOf("north", "south", "west", "east"))
    inputLines.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (char == '#') elfList.add(Elf(x, y))
        }
    }
    simulateRounds(elfList, 10)
}

private fun markStillElves(elfList: MutableList<Elf>) {
    elfList.forEach { elf ->
        elf.action = if (elfList.filter { filterElf -> elf != filterElf }
                .all { otherElf -> chebyshevDistance(elf, otherElf) > 1 }) "None" else "NoAction"
    }
}

private fun planElvesMoves(
    elfList: MutableList<Elf>, dirOrder: ArrayDeque<String>
): MutableMap<Pair<Int, Int>, MutableList<Elf>> {
    val plannedMoveMap: MutableMap<Pair<Int, Int>, MutableList<Elf>> = mutableMapOf()
    elfList.filter { elf -> elf.action == "NoAction" }.forEach { elf ->
        for (order in dirOrder) {
            var dirPoints = dirMap[order]!!
            var canMove = dirPoints.all { pos ->
                val targetX = elf.x + pos.first
                val targetY = elf.y + pos.second
                elfList.none { otherElf -> otherElf.x == targetX && otherElf.y == targetY }
            }
            if (canMove) {
                var targetMovePos = Pair(elf.x + dirPoints[1].first, elf.y + dirPoints[1].second)
                plannedMoveMap.putIfAbsent(targetMovePos, mutableListOf())
                plannedMoveMap[targetMovePos]!!.add(elf)
                elf.action = order
                break
            }
        }
    }
    return plannedMoveMap
}

private fun simulateRounds(elfList: MutableList<Elf>, rounds: Int) {
    var currentRound = 0
    while(currentRound < rounds){
        currentRound++
        var elvesMovedCount = 0
        markStillElves(elfList)
        var plannedMoves = planElvesMoves(elfList, directionQueue)
        plannedMoves.forEach { (position, elves) ->
            if (elves.size == 1) {
                elves[0].x = position.first
                elves[0].y = position.second
                elvesMovedCount++
            }
        }
        if(elvesMovedCount == 0) break
        directionQueue.addLast(directionQueue.removeFirst())
    }
    var width = abs(elfList.maxOf { e -> e.x } - elfList.minOf { e -> e.x } + 1)
    var height = abs(elfList.maxOf { e -> e.y } - elfList.minOf { e -> e.y } + 1)
    println("Empty ground tiles ${(width * height) - elfList.size}")
    println("Round when no elf moved ${currentRound}")
}


private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val elfList: MutableList<Elf> = mutableListOf()
    directionQueue = ArrayDeque(mutableListOf("north", "south", "west", "east"))
    inputLines.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (char == '#') elfList.add(Elf(x, y))
        }
    }
    simulateRounds(elfList, 10000)
}

fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}
