package fr.uparis.languesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CleanWordViewModel(application : Application) : AndroidViewModel(application) {

    val dao = (application as WordApplication).database.myDao()

    fun loadAllWord() = dao.loadAllWord()

    var wordsToDelete : List<Word> = listOf()

    var dicosToDelete : List<Dico> = listOf()

    val deleteInfo = MutableLiveData<Int>()

    fun loadKnownWord() = dao.loadKnownWord()

    fun loadWordByDst(lg : String) = dao.loadWordByLanguageDst(lg)

    fun loadAllDico() = dao.loadAllDico()

    fun loadDicoDst(lg : String) = dao.loadDicoByLanguageDst(lg)

    fun deleteWords() {
        Thread {
            val i = dao.deleteSomeWords(wordsToDelete.toMutableList())
            deleteInfo.postValue(i)
        }.start()
    }

    fun deleteDicos() {
        Thread {
            val i = dao.deleteSomeDicos(dicosToDelete.toMutableList())
            deleteInfo.postValue(i)
        }.start()
    }

}