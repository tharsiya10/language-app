package fr.uparis.languesapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import fr.uparis.languesapp.databinding.ActivityCleanDicoBinding

class CleanDicoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCleanDicoBinding
    private lateinit var model: CleanWordViewModel
    private lateinit var adapter: CleanDicoAdapter

    // var toDelete: List<Dico> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCleanDicoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this).get(CleanWordViewModel::class.java)
        adapter = CleanDicoAdapter(model)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        showList()

        // listener on edittext
        binding.langueDst.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.dst.isChecked) {
                    // Log.d("projetclean", s.toString())
                    s?.toString()?.let { showList(it) }
                } else {
                    showList()
                }

            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.supprDicos?.setOnClickListener {
            model.deleteDicos()
            showList()
        }

    }

    fun showList(){
        model.loadAllDico().observe(this){
            adapter.setDicos(it)
            // toDelete = it
        }
    }

    fun showList(s : String) {
        model.loadDicoDst(s).observe(this){
            adapter.setDicos(it)
            // toDelete = it
        }
    }


}