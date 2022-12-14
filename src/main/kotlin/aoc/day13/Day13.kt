package aoc.day13
// DAY 13

private var testFile: String = "input/day13_test.txt";
private var inputFile: String = "input/day13_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private data class Node(
    val intValue: Int? = null,
    var nodeList: MutableList<Node>? = null,
    var nextPacket: Node? = null
) {
    fun isEmpty(): Boolean {
        return intValue == null && nodeList == null
    }
}

// [1,[2,[3,[4,[5,6,7]]]],8,9]

private fun parseLine(stringToParse: String, starterIndex: Int): Pair<Int, Node> {
    var currentIndex = starterIndex + 1
    var freshNode: Node = Node()
    var integerCreation: String = ""
    while (currentIndex < stringToParse.length && stringToParse[currentIndex] != ']') {
        when (stringToParse[currentIndex]) {
            ',' -> {
                if (freshNode.nodeList == null) freshNode.nodeList = mutableListOf()
                if (integerCreation != "") {
                    freshNode.nodeList?.add(Node(integerCreation.toInt()))
                    integerCreation = ""
                }
                currentIndex++
            }

            '[' -> {
                val (newIndex, childNode) = parseLine(stringToParse, currentIndex)
                currentIndex = newIndex
                if (freshNode.nodeList == null) freshNode.nodeList = mutableListOf()
                freshNode.nodeList?.add(childNode)
            }

            else -> {
                integerCreation = integerCreation.plus(stringToParse[currentIndex])
                currentIndex++
            }
        }
    }
    if (integerCreation != "") {
        if (freshNode.nodeList == null) freshNode.nodeList = mutableListOf()
        freshNode.nodeList?.add(Node(integerCreation.toInt()))
    }
    currentIndex++
    return Pair(currentIndex, freshNode)
}

private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    var index = 1
    var sum = 0
    inputLines.windowed(2, 3) { line ->
        var result = compareNodes(parseLine(line[0], 0).second, parseLine(line[1], 0).second)
        if (result == packetState.IN_ORDER || result == packetState.UNKNOWN) {
            sum += index
            println("Add index to sum $index")
        }
        index++
    }
    println("SUM :) $sum")
}

enum class packetState {
    IN_ORDER, OUT_OF_ORDER, UNKNOWN;
}

private fun compareIntegers(value1: Int, value2: Int): packetState {
    if (value1 < value2) {
        return packetState.IN_ORDER
    };
    if (value1 > value2) {
        return packetState.OUT_OF_ORDER
    };
    return packetState.UNKNOWN
}


private fun compareLists(leftList: MutableList<Node>, rightList: MutableList<Node>): packetState {
    for (i in 0 until leftList.size) {
        if (i >= rightList.size) {
            return packetState.OUT_OF_ORDER
        }
        var state = compareNodes(leftList[i], rightList[i])
        if (state != packetState.UNKNOWN) {
            return state
        }
    }
    if (leftList.size < rightList.size) return packetState.IN_ORDER
    return packetState.UNKNOWN
}

private fun compareNodes(leftPacket: Node, rightPacket: Node): packetState {
    if (leftPacket.intValue != null && rightPacket.intValue != null) return compareIntegers(
        leftPacket.intValue,
        rightPacket.intValue
    )
    if (leftPacket.nodeList != null && rightPacket.nodeList != null) return compareLists(
        leftPacket.nodeList!!,
        rightPacket.nodeList!!
    )

    if (leftPacket.intValue != null && rightPacket.nodeList != null) {
        return compareNodes(Node(null, mutableListOf(Node(leftPacket.intValue, null))), rightPacket)
    }
    if (rightPacket.intValue != null && leftPacket.nodeList != null) {
        return compareNodes(leftPacket, Node(null, mutableListOf(Node(rightPacket.intValue, null))))
    }
    if ((leftPacket.nodeList != null || leftPacket.intValue != null) && rightPacket.isEmpty()) {
        return packetState.OUT_OF_ORDER
    }; // right less items than left
    if ((rightPacket.nodeList != null || rightPacket.intValue != null) && leftPacket.isEmpty()) {
        return packetState.IN_ORDER
    }; // left more items than right
    return packetState.UNKNOWN
}


private fun findTwoStarSolution(givenFile: String) {
    val inputLines: MutableList<String> = readFileLineByLineUsingForEachLine(givenFile)
    inputLines.removeIf { line -> line.isEmpty() }
    inputLines.sortWith{ e1, e2 -> if (compareNodes( parseLine(e1, 0).second,  parseLine(e2, 0).second) == packetState.IN_ORDER) -1 else 1 }
    println((inputLines.indexOf("[[2]]") + 1) * (inputLines.indexOf("[[6]]") + 1))
}

private fun findSolutions(givenFile: String) {
//    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}


fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}

