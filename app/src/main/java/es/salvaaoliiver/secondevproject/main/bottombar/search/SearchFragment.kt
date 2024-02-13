package es.salvaaoliiver.secondevproject.main.bottombar.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.FragmentSearchBinding
import es.salvaaoliiver.secondevproject.main.MainActivity
import es.salvaaoliiver.secondevproject.main.database.Recipe
import es.salvaaoliiver.secondevproject.main.database.RecipesRepository
import es.salvaaoliiver.secondevproject.main.bottombar.home.RecipeDetailFragment
import es.salvaaoliiver.secondevproject.main.bottombar.home.RecipesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment(), RecipesAdapter.RecipeClickListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var recipesList: MutableList<Recipe>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = "Search"

        recipesList = mutableListOf()

        loadPublicRecipes()

        recipesAdapter = RecipesAdapter(recipesList, this@SearchFragment)
        binding.recyclerViewRecipes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recipesAdapter
        }
    }

    private fun loadPublicRecipes() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val publicRecipes = RecipesRepository.loadPublicRecipes()
                withContext(Dispatchers.Main) {
                    recipesList.addAll(publicRecipes)
                    recipesAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    override fun onRecipeClicked(recipe: Recipe) {
        val fragment = RecipeDetailFragment.newInstance(recipe)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.menuFragmentoContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
