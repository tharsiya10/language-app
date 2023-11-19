package fr.uparis.languesapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import fr.uparis.languesapp.databinding.ActivityMainBinding
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val serviceIntent = Intent(this, MyService::class.java).apply {
            Log.d("projet", "serviceIntent")
            action = "start"
        }
        startService(serviceIntent)
    }

    fun ask_search(v : View) {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    fun settings(v : View) {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    fun clean_word(v : View){
        val intent = Intent(this, CleanWordActivity::class.java)
        startActivity(intent)
    }

    fun clean_dico(v : View){
        val intent = Intent(this, CleanDicoActivity::class.java)
        startActivity(intent)
    }

}