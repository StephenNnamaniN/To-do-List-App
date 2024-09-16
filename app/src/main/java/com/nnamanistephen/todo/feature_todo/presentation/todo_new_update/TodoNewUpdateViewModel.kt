package com.nnamanistephen.todo.feature_todo.presentation.todo_new_update

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nnamanistephen.todo.core.util.NewUpdateStrings
import com.nnamanistephen.todo.feature_todo.data.di.IODispatcher
import com.nnamanistephen.todo.feature_todo.domain.use_case.TodoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TodoNewUpdateViewModel @Inject constructor(
    private val todoUseCases: TodoUseCases,
    savedStateHandle: SavedStateHandle,
    @IODispatcher private val dispatcher: CoroutineDispatcher
): ViewModel(){
    private val _state = mutableStateOf(TodoNewUpdateState())
    val state: State<TodoNewUpdateState> = _state

    private val errorHandler = CoroutineExceptionHandler {_, e ->
        e.printStackTrace()
        _state.value = _state.value.copy(
            error = e.message,
            isLoading = false
        )
    }

    private var currentTodoId: Int? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class showSnackbar(val message: String): UiEvent()
        object SaveTodo: UiEvent()
        object Back: UiEvent()
    }

    init {
        savedStateHandle.get<Int>("todoId")?.let{ id ->
            if(id != -1){
                viewModelScope.launch {
                    todoUseCases.getTodoItemById(id)?.also { todo->
                        currentTodoId = id
                        _state.value = _state.value.copy(
                            todo = todo,
                            isLoading = false,
                            isTitleHintVisible = todo.title.isBlank(),
                            isDescriptionHintVisible = todo.description.isBlank()
                        )

                    }
                }
            } else {
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: TodoNewUpdateEvent){
        when {
            event == TodoNewUpdateEvent.Back -> {
                viewModelScope.launch (dispatcher + errorHandler) {
                    _eventFlow.emit(UiEvent.Back)
                }
            }
            event is TodoNewUpdateEvent.ChangedDescriptionFocus -> {
                val shouldDescriptionHintBeVisible = !event.focusState.isFocused && _state.value.todo.description.isBlank()
                _state.value = _state.value.copy(
                    isDescriptionHintVisible = shouldDescriptionHintBeVisible
                )
            }
            event is TodoNewUpdateEvent.ChangedTitleFocus -> {
                val shouldTitleHintBeVisible = !event.focusState.isFocused && _state.value.todo.title.isBlank()
                _state.value = _state.value.copy(
                    isTitleHintVisible = shouldTitleHintBeVisible
                )
            }
            event == TodoNewUpdateEvent.Delete -> {
                viewModelScope.launch(dispatcher + errorHandler) {
                    if(currentTodoId != null){
                        todoUseCases.deleteTodoItem(_state.value.todo)
                    }
                    _eventFlow.emit(UiEvent.Back)
                }
            }
            event is TodoNewUpdateEvent.EnteredDescription -> {
                _state.value = _state.value.copy(
                    todo = _state.value.todo.copy(
                        description = event.value
                    )
                )
            }
            event is TodoNewUpdateEvent.EnteredTitle -> {
                _state.value = _state.value.copy(
                    todo = _state.value.todo.copy(
                        title = event.value
                    )
                )
            }
            event == TodoNewUpdateEvent.SaveTodo -> {
                viewModelScope.launch (dispatcher + errorHandler){
                    try {
                        if(currentTodoId != null){
                            todoUseCases.updateTodoItem((_state.value.todo))
                        } else {
                            todoUseCases.addTodoItem(_state.value.todo.copy(
                                timestamp = System.currentTimeMillis(),
                                id = null
                            ))
                        }
                        _eventFlow.emit(UiEvent.SaveTodo)
                    } catch(e: Exception){
                        _eventFlow.emit(
                            UiEvent.showSnackbar(
                                message = e.message ?: NewUpdateStrings.SAVE_ERROR
                            )
                        )
                    }
                }
            }
            event == TodoNewUpdateEvent.ToggleArchived -> {
                _state.value = _state.value.copy(
                    todo = _state.value.todo.copy(
                        archived = !_state.value.todo.archived
                    )
                )
            }
            event == TodoNewUpdateEvent.ToggleCompleted -> {
                _state.value = _state.value.copy(
                    todo = _state.value.todo.copy(
                        completed = !_state.value.todo.completed
                    )
                )
            }
        }

    }
}