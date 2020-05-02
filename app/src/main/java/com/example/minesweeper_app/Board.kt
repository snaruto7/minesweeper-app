package com.example.minesweeper_app

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import java.util.*
import kotlin.collections.ArrayList


class Board() {
    var grid: Array<Array<Cell?>>? = null
    private var gameView: GameView? = null
    private var cellSpriteSheet: Bitmap? = null
    private var boardSize = 0
    private var bombCount = 0
    private var cellsRevealed = 0

    constructor(gameView: GameView?, boardSize: Int, bombCount: Int) : this() {
        grid = Array<Array<Cell?>>(boardSize) { arrayOfNulls<Cell>(boardSize) }
        this.gameView = gameView
        cellSpriteSheet = BitmapFactory.decodeResource(
            this.gameView!!.context!!.resources,
            R.drawable.hud_sprite_sheet.png
        )
        this.boardSize = boardSize
        this.bombCount = bombCount
    }

    /*
     * Draw a particular sprite from the sprite sheet to the screen.  If the cell has
     * not yet been revealed, show the blank sprite.  If the cell has been revealed and is
     * an indication of cheating, show a flag sprite.  If the cell has been revealed, show a
     * bomb.  Otherwise, show the number of neighbors that contain bombs in the cell.
     *
     * @param    Canvas canvas
     * @return   void
     */
    fun draw(canvas: Canvas?) {
        for (i in grid!!.indices) {
            for (j in grid!!.indices) {
                if (grid!![i][j]!!.isRevealed) {
                    if (grid!![i][j]!!.isCheat) {
                        grid!![i][j]?.onDraw(canvas!!, 2, 3)
                    } else if (grid!![i][j]!!.isBomb) {
                        grid!![i][j]!!.onDraw(canvas!!, 1, 0)
                    } else {
                        when (grid!![i][j]!!.getBombNeighborCount()) {
                            0 -> grid!![i][j]!!.onDraw(canvas!!, 2, 0)
                            1 -> grid!![i][j]!!.onDraw(canvas!!, 0, 1)
                            2 -> grid!![i][j]!!.onDraw(canvas!!, 1, 1)
                            3 -> grid!![i][j]!!.onDraw(canvas!!, 2, 1)
                            4 -> grid!![i][j]!!.onDraw(canvas!!, 0, 2)
                            5 -> grid!![i][j]!!.onDraw(canvas!!, 1, 2)
                            6 -> grid!![i][j]!!.onDraw(canvas!!, 2, 2)
                            7 -> grid!![i][j]!!.onDraw(canvas!!, 0, 3)
                            8 -> grid!![i][j]!!.onDraw(canvas!!, 1, 3)
                            else -> grid!![i][j]!!.onDraw(canvas!!, 0, 0)
                        }
                    }
                } else {
                    grid!![i][j]!!.onDraw(canvas!!, 0, 0)
                }
            }
        }
    }

    /*
     * Reset the board to the initial state.  This can act the same as an initialization function.
     * Resetting involves creating a new board, and placing bombs in new positions.
     *
     * @param
     * @return   void
     */
    fun reset() {
        for (i in grid!!.indices) {
            for (j in grid!!.indices) {
                grid!![i][j] = Cell(gameView, cellSpriteSheet, i, j, false)
            }
        }
        shuffleBombs(bombCount)
        calculateCellNeighbors()
        setPositions()
        cellsRevealed = 0
    }

    /*
     * Pick random locations for bombs to appear in the board grid.  If the grid location
     * already contains a bomb, loop until it finds a spot that is available.
     *
     * @param    int bombCount
     * @return   void
     */
    fun shuffleBombs(bombCount: Int) {
        var spotAvailable = true
        val random = Random()
        var row: Int
        var column: Int
        for (i in 0 until bombCount) {
            do {
                column = random.nextInt(8)
                row = random.nextInt(8)
                spotAvailable = grid!![column][row]!!.isBomb
            } while (spotAvailable)
            grid!![column][row]!!.isBomb = true
        }
    }

    /*
     * Determine all the cells that touch a particular cell
     *
     * @param
     * @return   void
     */
    fun calculateCellNeighbors() {
        for (x in grid!!.indices) {
            for (y in grid!!.indices) {
                for (i in grid!![x][y]!!.getX() - 1..grid!![x][y]!!.getX() + 1) {
                    for (j in grid!![x][y]!!.getY() - 1..grid!![x][y]!!.getY() + 1) {
                        if (i >= 0 && i < grid!!.size && j >= 0 && j < grid!!.size) {
                            grid!![x][y]!!.addNeighbor(grid!![i][j]!!)
                        }
                    }
                }
            }
        }
    }

    /*
     * Set the position of each cell on the board.  To make things a little more pleasing visually,
     * determine an offset so that the grid is a little more centered in the screen
     *
     * @param
     * @return   void
     */
    fun setPositions() {
        val horizontalOffset = (320 - boardSize * 25) / 2
        for (i in grid!!.indices) {
            for (j in grid!!.indices) {
                grid!![i][j]!!.setX(horizontalOffset + i * 25)
                grid!![i][j]!!.setY(90 + j * 25)
            }
        }
    }

    /*
     * Reveal a particular cell on the board.  If it is not a bomb, check to see if it has zero
     * neighbors that are bombs.  Reveal all cells recursively that are neighbors until it finds
     * a neighboring cell that is a bomb and stop.
     *
     * @param    Cell c
     * @return   boolean bomb
     */
    fun reveal(c: Cell): Boolean {
        c.reveal()
        if (!c.isBomb) {
            cellsRevealed++
            if (c.getBombNeighborCount() === 0) {
                val neighbors: ArrayList<Cell> = c.getNeighbors()!!
                for (i in 0 until neighbors.size) {
                    if (!neighbors[i].isRevealed) {
                        reveal(neighbors[i])
                    }
                }
            }
        }
        return c.isBomb
    }

    /*
     * Reveal a board cell only if it is a bomb.  This is useful for scenarios like game
     * over, where you might want to show the user all the bombs on the board
     *
     * @param    Cell c
     * @return   void
     */
    fun showBombs(c: Cell) {
        if (c.isBomb) {
            c.reveal()
        }
    }

    /*
     * Return how many cells on the board have been revealed.  Useful for calculating score
     *
     * @param
     * @return   int count
     */
    fun getRevealedCount(): Int {
        return cellsRevealed
    }

}