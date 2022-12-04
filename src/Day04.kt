fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    return input
        .map { it.toRangePair() }
        .filter { (first, second) -> first.fullyContains(second) || second.fullyContains(first) }
        .size
}

private fun part2(input: List<String>): Int {
    return input
        .map { it.toRangePair() }
        .filter { (first, second) -> first.intersects(second) || second.intersects(first) }
        .size
}

private fun Iterable<Int>.fullyContains(otherRange: Iterable<Int>): Boolean {
    return first() <= otherRange.first() && last() >= otherRange.last()
}

private fun IntRange.intersects(otherRange: IntRange): Boolean {
    return contains(otherRange.first()) || contains(otherRange.last())
}

private fun String.toRangePair(): Pair<IntRange, IntRange> {
    return substringBefore(',').toIntRange() to substringAfter(',').toIntRange()
}

private fun String.toIntRange(): IntRange {
    return substringBefore('-').toInt()..substringAfter('-').toInt()
}
