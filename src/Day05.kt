import java.util.Stack

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>): String {
    val (stacks: Map<Int, CrateStack>, instructions: List<Instruction>) = parseInput(input)

    instructions.forEach { handlePart1Instruction(it, stacks) }
    return printResult(stacks)
}

private fun part2(input: List<String>): String {
    val (stacks: Map<Int, CrateStack>, instructions: List<Instruction>) = parseInput(input)

    instructions.forEach { handlePart2Instruction(it, stacks) }
    return printResult(stacks)
}

private fun handlePart1Instruction(it: Instruction, inputStacks: Map<Int, CrateStack>) {
    val originStack = inputStacks.getValue(it.origin)
    val destinationStack = inputStacks.getValue(it.destination)

    (1..it.amount).forEach { _ -> destinationStack.push(originStack.pop()) }
}

private fun handlePart2Instruction(it: Instruction, inputStacks: Map<Int, CrateStack>) {
    val originStack = inputStacks.getValue(it.origin)
    val destinationStack = inputStacks.getValue(it.destination)

    (1..it.amount)
        .map { originStack.pop() }
        .reversed()
        .forEach { destinationStack.push(it) }
}

fun parseInput(input: List<String>): Pair<Map<Int, CrateStack>, List<Instruction>> {
    val stacks = parseStacks(input.subList(0, input.indexOf("") - 1))
    val instructions = parseInstructions(input.subList(input.indexOf("") + 1, input.size))

    return stacks to instructions
}

fun parseStacks(stacksInput: List<String>): Map<Int, CrateStack> {
    val numberOfStacks = stacksInput[stacksInput.size - 1].split(" ").size
    val stacksMap = (1..numberOfStacks).associateBy({ it }, { CrateStack() })

    stacksInput.map { line ->
        line.chunked(4)
            .forEachIndexed { index, item ->
                item[1].takeIf { item.isNotBlank() }?.let { stacksMap[index + 1]!!.add(0, it) }
            }
    }

    return stacksMap
}

fun parseInstructions(instructionsInput: List<String>): List<Instruction> {
    return instructionsInput
        .map { it.split("move ", " from ", " to ") }
        .map { Instruction(origin = it[2].toInt(), destination = it[3].toInt(), amount = it[1].toInt()) }
}

private fun printResult(stacks: Map<Int, CrateStack>) =
    stacks.entries.sortedBy { it.key }.map { it.value.peek() }.joinToString("") { it.toString() }

typealias Crate = Char

typealias CrateStack = Stack<Crate>

//data class CrateStack(val index: Int, val crates: Stack<Crate> = Stack())

data class Instruction(val origin: Int, val destination: Int, val amount: Int)
