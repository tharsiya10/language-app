package fr.uparis.languesapp

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import fr.uparis.languesapp.databinding.SearchItemLayoutBinding

class SearchAdapter(val model : SearchViewModel) : RecyclerView.Adapter<SearchAdapter.VH>() {

    class VH(val binding : SearchItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        lateinit var dico : Dico
    }

    var dicos : List<Dico> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = SearchItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = VH(binding)
        holder.itemView.setOnClickListener {
            Log.d("projetclick", "clicked")
            if(model.checkedDico.value == dicos[holder.absoluteAdapterPosition]) {
                model.checkedDico.value = null
            }
            else {
                model.checkedDico.value = dicos[holder.absoluteAdapterPosition]
            }
            notifyDataSetChanged()
        }
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.dico = dicos[position]
        holder.binding.nom.text = holder.dico.nom
        holder.binding.langueSrc.text = holder.dico.langue_src
        holder.binding.langueDst.text = holder.dico.langue_dst
        affecteCouleurParite(holder, position)
        if(holder.dico == model.checkedDico.value) {
            holder.itemView.setSelected(true)
        }
        else {
            affecteCouleurParite(holder, position)
            holder.itemView.setSelected(false)
        }
    }

    override fun getItemCount(): Int = dicos.size

    fun affecteCouleurParite(holder: VH, position: Int) {
        if(position % 2 == 0){
            holder.itemView.setBackgroundColor(Color.argb(30, 0, 220, 0))
        }
        else {
            holder.itemView.setBackgroundColor(Color.argb(30, 0, 0, 220))
        }
    }

    fun set_dicos(d : List<Dico>){
        dicos = d
        notifyDataSetChanged()
    }
}