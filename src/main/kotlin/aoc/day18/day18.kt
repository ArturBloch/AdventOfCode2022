package aoc.day18

import kotlin.math.abs

// DAY 18

private var testFile: String = "input/day18_test.txt";
private var inputFile: String = "input/day18_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private data class Cube(val x: Long, val y: Long, val z: Long, var isInside: Boolean? = false) {}

private data class Pos3D(val x: Long, val y: Long, val z: Long) {}

private fun viableEmptyAir(x: Long, y: Long, z: Long, cubeList: List<Cube>): Boolean {
    return x >= -1 && y >= -1 && z >= -1 && x <= 25 && y <= 25 && z <= 25 && cubeList.none { cube -> x == cube.x && y == cube.y && z == cube.z }
}

private val neighbourList =
    mutableListOf(Pos3D(0, 0, 1), Pos3D(0, 1, 0), Pos3D(1, 0, 0), Pos3D(0, 0, -1), Pos3D(0, -1, -0), Pos3D(-1, 0, 0))

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}

private fun sideCovered(firstCube: Cube, secondCube: Cube): Boolean {
    var distance = abs(firstCube.x - secondCube.x) + abs(firstCube.y - secondCube.y) + abs(firstCube.z - secondCube.z)
    return distance == 1L
}

private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val cubes: List<Cube> = inputLines.map { line ->
        val splitLine = line.split(",")
        Cube(splitLine[0].toLong(), splitLine[1].toLong(), splitLine[2].toLong())
    }
    val coveredSides = cubes.sumOf { cube1 ->
        var coveredSides: Long = cubes.sumOf { cube2 ->
            if (cube1 == cube2) 0
            if (sideCovered(cube1, cube2)) 1L else 0
        }
        coveredSides
    }
    val cubesAllSides = cubes.size * 6
    println("Cubes all sides $cubesAllSides and covered $coveredSides results in ${cubesAllSides - coveredSides} area")
}

private fun floodfill(cubes: List<Cube>): Long {
    var touchedSides = 0L
    var cubesToProcess = ArrayDeque<Cube>()
    var visitedNodes = mutableListOf<Cube>()
    var starterAirCube = Cube(-1, -1, -1)
    cubesToProcess.add(starterAirCube)
    visitedNodes.add(starterAirCube)
    while (!cubesToProcess.isEmpty()) {
        val currentAirCube = cubesToProcess.removeFirst()
        touchedSides += cubes.sumOf { lavaCube ->
            if(sideCovered(currentAirCube, lavaCube)) 1L else 0
        }
        neighbourList.forEach { neighbour ->
            var newAirCube = Cube(currentAirCube.x + neighbour.x, currentAirCube.y + neighbour.y, currentAirCube.z + neighbour.z)
            if(viableEmptyAir(newAirCube.x, newAirCube.y, newAirCube.z, cubes) && !visitedNodes.contains(newAirCube)) {
                cubesToProcess.add(newAirCube)
                visitedNodes.add(newAirCube)
            }
        }
    }
    return touchedSides
}

private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val cubes: List<Cube> = inputLines.map { line ->
        val splitLine = line.split(",")
        Cube(splitLine[0].toLong(), splitLine[1].toLong(), splitLine[2].toLong())
    }
    println("External surface is ${floodfill(cubes)}")
}

fun main() {
//    findSolutions(testFile)
    findSolutions(inputFile)
}
