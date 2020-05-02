package com.example.minesweeper_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(GameView (this));  // Set the view to display content from our GameView SurfaceView
    }
}
