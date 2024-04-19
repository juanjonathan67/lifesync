package com.dicoding.lifesync.ui.main.tabs.notes

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
import com.dicoding.lifesync.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val notesViewModel by viewModels<NotesViewModel> { ViewModelFactory.getInstance(requireActivity()) }

    private val listNotesAdapter = ListNotesAdapter()

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
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_fab_note, null)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Create New Note")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val tfTitle = dialogView.findViewById<TextInputLayout>(R.id.tfFabNoteTitle)
                val tfDesc = dialogView.findViewById<TextInputLayout>(R.id.tfFabNoteDesc)
                val tfImage = dialogView.findViewById<TextInputLayout>(R.id.tfFabNoteImageUrl)

                if (tfTitle != null && tfDesc != null && tfImage != null){
                    notesViewModel.createNote(
                        tfTitle.editText?.text.toString(),
                        tfImage.editText?.text.toString(),
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