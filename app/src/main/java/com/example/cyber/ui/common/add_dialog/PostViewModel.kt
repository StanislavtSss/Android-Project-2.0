package com.example.cyber.ui.common.add_dialog

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyber.db.PostDatabase
import com.example.cyber.db.model.PostModel
import com.example.cyber.ui.post_edits.PostModelEntity
import com.example.cyber.ui.post_edits.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = PostDatabase.getDatabase(application).postDao()

    fun addPost(post: PostModel) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertPost(post)
        }
    }

    fun updatePost(post: PostModelEntity) {
        viewModelScope.launch {
            Log.e("CHECK_POST", "${post.map()}")
            dao.updatePost(post.map())
        }
    }
}
