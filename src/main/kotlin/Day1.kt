// DAY 1

private var testFile: String = "day1_test.txt";
private var inputFile: String = "day1_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String>? = object {}.javaClass.getResourceAsStream(givenFile)?.bufferedReader()?.readLines()
private fun countElfCalories(fileLines: List<String>?): MutableList<Int>{
    var elfCalories: MutableList<Int> = mutableListOf()
    var currentCalories = 0;
    fileLines?.forEach{
        line ->
        if(line == ""){
            elfCalories.add(currentCalories)
            currentCalories = 0;
        } else {
            currentCalories += Integer.parseInt(line)
        }
    }
    if(currentCalories != 0) elfCalories.add(currentCalories)
    return elfCalories
}

private fun findDayOneSolutions(givenFile: String){
    val givenInput: List<String>? = readFileLineByLineUsingForEachLine(givenFile)
    val result = countElfCalories(givenInput)
    println(result.sortedDescending().take(3).sum())
}

fun main() {
    findDayOneSolutions(testFile)
    findDayOneSolutions(inputFile)
}

