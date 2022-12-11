// DAY 11

private var testFile: String = "day11_test.txt";
private var inputFile: String = "day11_input.txt";
private var magicNumber: Long = 0

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readText().split("\r\n\r\n")

private data class Monkey(
    var items: MutableList<Long>,
    var divisibleBy: Long,
    var throwToFalse: Int,
    var throwToTrue: Int,
    var operation: String,
    var operationInteger: Long?,
    var inspectedCount: Long = 0L
) {

}

private fun simulateMonkeys(rounds: Int, monkeys: List<Monkey>) {
    for (i in 0 until rounds) {
        monkeys.forEachIndexed { index, monkey ->
            monkey.items.map { item ->
                val targetModifier = monkey.operationInteger ?: item
                var newValue = if (monkey.operation == "*") item.times(targetModifier) else item.plus(targetModifier)
                newValue %= magicNumber
                newValue
            }.forEach { item ->
                monkey.inspectedCount++
                if ((item % monkey.divisibleBy) == 0L) {
                    monkeys[monkey.throwToTrue].items.add(item)
                } else {
                    monkeys[monkey.throwToFalse].items.add(item)
                }
            }
            monkey.items.clear()
        }
    }
}

private fun findSolution(givenFile: String) {
    val inputPerMonkey: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    var monkeyList: MutableList<Monkey> = mutableListOf()
    inputPerMonkey.forEach { monkeyInput ->
        println(monkeyInput)
        val splitMonkeyInput = monkeyInput.lines()
        val operation = if (splitMonkeyInput[2].contains("*")) "*" else "+"
        val operationInteger =
            Regex("(\\d+)").findAll(splitMonkeyInput[2]).map { it.value.toLong() }.toList().firstOrNull()
        val items = Regex("(\\d+)").findAll(splitMonkeyInput[1]).map { it.value.toLong() }.toMutableList()
        val divisibleBy = Regex("(\\d+)").findAll(splitMonkeyInput[3]).map { it.value.toLong() }.toList().first()
        val throwToTrue = Regex("(\\d+)").findAll(splitMonkeyInput[4]).map { it.value.toInt() }.toList().first()
        val throwToFalse = Regex("(\\d+)").findAll(splitMonkeyInput[5]).map { it.value.toInt() }.toList().first()
        monkeyList.add(Monkey(items, divisibleBy, throwToFalse, throwToTrue, operation, operationInteger))
    }
    magicNumber = monkeyList.map { it.divisibleBy }.reduce{acc, divisibleBy -> acc * divisibleBy}
    simulateMonkeys(10000, monkeyList)
    println(monkeyList.map { e -> e.inspectedCount }.sortedByDescending{ it }.take(2).reduce { acc, i -> acc * i })
}


private fun findSolutions(givenFile: String) {
    findSolution(givenFile)
}


fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}

