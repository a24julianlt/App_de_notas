package com.example.appdenotas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.appdenotas.databinding.FragmentAjustesBinding

class AjustesFragment : Fragment() {
    private var _binding: FragmentAjustesBinding? = null
    private val binding get() = _binding!!

    private val model: NotaViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()

        binding.tema.setOnClickListener {
            binding.switchTema.isChecked = !binding.switchTema.isChecked
            if (binding.switchTema.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.txtTema.text = getString(R.string.tema)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.switchTema.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.ordenar.setOnClickListener {
            if (binding.txtOrdenar.text == getString(R.string.ordenar_fecha)) {
                binding.txtOrdenar.text = getString(R.string.ordenar_titulo)
                binding.imgOrdenar.setImageResource(R.drawable.title)
            } else {
                binding.txtOrdenar.text = getString(R.string.ordenar_fecha)
                binding.imgOrdenar.setImageResource(R.drawable.calendar)
            }
            model.ordenar = binding.txtOrdenar.text.toString()
        }

        binding.ver.setOnClickListener {
            if (binding.txtVer.text == getString(R.string.ver_mosaico)) {
                binding.txtVer.text = getString(R.string.ver_lista)
                binding.imgVer.setImageResource(R.drawable.list)
            } else {
                binding.txtVer.text = getString(R.string.ver_mosaico)
                binding.imgVer.setImageResource(R.drawable.mosaic)
            }
            model.ver = binding.txtVer.text.toString()
        }


        binding.politica.setOnClickListener {
            findNavController().navigate(R.id.action_ajustesFragment_to_politicaFragment)
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
