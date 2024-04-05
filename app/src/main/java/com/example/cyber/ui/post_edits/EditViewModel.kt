package com.example.cyber.ui.post_edits

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyber.db.PostDatabase
import com.example.cyber.db.model.PostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = PostDatabase.getDatabase(application).postDao()

    private val _postsFLow = MutableStateFlow<List<PostModel>>(emptyList())
    val postsFlow: StateFlow<List<PostModel>> = _postsFLow

    init {
        getAllPosts()
    }

    private fun getAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.getAllPosts().collect { posts ->
                _postsFLow.value = posts
            }
        }
    }

    fun deletePost(model: PostModel) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deletePost(model)
        }
    }
}


fun List<PostModel>.mapList(): List<PostModelEntity> {
    return this.map { it.map() }
}
fun PostModel.map() = PostModelEntity(
    id = id,
    title = title,
    description = description
)

fun PostModelEntity.map() = PostModel(
    id = id ?: 0,
    title = title,
    description = description
)