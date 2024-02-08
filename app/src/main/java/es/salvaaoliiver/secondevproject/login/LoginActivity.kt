package es.salvaaoliiver.secondevproject.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.ActivityLoginBinding
import es.salvaaoliiver.secondevproject.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity(), LoginFragment.LoginListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onBtnLoginClicked(usuario: String, password: String) {
        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "RELLENA LOS CAMPOS", Toast.LENGTH_SHORT).show()
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                val userLogged = AuthManager.login(usuario, password)
                withContext(Dispatchers.Main) {
                    if (userLogged != null) {
                        AuthManager.setCorreo(usuario)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "BAD CREDENTIALS", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onBtnRegisterClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.menuFragmentoContainerLogin, RegistroUsuarioFragment())
            .commit()
    }

    override fun onBtnForgotPasswordClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.menuFragmentoContainerLogin, ResetpasswordFragment())
            .commit()
    }
}