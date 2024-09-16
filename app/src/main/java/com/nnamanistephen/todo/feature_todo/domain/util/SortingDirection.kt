package com.nnamanistephen.todo.feature_todo.domain.util


sealed class SortingDirection {
    object up: SortingDirection()
    object down: SortingDirection()
}