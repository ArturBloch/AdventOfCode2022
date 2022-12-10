import java.lang.Math.abs

// DAY 10

private var testFile: String = "day10_test.txt";
private var inputFile: String = "day10_input.txt";

private fun readFileLineByLineUsingForEachLine(givenFile: String): List<String> =
    object {}.javaClass.getResourceAsStream(givenFile)!!.bufferedReader().readLines()

private fun calculateRegisterValues(givenInput: List<String>): IntArray {
    val signalTicks: IntArray = IntArray(givenInput.size * 3)
    signalTicks[0] = 1
    var currentCycle = 0
    givenInput.forEachIndexed { index, lineInput ->
        signalTicks[currentCycle + 1] = signalTicks[currentCycle]
        if (lineInput == "noop") {
        } else {
            signalTicks[currentCycle + 2] = signalTicks[currentCycle + 1] + lineInput.split(" ")[1].toInt()
            currentCycle += 1
        }
        currentCycle++
    }
    return signalTicks
}

private fun findOneStarSolution(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val registerValues = calculateRegisterValues(givenInput)

    println(
        "First star answer ${
            (20..220 step 40).sumOf { index ->
                index * registerValues[index - 1]
            }
        }")
}


private fun findTwoStarSolution(givenFile: String) {
    val givenInput: List<String> = readFileLineByLineUsingForEachLine(givenFile)
    val registerValues = calculateRegisterValues(givenInput)

    for (pixelRow in 0..5) {
        println()
        for (pixelX in 0..39) {
            var currentPixel = pixelRow * 40 + pixelX
            print(if (kotlin.math.abs(pixelX - registerValues[currentPixel]) <= 1) "#" else " ")
            print(" ")
        }
    }
    println()
}

private fun findSolutions(givenFile: String) {
    findOneStarSolution(givenFile)
    findTwoStarSolution(givenFile)
}


fun main() {
    findSolutions(testFile)
    findSolutions(inputFile)
}

