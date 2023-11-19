package fr.uparis.languesapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.languesapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding

    val model by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private val adapter by lazy {
        SearchAdapter(model)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.itemAnimator = null
        model.allDicos.observe(this) {
            adapter.set_dicos(it)
        }
    }

    // premiere recherche
    fun search(v : View) {
        with(binding) {
            search.setOnClickListener {
                val word = word.text.toString()
                val txt : String
                val intent = Intent(Intent.ACTION_VIEW)
                if(model.checkedDico.value != null){
                    val url = model.checkedDico.value!!.url

                    txt = (url).replace("*", word)


                    intent.setData(Uri.parse(txt))
                    startActivity(intent)
                }
                else {
                    txt = "http://www.google.fr/search?q="+word
                    intent.setData(Uri.parse(txt))
                    startActivity(intent)
                }

            }
        }
    }
}