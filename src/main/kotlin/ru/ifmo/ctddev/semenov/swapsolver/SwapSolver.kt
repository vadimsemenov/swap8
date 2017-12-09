package ru.ifmo.ctddev.semenov.swapsolver


data class State(val space: Int, val mask: Int)

val startState = State(8, 0xff)
val finishState = State(8, 0xff shl 9)


fun main(args: Array<String>) {
    solve(startState)
    println("Total states = ${dp.map { it.size }.sum()}")
    printShortestSolution()
}


private val dp = Array<MutableMap<Int, List<Pair<State, Int>>>>(17, { hashMapOf() })
private val moves = arrayOf(Pair(0, 1), Pair(1, 0))

private fun solve(state: State): Boolean {
    if (state == finishState) return true
    dp[state.space][state.mask]?.let { return it.isNotEmpty() }

    val nextStates = nextStates(state).filter { solve(it) }
    dp[state.space].put(state.mask, nextStates.map {
        val nextStateDistance = dp[it.space][it.mask]
                ?.map { it.second }
                ?.max()
                ?: 0 // otherwise nextState == finishState
        Pair(it, 1 + nextStateDistance)
    })
    return nextStates.isNotEmpty()
}

internal operator fun Cell.plus(move: Pair<Int, Int>)  = Cell.unsafeCreate(x + move.first, y + move.second)
internal operator fun Cell.minus(move: Pair<Int, Int>) = Cell.unsafeCreate(x - move.first, y - move.second)
internal operator fun Pair<Int, Int>.times(scalar: Int) = Pair(first * scalar, second * scalar)

private fun nextStates(state: State): List<State> {
    val next = arrayListOf<State>()
    val current = Cell.decode(state.space)
    for (mul in 1..2) {
        // check backward
        for (move in moves) {
            val st = current + move * mul ?: continue
            val code = st.encode()
            if (state.mask ushr code and 1 == 0) {
                next.add(State(code, state.mask))
            }
        }
        // check forward
        for (move in moves) {
            val st = current - move * mul ?: continue
            val code = st.encode()
            if (state.mask ushr code and 1 == 1) {
                val xorMask = (1 shl code) or (1 shl state.space)
                next.add(State(code, state.mask xor xorMask))
            }
        }
    }
    return next
}

private fun printShortestSolution() {
    println("=== Shortest solution ===")
    var qty = 0
    var current = startState
    println(current.prettify())
    while (current != finishState) {
        ++qty
        current = dp[current.space][current.mask]!!.minBy { it.second }!!.first
        println(current.prettify())
    }
    println("=== Steps: $qty ===")
}

private fun State.prettify(): String {
    val a = (0..16).map { when {
        it == space            -> '_'
        mask shr it and 1 == 1 -> 'x'
        else                   -> 'o'
    } }
    return """
        |    ${a[2]}       ${a[10]}
        |   / \     / \
        |  ${a[1]}   ${a[5]}   ${a[9]}   ${a[13]}
        | / \ / \ / \ / \
        |${a[0]}   ${a[4]}   ${a[8]}   ${a[12]}   ${a[16]}
        | \ / \ / \ / \ /
        |  ${a[3]}   ${a[7]}   ${a[11]}   ${a[15]}
        |   \ /     \ /
        |    ${a[6]}       ${a[14]}
        |""".trimMargin()
}