package br.com.fausto.headsuppics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.fausto.headsuppics.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateAccountFragment : Fragment() {

    lateinit var emailText: TextInputEditText
    lateinit var passwordText: TextInputEditText
    private var userEmail: String? = null
    private var userPassword: String? = null
    lateinit var registerButton: Button
    private lateinit var loginProgressBar: ProgressBar

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
        loginProgressBar = requireView().findViewById(R.id.loginProgressBar)
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
                loginProgressBar.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(userEmail!!, userPassword!!)
                        .addOnCompleteListener {
                            try {
                                val firebaseUser: FirebaseUser = it.result!!.user!!
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (it.isSuccessful) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Successfully registered",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            } catch (exception: Exception) {
                                Toast.makeText(
                                    requireContext(),
                                    exception.cause!!.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            loginProgressBar.visibility = View.INVISIBLE
                        }
                    emailText.setText("")
                    passwordText.setText("")
                }
            }
        }
    }
}