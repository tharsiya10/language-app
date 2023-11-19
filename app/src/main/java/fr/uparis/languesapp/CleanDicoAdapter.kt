package fr.uparis.languesapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.uparis.languesapp.databinding.DicoItemLayoutBinding
import fr.uparis.languesapp.databinding.WordItemLayoutBinding

class CleanDicoAdapter(val model : CleanWordViewModel) : RecyclerView.Adapter<CleanDicoAdapter.VH>() {

    private var allDicos : List<Dico> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleanDicoAdapter.VH {
        val binding = DicoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = CleanDicoAdapter.VH(binding)
        return holder
    }

    override fun onBindViewHolder(holder: CleanDicoAdapter.VH, position: Int) {
        holder.dico = allDicos[position]
        holder.binding.dico.text = holder.dico.nom
        holder.binding.langueSrc.text = holder.dico.langue_src
        holder.binding.langueDst.text = holder.dico.langue_dst
        holder.itemView.setBackgroundColor(
            if (position % 2 == 0) Color.argb(30, 0, 220, 0)
            else Color.argb(30, 0, 0, 220)
        )
    }

    override fun getItemCount(): Int = allDicos.size

    fun setDicos(dico : List<Dico>) {
        this.allDicos = dico
        model.dicosToDelete = dico
        notifyDataSetChanged()
    }

    class VH(val binding : DicoItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var dico : Dico
    }
}