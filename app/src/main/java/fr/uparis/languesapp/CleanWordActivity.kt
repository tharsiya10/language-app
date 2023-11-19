package fr.uparis.languesapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.languesapp.databinding.ActivityCleanWordBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CleanWordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCleanWordBinding
    private lateinit var model : CleanWordViewModel
    private lateinit var adapter: CleanWordAdapter

    // var toDelete : List<Word> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCleanWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(CleanWordViewModel::class.java)
        adapter = CleanWordAdapter(model)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        showList()

        // listener on radiobutton appris
        binding.known.setOnCheckedChangeListener {
            buttonView, isChecked ->
            if(isChecked) {
                showKnowWord()
            }
            else {
                showList()
            }
        }

        // listener on edittext
        binding.langueDst.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.dst.isChecked) {
                    if(s == null){
                        showList()
                    }
                    else {
                        showList(s.toString())
                    }
                }
                else {
                    showList()
                }
            }

            override fun afterTextChanged(s: Editable?) { }

        })

        binding.suppr.setOnClickListener {
            // model.deleteWords(toDelete.toMutableList())
            model.deleteWords()
            showList()
            model.deleteInfo.observe(this) {
                val txt = if(it == 0) "aucun élément supprimé"
                else "suppression réussie"
                Toast.makeText(this, txt, Toast.LENGTH_LONG).show()
            }

        }
    }

    fun showList(){
        model.loadAllWord().observe(this){
            adapter.setWords(it)
        }
    }

    fun showList(s : String) {
        model.loadWordByDst(s).observe(this){
            adapter.setWords(it)
        }
    }

    fun showKnowWord(){
        model.loadKnownWord().observe(this) {
            adapter.setWords(it)
        }
    }


}