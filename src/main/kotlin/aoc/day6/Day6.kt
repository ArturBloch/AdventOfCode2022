package aoc.day6
// DAY 6

private var testFile: String = "input/day6_test.txt";
private var inputFile: String = "input/day6_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()


private fun findDaySixolutions(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    findOneStarSolution(givenInput)
    findTwoStarSolution(givenInput)
}

private fun findUniqueCharSequence(givenString: String, length: Int): Int{
    var signalFound = false
    var index = length
    while(!signalFound){
        var charSet = givenString.substring(index - length, index).toHashSet()
        if(charSet.size == length) break
        index++
    }
    return index
}

private fun findTwoStarSolution(givenInput: List<String>) {
    println(findUniqueCharSequence(givenInput[0], 14))
}

private fun findOneStarSolution(givenInput: List<String>) {
    println(findUniqueCharSequence(givenInput[0], 4))
}

fun main() {
    findDaySixolutions(testFile)
    findDaySixolutions(inputFile)
}

