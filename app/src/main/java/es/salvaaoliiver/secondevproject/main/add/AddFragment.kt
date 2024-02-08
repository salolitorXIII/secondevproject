package es.salvaaoliiver.secondevproject.main.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import es.salvaaoliiver.secondevproject.databinding.FragmentAddBinding
import java.util.UUID

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private var imagenPrincipal: Uri? = null

    private lateinit var storageRef: StorageReference

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

        storageRef = FirebaseStorage.getInstance().reference

        binding.imageViewCover.setOnClickListener {
            openImagePicker()
        }

        binding.btnSaveRecipe.setOnClickListener {
            uploadRecipe()
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

        if (title.isNotEmpty() && steps.isNotEmpty() && imagenPrincipal != null) {
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
                    // Ahora puedes guardar 'downloadUri' en Firestore junto con otros datos de la receta (título, pasos, etc.)
                    // Por simplicidad, aquí solo mostramos la URL de la imagen subida
                    println("URL de la imagen subida: $downloadUri")
                } else {
                    // Manejar el error
                }
            }
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
