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
        .count { (first, second) -> first in second || second in first }
}

private fun part2(input: List<String>): Int {
    return input
        .map { it.toRangePair() }
        .count { (first, second) -> first.intersects(second) || second.intersects(first) }
}

private operator fun IntRange.contains(otherRange: IntRange): Boolean {
    return first() <= otherRange.first() && last() >= otherRange.last()
}

private fun IntRange.intersects(otherRange: IntRange): Boolean {
    return first() <= otherRange.last && otherRange.first <= last
}

private fun String.toRangePair(): Pair<IntRange, IntRange> {
    return substringBefore(',').toIntRange() to substringAfter(',').toIntRange()
}

private fun String.toIntRange(): IntRange {
    return substringBefore('-').toInt()..substringAfter('-').toInt()
}
