package com.example.cyber.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.cyber.db.model.PostModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts")
    fun getAllPosts(): Flow<List<PostModel>>

    @Insert
    suspend fun insertPost(post: PostModel)

    @Delete
    suspend fun deletePost(post: PostModel)

    @Update
    suspend fun updatePost(post: PostModel)

}
