package br.com.fausto.headsuppics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.fausto.headsuppics.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var createAccountText: TextView
    private lateinit var loginButton: Button
    private var userEmailText: TextInputEditText? = null
    private var userPasswordText: TextInputEditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createAccountText = requireView().findViewById(R.id.createAccountText)
        loginButton = requireView().findViewById(R.id.registerButton)
        userEmailText = requireView().findViewById(R.id.emailFieldEdit)
        userPasswordText = requireView().findViewById(R.id.passwordFieldEdit)

        createAccountText.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment()
            )
        }

        loginButton.setOnClickListener {
            val userEmail: String = userEmailText!!.text.toString()
            val userPassword: String = userPasswordText!!.text.toString()
            if ((userEmail!!.length < 5) || (userPassword!!.length < 5)) {
                Toast.makeText(
                    requireContext(),
                    "Email and password must have 6 characters at least",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPictureFragment())
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Wrong email and/or wrong password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}