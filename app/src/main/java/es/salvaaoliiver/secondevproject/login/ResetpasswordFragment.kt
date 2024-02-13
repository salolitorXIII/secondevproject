package es.salvaaoliiver.secondevproject.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.FragmentResetpasswordBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResetpasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetpasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.menuFragmentoContainerLogin, LoginFragment())
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetpasswordBinding.inflate(layoutInflater, container, false)

        binding.btnEnviar.setOnClickListener {
            val email = binding.inputUser.text.toString()
            if (email.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        AuthManager.sendPasswordResetEmail(email)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Reseteo de contraseña enviado", Toast.LENGTH_SHORT).show()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.menuFragmentoContainerLogin, LoginFragment())
                                .commit()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Introduce el email primero", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

}