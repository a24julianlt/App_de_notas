package com.example.appdenotas

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.appdenotas.databinding.FragmentPoliticaBinding

class PoliticaFragment : Fragment() {
    private var _binding: FragmentPoliticaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoliticaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
    }

    override fun onResume() {
        super.onResume()
        // Cuando el fragmento es visible, aplicamos el tamaño de letra pequeño
        activity?.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setTitleTextAppearance(requireContext(), R.style.TextAppearance_AppDeNotas_ToolbarTitle_Small)
        }
    }

    override fun onPause() {
        super.onPause()
        // Cuando el fragmento se va, restauramos el tamaño de letra original
        activity?.findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            toolbar.setTitleTextAppearance(requireContext(), R.style.TextAppearance_AppDeNotas_ToolbarTitle)
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
