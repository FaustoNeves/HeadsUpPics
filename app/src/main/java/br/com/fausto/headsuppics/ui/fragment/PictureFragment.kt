package br.com.fausto.headsuppics.ui.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var userEmail: String
    var imagesUrls: MutableList<String>? = mutableListOf()
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userEmail = firebaseUser!!.email!!
    }

    override fun onResume() {
        super.onResume()

        getImagesFromFirestore()
        userEmail = firebaseUser!!.email!!
        uploadButton = requireView().findViewById(R.id.uploadPicButton)
        recyclerView = requireView().findViewById(R.id.recyclerView)

        uploadButton.setOnClickListener {
            findNavController().navigate(PictureFragmentDirections.actionPictureFragmentToGalleryFragment())
        }
    }

    private fun getImagesFromFirestore() {
        imagesUrls!!.clear()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val images = imageRef.child("images/$userEmail/").listAll().await()
                for (image in images.items) {
                    val url = image.downloadUrl.await()
                    imagesUrls!!.add(url.toString())
                }
                setRecyclerView(imagesUrls!!)
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun itemDeleteListClick(url: String) {
        deleteImage(url)
    }

    private fun itemDownloadListClick(url: String) {
        downloadImage(url)
    }

    private fun downloadImage(filename: String) {
        Log.e("download filename", filename)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                imageRef.downloadUrl
                val maxDownloadSize = 5L * 1024 * 2014
                val bytes = imageRef.child("images/$filename").getBytes(maxDownloadSize).await()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun deleteImage(filename: String) {
        Log.e("delete filename", filename)
        val imageUrLRef = Firebase.storage.getReferenceFromUrl(filename)
        imagesUrls!!.remove(filename)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                imageUrLRef.delete().await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT)
                        .show()
                    getImagesFromFirestore()
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun setRecyclerView(UrlList: List<String>) {
        withContext(Dispatchers.Main) {
            recyclerView.run {
                imageAdapter = ImageAdapter(UrlList, {
                    itemDeleteListClick(it)
                }, {
                    itemDownloadListClick(it)
                })
                adapter = imageAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
    }
}