package aoc.day20

private var testFile: String = "input/day20_test.txt";
private var inputFile: String = "input/day20_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}

private data class Node(val initialPosition: Int, var value:Long){}

private fun MutableList<Node>.decrypt() {
    indices.forEach { initialIndex ->
        val index = indexOfFirst { it.initialPosition == initialIndex }
        val removedIndex = removeAt(index)
        add((index + removedIndex.value).mod(size), removedIndex)
    }
}

private fun findOneStarSolution(givenFile: String) {
    val inputNumbers: MutableList<Node> = readFileLineByLineUsingForEachLine(givenFile).mapIndexed { index, value ->
        Node(index, value.toLong())
    }.toMutableList()
    inputNumbers.decrypt()
    var zeroIndex = inputNumbers.indexOfFirst { node -> node.value == 0L }
    var sum = 0L
    for (i in 0..3000){
        var currentIndex = (zeroIndex + i) % inputNumbers.size
        if(i % 1000 == 0) sum +=inputNumbers[currentIndex].value
    }
    println("Answer one star $sum")
}

private fun findTwoStarSolution(givenFile: String) {
    val inputNumbers: MutableList<Node> = readFileLineByLineUsingForEachLine(givenFile).mapIndexed { index, value ->
        Node(index, value.toLong() * 811589153L)
    }.toMutableList()
    (1 .. 10).forEach { _ -> inputNumbers.decrypt() }
    var zeroIndex = inputNumbers.indexOfFirst { node -> node.value == 0L }
    var sum = 0L
    for (i in 0..3000){
        var currentIndex = (zeroIndex + i) % inputNumbers.size
        if(i % 1000 == 0) sum +=inputNumbers[currentIndex].value
    }
    println("Answer two star $sum")

}

fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}
