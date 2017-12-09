package ru.ifmo.ctddev.semenov.swapsolver


/*
    x       o
   / \     / \
  x   x   o   o
 / \ / \ / \ / \
x   x   _   o   o
 \ / \ / \ / \ /
  x   x   o   o
   \ /     \ /
    x       o
 */

data class Cell(val x: Int, val y: Int) {
    init {
        require(isValidCell(x, y))
    }

    companion object {
        fun decode(code: Int): Cell {
            return when {
                code in 0..8 -> Cell(code / 3, code % 3)
                code < 17    -> {
                    val c = code - 8
                    Cell(c / 3 + 2, c % 3 + 2)
                }
                else         -> throw IllegalArgumentException("$code !in [0, 16]")
            }
        }

        fun isValidCell(x: Int, y: Int): Boolean = x in 0..4 && y in when {
            x < 2 -> 0..2
            x > 2 -> 2..4
            else  -> 0..4
        }

        fun unsafeCreate(x: Int, y: Int): Cell? = if (isValidCell(x, y)) Cell(x, y) else null
    }

    fun encode(): Int {
        return when {
            x < 2 || (x == 2 && y < 2) -> 3 * x + y
            else                       -> 8 + 3 * (x - 2) + (y - 2)
        }
    }
}