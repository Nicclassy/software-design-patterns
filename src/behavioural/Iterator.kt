package behavioural

import java.util.*

class TreeNode<T>(val value: T, var left: TreeNode<T>? = null, var right: TreeNode<T>? = null)

class BinarySearchTree<T: Comparable<T>>(var root: TreeNode<T>? = null)

abstract class TreeIterator<T: Comparable<T>>: Iterator<T>

class PreorderTraversal<T: Comparable<T>>(tree: BinarySearchTree<T>): TreeIterator<T>() {
    private val stack: Stack<TreeNode<T>> = Stack()

    init {
        tree.root?.let { stack.push(it) }
    }

    override fun hasNext(): Boolean = stack.isNotEmpty()

    override fun next(): T {
        val node = stack.pop()
        node.right?.let { stack.push(it) }
        node.left?.let { stack.push(it) }
        return node.value
    }
}

class InorderTraversal<T: Comparable<T>>(tree: BinarySearchTree<T>): TreeIterator<T>() {
    private val stack: Stack<TreeNode<T>> = Stack()

    init {
        populate(tree.root)
    }

    override fun hasNext(): Boolean = stack.isNotEmpty()

    override fun next(): T {
        val node = stack.pop()
        populate(node.right)
        return node.value
    }

    private fun populate(node: TreeNode<T>?) {
        var current = node
        while (current != null) {
            stack.push(current)
            current = current.left
        }
    }
}

fun main() {
    val tree = BinarySearchTree<Int>().apply {
        root = TreeNode(10).apply {
            left = TreeNode(5).apply {
                left = TreeNode(2)
                right = TreeNode(6)
            }
            right = TreeNode(20).apply {
                left = TreeNode(12)
                right = TreeNode(35).apply {
                    right = TreeNode(40)
                }
            }
        }
    }

    print("Preorder: ")
    for (value in PreorderTraversal(tree)) {
        print("$value -> ")
    }
    println("(end)")

    print("Inorder: ")
    for (value in InorderTraversal(tree)) {
        print("$value -> ")
    }
    println("(end)")
}


