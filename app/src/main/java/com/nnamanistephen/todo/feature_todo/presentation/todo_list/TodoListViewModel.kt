package com.nnamanistephen.todo.feature_todo.presentation.todo_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nnamanistephen.todo.core.util.TodoListStrings
import com.nnamanistephen.todo.feature_todo.data.di.IODispatcher
import com.nnamanistephen.todo.feature_todo.domain.model.TodoItem
import com.nnamanistephen.todo.feature_todo.domain.use_case.TodoUseCaseResult
import com.nnamanistephen.todo.feature_todo.domain.use_case.TodoUseCases
import com.nnamanistephen.todo.feature_todo.domain.util.TodoItemOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases,
    @IODispatcher private val dispatcher: CoroutineDispatcher
): ViewModel() {
    val _state = mutableStateOf(TodoListState())
    val state: State<TodoListState> = _state

    private var undoTodoItem: TodoItem? = null
    private var getTodoItemsJob: Job? = null
    private val errorHandler = CoroutineExceptionHandler {_, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
    )}

    fun onEvent(event: TodoListEvent){
        when(event){
            is TodoListEvent.Delete -> {
                viewModelScope.launch (dispatcher + errorHandler){
                    todoUseCases.deleteTodoItem(event.todo)
                    getTodoItems()
                    undoTodoItem = event.todo
                }
            }
            is TodoListEvent.Sort -> {
                val stateOrderAlreadyMatchesEventOrder =
                    _state.value.todoItemOrder::class == event.todoItemOrder::class &&
                            _state.value.todoItemOrder.showArchived == event.todoItemOrder.showArchived &&
                            _state.value.todoItemOrder.sortingDirection == event.todoItemOrder.sortingDirection
                if(stateOrderAlreadyMatchesEventOrder){
                    return
                }

                _state.value = _state.value.copy(
                    todoItemOrder = event.todoItemOrder
                )
                getTodoItems()
            }
            is TodoListEvent.ToggleArchived -> {
                viewModelScope.launch (dispatcher + errorHandler) {
                    todoUseCases.toggleArchiveTodoItem(todo = event.todo)
                    getTodoItems()
                }
            }
            is TodoListEvent.ToggleCompleted -> {
                viewModelScope.launch (dispatcher + errorHandler) {
                    todoUseCases.toggleCompleteTodoItem(todo = event.todo)
                    getTodoItems()
                }
            }
            TodoListEvent.UndoDelete -> {
                viewModelScope.launch (dispatcher + errorHandler){
                    todoUseCases.addTodoItem(undoTodoItem ?: return@launch)
                    undoTodoItem = null
                    getTodoItems()
                }
            }
        }
    }

    fun getTodoItems(){
        getTodoItemsJob?.cancel()

        getTodoItemsJob = viewModelScope.launch(dispatcher + errorHandler) {
            val result = todoUseCases.getTodoItems(
                todoItemOrder = _state.value.todoItemOrder
            )
            when(result){
                is TodoUseCaseResult.Success -> {
                    _state.value = _state.value.copy(
                        todoItems = result.todoItems,
                        todoItemOrder = state.value.todoItemOrder,
                        isLoading = false
                    )
                }
                is TodoUseCaseResult.Error -> {
                    _state.value = _state.value.copy(
                        error = TodoListStrings.CANT_GET_TODOS,
                        isLoading = false
                    )
                }
            }
        }
    }
}