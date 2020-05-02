package com.example.minesweeper_app

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.MotionEvent
import android.widget.Toast


class Game {
    private var gameView: GameView? = null
    var gameBoard: Board? = null
    private var hudSpriteSheet: Bitmap? = null
    private var boardSize = 0
    private var bombCount = 0
    private var isGameOver = false
    private var score = 0
    var hud: Array<HUD?> = arrayOfNulls<HUD>(3)

    /*
     * Initialize various game components in the constructor.  Record the size of the board,
     * how many bombs must exist on it, and also prepare various HUD components.
     *
     * @param    GameView gameView
     * @param    int boardSize
     * @param    int bombCount
     */
    constructor(gameView: GameView?, boardSize: Int, bombCount: Int) {
        this.gameView = gameView
        this.boardSize = boardSize
        this.bombCount = bombCount
        gameBoard = Board(gameView, boardSize, bombCount)
        //hud = arrayOfNulls<HUD>(3)
        hudSpriteSheet = BitmapFactory.decodeResource(
            this.gameView?.context!!.resources,
            R.drawable.gallery_thumb
        )
        hud[0] = HUD(this.gameView, hudSpriteSheet, 0, 0)
        hud[1] = HUD(this.gameView, hudSpriteSheet, 160, 0)
        hud[2] = HUD(this.gameView, hudSpriteSheet, 80, 40)
    }

    /*
     * Start a new game by resetting the board and clearing the game over indicator
     *
     * @param
     * @return   void
     */
    fun start() {
        isGameOver = false
        score = 0
        gameBoard!!.reset()
    }

    /*
     * Cheat in the game by marking one of the known bombs with a flag
     *
     * @param
     * @return   void
     */
    fun cheat() {
        outerLoop@ for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (!gameBoard!!.grid!![i][j]!!.isRevealed && gameBoard!!.grid!![i][j]!!.isBomb
                ) {
                    gameBoard!!.grid!![i][j]!!.isCheat = true
                    gameBoard!!.grid!![i][j]!!.reveal()
                    break@outerLoop
                }
            }
        }
    }

    /*
     * Game over has ocurred because we hit a bomb.  Show all bombs that were remaining and set
     * the game over indicator to true so that way further cells cannot be unlocked
     *
     * @param
     * @return   void
     */
    fun gameOver() {
        isGameOver = true
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                gameBoard!!.showBombs(gameBoard!!.grid!![i][j]!!)
            }
        }
        Toast.makeText(gameView!!.context, "Game over!", Toast.LENGTH_LONG).show()
    }

    /*
     * The game was completed without triggering a bomb.  Lock the game and present a victory message
     *
     * @param
     * @return   void
     */
    fun gameFinished() {
        isGameOver = true
        Toast.makeText(gameView!!.context, "You've beat the game!", Toast.LENGTH_LONG).show()
    }

    /*
     * Manually validate to see if the game was won.  If the game truly was not completed, it is
     * instant game over
     *
     * @param
     * @return   void
     */
    fun validate() {
        if (score == boardSize * boardSize - bombCount) {
            gameFinished()
        } else {
            gameOver()
        }
    }

    /*
     * Draw the two HUD components to the screen.  The two components being a new game
     * and cheat button in a sprite sheet
     *
     * @param    Canvas canvas
     * @return   void
     */
    fun draw(canvas: Canvas?) {
        hud[0]?.onDraw(canvas!!, 0, 0)
        hud[1]?.onDraw(canvas!!, 0, 1)
        hud[2]?.onDraw(canvas!!, 0, 2)
    }

    /*
     * Check for touch collisions on any of the components in our game.  Components are anything
     * from HUD elements to board cells.  If a touch event collides with the new game button, then start
     * a new game.  If a touch event collides with the cheat button, then cheat.  However, things get
     * more complex with board cells because they are in an array.  On every touch event, check to see if
     * any of the board cells were hit.  If a bomb is touched or the score matches the max possible, then
     * end the game
     *
     * @param    MotionEvent event
     * @return   void
     */
    fun registerTouch(event: MotionEvent) {
        if (hud[0]!!.hasCollided(event.x, event.y)) {
            start()
        }
        if (hud[1]!!.hasCollided(event.x, event.y)) {
            cheat()
        }
        if (hud[2]!!.hasCollided(event.x, event.y)) {
            validate()
        }
        if (!isGameOver) {
            for (i in 0 until boardSize) {
                for (j in 0 until boardSize) {
                    if (gameBoard!!.grid!![i][j]!!.hasCollided(
                            event.x,
                            event.y
                        )
                    ) { // If the cell touched was a bomb, then game over, otherwise check the score and see if we've won
                        if (gameBoard!!.reveal(gameBoard!!.grid!![i][j]!!)) {
                            gameOver()
                        } else {
                            score = gameBoard!!.getRevealedCount()
                            if (score == boardSize * boardSize - bombCount) {
                                gameFinished()
                            }
                        }
                        break
                    }
                }
            }
        }
    }
}