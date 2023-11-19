package fr.uparis.languesapp

import android.app.Application

class WordApplication : Application() {

    val database by lazy {
        DicoBD.getDatabase(this)
    }
}