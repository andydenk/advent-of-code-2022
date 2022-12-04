import java.lang.IllegalStateException

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    return input
        .sumOf { calculatePriorityOfDuplicate(it) }
}

private fun part2(input: List<String>): Int {
    return input
        .chunked(3)
        .sumOf { calculatePriorityOfBadge(it) }
}

private fun calculatePriorityOfBadge(elfGroup: List<String>): Int {
    if (elfGroup.size != 3) throw IllegalStateException("ElfGroup Size is not valid")

    return elfGroup
        .map { it.toSet() }
        .reduce { acc, element -> acc.toSet() intersect element.toSet() }
        .single()
        .priority()
}

private fun calculatePriorityOfDuplicate(input: String): Int {
    if (input.length % 2 != 0) throw IllegalStateException("Compartment Size is not equal")

    val firstCompartment = input.take(input.length / 2).associateBy { it }
    val secondCompartment = input.takeLast(input.length / 2)
    return secondCompartment.first { firstCompartment.contains(it) }.priority()
}

private typealias Item = Char

private fun Item.priority(): Int = code - (('a'.code - 1).takeIf { isLowerCase() } ?: ('A'.code - 27))