import java.lang.IllegalStateException
import kotlin.math.abs

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val largerTestInput = readInput("Day09_test_larger")
    val input = readInput("Day09")

    val part1Test = part1(testInput)
    val part2Test = part2(testInput)
    val part2TestLarger = part2(largerTestInput)

    check(part1Test == 13)
    check(part2Test == 1)
    check(part2TestLarger == 36)

    val part1Result = part1(input)
    val part2Result = part2(input)

    println("Part 1: $part1Result")
    println("Part 2: $part2Result")
}

private fun part1(input: List<String>): Int {
    return Rope.fromInput(input, 2).getVisitedPlaces()
}

private fun part2(input: List<String>): Int {
    return Rope.fromInput(input, 10).getVisitedPlaces()
}

private class Rope(val moves: List<Move>, val ropeLength: Int) {
    val nodes: ArrayDeque<Node> = ArrayDeque((0 until ropeLength).map { 0 to 0 })
    val visited = mutableSetOf<Node>()

    fun getVisitedPlaces(): Int {
        visited.add(nodes.first().first to nodes.first().second)

        //Visualization(nodes, visited).printState()

        moves.forEach { move ->
            repeat(move.steps) {
                moveHead(move)
                (1 until ropeLength).forEach { nodeNumber -> moveTail(nodeNumber) }
            }
        }
        Visualization(nodes, visited).printState(endState = true)
        return visited.size
    }

    private fun moveHead(move: Move) {
        nodes[0] = nodes[0].first + move.direction.xModifier to nodes[0].second + move.direction.yModifier
    }

    private fun moveTail(nodeNumber: Int) {
        val (leadingX, leadingY) = nodes[nodeNumber - 1]
        val (x, y) = nodes[nodeNumber]
        val xModifier = leadingX - x
        val yModifier = leadingY - y

        if (abs(xModifier) < 2 && abs(yModifier) < 2) {
            return
        }

        val updatedNode = x + xModifier.toMoveAmount() to y + yModifier.toMoveAmount()

        nodes[nodeNumber] = updatedNode
        if (nodeNumber == ropeLength - 1) {
            visited.add(updatedNode)
        }
    }

    private fun Int.toMoveAmount(): Int = coerceAtMost(1).coerceAtLeast(-1)

    companion object {
        fun fromInput(input: List<String>, ropeLength: Int): Rope {
            return Rope(input.map { Move.fromLine(it) }, ropeLength)
        }
    }
}

private class Visualization(val nodes: ArrayDeque<Node>, val visited: Set<Node>) {

        fun printState(endState: Boolean = false) {
            val yGridRange = (233 downTo -19)
            val xGridRange = (-12..304)

            yGridRange.forEach { y -> println(xGridRange.map { x -> getCharacter(x, y, endState) }.toCharArray()) }

            println()
        }

        private fun getCharacter(x: Int, y: Int, endState: Boolean): Char {
            return if (nodes[0].first == x && nodes[0].second == y) {
                'H'
            } else if (nodes.drop(1).contains(Node(x, y))) {
                nodes.indexOf(Node(x, y)).toString()[0]
            } else if (x == 0 && y == 0 * -1) {
                's'
            } else if (visited.contains(x to y)) {
                '#'.takeIf { endState } ?: '.'
            } else {
                '.'
            }
        }

}

typealias Node = Pair<Int, Int>

private data class Move(val direction: Direction, val steps: Int) {

    companion object {
        fun fromLine(line: String): Move {
            return Move(Direction.forInput(line[0]), line.drop(2).toInt())
        }
    }
}

private enum class Direction(val xModifier: Int, val yModifier: Int) {
    RIGHT(1, 0),
    LEFT(-1, 0),
    UP(0, 1),
    DOWN(0, -1);

    companion object {
        fun forInput(input: Char): Direction {
            return when (input) {
                'R' -> RIGHT
                'L' -> LEFT
                'U' -> UP
                'D' -> DOWN
                else -> throw IllegalStateException("Unsupported Direction")
            }
        }
    }
}