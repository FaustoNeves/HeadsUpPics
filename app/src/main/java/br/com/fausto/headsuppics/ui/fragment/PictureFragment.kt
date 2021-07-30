package br.com.fausto.headsuppics.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.fausto.headsuppics.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val REQUEST_CODE_IMAGE_PICK = 0

class PictureFragment : Fragment() {

    var currentFilePath: Uri? = null
    var imageRef = Firebase.storage.reference
    lateinit var uploadButton: ExtendedFloatingActionButton
    lateinit var imageTest: ImageView
    var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUser!!.getIdToken(true).addOnCompleteListener {
            token = it.result!!.token
        }

        uploadButton = requireView().findViewById(R.id.uploadPicButton)
        imageTest = requireView().findViewById(R.id.imageTest)

        uploadButton.setOnClickListener {
            val randomName = java.util.UUID.randomUUID().toString()
            uploadImageToFirestore("$token|$randomName")
        }
        imageTest.setOnClickListener {
            pickImage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }

    private fun pickImage() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
        }
    }

    private fun uploadImageToFirestore(filename: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                currentFilePath.let {
                    imageRef.child("images/$filename").putFile(it!!).await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully uploaded",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                currentFilePath = it
                imageTest.setImageURI(it)
            }
        }
    }
}