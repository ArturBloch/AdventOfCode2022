package aoc.day8
// DAY 8

private var testFile: String = "input/day8_test.txt";
private var inputFile: String = "input/day8_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()

private fun findOneStarSolution(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val treeRowSize = givenInput[0].length
    val treeColumnSize = givenInput.size
    var visibleTrees = 0
    givenInput.forEachIndexed { y, treeRow ->
        treeRow.forEachIndexed inner@{ x, tree ->
            if (x == 0 || y == 0 || (x == treeRowSize - 1) || (y == treeColumnSize - 1)) {
                visibleTrees++
                return@inner
            }
            val currentTreeSize = givenInput[y][x].digitToInt()
            if (allTreesSmallerThan(givenInput, currentTreeSize, (x - 1 downTo 0).map { newX -> Pair(y, newX) }.toList())){
                visibleTrees++
                return@inner
            }
            if(allTreesSmallerThan(givenInput, currentTreeSize, (x + 1 until treeRowSize).map { newX -> Pair(y, newX) }.toList())){
                visibleTrees++
                return@inner
            }
            if(allTreesSmallerThan(givenInput, currentTreeSize, (y - 1 downTo 0).map { newY -> Pair(newY, x) }.toList())){
                visibleTrees++
                return@inner
            }
            if(allTreesSmallerThan(givenInput, currentTreeSize, (y + 1 until treeColumnSize).map { newY -> Pair(newY, x) }.toList())){
                visibleTrees++
                return@inner
            }
        }

    }
    println(visibleTrees)
}

private fun allTreesSmallerThan(givenInput: List<String>, targetTreeSize: Int, treeTargetPositions: List<Pair<Int, Int>>): Boolean {
    return treeTargetPositions.all { (y, x) -> givenInput[y][x].digitToInt() < targetTreeSize }
}

private fun countTreeScore(
    givenInput: List<String>, treeIndexes: List<Int>, currentTree: Pair<Int, Int>, isVertical: Boolean
): Int {
    var currentTreeSize = Integer.parseInt(givenInput[currentTree.first][currentTree.second].toString())
    var treeCount = 0
    for (treeIndex in treeIndexes) {
        val targetTree =
            Integer.parseInt(if (!isVertical) givenInput[currentTree.first][treeIndex].toString() else givenInput[treeIndex][currentTree.second].toString())
        treeCount++
        if (targetTree >= currentTreeSize) break
    }
    return treeCount
}

private fun findTwoStarSolution(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val treeRowSize = givenInput[0].length
    val treeColumnSize = givenInput.size
    var maxScoreSoFar = 0
    givenInput.forEachIndexed { y, treeRow ->
        treeRow.forEachIndexed inner@{ x, _ ->
            if (x == 0 || y == 0 || (x == treeRowSize - 1) || (y == treeColumnSize - 1)) {
                return@inner
            }
            val treeScore =
                countTreeScore(givenInput, (x - 1 downTo 0).toList(), Pair(y, x), false) *
                        countTreeScore(givenInput, (x + 1 until treeRowSize).toList(), Pair(y, x), false) *
                        countTreeScore(givenInput, (y - 1 downTo 0).toList(), Pair(y, x), true) *
                        countTreeScore(givenInput, (y + 1 until treeColumnSize).toList(), Pair(y, x), true)
            if (treeScore > maxScoreSoFar) {
                maxScoreSoFar = treeScore
                println("Max score: $maxScoreSoFar x=$x y=$y")
            }
        }

    }
    println("Max score: $maxScoreSoFar")
}

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}


fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}

