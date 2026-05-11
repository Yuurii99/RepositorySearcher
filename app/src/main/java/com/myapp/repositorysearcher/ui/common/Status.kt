package com.myapp.repositorysearcher.ui.common

sealed interface Status {
    object Idle : Status
    object Loading : Status
    object Success : Status
    data class Error(val message: String) : Status
}