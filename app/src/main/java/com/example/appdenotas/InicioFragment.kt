package com.example.appdenotas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appdenotas.databinding.FragmentInicioBinding
import java.io.File

class InicioFragment : Fragment() {
    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    private lateinit var notaAdapter: NotaAdapter
    private val model: NotaViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notaAdapter = NotaAdapter(emptyList()) { nota ->
            val bundle = bundleOf("titulo" to nota.titulo, "texto" to nota.texto)
            requireView().findNavController()
                .navigate(R.id.action_inicioFragment_to_notaFragment, bundle)
        }
        binding.listaNotas.adapter = notaAdapter

        setupMenu()

        binding.add.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_inicioFragment_to_notaFragment)
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loadNotes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        val isGridView = model.ver == getString(R.string.ver_mosaico)
        notaAdapter.setViewType(isGridView)

        while (binding.listaNotas.itemDecorationCount > 0) {
            binding.listaNotas.removeItemDecorationAt(0)
        }

        binding.listaNotas.apply {
            layoutManager = if (isGridView) {
                val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                addItemDecoration(GridSpacingItemDecoration(2, spacing, true))
                GridLayoutManager(context, 2)
            } else {
                LinearLayoutManager(context)
            }
        }
    }

    private fun loadNotes(query: String? = null) {
        val notesDir = requireContext().filesDir
        val noteFiles = notesDir.listFiles { _, name -> name.endsWith(".txt") }
        var notes = noteFiles?.map { file ->
            val titulo = file.nameWithoutExtension
            val texto = file.readText()
            val lastModified = file.lastModified()
            Nota(titulo, texto, lastModified)
        } ?: emptyList()

        if (!query.isNullOrEmpty()) {
            notes = notes.filter { it.titulo.contains(query, ignoreCase = true) }
        }

        notes = if (model.ordenar == getString(R.string.ordenar_titulo)) {
            notes.sortedBy { it.titulo.lowercase() }
        } else {
            notes.sortedByDescending { it.lastModified }
        }

        notaAdapter.updateNotas(notes)
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
        loadNotes()
    }

    fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {

                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.toolbar_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_settings -> navigateToSettings()
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    fun navigateToSettings(): Boolean {
        requireView().findNavController().navigate(R.id.action_inicioFragment_to_ajustesFragment)
        return true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
