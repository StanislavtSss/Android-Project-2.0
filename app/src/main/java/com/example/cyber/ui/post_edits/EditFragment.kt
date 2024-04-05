package com.example.cyber.ui.post_edits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyber.databinding.FragmentCreatePostBinding
import com.example.cyber.ui.common.add_dialog.AddPostDialog
import com.example.cyber.ui.common.update_dialog.UpdatePostDialog
import kotlinx.coroutines.launch

class EditFragment : Fragment(), OnItemActionListener  {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    private lateinit var editViewModel: EditViewModel
    private lateinit var adapter: PostListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editViewModel = ViewModelProvider(requireActivity())[EditViewModel::class.java]

        adapter = PostListAdapter(this, true)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            editViewModel.postsFlow.collect { posts ->
                adapter.submitList(posts.mapList())
            }
        }

        binding.buttonAddItem.setOnClickListener {
            val dialog = AddPostDialog()
            dialog.show(parentFragmentManager, "AddPostDialog")
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

    override fun openPostDetails(item: PostModelEntity) {}
}
