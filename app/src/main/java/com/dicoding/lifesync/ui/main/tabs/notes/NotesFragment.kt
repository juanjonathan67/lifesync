package com.dicoding.lifesync.ui.main.tabs.notes

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.lifesync.Manifest
import com.dicoding.lifesync.R
import com.dicoding.lifesync.data.Result
import com.dicoding.lifesync.data.local.entity.NoteEntity
import com.dicoding.lifesync.data.local.entity.UserWithNoteEntity
import com.dicoding.lifesync.data.remote.response.NoteItem
import com.dicoding.lifesync.data.remote.response.UserResponse
import com.dicoding.lifesync.databinding.DialogFabNoteBinding
import com.dicoding.lifesync.databinding.FragmentNotesBinding
import com.dicoding.lifesync.ui.landing.signup.SignUpViewModel
import com.dicoding.lifesync.ui.main.MainActivity.Companion.EXTRA_USER
import com.dicoding.lifesync.ui.main.UCropActivity
import com.dicoding.lifesync.utils.ViewModelFactory
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val notesViewModel by viewModels<NotesViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    private val listNotesAdapter = ListNotesAdapter()
    private var currentImageUri: Uri? = null

    private lateinit var cropImage: ActivityResultLauncher<String>
    private val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_fab_note, null)

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == 101) {
            // val tvNoteImage
            val cropResult = result.data?.getStringExtra(UCropActivity.CROP_RESULT)
            currentImageUri = result.data?.data

            if (cropResult != null) {
                currentImageUri = Uri.parse(cropResult)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cropImage = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            val cropIntent = Intent(requireActivity(), UCropActivity::class.java)
            cropIntent.putExtra(UCropActivity.SEND_IMAGE_DATA, result.toString())
            startForResult.launch(cropIntent)
        }

        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvNotes.adapter = listNotesAdapter

        val userParcelable = if (Build.VERSION.SDK_INT >= 33) {
            requireActivity().intent.getParcelableExtra(EXTRA_USER, UserWithNoteEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            requireActivity().intent.getParcelableExtra(EXTRA_USER)
        }

        if (userParcelable != null) {
            setNotes(userParcelable.note)
        }

        binding.fabNotesAdd.setOnClickListener {_ -> showDialog()}
    }

    private fun showDialog() {
        val tfTitle = dialogView.findViewById<TextInputLayout>(R.id.tfFabNoteTitle)
        val tfDesc = dialogView.findViewById<TextInputLayout>(R.id.tfFabNoteDesc)
        val btChooseImage = dialogView.findViewById<MaterialButton>(R.id.btFabNoteImage)

        btChooseImage.setOnClickListener {
            startGallery()
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Create New Note")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                if (tfTitle != null && tfDesc != null && currentImageUri != null){
                    notesViewModel.createNote(
                        tfTitle.editText?.text.toString(),
                        currentImageUri.toString(),
                        tfDesc.editText?.text.toString()
                    ).observe(viewLifecycleOwner) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.notesProgressBar.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    setNotes(result.data.note)
                                    binding.notesProgressBar.visibility = View.GONE
                                }
                                is Result.Error -> {
                                    Toast.makeText(requireContext(), "Error Adding Note", Toast.LENGTH_SHORT).show()
                                    binding.notesProgressBar.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Error Occured", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun startGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        Dexter.withContext(requireActivity())
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    cropImage.launch("image/*")
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                    permissionToken?.continuePermissionRequest()
                }

            }).check()
    }

    private fun setNotes(listNote: List<NoteEntity>) {
        listNotesAdapter.submitList(listNote)
        binding.rvNotes.adapter = listNotesAdapter

        listNotesAdapter.setOnItemClickCallback(object : ListNotesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: NoteEntity) {
                Toast.makeText(requireActivity(), "Clicked ${data.title}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}