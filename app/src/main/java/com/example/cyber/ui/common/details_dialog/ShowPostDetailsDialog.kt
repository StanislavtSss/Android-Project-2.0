package com.example.cyber.ui.common.details_dialog

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.cyber.databinding.PostDetailsDialogBinding
import com.example.cyber.ui.common.add_dialog.PostViewModel
import com.example.cyber.ui.post_edits.PostModelEntity
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class ShowPostDetailsDialog : DialogFragment() {
    private var _binding: PostDetailsDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var postViewModel: PostViewModel
    private val args: Args? by lazy { arguments?.getParcelable(ARG_POST) }


    companion object {
        private const val ARG_POST = "arg_post"

        @Parcelize
        data class Args(
            val model: @RawValue PostModelEntity
        ): Parcelable

        fun newInstance(requestKey: PostModelEntity) = ShowPostDetailsDialog().apply {
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
        _binding = PostDetailsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun fillFields(post: PostModelEntity) {
        binding.editTextTitle.text = post.title
        binding.editTextDescription.text = post.description
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postViewModel = ViewModelProvider(requireActivity())[PostViewModel::class.java]

        args?.model?.let { fillFields(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
