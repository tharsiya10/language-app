package fr.uparis.languesapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class, Dico::class], version = 8) // 7
abstract class DicoBD : RoomDatabase(){
    abstract fun myDao() : DicoDao

    companion object {
        @Volatile
        private var instance : DicoBD? = null

        fun getDatabase(context : Context) : DicoBD {
            if(instance != null) return instance!!
            val db = Room.databaseBuilder(context.applicationContext,
            DicoBD::class.java, "dico")
                .fallbackToDestructiveMigration()
                .build()
            instance = db
            return instance!!
        }
    }
}