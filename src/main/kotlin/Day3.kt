// DAY 3

private var testFile: String = "day3_test.txt";
private var inputFile: String = "day3_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> = object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()


private fun findDayThreeSolutions(givenFile: String){
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    findOneStarSolution(givenInput)
    findTwoStarSolution(givenInput)
}

private fun charIntoScore(givenChar: Char): Int{
    var score = givenChar.code % 96
    if(score > 26) score -= 38;
    return score;
}

private fun findTwoStarSolution(givenInput: List<String>){
    println(givenInput.windowed(3, 3).sumOf{
            backpacks ->
        var result = backpacks[0].first() { char -> backpacks[1].contains(char) && backpacks[2].contains(char) }
        charIntoScore(result)
    })
}

private fun findOneStarSolution(givenInput: List<String>){
    println(givenInput. sumOf{
        backpack ->
        var firstSide: List<Char> = backpack.substring(0, backpack.length / 2).toCharArray().sorted()
        var secondSide: List<Char> = backpack.substring(backpack.length / 2, backpack.length).toCharArray().sorted()
        var result = firstSide.first() { char -> secondSide.contains(char) }
        charIntoScore(result)
    })
}

fun main() {
    findDayThreeSolutions(testFile)
    findDayThreeSolutions(inputFile)
}

