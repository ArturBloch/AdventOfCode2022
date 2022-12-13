package aoc.day4
// DAY 4

private var testFile: String = "input/day4_test.txt";
private var inputFile: String = "input/day4_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()


private fun findDayFourSolutions(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    findOneStarSolution(givenInput)
    findTwoStarSolution(givenInput)
}

private fun assignmentFullOverlap(firstAssignment: List<Int>, secondAssignment: List<Int>): Boolean {
    return (firstAssignment[0] <= secondAssignment[0] && firstAssignment[1] >= secondAssignment[1])
}

private fun assignmentPartialOverlap(firstAssignment: List<Int>, secondAssignment: List<Int>): Boolean {
    return (firstAssignment[0] >= secondAssignment[0] && firstAssignment[0] <= secondAssignment[1]) ||
            (firstAssignment[1] >= secondAssignment[0] && firstAssignment[1] <= secondAssignment[1])
}

private fun findTwoStarSolution(givenInput: List<String>) {
    println(givenInput.count { line ->
        var elfAssignments = line.split(",")
        var firstElf = elfAssignments[0].split("-").map { e -> Integer.parseInt(e) }
        var secondElf = elfAssignments[1].split("-").map { e -> Integer.parseInt(e) }
        assignmentPartialOverlap(firstElf, secondElf) || assignmentPartialOverlap(secondElf, firstElf)
    })
}

private fun findOneStarSolution(givenInput: List<String>) {
    println(givenInput.count { line ->
        var elfAssignments = line.split(",")
        var firstElf = elfAssignments[0].split("-").map { e -> Integer.parseInt(e) }
        var secondElf = elfAssignments[1].split("-").map { e -> Integer.parseInt(e) }
        assignmentFullOverlap(firstElf, secondElf) || assignmentFullOverlap(secondElf, firstElf)
    })
}

fun main() {
    findDayFourSolutions(testFile)
    findDayFourSolutions(inputFile)
}

