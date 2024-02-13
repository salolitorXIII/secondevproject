package es.salvaaoliiver.secondevproject.main.bottombar.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import es.salvaaoliiver.secondevproject.databinding.FragmentRecipesDetailBinding
import es.salvaaoliiver.secondevproject.main.database.Recipe

class RecipeDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecipesDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = requireArguments().getSerializable(ARG_RECIPE) as? Recipe

        recipe?.let {
            binding.apply {
                textTitle.text = it.title
                Glide.with(binding.root.context)
                    .load(recipe.imagePath)
                    .into(binding.imageRecipe)
                textSteps.text = it.steps
            }
        }
    }

    companion object {
        private const val ARG_RECIPE = "recipe"

        fun newInstance(recipe: Recipe): RecipeDetailFragment {
            val fragment = RecipeDetailFragment()
            val args = Bundle()
            args.putSerializable(ARG_RECIPE, recipe)
            fragment.arguments = args
            return fragment
        }
    }
}