package com.example.cyber.ui.post_edits

interface OnItemActionListener {
    fun onEditItem(item: PostModelEntity)
    fun onDeleteItem(item: PostModelEntity)
    fun openPostDetails(item: PostModelEntity)
}
