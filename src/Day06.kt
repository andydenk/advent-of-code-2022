fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput[0]) == 7)
    check(part1(testInput[1]) == 5)
    check(part1(testInput[2]) == 6)
    check(part1(testInput[3]) == 10)
    check(part1(testInput[4]) == 11)

    check(part2(testInput[0]) == 19)
    check(part2(testInput[1]) == 23)
    check(part2(testInput[2]) == 23)
    check(part2(testInput[3]) == 29)
    check(part2(testInput[4]) == 26)

    val input = readInput("Day06").first()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: String): Int = detectSignal(input, 4)

private fun part2(input: String): Int = detectSignal(input, 14)

private fun detectSignal(input: String, uniqueCharacters: Int): Int {
    return input
        .windowedSequence(uniqueCharacters)
        .indexOfFirst { it.toSet().size == uniqueCharacters } + uniqueCharacters
}
