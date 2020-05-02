package com.example.minesweeper_app

import android.graphics.Bitmap




class Cell : Sprite {
    var isRevealed = false
    var isBomb = false
    var isCheat = false
    private var neighbors: ArrayList<Cell>? = null
    private var bombNeighborCount = 0

    constructor(
        gameView: GameView?,
        spriteSheet: Bitmap?,
        x: Int,
        y: Int,
        isBomb: Boolean
    ) : super(gameView, spriteSheet!!, x, y, 3, 4){
        this.isBomb = isBomb
        isRevealed = false
        isCheat = false
        bombNeighborCount = 0
        neighbors = ArrayList()
    }

    /*
     * Keep track of every neighbor that touches the current cell.  Will have a maximum of
     * eight neighbors if not an edge or corner cell.  If the neighbor is a bomb, then add
     * it to the count.
     *
     * @param    Cell neighbor
     * @return   void
     */
    fun addNeighbor(neighbor: Cell) {
        neighbors!!.add(neighbor)
        if (neighbor.isBomb) {
            bombNeighborCount++
        }
    }

    /*
     * Get all neighbors that touch the current cell.  Will have a maximum of eight neighbors if
     * not an edge or corner cell
     *
     * @param
     * @return    ArrayList<Cell> neighbors
     */
    fun getNeighbors(): ArrayList<Cell>? {
        return neighbors
    }

    /*
     * Get the total number of neighbors to the current cell that have bombs.  Can be a number from
     * zero to eight
     *
     * @param
     * @return    int total
     */
    fun getBombNeighborCount(): Int {
        return bombNeighborCount
    }

    /*
     * Reveal the current cell
     *
     * @param
     * @return    void
     */
    fun reveal() {
        isRevealed = true
    }
}