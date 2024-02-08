package es.salvaaoliiver.secondevproject.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.FragmentRegistroUsuarioBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroUsuarioFragment : Fragment() {
    private lateinit var binding: FragmentRegistroUsuarioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistroUsuarioBinding.inflate(layoutInflater, container, false)

        binding.btnRegister.setOnClickListener {
            val email = binding.inputUser.text.toString()
            val password = binding.inputPassword.text.toString()
            val password2 = binding.inputPassword2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) {
                if (password == password2) {

                    lifecycleScope.launch(Dispatchers.IO) {
                        val userRegistered = AuthManager.register(email, password)
                        withContext(Dispatchers.Main) {

                            if (userRegistered != null) {
                                Toast.makeText(context, "Registered: ${userRegistered.email}", Toast.LENGTH_SHORT).show()
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.menuFragmentoContainerLogin, LoginFragment())
                                    .commit()
                            } else {
                                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }

                } else {
                    Toast.makeText(context, "LAS CONTRASEÑAS DEBEN COINCIDIR", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "RELLENA LOS CAMPOS", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}