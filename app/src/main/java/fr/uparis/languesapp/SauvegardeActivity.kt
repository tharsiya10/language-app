package fr.uparis.languesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.languesapp.databinding.ActivityMainBinding
import fr.uparis.languesapp.databinding.ActivitySaveBinding
import fr.uparis.languesapp.databinding.ActivitySearchBinding
import java.net.URL
import java.sql.Timestamp

class SauvegardeActivity : AppCompatActivity() {

    private lateinit var save_binding : ActivitySaveBinding
    private lateinit var save_model : SaveViewModel
    private lateinit var save_adapter : SaveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        save_binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(save_binding.root)

        if(intent.action.equals("android.intent.action.SEND")) {
            val txt = intent.extras?.getString("android.intent.extra.TEXT")
            save_model = ViewModelProvider(this).get(SaveViewModel::class.java)
            save_adapter = SaveAdapter()

            save_binding.recyclerView.layoutManager = LinearLayoutManager(this)
            save_binding.recyclerView.adapter = save_adapter

            onSave(txt)

            save_model.loadAllWord().observe(this) {
                save_adapter.setWords(it)
            }

        }

    }

    fun onSave(txt : String?) {
        var url : String
        with(save_binding){
            save.setOnClickListener {
                val get_mot = word.text.toString()
                val get_src = langueSrc.text.toString()
                val get_dst = langueDst.text.toString()
                if(get_mot == "" && get_src == "" && get_dst == "" && txt == ""){
                    return@setOnClickListener
                }

                val list = get_mot.split("""\W+""".toRegex())
                // extraire de url les mots si plusieurs
                if(list.size > 1) {
                    val sub = txt!!.subSequence(txt.indexOf(list[0], 0, true),
                        txt.indexOf(list[list.size-1], 0, true)+list[list.size-1].length)
                    url = txt.replace(sub.toString(), "*")

                }
                else {
                    url = txt!!.replace(get_mot, "*")
                }

                save_model.addWords( get_mot, get_src, get_dst, txt)
                save_model.addDicos(get_src, get_dst, URL(txt).host, url)

                save_binding.word.text.clear()
                save_binding.langueSrc.text.clear()
                save_binding.langueDst.text.clear()

            }
        }
    }

}

