package es.salvaaoliiver.secondevproject.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.salvaaoliiver.secondevproject.R
import es.salvaaoliiver.secondevproject.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding

    private var listener: LoginListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is LoginListener){
            listener = context
        } else{
            throw Exception("EXCEPCION")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.forgotPassword.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLogin -> {
                listener?.onBtnLoginClicked(
                    binding.inputUser.text.toString(),
                    binding.inputPassword.text.toString()
                )
            }
            R.id.btnRegister -> {
                listener?.onBtnRegisterClicked()
            }
            R.id.forgot_password -> {
                listener?.onBtnForgotPasswordClicked()
            }
        }
    }

    interface LoginListener{
        fun onBtnLoginClicked(usuario: String, password: String)
        fun onBtnRegisterClicked()
        fun onBtnForgotPasswordClicked()
    }
}