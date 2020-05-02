package com.example.minesweeper_app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GameView/*
             * Not used in our code, but required to prevent errors.  Just detects changes in the
             * SurfaceView
             *
             * @param    SurfaceHolder holder
             * @param    int format
             * @param    int width
             * @param    int height
             * @return   void
             *//*
             * As soon as the drawable surface is created, activate the thread that will
             * be in charge of all rendering
             *
             * @param    SurfaceHolder holder
             * @return   void
             *//*
             * If the game is closed, the view needs to be gracefully destroyed. This
             * means that the rendering thread must be stopped so it doesn't chew through
             * the device battery or throw errors while the game is closed
             *
             * @param    SurfaceHolder holder
             * @return   void
             */// The game will be 8x8 with 10 bombs
    (val context1: Context?) : SurfaceView(context1) {

    var holder1: SurfaceHolder? = null
    private var gameLoopThread: GameLoopThread? = null
    private val gameBoard: Board? = null
    private var game: Game? = null
    private var lastClick: Long = 0

    init {
        gameLoopThread = GameLoopThread()
        game = Game(this, 8, 10)
        game!!.start()
        this.holder1 = getHolder()
        holder1!!.addCallback(object : SurfaceHolder.Callback {
            /*
             * If the game is closed, the view needs to be gracefully destroyed. This
             * means that the rendering thread must be stopped so it doesn't chew through
             * the device battery or throw errors while the game is closed
             *
             * @param    SurfaceHolder holder
             * @return   void
             */
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                var retry = false
                gameLoopThread!!.setRunning(false)
                while (retry) {
                    try {
                        gameLoopThread!!.join()
                        retry = false
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }

            /*
             * As soon as the drawable surface is created, activate the thread that will
             * be in charge of all rendering
             *
             * @param    SurfaceHolder holder
             * @return   void
             */
            override fun surfaceCreated(holder: SurfaceHolder) {
                gameLoopThread!!.setRunning(true)
                gameLoopThread!!.start()
            }

            /*
             * Not used in our code, but required to prevent errors.  Just detects changes in the
             * SurfaceView
             *
             * @param    SurfaceHolder holder
             * @param    int format
             * @param    int width
             * @param    int height
             * @return   void
             */
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }
        })
    }

    /*
     * All sprite components to be drawn in the game.  This includes anything from HUD
     * components to board cells.  If it inherits the Sprite class, it should probably be
     * in this method
     *
     * @param    Canvas canvas
     * @return   void
     */
      fun SurfaceView.draw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        game!!.draw(canvas)
        game!!.gameBoard!!.draw(canvas)
    }


    /*
     * Detect click events on the canvas.  Time restrictions are placed on the click to prevent
     * like a million clicks from being registered if it is held too long.  In other words, it
     * is to prevent accidental clicks
     *
     * @param    MotionEvent event
     * @return   boolean clicked
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (System.currentTimeMillis() - lastClick > 500) {
            lastClick = System.currentTimeMillis()
            synchronized(getHolder()) { game!!.registerTouch(event!!) }
        }
        return true
    }
}

