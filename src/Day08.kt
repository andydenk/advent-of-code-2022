fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    return TreetopLocator.forInput(input).locateVisibleTrees().size
}

private fun part2(input: List<String>): Int {
    return TreetopLocator.forInput(input).highestScenicScore()
}

private class TreetopLocator(val treeMap: Map<Pair<X, Y>, Tree>, val arrayDimension: Int) {

    private val visibleTrees: MutableSet<Pair<X, Y>> = mutableSetOf()

    fun locateVisibleTrees(): Set<Pair<X, Y>> {
        var highestFromLeft: Int
        var highestFromRight: Int
        var highestFromTop: Int
        var highestFromBottom: Int

        for (i in 0..arrayDimension) {
            highestFromLeft = -1
            highestFromRight = -1
            highestFromTop = -1
            highestFromBottom = -1

            for (j in 0..arrayDimension) {
                highestFromLeft = handleDirection(highestSoFar = highestFromLeft, x = j, y = i)
                highestFromRight = handleDirection(highestSoFar = highestFromRight, x = arrayDimension - j, y = i)
                highestFromTop = handleDirection(highestSoFar = highestFromTop, x = i, y = j)
                highestFromBottom = handleDirection(highestSoFar = highestFromBottom, x = i, y = arrayDimension - j)

                if (highestFromLeft == 9 && highestFromRight == 9 && highestFromTop == 9 && highestFromBottom == 9) {
                    break //possibly we can exit early
                }
            }
        }

        return visibleTrees
    }

    fun highestScenicScore(): Int {
        return treeMap.maxOf { it.value.calculateScenicScore() }
    }

    private fun Tree.calculateScenicScore(): Int {
        var leftSpace = 0
        var rightSpace = 0
        var topSpace = 0
        var bottomSpace = 0

        var leftFree = true
        var rightFree = true
        var topFree = true
        var bottomFree = true

        for (i in 1 until arrayDimension) {
            rightFree = checkVisibility(x = x + i, y = y).takeIf { rightFree }?.apply { rightSpace++ } ?: rightFree
            leftFree = checkVisibility(x = x - i, y = y).takeIf { leftFree }?.apply { leftSpace++ } ?: leftFree
            topFree = checkVisibility(x = x, y = y + i).takeIf { topFree }?.apply { topSpace++ } ?: topFree
            bottomFree = checkVisibility(x = x, y = y - i).takeIf { bottomFree }?.apply { bottomSpace++ } ?: bottomFree
        }

        return leftSpace * rightSpace * topSpace * bottomSpace
    }

    private fun Tree.checkVisibility(x: Int, y: Int): Boolean {
        return if (x <= 0 || y <= 0 || x >= arrayDimension || y >= arrayDimension) {
            false
        } else {
            treeMap.getValue(x to y).height < height
        }
    }

    private fun handleDirection(x: Int, y: Int, highestSoFar: Int): Int {
        if (highestSoFar == 9) { //can't get any higher
            return highestSoFar
        }

        val location = x to y
        val tree = treeMap.getValue(location)

        return if (tree.height > highestSoFar) {
            visibleTrees.add(location)
            return tree.height
        } else {
            highestSoFar
        }
    }

    companion object {
        fun forInput(treeGrid: List<String>): TreetopLocator {
            val treeMap: Map<Pair<X, Y>, Tree> = treeGrid
                .flatMapIndexed { y, line ->
                    line.toList()
                        .mapIndexed { x, height -> (x to y) to Tree(x, y, height.digitToInt()) }
                }.associate { it }
            return TreetopLocator(treeMap = treeMap, arrayDimension = treeGrid.size - 1)
        }
    }
}

private typealias X = Int

private typealias Y = Int

private data class Tree(val x: Int, val y: Int, val height: Int)
