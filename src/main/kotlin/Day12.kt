import java.util.PriorityQueue

// DAY 12

private var testFile: String = "day12_test.txt";
private var inputFile: String = "day12_input.txt";
private val END_CHAR = 'E'
private val START_STRING = "S"
private val stepsNeeded = "Sabcdefghijklmnopqrstuvwxyz"

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()

private data class Node(var x: Int, var y: Int, var cost: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

val neighbours: List<Pair<Int, Int>> = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))


private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val starterRow = inputLines.indexOfFirst { line -> line.contains(START_STRING) }
    findShortestPath(inputLines, inputLines[starterRow].indexOf(START_STRING), starterRow)
}

private fun findShortestPath(inputLines: List<String>, startX: Int, startY: Int): Int? {
    val visited = Array(inputLines.size) { BooleanArray(inputLines[0].length) }
    val nodesToProcess: PriorityQueue<Node> = PriorityQueue<Node> { n1, n2 ->
        n1.cost.compareTo(n2.cost)
    }
    nodesToProcess.add(Node(startX, startY, 0))
    while (!nodesToProcess.isEmpty()) {
        val currentNode: Node = nodesToProcess.remove()
        if (visited[currentNode.y][currentNode.x]) continue
        visited[currentNode.y][currentNode.x] = true
        if (inputLines[currentNode.y][currentNode.x] == END_CHAR) {
            println("Done $currentNode")
            return currentNode.cost
        }
        neighbours.forEach { pair ->
            val newX = currentNode.x + pair.second
            val newY = currentNode.y + pair.first
            if (newX >= 0 && newX < inputLines[0].length && newY >= 0 && newY < inputLines.size && !visited[newY][newX]) {
                var currentHeight = stepsNeeded.indexOf(inputLines[currentNode.y][currentNode.x])
                val targetHeight =
                    if (inputLines[newY][newX] == 'E') stepsNeeded.indexOf('z') else stepsNeeded.indexOf(inputLines[newY][newX])
                var node = Node(newX, newY, currentNode.cost + 1)
                if (targetHeight <= currentHeight + 1) {
                    nodesToProcess.add(node)
                }
            }
        }
    }
    return null
}

private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val result = inputLines.flatMapIndexed { y, s ->
        s.mapIndexed { x, height ->
            if (height == 'a') findShortestPath(inputLines, x, y) else null
        }
    }.filterNotNull().min()
    println(result)

}

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}


fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}

