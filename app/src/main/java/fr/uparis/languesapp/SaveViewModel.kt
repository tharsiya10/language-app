package fr.uparis.languesapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.sql.Timestamp


class SaveViewModel(application : Application) : AndroidViewModel(application) {
    val dao = (application as WordApplication).database.myDao()
    val insertInfo = MutableLiveData<Int>()

    fun loadAllWord() = dao.loadAllWord()

    fun addWords(mot : String, langue_src: String, langue_dst: String, adr_url : String) {
        Thread {
            val l = dao.addWords(Word(mot = mot.trim(), langue_src = langue_src.trim(), langue_dst =  langue_dst.trim(), adr_url = adr_url.trim(),
            count = 0, timestamp = (0)))
            insertInfo.postValue(if (l[0] == -1L) 0 else 1)
        }.start()
    }

    fun addDicos(langue_src : String, langue_dst : String, nom : String, url : String) {
        Thread {
            val l = dao.addDicos(Dico(langue_src = langue_src, langue_dst = langue_dst, nom = nom, url = url))
            insertInfo.postValue(if(l[0] == -1L) 0 else 1)
        }.start()
    }

}