package com.nnamanistephen.todo.feature_todo.data.remote.dto

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serial

/* Here we serialize all the data in our firebase database stored in a json file
we use @SerializedName to capture all the parameters as they are in the json file
 */
data class RemoteTodoItem(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("Timestamp")
    val timestamp: Long,
    @SerializedName("Completed")
    val completed: Boolean,
    @SerializedName("Archived")
    val archived: Boolean,
    @SerializedName("Id")
    val id: Int?
)