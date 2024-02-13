package es.salvaaoliiver.secondevproject.main.bottombar.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.FragmentAddBinding
import es.salvaaoliiver.secondevproject.main.MainActivity
import es.salvaaoliiver.secondevproject.main.database.Recipe
import es.salvaaoliiver.secondevproject.main.database.RecipesRepository
import kotlinx.coroutines.launch
import java.util.UUID

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private var imagenPrincipal: Uri? = null
    private lateinit var storageRef: StorageReference
    private var isRecipePublic: Boolean = false

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = "Add"

        storageRef = FirebaseStorage.getInstance().reference

        binding.imageViewCover.setOnClickListener {
            openImagePicker()
        }

        binding.btnSaveRecipe.setOnClickListener {
            uploadRecipe()
        }

        binding.checkBoxPublic.setOnCheckedChangeListener { _, isChecked ->
            isRecipePublic = isChecked
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    private fun uploadRecipe() {
        val title = binding.editTextTitle.text.toString()
        val steps = binding.editTextSteps.text.toString()

        if (title.isNotEmpty() && steps.isNotEmpty()) {
            if (imagenPrincipal != null) {
                val imageRef = storageRef.child("imagenes/${UUID.randomUUID()}.jpg")

                val uploadTask = imageRef.putFile(imagenPrincipal!!)

                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val recipe = Recipe(title, steps, downloadUri.toString(), isRecipePublic)
                        lifecycleScope.launch {
                            try {
                                RecipesRepository.addRecipe(recipe) {
                                    Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Log.e("MainActivity", "Error al agregar receta", e)
                            }
                        }
                    } else {
                        Log.e("MainActivity", "Error al subir la imagen")
                    }
                }
            } else {
                val recipe = Recipe(title, steps, "", isRecipePublic)
                lifecycleScope.launch {
                    try {
                        RecipesRepository.addRecipe(recipe) {
                            Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.menuFragmentoContainer, AddFragment())
                                .commit()
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error al agregar receta", e)
                    }
                }
            }
        } else {
            Toast.makeText(context, "Title and steps cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            imagenPrincipal = data?.data
            binding.imageViewCover.setImageURI(imagenPrincipal)
        }
    }
}
