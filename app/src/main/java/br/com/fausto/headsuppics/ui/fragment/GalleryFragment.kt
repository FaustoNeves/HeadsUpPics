package br.com.fausto.headsuppics.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.fausto.headsuppics.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val REQUEST_CODE_IMAGE_PICK = 0

class GalleryFragment : Fragment() {

    private var currentFilePath: Uri? = null
    private var imageRef = Firebase.storage.reference
    private lateinit var imageToUpload: ImageView
    private lateinit var userEmail: String
    lateinit var uploadButton: Button
    private lateinit var cancelButton: Button
    lateinit var openGallery: Button
    private lateinit var galleryProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadButton = requireView().findViewById(R.id.uploadButton)
        cancelButton = requireView().findViewById(R.id.cancelButton)
        imageToUpload = requireView().findViewById(R.id.imageToUpload)
        galleryProgressBar = requireView().findViewById(R.id.galleryProgressBar)
        openGallery = requireView().findViewById(R.id.openMediaGallery)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        userEmail = firebaseUser!!.email!!

        openMediaGallery()

        uploadButton.setOnClickListener {
            val randomName = java.util.UUID.randomUUID().toString()
            uploadImageToFirestore(randomName)
        }

        cancelButton.setOnClickListener {
            findNavController().navigate(GalleryFragmentDirections.actionGalleryFragmentToPictureFragment())
        }

        openGallery.setOnClickListener {
            openMediaGallery()
        }
    }

    private fun openMediaGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                currentFilePath = it
                imageToUpload.setImageURI(it)
            }
        }
    }

    private fun uploadImageToFirestore(filename: String) {
        galleryProgressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                currentFilePath.let {
                    imageRef.child("images/$userEmail/$filename").putFile(it!!).await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully uploaded",
                            Toast.LENGTH_SHORT
                        ).show()
                        galleryProgressBar.visibility = View.INVISIBLE
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                    galleryProgressBar.visibility = View.INVISIBLE
                }
            }
        }
    }
}