package com.example.arithmeticoperationsgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.PopupWindow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newGameButton = findViewById<Button>(R.id.newgameButton)
        val aboutButton = findViewById<Button>(R.id.aboutButton)

        newGameButton.setOnClickListener {
            val gameIntent = Intent(this, GameActivity::class.java)
            startActivity(gameIntent)
        }

        aboutButton.setOnClickListener{
            var popupView = layoutInflater.inflate(R.layout.about_popup_window, null)
            var popupWindow = PopupWindow(this)
            popupWindow.contentView = popupView
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

            popupView.setOnClickListener{
                popupWindow.dismiss()
            }
        }
    }
}