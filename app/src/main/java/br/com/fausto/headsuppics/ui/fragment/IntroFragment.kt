package br.com.fausto.headsuppics.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.fausto.headsuppics.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            delay(3000)
            findNavController().navigate(
                IntroFragmentDirections.actionIntroFragmentToLoginFragment()
            )
        }
    }
}