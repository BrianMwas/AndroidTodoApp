package com.droid.koto.fragments.add

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.droid.koto.R
import com.droid.koto.data.models.Priority
import com.droid.koto.data.models.TodoData
import com.droid.koto.data.viewmodels.TodoViewModel
import com.droid.koto.databinding.FragmentAddNewBinding
import com.droid.koto.fragments.SharedViewModel
import com.google.android.material.snackbar.Snackbar


class AddNewFragment : Fragment() {
    private val createViewModel : TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding : FragmentAddNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewBinding.inflate(inflater, container, false)

//        Show Menu
        setHasOptionsMenu(true)
        binding.prioritiesSpinner.onItemSelectedListener = sharedViewModel.listener
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.create_menu) {
            createTodo()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createTodo() {
        val title = binding.nameTv.text.toString()
        val description = binding.descriptionTv.text.toString()
        val priority = binding.prioritiesSpinner.selectedItem.toString()

        val validated = sharedViewModel.validateData(title, description)

        if (validated) {
            val data = TodoData(
                    id =  0,
                    title = title,
                    description = description,
                    priority = sharedViewModel.parsePriority(priority)
            )

            createViewModel.insertData(data)
            Toast.makeText(context, "Successfully created task", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addNewFragment_to_listFragment)
        } else {
            Snackbar.make(binding.root, "Please fill all fields", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.parseColor("#F85C50"))
                    .setTextColor(Color.parseColor("#ffffff"))
                    .show()

        }
    }
}