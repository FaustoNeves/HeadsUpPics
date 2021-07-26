package br.com.fausto.headsuppics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.fausto.headsuppics.R

class LoginFragment : Fragment() {

    private lateinit var createAccountText: TextView
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createAccountText = requireView().findViewById(R.id.createAccountText)
        loginButton = requireView().findViewById(R.id.loginButton)

        createAccountText.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment()
            )
        }

        loginButton.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToPictureFragment()
            )
        }
    }
}