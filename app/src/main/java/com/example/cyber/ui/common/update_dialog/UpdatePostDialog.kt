package com.example.cyber.ui.common.update_dialog

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cyber.databinding.PostDialogLayoutBinding
import com.example.cyber.ui.common.add_dialog.PostViewModel
import com.example.cyber.ui.post_edits.PostModelEntity
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class UpdatePostDialog : DialogFragment() {
    private var _binding: PostDialogLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var postViewModel: PostViewModel
    private val args: Args? by lazy { arguments?.getParcelable(ARG_POST) }


    companion object {
        private const val ARG_POST = "arg_post"

        @Parcelize
        data class Args(
            val model: @RawValue PostModelEntity
        ): Parcelable

        fun newInstance(requestKey: PostModelEntity) = UpdatePostDialog().apply {
            arguments = bundleOf(
                ARG_POST to Args(
                    requestKey
                )
            )
        }
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.window?.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            setLayout(width, height)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PostDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun fillFields(post: PostModelEntity) {
        binding.editTextTitle.setText(post.title)
        binding.editTextDescription.setText(post.description)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]

        args?.model?.let { fillFields(it) }
        binding.positiveButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()

            val post = args?.model?.copy(
                title = title, description = description
            )
            post?.let { it1 -> postViewModel.updatePost(it1) }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
