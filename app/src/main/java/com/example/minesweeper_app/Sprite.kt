package com.example.minesweeper_app

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect


open class Sprite// The width of each sprite in the sheet based on column count
// The height of each sprite in the sheet based on row count
    (
    private var gameView: GameView?,
    bmp: Bitmap,
    private var x: Int,
    private var y: Int,
    columns: Int,
    rows: Int
) {
    private var sheet: Bitmap? = bmp
    private var width = 0
    private var height = 0

    init {
        width =
            bmp.width / columns
        height =
            bmp.height / rows
    }

    /*
     * Draw the sprite to the canvas.  Since we are working with sprite sheets, the particular
     * row and column of the sprite need to be specified.  All sprite sheets must contain sprites
     * that are the same size
     *
     * @param    Canvas canvas
     * @param    int spriteColumn
     * @param    int spriteRow
     * @return   void
     */
    fun onDraw(
        canvas: Canvas,
        spriteColumn: Int,
        spriteRow: Int
    ) { // The source rectangle of the sprite sheet based on the column and row specified and the sprite dimensions
        val src = Rect(
            spriteColumn * width,
            spriteRow * height,
            spriteColumn * width + width,
            spriteRow * height + height
        )
        // The destination rectangle of where to place the cut sprite on the canvas
        val dst = Rect(x, y, x + width, y + height)
        canvas.drawBitmap(sheet!!, src, dst, null)
    }

    /*
     * Check to see if a point, x and y, has collided in the dimensions of the particular
     * sprite.  This is for example a collision of touching or clicking a sprite
     *
     * @param    float otherX
     * @param    float otherY
     * @return   boolean collision
     */
    fun hasCollided(otherX: Float, otherY: Float): Boolean {
        return x < otherX && y < otherY && x + width > otherX && y + height > otherY
    }

    /*
     * Set the current horizontal position of the sprite
     *
     * @param    int x
     * @return   void
     */
    fun setX(x: Int) {
        this.x = x
    }

    /*
     * Set the current vertical position of the sprite
     *
     * @param    int y
     * @return   void
     */
    fun setY(y: Int) {
        this.y = y
    }

    /*
     * Return the current horizontal position of the sprite
     *
     * @param
     * @return   int x
     */
    fun getX(): Int {
        return x
    }

    /*
     * Return the current vertical position of the sprite
     *
     * @param
     * @return   int y
     */
    fun getY(): Int {
        return y
    }

    /*
     * Return the width that each sprite in the sheet has
     *
     * @param
     * @return   int width
     */
    fun getWidth(): Int {
        return width
    }

    /*
     * Return the height that each sprite in the sheet has
     * @param
     * @return   int height
     */
    fun getHeight(): Int {
        return height
    }
}