package aoc.day21

private var testFile: String = "input/day21_test.txt";
private var inputFile: String = "input/day21_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): MutableList<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines() as MutableList<String>

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}

private data class Operation(val valueLeft: String, val valueRight: String? = null, val operator: String? = null){
    fun getOperationResult(valueMap: Map<String, Operation>): Long{
        return when(operator){
            "+" -> valueMap[valueLeft]!!.getOperationResult(valueMap) + valueMap[valueRight]!!.getOperationResult(valueMap)
            "-" -> valueMap[valueLeft]!!.getOperationResult(valueMap) - valueMap[valueRight]!!.getOperationResult(valueMap)
            "*" -> valueMap[valueLeft]!!.getOperationResult(valueMap) * valueMap[valueRight]!!.getOperationResult(valueMap)
            "/" -> valueMap[valueLeft]!!.getOperationResult(valueMap) / valueMap[valueRight]!!.getOperationResult(valueMap)
            "=" -> if(valueMap[valueLeft]!!.getOperationResult(valueMap) == valueMap[valueRight]!!.getOperationResult(valueMap)) 1L else 0L
            else -> valueLeft.toLong()
        }
    }
}

private fun reversedOperation(leftNumber: Long, rightNumber:Long, operator:String): Long{
    return when(operator){
        "+" -> leftNumber - rightNumber
        "-" -> leftNumber + rightNumber
        "*" -> leftNumber / rightNumber
        "/" -> leftNumber * rightNumber
        else -> leftNumber.toLong()
    }
}

private fun findOneStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    var operationMap = inputLines.associate { line ->
        val splitLine = line.split(" ")
        if(splitLine.size == 4){
            val operation = Operation(splitLine[1], splitLine[3], splitLine[2])
            Pair(line.substring(0, 4), operation)
        } else {
            val operation = Operation(splitLine[1])
            Pair(line.substring(0, 4), operation)
        }
    }
    println("The result of root is ${operationMap["root"]!!.getOperationResult(operationMap)}")
}

private fun findTwoStarSolution(givenFile: String) {
    val inputLines: List<String> = readFileLineByLineUsingForEachLine(givenFile)

}

fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}
