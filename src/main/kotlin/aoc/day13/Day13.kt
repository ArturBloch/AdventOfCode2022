package aoc.day13
// DAY 13

private var testFile: String = "input/day13_test.txt";
private var inputFile: String = "input/day13_input.txt";
private var currentLineIndex = 0

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()

private data class Node(val intValue: Int?, var nodeList: List<Node>?) {}

private fun parseLine(line: String): Node? {
    return null
}

private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    inputLines.forEach { line ->
        parseLine(line)
    }
}


private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
}

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}


fun main() {
    findSolutions(testFile)
//    findSolutions(inputFile)
}

