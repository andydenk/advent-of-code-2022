fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return calculateElvInventories(input)
        .maxOf { it.totalCalories() }
}

fun part2(input: List<String>): Int {
    return calculateElvInventories(input)
        .map { it.totalCalories() }
        .sortedDescending()
        .take(3)
        .sum()
}

fun calculateElvInventories(input: List<String>): MutableList<Elv> {
    val elves = mutableListOf<Elv>()
    var currentElv = Elv()

    for (inputLine in input) {
        val calorieItem = inputLine.toIntOrNull()

        calorieItem?.let { currentElv += calorieItem }

        if (inputLine.isEmpty()) {
            elves += currentElv
            currentElv = Elv()
        }
    }

    if (currentElv.calorieItems.isNotEmpty()) {
        elves += currentElv
    }

    return elves
}

typealias CalorieItem = Int

data class Elv(val calorieItems: MutableList<CalorieItem> = emptyList<CalorieItem>().toMutableList()) {
    operator fun plusAssign(calorieItem: CalorieItem) {
        calorieItems.add(calorieItem)
    }

    fun totalCalories() = calorieItems.sumOf { it }
}
