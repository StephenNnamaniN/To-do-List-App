package com.nnamanistephen.todo.feature_todo.presentation.todo_list.components

import androidx.compose.material3.Divider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import com.nnamanistephen.todo.core.util.TodoListStrings
import com.nnamanistephen.todo.feature_todo.domain.util.SortingDirection
import com.nnamanistephen.todo.feature_todo.domain.util.TodoItemOrder

@Composable
fun SortingDrawerOptions(
    todoItemOrder: TodoItemOrder,
    onOrderChange: (TodoItemOrder) -> Unit
){
    val titleSelected = todoItemOrder::class == TodoItemOrder.Title::class
    NavigationDrawerItem(
        label = {
                IconRow(text = TodoListStrings.TITLE,
                    isChecked = titleSelected) },
        selected = false,
        onClick = {
            onOrderChange(TodoItemOrder.Title(todoItemOrder.sortingDirection, todoItemOrder.showArchived))
        }
    )

    val timeSelected = todoItemOrder::class == TodoItemOrder.Time::class
    NavigationDrawerItem(
        label = {
            IconRow(text = TodoListStrings.TIME,
                isChecked = timeSelected) },
        selected = false,
        onClick = {
            onOrderChange(TodoItemOrder.Time(todoItemOrder.sortingDirection, todoItemOrder.showArchived))
        }
    )

    val completedSelected = todoItemOrder::class == TodoItemOrder.Completed::class
    NavigationDrawerItem(
        label = {
            IconRow(text = TodoListStrings.COMPLETED,
                isChecked = completedSelected) },
        selected = false,
        onClick = {
            onOrderChange(TodoItemOrder.Completed(todoItemOrder.sortingDirection, todoItemOrder.showArchived))
        }
    )

    Divider()

    val sortDownSelected = todoItemOrder.sortingDirection == SortingDirection.down
    NavigationDrawerItem(
        label = {
            IconRow(text = TodoListStrings.SORT_DOWN,
                isChecked = sortDownSelected) },
        selected = false,
        onClick = {
            onOrderChange(todoItemOrder.copy(SortingDirection.down, todoItemOrder.showArchived))
        }
    )

    val sortUpSelected = todoItemOrder.sortingDirection == SortingDirection.up
    NavigationDrawerItem(
        label = {
            IconRow(text = TodoListStrings.SORTED_UP,
                isChecked = sortUpSelected) },
        selected = false,
        onClick = {
            onOrderChange(todoItemOrder.copy(SortingDirection.up, todoItemOrder.showArchived))
        }
    )

    Divider()

    NavigationDrawerItem(
        label = {
            IconRow(text = TodoListStrings.SHOW_ARCHIVED,
                isChecked = todoItemOrder.showArchived) },
        selected = false,
        onClick = {
            onOrderChange(todoItemOrder.copy(todoItemOrder.sortingDirection, !todoItemOrder.showArchived))
        }
    )
}