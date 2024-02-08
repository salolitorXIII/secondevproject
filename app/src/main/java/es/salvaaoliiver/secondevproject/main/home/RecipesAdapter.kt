package es.salvaaoliiver.secondevproject.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.salvaaoliiver.secondevproject.databinding.ItemRecipeBinding
import es.salvaaoliiver.secondevproject.main.database.Recipe

class RecipesAdapter(
    private val recipes: MutableList<Recipe>,
    private val listener: RecipeClickListener
) : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)

        holder.itemView.setOnClickListener {
            listener.onRecipeClicked(recipe)
        }
    }

    override fun getItemCount(): Int = recipes.size

    class ViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.textTitle.text = recipe.title
            Glide.with(binding.root.context)
                .load(recipe.imagePath)
                .into(binding.imageRecipe)
        }

    }


    interface RecipeClickListener {
        fun onRecipeClicked(recipe: Recipe)
    }
}