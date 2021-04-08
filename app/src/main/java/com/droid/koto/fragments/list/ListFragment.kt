package com.droid.koto.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.droid.koto.R
import com.droid.koto.data.models.TodoData
import com.droid.koto.data.viewmodels.TodoViewModel
import com.droid.koto.databinding.FragmentListBinding
import com.droid.koto.fragments.SharedViewModel
import com.droid.koto.fragments.list.adapter.ListAdapter
import com.droid.koto.utils.hideKeyboard
import com.droid.koto.utils.observeOnce
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    private val todoViewModel :TodoViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy {
        ListAdapter()
    }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addNewFragment)
        }

        setupRecyclerView()

        todoViewModel.getTodos().observe(viewLifecycleOwner, {
            adapter.setData(it)
            sharedViewModel.checkDbForTodos(it)
        })

        sharedViewModel.isEmpty.observe(viewLifecycleOwner, {
            showEmptyView(it)
        })

        setHasOptionsMenu(true)

        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerView() {
        val listView = binding.listView
        listView.adapter = adapter
        listView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        listView.itemAnimator = FadeInAnimator().apply {
            addDuration = 400
            removeDuration = 200

        }

        swipeToDelete(listView)
    }

    private fun showEmptyView(empty: Boolean) {
        if(empty) {
           binding.emptyboxImg.visibility = View.VISIBLE
           binding.emptyboxTv.visibility = View.VISIBLE
        } else {
            binding.emptyboxImg.visibility = View.GONE
            binding.emptyboxTv.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val sView = search.actionView as? SearchView
        sView?.isSubmitButtonEnabled = true
        sView?.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun swipeToDelete(recyclerView: RecyclerView)  {
        val swipeToDeleteCallback = object: SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val data = adapter.dataList[viewHolder.adapterPosition]
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                todoViewModel.deleteData(data)
//                Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()
                restoreDeleteTodo(viewHolder.itemView, data, viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeleteTodo(view:View, todoData: TodoData, position: Int) {
        val snack = Snackbar.make(view, "Deleted ${todoData.title}", Snackbar.LENGTH_LONG)
        snack.setAction("UNDO") {
            todoViewModel.insertData(todoData)
        }
        snack.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_deleteAll) {
            confirmDelete()
        }

        when(item.itemId) {
            R.id.menu_deleteAll -> confirmDelete()
            R.id.priorityHigh -> todoViewModel.sortByHighPriority().observe(viewLifecycleOwner, {
                adapter.setData(it)
            })
            R.id.priorityLow -> todoViewModel.sortByLowPriority().observe(viewLifecycleOwner, {
                adapter.setData(it)
            })
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDelete() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Delete All")
            .setMessage("Are sure you want to delete all your tasks?")
            .setPositiveButton("Continue") { _, _ ->
                todoViewModel.deleteEverything()
                Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dg, _ ->
                dg.dismiss()
            }
            .create()

        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchAllTasks(query)
        }
        return true
    }



    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null) {
            searchAllTasks(newText)
        }
        return true
    }

    private fun searchAllTasks(query: String) {
        val searchQ = "%$query%"
        todoViewModel.searchTasks(searchQ).observeOnce(viewLifecycleOwner, {
            it?.let {
                adapter.setData(it)
            }
        })
    }
}