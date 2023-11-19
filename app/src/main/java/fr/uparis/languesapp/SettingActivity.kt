package fr.uparis.languesapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import fr.uparis.languesapp.databinding.ActivitySettingsBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showOldSettings()
        // set values in edit text with sharedPreferences
        with(binding) {
            send.setOnClickListener {
                val nb_mots = if(nbWords.text.toString() == ""){
                    1
                }
                else {
                    nbWords.text.toString().toInt()
                }
                val freq = if(freq.text.toString() == ""){
                    1
                }
                else {
                    freq.text.toString().toInt()
                }
                val langue_src_lundi = langueSrc1.text.toString()
                val langue_dst_lundi = langueDst1.text.toString()
                val langue_src_mardi = langueSrc2.text.toString()
                val langue_dst_mardi = langueDst2.text.toString()
                val langue_src_mercr = langueSrc3.text.toString()
                val langue_dst_mercr = langueDst3.text.toString()
                val langue_src_jeudi = langueSrc4.text.toString()
                val langue_dst_jeudi = langueDst4.text.toString()
                val langue_src_vendr = langueSrc5.text.toString()
                val langue_dst_vendr = langueDst5.text.toString()
                val langue_src_samedi = langueSrc6.text.toString()
                val langue_dst_samedi = langueDst6.text.toString()
                val langue_src_dim = langueSrc7.text.toString()
                val langue_dst_dim = langueDst7.text.toString()
               sharePrefSettings(nb_mots, freq, langue_src_lundi, langue_dst_lundi, langue_src_mardi, langue_dst_mardi,
               langue_src_mercr, langue_dst_mercr, langue_src_jeudi, langue_dst_jeudi, langue_src_vendr, langue_dst_vendr,
               langue_src_samedi, langue_dst_samedi, langue_src_dim, langue_dst_dim)

                // start Service
                val serviceIntent = Intent(applicationContext, MyService::class.java).apply {
                    action = "setting"
                }
                startService(serviceIntent)
            }
        }
    }

    fun sharePrefSettings(nb_mots : Int, freq : Int, langue_src1 : String, langue_dst1 : String,
                          langue_src2 : String, langue_dst2 : String, langue_src3 : String, langue_dst3 : String,
                          langue_src4 : String, langue_dst4 : String, langue_src5 : String, langue_dst5 : String,
                          langue_src6 : String, langue_dst6 : String, langue_src7 : String, langue_dst7 : String){
        val sharedPrefences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = sharedPrefences.edit()
        editor.clear()
        putInfo(editor, "nb_mots", nb_mots)
        putInfo(editor, "frequence", freq)

        putInfo(editor, "lundi_src", langue_src1)
        putInfo(editor, "lundi_dst", langue_dst1)
        putInfo(editor, "mardi_src", langue_src2)
        putInfo(editor, "mardi_dst", langue_dst2)
        putInfo(editor, "mercr_src", langue_src3)
        putInfo(editor, "mercr_dst", langue_dst3)
        putInfo(editor,"jeudi_src", langue_src4)
        putInfo(editor, "jeudi_dst", langue_dst4)
        putInfo(editor, "vendr_src", langue_src5)
        putInfo(editor, "vendr_dst", langue_dst5)
        putInfo(editor, "samedi_src", langue_src6)
        putInfo(editor, "samedi_dst", langue_dst6)
        putInfo(editor, "dim_src", langue_src7)
        putInfo(editor, "dim_dst", langue_dst7)

        editor.commit()
    }

    private fun putInfo(editor : SharedPreferences.Editor, key : String, value : String) {
        editor.putString(key, value)
    }

    private fun putInfo(editor : SharedPreferences.Editor, key : String, value : Int){
        editor.putInt(key, value)
    }

    private fun setText(e: EditText, s : SharedPreferences, key : String, defaultValue : String) {
        e.setText(s.getString(key, defaultValue))
    }

    fun showOldSettings(){
        val sharedPrefences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val defVal = ""
        with(binding){
            setText(langueSrc1, sharedPrefences,"lundi_src", defVal)
            setText(langueSrc2, sharedPrefences, "mardi_src", defVal)
            setText(langueSrc3, sharedPrefences, "mercr_src", defVal)
            setText(langueSrc4, sharedPrefences, "jeudi_src", defVal)
            setText(langueSrc5, sharedPrefences, "vendr_src", defVal)
            setText(langueSrc6, sharedPrefences, "samedi_src", defVal)
            setText(langueSrc7, sharedPrefences, "dim_src", defVal)

            setText(langueDst1, sharedPrefences, "lundi_dst", defVal)
            setText(langueDst2, sharedPrefences, "mardi_dst", defVal)
            setText(langueDst3, sharedPrefences, "mercr_dst", defVal)
            setText(langueDst4, sharedPrefences, "jeudi_dst", defVal)
            setText(langueDst5, sharedPrefences, "vendr_dst", defVal)
            setText(langueDst6, sharedPrefences, "samedi_dst", defVal)
            setText(langueDst7, sharedPrefences, "dim_dst", defVal)

        }
    }
}