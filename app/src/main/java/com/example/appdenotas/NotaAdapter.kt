package com.example.appdenotas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appdenotas.databinding.NotaItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotaAdapter(
    private var notas: List<Nota>,
    private val onItemClick: (Nota) -> Unit
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val binding = NotaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notas[position]
        holder.bind(nota)
        holder.itemView.setOnClickListener {
            onItemClick(nota)
        }
    }

    override fun getItemCount(): Int = notas.size

    fun updateNotas(newNotas: List<Nota>) {
        notas = newNotas
        notifyDataSetChanged()
    }

    class NotaViewHolder(private val binding: NotaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nota: Nota) {
            binding.notaTitulo.text = nota.titulo
            binding.notaTexto.text = nota.texto

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = Date(nota.lastModified)
            binding.modificacion.text = sdf.format(date)
        }
    }
}
