package fr.uparis.languesapp

import android.app.Application
import android.util.Log
import android.widget.CheckBox
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = (application as WordApplication).database.myDao()
    var checkedDico = MutableLiveData<Dico?>()
    val allDicos : LiveData<List<Dico>> = dao.loadAllDico()

}