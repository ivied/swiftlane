package dev.udev.swiftlane.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.udev.swiftlane.R
import dev.udev.swiftlane.adapters.PicListAdapter
import dev.udev.swiftlane.databinding.FragmentPhotoBinding
import dev.udev.swiftlane.di.Injectable
import dev.udev.swiftlane.infrastructure.BaseExecutors
import dev.udev.swiftlane.ui.viewmodels.PicViewModel
import dev.udev.swiftlane.utils.clearable
import kotlinx.android.synthetic.main.fragment_photo.*
import javax.inject.Inject

class PicFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var executors: BaseExecutors

    lateinit var dataBindingComponent: DataBindingComponent
    var binding by clearable<FragmentPhotoBinding>()
    var adapter by clearable<PicListAdapter>()
    lateinit var picViewModel: PicViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        picViewModel = ViewModelProviders.of(this, viewModelFactory).get(PicViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()

        val rvAdapter = PicListAdapter(appExecutors = executors) {
            Toast.makeText(activity, "To be implemented later", Toast.LENGTH_LONG).show()
        }

        binding.query = picViewModel.query
        binding.photoList.adapter = rvAdapter
        adapter = rvAdapter

        initInputListener()
    }

    private fun initInputListener() {
        binding.searchView.setOnQueryTextListener (object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    fetch(search_view, query!!)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
        })
    }

    private fun fetch(v: View, query: String) {
        dismissKeyboard(v.windowToken)
        picViewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initRecyclerView() {
        binding.photoList.layoutManager = LinearLayoutManager(activity)
        binding.photoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastVisibleItemPosition() == adapter.itemCount - 1) {
                    Toast.makeText(activity, "To be added later", Toast.LENGTH_LONG).show()
                }
            }
        })
        binding.searchResult = picViewModel.results
        picViewModel.results.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result?.data)
        })
    }
}