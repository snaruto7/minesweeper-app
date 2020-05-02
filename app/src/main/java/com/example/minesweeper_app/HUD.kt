package com.example.minesweeper_app

import android.graphics.Bitmap




class HUD : Sprite{

    /*
     * Construct the HUD sprite.  In this game there will be two HUD sprites, one
     * for starting a new game and another for cheating
     *
     * @param    GameView gameView
     * @param    Bitmap spriteSheet
     * @param    int x
     * @param    int y
     */
    constructor(gameView: GameView?, spriteSheet: Bitmap?, x: Int, y: Int) : super(gameView, spriteSheet!!, x, y, 1, 3) { // The parent class Sprite also expects to know how many columns and rows exist in the sheet
    }
}