package ru.ifmo.ctddev.semenov.swapsolver

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class CellTest {
    @Test
    fun testCell2Code() {
        for (x in 0..4) {
            for (y in when {
                x < 2 -> 0..2
                x > 2 -> 2..4
                else  -> 0..4
            }) {
                checkCell(Cell(x, y))
            }
        }
    }

    @Test
    fun testCode2Cell() {
        for (code in 0..16) {
            checkCode(code)
        }
    }

    private fun checkCell(cell: Cell) {
        assertEquals(cell, Cell.decode(cell.encode()))
    }

    private fun checkCode(code: Int) {
        assertEquals(code, Cell.decode(code).encode())
    }
}