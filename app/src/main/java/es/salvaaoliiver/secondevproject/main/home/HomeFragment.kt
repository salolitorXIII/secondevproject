package es.salvaaoliiver.secondevproject.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private lateinit var recipesList: MutableList<Recipe>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val userId = RecipesRepository.getUserId()
                val loadedRecipes = RecipesRepository.loadRecipes(userId)
                withContext(Dispatchers.Main) {
                    recipesAdapter = RecipesAdapter(loadedRecipes, this@HomeFragment)
                    binding.recyclerViewRecipes.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerViewRecipes.adapter = recipesAdapter
                    Log.d("HomeFragment", "Recipes loaded successfully")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("HomeFragment", "Error loading recipes", e)
                    Toast.makeText(requireContext(), "Error loading recipes", Toast.LENGTH_SHORT).show()
                }
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