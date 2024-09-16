package com.nnamanistephen.todo.feature_todo.data.remote

import com.nnamanistephen.todo.feature_todo.data.remote.dto.RemoteTodoItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/* Here we implement all the functions needed to make HTTP Requests to our remote database
with these functions we call call up data from our remote database or server
 */
interface TodoApi {
    @GET("todo.json")
    suspend fun getAllTodos(): List<RemoteTodoItem>

    @GET("todo.json?orderBy=\"Id\"")
    suspend fun getSingleTodoByID(@Query("equalTo") id: Int?): Map<String, RemoteTodoItem>

    /* you could use this command if your data source is just remote. What this means is that since we have set our Room database to
    autogenerate a primary key for ID we won't need retrofit to generate another id. So, we use the @PUT instead.
     */
//    @POST("todo.json")
//    suspend fun addTodo(@Body url: String, @Body updatedTodo: RemoteTodoItem): Response<Unit>

    @PUT
    suspend fun addTodo(@Url url: String, @Body updatedTodo: RemoteTodoItem): Response<Unit>

    @DELETE("todo/{id}.json")
    suspend fun deleteTodo(@Path("id") id: Int?): Response<Unit>

    @PUT("todo/{id}.json")
    suspend fun updateTodoItem(@Path("id") id: Int?, @Body todoItem: RemoteTodoItem): Response<Unit>
}