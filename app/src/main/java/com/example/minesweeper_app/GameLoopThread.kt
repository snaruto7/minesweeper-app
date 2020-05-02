package com.example.minesweeper_app

import android.graphics.Canvas

class GameLoopThread : Thread() {
    private var gameView: GameView? = null
    private var isRunning = false

    fun GameLoopThread(gameView: GameView?) {
        this.gameView = gameView
        isRunning = false
    }

    /*
     * Start or stop the thread which does all of our drawing
     *
     * @param    boolean isRunning
     * @return   void
     */
    fun setRunning(isRunning: Boolean) {
        this.isRunning = isRunning
    }

    /*
     * Lock the drawable canvas to draw graphics to the screen, then release the lock
     * when all drawing has completed
     *
     * @param
     * @return   void
     */
    override fun run() { // When the thread stops, so do our renders
        while (isRunning) {
            var canvas: Canvas? = null
            try {
                canvas = gameView!!.holder.lockCanvas()
                synchronized(gameView!!.holder) {
                    if (canvas != null) {
                        gameView!!.draw(canvas)
                    }
                }
            } finally {
                if (canvas != null) {
                    gameView!!.holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}