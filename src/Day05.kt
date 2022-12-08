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
    val (stacks: Map<Int, Stack>, instructions: List<Instruction>) = parseInput(input)

    instructions.forEach { applyPart1Instruction(it, stacks) }
    return printResult(stacks)
}

private fun part2(input: List<String>): String {
    val (stacks: Map<Int, Stack>, instructions: List<Instruction>) = parseInput(input)

    instructions.forEach { applyPart2Instruction(it, stacks) }
    return printResult(stacks)
}

private fun applyPart1Instruction(instruction: Instruction, stacks: Map<Int, Stack>) {
    val originStack = stacks.getValue(instruction.origin)
    val destinationStack = stacks.getValue(instruction.destination)

    repeat(instruction.amount) { destinationStack.addFirst(originStack.removeFirst()) }
}

private fun applyPart2Instruction(instruction: Instruction, stacks: Map<Int, Stack>) {
    val originStack = stacks.getValue(instruction.origin)
    val destinationStack = stacks.getValue(instruction.destination)

    (1..instruction.amount)
        .map { originStack.removeFirst() }
        .reversed()
        .forEach { destinationStack.addFirst(it) }
}

private fun parseInput(input: List<String>): Pair<Map<Int, Stack>, List<Instruction>> {
    val stacks = parseStacks(input.subList(0, input.indexOf("") - 1))
    val instructions = input.subList(input.indexOf("") + 1, input.size).map { Instruction.of(it) }

    return stacks to instructions
}

private fun parseStacks(stacksInput: List<String>): Map<Int, Stack> {
    val numberOfStacks = stacksInput.last().split(" ").size
    val stacksMap = (1..numberOfStacks).associateBy({ it }, { Stack() })

    stacksInput.map { line ->
        line.chunked(4)
            .forEachIndexed { index, item ->
                item[1].takeIf { item.isNotBlank() }?.let { stacksMap.getValue(index + 1).addLast(it) }
            }
    }

    return stacksMap
}

private fun printResult(stacks: Map<Int, Stack>) =
    stacks.entries.sortedBy { it.key }.map { it.value.first() }.joinToString("") { it.toString() }

private typealias Crate = Char

private typealias Stack = ArrayDeque<Crate>

private data class Instruction(val origin: Int, val destination: Int, val amount: Int) {

    companion object {
        fun of(line: String): Instruction {
            return line
                .split("move ", " from ", " to ")
                .let { Instruction(origin = it[2].toInt(), destination = it[3].toInt(), amount = it[1].toInt()) }
        }
    }
}
