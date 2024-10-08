package com.wcsm.confectionaryadmin.data.model.states

data class LoginState(
    val email: String = "",
    val password: String = "",
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val isLogged: Boolean = false
)