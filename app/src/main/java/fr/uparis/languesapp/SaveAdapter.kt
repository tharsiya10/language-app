package fr.uparis.languesapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.uparis.languesapp.databinding.ActivitySaveBinding
import fr.uparis.languesapp.databinding.SaveItemLayoutBinding

class SaveAdapter() : RecyclerView.Adapter<SaveAdapter.VH>() {

    private var allWords : List<Word> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = SaveItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = VH(binding)
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.word = allWords[position]
        holder.binding.word.text = holder.word.mot
        holder.binding.langueSrc.text = holder.word.langue_src
        holder.binding.langueDst.text = holder.word.langue_dst
        holder.binding.url.text = holder.word.adr_url
        holder.itemView.setBackgroundColor(
            if (position % 2 == 0) Color.argb(30, 0, 220, 0)
            else Color.argb(30, 0, 0, 220)
        )
    }

    override fun getItemCount(): Int = allWords.size

    fun setWords(words : List<Word>) {
        this.allWords = words
        notifyDataSetChanged()
    }

    class VH(val binding : SaveItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var word : Word
    }
}