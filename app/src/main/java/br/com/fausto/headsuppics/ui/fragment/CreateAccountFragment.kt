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
    lateinit var userEmail: String
    lateinit var userPassword: String
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
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Successfuly registered $it",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    val firebaseUser: FirebaseUser = it.result!!.user!!
                }
        }
    }
}