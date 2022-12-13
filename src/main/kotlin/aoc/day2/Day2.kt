package aoc.day2
import java.lang.IllegalArgumentException

// DAY 2

private var testFile: String = "input/day2_test.txt";
private var inputFile: String = "input/day2_input.txt";

private val gameMap: Map<String, Int> = hashMapOf(
    "AX" to 3, "AY" to 6, "AZ" to 0, "BX" to 0, "BY" to 3, "BZ" to 6, "CX" to 6, "CY" to 0, "CZ" to 3
)

private val myMoves: List<String> = mutableListOf("X", "Y", "Z")

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String>? =
    object {}.javaClass.getResourceAsStream(givenFile)?.bufferedReader()?.readLines()

private fun findNecessaryMove(expectedScore: Int, enemyMove: String): String {
    return myMoves.first { myMove -> gameMap["$enemyMove$myMove"] == expectedScore }
}

private fun getScoreForMove(myMove: String): Int {
    return when (myMove) {
        "X" -> 1
        "Y" -> 2
        "Z" -> 3
        else -> 0
    }
}

private fun getExpectedScore(expectedResult: String): Int {
    return when (expectedResult) {
        "X" -> 0
        "Y" -> 3
        "Z" -> 6
        else -> throw IllegalArgumentException("There is no result like that :)")
    }
}

private fun getScoreForOneRound(roundInput: String): Int {
    val actionsPlayed = roundInput.split(" ")
    val enemyMove = actionsPlayed[0]
    val myMove = actionsPlayed[1]
    return getScoreForMove(myMove) + gameMap["$enemyMove$myMove"]!!
}

private fun findDayTwoFirstStar(givenFile: String) {
    val givenInput: List<String>? = readFileLineByLineUsingForEachLine(givenFile)
    println(givenInput?.sumOf { roundInput -> getScoreForOneRound(roundInput) })
}

private fun findDayTwoSecondStar(givenFile: String) {
    val givenInput: List<String>? = readFileLineByLineUsingForEachLine(givenFile)
    println(givenInput?.sumOf { roundInput ->
        val actionsPlayed = roundInput.split(" ")
        val enemyMove = actionsPlayed[0]
        val expectedResult = actionsPlayed[1]
        val myMove = findNecessaryMove(getExpectedScore(expectedResult), enemyMove)
        getScoreForOneRound("$enemyMove $myMove")
    })
}

private fun findDayTwoSolution(givenFile: String) {
    findDayTwoFirstStar(givenFile)
    findDayTwoSecondStar(givenFile)
}

fun main() {
    findDayTwoSolution(testFile)
    findDayTwoSolution(inputFile)
}
