// DAY 5

private var testFile: String = "day5_test.txt";
private var inputFile: String = "day5_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()

private fun getIndexesOfBoxes(givenLine: String): List<Int> {
    return Regex("[A-Z]").findAll(givenLine).map { it.range.first }.toList()
}

private fun getBoxStacks(givenInput: List<String>, boxFloorIndex: Int): Array<ArrayDeque<Char>> {
    val boxFloor = givenInput[boxFloorIndex]
    val numberOfStacks = Integer.parseInt(givenInput[boxFloorIndex].last { e -> e.isDigit() }.toString())
    val boxStacks: Array<ArrayDeque<Char>> = Array(numberOfStacks + 1) { ArrayDeque() }
    val boxConfiguration = givenInput.subList(0, boxFloorIndex)
    boxConfiguration.reversed().forEach { line ->
        getIndexesOfBoxes(line).forEach() { boxIndex ->
            boxStacks[Integer.parseInt(boxFloor[boxIndex].toString())].addLast(line[boxIndex])
        }
    }
    return boxStacks
}

private fun getMoveDetails(lineCommand: String): Triple<Int, Int, Int> {
    val result = Regex("move (\\d+) from (\\d+) to (\\d+)").find(lineCommand)
    val moveCount = Integer.parseInt(result!!.groupValues[1])
    val fromStack = Integer.parseInt(result!!.groupValues[2])
    val toStack = Integer.parseInt(result!!.groupValues[3])
    return Triple(moveCount, fromStack, toStack)
}

private fun findDayFiveSolutions(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    findOneStarSolution(givenInput)
    findTwoStarSolution(givenInput)
}

private fun findTwoStarSolution(givenInput: List<String>) {
    val boxFloorIndex = givenInput.indexOfFirst { e -> e == "" } - 1
    val boxStacks = getBoxStacks(givenInput, boxFloorIndex)
    val moveCommands = givenInput.subList(boxFloorIndex + 2, givenInput.size)
    moveCommands.forEach { lineCommand ->
        val (moveCount, fromStack, toStack) = getMoveDetails(lineCommand)
        val tempList: ArrayDeque<Char> = ArrayDeque()
        for (i in 1..moveCount) {
            tempList.addFirst(boxStacks[fromStack].removeLast())
        }
        boxStacks[toStack].addAll(tempList)
    }
    println(boxStacks.joinToString(separator = "") { stack -> if (stack.isEmpty()) "" else stack.last().toString() })
}

private fun findOneStarSolution(givenInput: List<String>) {
    val boxFloorIndex = givenInput.indexOfFirst { e -> e == "" } - 1
    val boxStacks = getBoxStacks(givenInput, boxFloorIndex)
    val moveCommands = givenInput.subList(boxFloorIndex + 2, givenInput.size)
    moveCommands.forEach { lineCommand ->
        val (moveCount, fromStack, toStack) = getMoveDetails(lineCommand)
        for (i in 1..moveCount) {
            boxStacks[toStack].addLast(boxStacks[fromStack].removeLast())
        }
    }
    println(boxStacks.joinToString(separator = "") { stack -> if (stack.isEmpty()) "" else stack.last().toString() })
}

fun main() {
    findDayFiveSolutions(testFile)
    findDayFiveSolutions(inputFile)
}

