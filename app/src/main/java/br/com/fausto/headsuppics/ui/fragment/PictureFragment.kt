package br.com.fausto.headsuppics.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fausto.headsuppics.R
import br.com.fausto.headsuppics.ui.adapter.ImageAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PictureFragment : Fragment() {

    private var imageRef = Firebase.storage.reference
    private lateinit var uploadButton: ExtendedFloatingActionButton
    private lateinit var imageTest: ImageView
    private lateinit var downloadButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var userEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        getImagesFromFirestore()

        userEmail = firebaseUser!!.email!!
        uploadButton = requireView().findViewById(R.id.uploadPicButton)
        imageTest = requireView().findViewById(R.id.imageTest)
        downloadButton = requireView().findViewById(R.id.downloadButton)
        recyclerView = requireView().findViewById(R.id.recyclerView)

        uploadButton.setOnClickListener {
            findNavController().navigate(PictureFragmentDirections.actionPictureFragmentToGalleryFragment())
        }

        downloadButton.setOnClickListener {
//            val randomName = java.util.UUID.randomUUID().toString()
//            downloadImageFromFirestore("imagem_teste")
        }
    }

//    private fun downloadImageFromFirestore(filename: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val maxDownloadSize = 5L * 1024 * 2014
//                val bytes = imageRef.child("images/$filename").getBytes(maxDownloadSize).await()
//                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                withContext(Dispatchers.Main) {
//                    imageTest.setImageBitmap(bitmap)
//                }
//            } catch (exception: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        }
//    }

    private fun getImagesFromFirestore() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val images = imageRef.child("images/$userEmail/").listAll().await()
                val imagesUrls = mutableListOf<String>()
                Log.e("contagem", images.items.size.toString())
                for (image in images.items) {
                    val url = image.downloadUrl.await()
                    imagesUrls.add(url.toString())
                }
                withContext(Dispatchers.Main) {
                    recyclerView.run {
                        adapter = ImageAdapter(imagesUrls) {
                            itemListClick(it)
                        }
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    }
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun itemListClick(url: String) {
        Toast.makeText(requireContext(), url, Toast.LENGTH_SHORT).show()
        Log.e("image url", url)
        deleteImage(url)
    }

    private fun deleteImage(filename: String) {
        val imageUrLRef = Firebase.storage.getReferenceFromUrl(filename)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                imageUrLRef.delete().await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }
}