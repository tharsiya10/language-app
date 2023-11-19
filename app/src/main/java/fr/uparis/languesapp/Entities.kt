package fr.uparis.languesapp

import android.os.Parcelable
import androidx.room.*
import java.io.Serializable
import java.sql.Timestamp

@Entity (indices = [Index( value = [ "url" ], unique = true  )])
data class Dico(
    @PrimaryKey(autoGenerate = true)
    val idDico : Long = 0,
    val langue_src: String,
    val langue_dst: String,
    val nom : String,
    val url : String
)

@Entity
data class Word(
    @PrimaryKey(autoGenerate = true)
    val idWord : Long = 0,
    val mot : String,
    val langue_src : String,
    val langue_dst : String,
    val adr_url : String,
    var count : Int,
    var timestamp : Long
)