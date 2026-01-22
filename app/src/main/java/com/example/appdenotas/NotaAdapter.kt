package com.example.appdenotas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.appdenotas.databinding.NotaGridItemBinding
import com.example.appdenotas.databinding.NotaItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotaAdapter(
    private var notas: List<Nota>,
    private val onItemClick: (Nota) -> Unit
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    private var isGridView = false

    companion object {
        private const val LIST_VIEW_TYPE = 0
        private const val GRID_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val binding = if (viewType == GRID_VIEW_TYPE) {
            NotaGridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            NotaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return NotaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notas[position]
        holder.bind(nota)
        holder.itemView.setOnClickListener {
            onItemClick(nota)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridView) GRID_VIEW_TYPE else LIST_VIEW_TYPE
    }

    override fun getItemCount(): Int = notas.size

    fun updateNotas(newNotas: List<Nota>) {
        notas = newNotas
        notifyDataSetChanged()
    }

    fun setViewType(isGrid: Boolean) {
        if (isGridView != isGrid) {
            isGridView = isGrid
            notifyDataSetChanged()
        }
    }

    class NotaViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nota: Nota) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = Date(nota.lastModified)
            val formattedDate = sdf.format(date)

            if (binding is NotaItemBinding) {
                binding.notaTitulo.text = nota.titulo
                binding.notaTexto.text = nota.texto
                binding.modificacion.text = formattedDate
            } else if (binding is NotaGridItemBinding) {
                binding.notaTitulo.text = nota.titulo
                binding.notaTexto.text = nota.texto
                binding.modificacion.text = formattedDate
            }
        }
    }
}
