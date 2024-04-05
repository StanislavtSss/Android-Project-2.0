package com.example.cyber.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyber.databinding.FragmentPostsFeedBinding
import com.example.cyber.ui.common.details_dialog.ShowPostDetailsDialog
import com.example.cyber.ui.common.update_dialog.UpdatePostDialog
import com.example.cyber.ui.post_edits.EditViewModel
import com.example.cyber.ui.post_edits.OnItemActionListener
import com.example.cyber.ui.post_edits.PostListAdapter
import com.example.cyber.ui.post_edits.PostModelEntity
import com.example.cyber.ui.post_edits.map
import com.example.cyber.ui.post_edits.mapList
import kotlinx.coroutines.launch

class PostsFeedFragment : Fragment(), OnItemActionListener {

    private var _binding: FragmentPostsFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var editViewModel: EditViewModel
    private lateinit var adapter: PostListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostsFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editViewModel = ViewModelProvider(requireActivity())[EditViewModel::class.java]

        adapter = PostListAdapter(this, false)

        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.feedRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            editViewModel.postsFlow.collect { posts ->
                adapter.submitList(posts.mapList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEditItem(item: PostModelEntity) {
        val dialog = UpdatePostDialog.newInstance(item)
        dialog.show(requireActivity().supportFragmentManager, "UpdatePostDialog")
        viewLifecycleOwner.lifecycleScope.launch {
            editViewModel.postsFlow.collect { posts ->
                adapter.submitList(posts.mapList())
            }
        }
    }

    override fun onDeleteItem(item: PostModelEntity) {
        editViewModel.deletePost(item.map())
        viewLifecycleOwner.lifecycleScope.launch {
            editViewModel.postsFlow.collect { posts ->
                adapter.submitList(posts.mapList())
            }
        }
    }

    override fun openPostDetails(item: PostModelEntity) {
        val dialog = ShowPostDetailsDialog.newInstance(item)
        dialog.show(requireActivity().supportFragmentManager, "UpdatePostDialog")
    }
}
