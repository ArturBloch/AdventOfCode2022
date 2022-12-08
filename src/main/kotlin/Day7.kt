// DAY 7

private var testFile: String = "day7_test.txt";
private var inputFile: String = "day7_input.txt";
private var lineIndex: Int = 0
private var sumOfOneStar: Int = 0
private var totalDiskSpaceAvailable = 70000000
private var unusedSpaceNeeded = 30000000
private var directoryListSizes = mutableListOf<Int>()

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()


private fun findSolutions(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    findSolution(givenInput)
}

private fun findSolution(givenInput: List<String>) {
    lineIndex = 0
    sumOfOneStar = 0
    val homeDirectorySize = newDirectory(givenInput)
    val spaceLeft = totalDiskSpaceAvailable - homeDirectorySize
    val smallestDir = directoryListSizes.filter { directorySize ->  spaceLeft + directorySize >= unusedSpaceNeeded }.min()
    println("First star solution $sumOfOneStar")
    println("Second star solution - smallest dir to delete: $smallestDir")
}

private fun newDirectory(givenInput: List<String>): Int{
    var directorySize = 0
    while(lineIndex < givenInput.size && !givenInput[lineIndex].contains("cd ..")){
        println(givenInput[lineIndex])
        if(givenInput[lineIndex].contains("cd ")){
            lineIndex++
            directorySize += newDirectory(givenInput)
        } else if(givenInput[lineIndex][0].isDigit()){
            directorySize += Integer.parseInt(givenInput[lineIndex].split(" ")[0])
        }
        lineIndex++
    }
    println("DIRECTORY SIZE $directorySize")
    if(directorySize <= 100000) sumOfOneStar += directorySize
    directoryListSizes += directorySize
    return directorySize
}

fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}

