package br.com.fausto.headsuppics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.fausto.headsuppics.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CreateAccountFragment : Fragment() {

    lateinit var emailText: TextInputEditText
    lateinit var passwordText: TextInputEditText
    private var userEmail: String? = null
    private var userPassword: String? = null
    lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailText = requireView().findViewById(R.id.emailFieldEdit)
        passwordText = requireView().findViewById(R.id.passwordFieldEdit)

        registerButton = requireView().findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            userEmail = emailText.text.toString()
            userPassword = passwordText.text.toString()
            if ((userEmail!!.length < 5) || (userPassword!!.length < 5)) {
                Toast.makeText(
                    requireContext(),
                    "Email and password must have 6 characters at least",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(userEmail!!, userPassword!!)
                    .addOnCompleteListener {
                        val firebaseUser: FirebaseUser = it.result!!.user!!
                        val response = firebaseUser.getIdToken(false)
                        val tokenResponse = response.result
                        val token = tokenResponse!!.token
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "token $token",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
            emailText.setText("")
            passwordText.setText("")
        }
    }
}