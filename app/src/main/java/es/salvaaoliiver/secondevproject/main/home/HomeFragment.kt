package es.salvaaoliiver.secondevproject.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.FragmentHomeBinding
import es.salvaaoliiver.secondevproject.main.database.Recipe
import es.salvaaoliiver.secondevproject.main.database.RecipesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(), RecipesAdapter.RecipeClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recipesAdapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val userId = RecipesRepository.getUserId()

        lifecycleScope.launch(Dispatchers.IO) {
            try {

                val loadedRecipes = RecipesRepository.loadRecipes(userId)
                withContext(Dispatchers.Main) {
                    recipesAdapter = RecipesAdapter(loadedRecipes, this@HomeFragment)
                    binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerViewRecipes.adapter = recipesAdapter
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }

        return binding.root
    }

    override fun onRecipeClicked(recipe: Recipe) {
        val fragment = RecipeDetailFragment.newInstance(recipe)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.menuFragmentoContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}