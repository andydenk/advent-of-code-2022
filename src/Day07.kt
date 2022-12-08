import java.lang.IllegalStateException

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun part1(input: List<String>): Int {
    val directoryTree = createDirectoryTree(input)
    return calculateTotalSizesOfMatchingDirectories(directoryTree)
}

private fun part2(input: List<String>): Int {
    val directoryTree = createDirectoryTree(input)
    return calculateDirectoryToDelete(directoryTree)
}

private fun createDirectoryTree(input: List<String>): Directory {
    val lines = input.drop(1)
    val directoryTree = Directory("root", emptyList())

    var currentDirectory = directoryTree

    for (line in lines) {
        line.apply {
            if (line == "$ cd ..") {
                currentDirectory = directoryTree.findDirectory(currentDirectory.path)
            } else if (startsWith("$ cd ")) {
                currentDirectory = currentDirectory.findDirectory(drop(5))
            } else if (isDirectory() || isFile()) {
                currentDirectory.children.add(asStructure(currentDirectory))
            }
        }
    }
    return directoryTree
}

private fun calculateTotalSizesOfMatchingDirectories(directory: Directory): Int {
    val matchingDirectories: MutableSet<Directory> = mutableSetOf()

    saveMatchingDirectories(directory, matchingDirectories) { it.getSize() < 100000 }

    return matchingDirectories.sumOf { it.getSize() }
}

private fun calculateDirectoryToDelete(directory: Directory): Int {
    val matchingDirectories: MutableSet<Directory> = mutableSetOf()
    saveMatchingDirectories(directory, matchingDirectories) { true }

    val neededSpace = directory.getSize() - (70000000 - 30000000)
    return matchingDirectories.map { it.getSize() }.filter { it > neededSpace }.min()
}

private fun saveMatchingDirectories(
    currentDirectory: Directory,
    matchingDirectories: MutableSet<Directory>,
    directoryIsRelevant: (Directory) -> Boolean
) {
    if (directoryIsRelevant.invoke(currentDirectory)) {
        matchingDirectories.add(currentDirectory)
    }

    val childDirectories = currentDirectory.getChildDirectories()

    if (childDirectories.isEmpty()) {
        return
    }

    childDirectories.forEach {
        saveMatchingDirectories(it, matchingDirectories, directoryIsRelevant)
    }
}

private typealias Line = String
private typealias PathComponent = String

private fun Line.asStructure(currentDirectory: Directory): Structure {
    val directory = asDirectory(currentDirectory).takeIf { isDirectory() }
    return directory ?: asFile().takeIf { isFile() } ?: throw IllegalStateException("Neither file nor directory")
}

private fun Line.isDirectory(): Boolean = startsWith("dir")

private fun Line.asDirectory(parent: Directory): Directory {
    val path = parent.name.takeUnless { it == "root" }?.let { parent.path + it } ?: parent.path
    return Directory(name = drop(4), path, children = mutableSetOf())
}

private fun Line.isFile(): Boolean = first().isDigit()

private fun Line.asFile() = split(" ").let { File(fileSize = it[0].toInt(), name = it[1]) }

private data class Directory(
    override val name: String,
    val path: List<PathComponent>,
    val children: MutableSet<Structure> = mutableSetOf()
) : Structure {

    override fun isDirectory(): Boolean = true

    override fun getSize() = children.sumOf { it.getSize() }

    fun getChildDirectories() = children.mapNotNull { it as? Directory }

    fun findDirectory(name: String): Directory {
        val directSubDirectory = children.find { it.name == name } as? Directory
        return directSubDirectory ?: throw IllegalStateException("No directory with this name found.")
    }

    fun findDirectory(path: List<String>): Directory {
        var target = this
        path.forEach { name -> target = target.findDirectory(name) }
        return target
    }
}

private data class File(override val name: String, val fileSize: Int) : Structure {
    override fun isDirectory(): Boolean = false
    override fun getSize(): Int = fileSize
}

private interface Structure {
    val name: String
    fun isDirectory(): Boolean
    fun getSize(): Int
}
