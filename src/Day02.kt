fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    return input
        .map { shapePair(it) }
        .map { (opponent, protagonist) -> Round(opponent, protagonist) }
        .sumOf { it.protagonistTotalScore() }
}

private fun part2(input: List<String>): Int {
    return input
        .map { shapeAndOutcome(it) }
        .map { (opponentShape, outcome) -> opponentShape to findNeededShape(opponentShape, outcome) }
        .map { (opponent, protagonist) -> Round(opponent, protagonist) }
        .sumOf { it.protagonistTotalScore() }
}

fun findNeededShape(opponentShape: Shape, outcomeNeeded: Outcome): Shape {
    return when (outcomeNeeded) {
        Outcome.WIN -> opponentShape.beatenBy()
        Outcome.DRAW -> opponentShape
        Outcome.LOSS -> opponentShape.beats()
    }
}

fun shapeFor(input: String) = when (input) {
    "A", "X" -> Rock
    "B", "Y" -> Paper
    "C", "Z" -> Scissors
    else -> throw IllegalStateException("Invalid input")
}

private fun shapePair(input: String): Pair<Shape, Shape> {
    val choices = input.split(" ")
    if (choices.size != 2) {
        throw java.lang.IllegalStateException("Invalid amount of choices")
    }

    return shapeFor(choices[0]) to shapeFor(choices[1])
}

private fun shapeAndOutcome(input: String): Pair<Shape, Outcome> {
    val choices = input.split(" ")
    if (choices.size != 2) {
        throw java.lang.IllegalStateException("Invalid amount of choices")
    }

    return shapeFor(choices[0]) to Outcome.forInput(choices[1])
}

class Round(private val opponent: Shape, private val protagonist: Shape) {
    fun protagonistTotalScore(): Int {
        return protagonist.scoreAgainst(opponent) + protagonist.baseScore
    }
}

sealed class Shape(val baseScore: Int) {
    abstract fun beats(): Shape
    abstract fun beatenBy(): Shape

    fun scoreAgainst(other: Shape): Int {
        val outcome = when (other) {
            beats() -> Outcome.WIN
            beatenBy() -> Outcome.LOSS
            this -> Outcome.DRAW
            else -> throw IllegalStateException("Invalid Shape Comparison")
        }
        return outcome.score
    }
}

object Rock : Shape(1) {
    override fun beats(): Shape = Scissors

    override fun beatenBy(): Shape = Paper
}

object Paper : Shape(2) {
    override fun beats(): Shape = Rock

    override fun beatenBy(): Shape = Scissors
}

object Scissors : Shape(3) {
    override fun beats(): Shape = Paper

    override fun beatenBy(): Shape = Rock
}

enum class Outcome(val score: Int) {
    WIN(6),
    DRAW(3),
    LOSS(0);

    companion object {
        fun forInput(input: String): Outcome = when (input) {
            "X" -> LOSS
            "Y" -> DRAW
            "Z" -> WIN
            else -> throw IllegalStateException("Invalid Objective")
        }
    }
}



