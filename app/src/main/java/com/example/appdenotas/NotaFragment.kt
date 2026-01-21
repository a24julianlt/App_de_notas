package com.example.appdenotas

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.appdenotas.databinding.FragmentNotaBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class NotaFragment : Fragment() {
    private var _binding: FragmentNotaBinding? = null
    private val binding get() = _binding!!

    private var oldTitle: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()

        val titulo = arguments?.getString("titulo")
        val texto = arguments?.getString("texto")

        if (titulo != null) {
            binding.titulo.setText(titulo)
            oldTitle = titulo
        }
        if (texto != null) {
            binding.texto.setText(texto)
        }

        binding.saveNote.setOnClickListener {
            val nuevoTitulo = binding.titulo.text.toString().trim()
            val nuevoTexto = binding.texto.text.toString()

            if (nuevoTitulo.isNotEmpty()) {
                saveNoteToFile(nuevoTitulo, nuevoTexto)
                findNavController().navigateUp()
            } else {
                Toast.makeText(
                    requireContext(),
                    "El título no puede estar vacío",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.delete.setOnClickListener {
            if (oldTitle != null) {
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Borrar nota")
                    .setMessage("¿Estás seguro de que quieres borrar esta nota? Esta acción no se puede deshacer.")
                    .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Borrar") { dialog, _ ->
                        deleteNoteFile(oldTitle!!)
                        findNavController().navigateUp()
                        dialog.dismiss()
                    }
                    .show()

                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.circle_bg)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.white_circle_bg)

            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun deleteNoteFile(titulo: String) {
        try {
            val file = File(requireContext().filesDir, "$titulo.txt")
            if (file.exists()) {
                file.delete()
                Toast.makeText(requireContext(), "Nota borrada", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error al borrar la nota: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun saveNoteToFile(titulo: String, texto: String) {
        try {
            if (oldTitle != null && oldTitle != titulo) {
                val oldFile = File(requireContext().filesDir, "$oldTitle.txt")
                if (oldFile.exists()) {
                    oldFile.delete()
                }
            }
            val file = File(requireContext().filesDir, "$titulo.txt")
            file.writeText(texto)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error al guardar la nota: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.atras_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_back_right -> {
                        findNavController().navigateUp()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
