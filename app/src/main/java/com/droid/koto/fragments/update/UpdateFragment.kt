package com.droid.koto.fragments.update

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.droid.koto.R
import com.droid.koto.data.models.Priority
import com.droid.koto.data.models.TodoData
import com.droid.koto.data.viewmodels.TodoViewModel
import com.droid.koto.databinding.FragmentUpdateBinding
import com.droid.koto.fragments.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val todoViewModel: TodoViewModel by viewModels()

    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        binding.updateName.setText(args.currentItem.title)
        binding.updateDescription.setText(args.currentItem.description)
        binding.updatePriorities.setSelection(args.currentItem.priority.ordinal)
        binding.updatePriorities.onItemSelectedListener = sharedViewModel.listener
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_update) {
            updateTodo()
        }
        if(item.itemId == R.id.menu_delete) {
            confirmDelete()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want delete ${args.currentItem.title}")
            .setPositiveButton("Yes") { dg, id ->
                deleteTodo()
                Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            .setNegativeButton("Cancel") {dg, id ->
                dg.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteTodo() {
       todoViewModel.deleteData(args.currentItem)
    }

    private fun updateTodo() {
        val title = binding.updateName.text.toString()
        val description = binding.updateDescription.text.toString()
        val priority = binding.updatePriorities.selectedItem.toString()

        if(sharedViewModel.validateData(title, description)) {
            val update = TodoData(
                args.currentItem.id,
                title,
                sharedViewModel.parsePriority(priority),
                description,
            )

            todoViewModel.updateData(update)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(binding.root.context, "Successfully updated", Toast.LENGTH_SHORT)
                .show()
        } else {
            Snackbar.make(
                binding.root,
                "Please complete setting up the task",
                Snackbar.LENGTH_SHORT
            )
                .setBackgroundTint(Color.parseColor("#F85C50"))
                .setTextColor(Color.parseColor("#ffffff"))
                .show()
        }
    }
}