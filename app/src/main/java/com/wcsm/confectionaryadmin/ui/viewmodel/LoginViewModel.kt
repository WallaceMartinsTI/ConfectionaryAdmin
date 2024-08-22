package com.wcsm.confectionaryadmin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.wcsm.confectionaryadmin.data.UserPreferences
import com.wcsm.confectionaryadmin.data.model.states.LoginState
import com.wcsm.confectionaryadmin.data.repository.NetworkRepository
import com.wcsm.confectionaryadmin.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkRepository: NetworkRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val TAG = "#FIREBASE_AUTH#USER_LOGIN#"

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _saveLogin = MutableStateFlow(false)
    val saveLogin = _saveLogin.asStateFlow()

    private val _isConnected = MutableStateFlow(networkRepository.isConnected())
    val isConnected = _isConnected.asStateFlow()

    private val _showConfirmSyncUpDialog = MutableStateFlow<Boolean?>(null)
    val showConfirmSyncUpDialog = _showConfirmSyncUpDialog.asStateFlow()

    private val _showConfirmSyncDownDialog = MutableStateFlow<Boolean?>(null)
    val showConfirmSyncDownDialog = _showConfirmSyncDownDialog.asStateFlow()

    init {
        checkConnection()
    }

    fun updateLoginState(newState: LoginState) {
        _loginState.value = newState
    }

    fun updateSaveLogin(status: Boolean) {
        _saveLogin.value = status
    }

    fun checkConnection() {
        viewModelScope.launch {
            _isConnected.value = networkRepository.isConnected()
        }
    }

    fun teste() {
        Log.i("#-# TESTE #-#", "Chamou TESTE")
        val text = "wallace159santos@hotmail.com"
        val secretKey = generateKey()
        val (encryptedText, iv) = encrypt(text, secretKey)
        val decryptText = decrypt(encryptedText, secretKey, iv)
        Log.i("#-# TESTE #-#", "key: $secretKey")
        Log.i("#-# TESTE #-#", "encryptedText: ${encryptedText.joinToString()}")
        Log.i("#-# TESTE #-#", "iv: ${iv.joinToString()}")
        Log.i("#-# TESTE #-#", "decryptText: $decryptText")
    }

    fun checkShowSyncUpConfirmDialog() {
        val currentUser = userRepository.getCurrentUser()
        if(currentUser != null) {
            val result = userPreferences.getSyncUpConfirmDialogPreference(currentUser.uid)
            _showConfirmSyncUpDialog.value = result
        }
    }

    fun changeSyncUpConfirmDialogPreference(status: Boolean) {
        val currentUser = userRepository.getCurrentUser()
        if(currentUser != null) {
            userPreferences.saveSyncUpConfirmDialogPreference(currentUser.uid, status)
        }
    }

    fun checkShowSyncDownConfirmDialog() {
        val currentUser = userRepository.getCurrentUser()
        if(currentUser != null) {
            val result = userPreferences.getSyncDownConfirmDialogPreference(currentUser.uid)
            _showConfirmSyncDownDialog.value = result
        }
    }

    fun changeSyncDownConfirmDialogPreference(status: Boolean) {
        val currentUser = userRepository.getCurrentUser()
        if(currentUser != null) {
            userPreferences.saveSyncDownConfirmDialogPreference(currentUser.uid, status)
        }
    }

    init {
        val savedUser = userPreferences.getUser()
        if (userPreferences.isLoggedIn() && savedUser.first != null && savedUser.second != null) {
            _loginState.value = _loginState.value.copy(
                email = savedUser.first!!,
                password = savedUser.second!!
            )
            _saveLogin.value = true
        }
    }

    private fun saveLoggedUser(email: String, password: String) {
        userPreferences.saveUser(email, password)
    }

    fun clearLoggedUser() {
        userPreferences.clearUser()
    }

    fun signIn() {
        val newState = _loginState.value.copy(
            emailErrorMessage = null,
            passwordErrorMessage = null
        )
        updateLoginState(newState)

        val email = loginState.value.email
        val password = "123456" //loginState.value.password
        if(!validateUserEmail(email) || !validateUserPassword(password)) {
            return
        }

        _loginState.value = loginState.value.copy(isLoading = true)

        viewModelScope.launch {
            userRepository.signIn(email, password)
                .addOnSuccessListener {
                    Log.i(TAG, "Usuário LOGADO com SUCESSO!")
                    if(saveLogin.value) {
                        saveLoggedUser(
                            email = loginState.value.email,
                            password = loginState.value.password
                        )
                    }
                    _loginState.value = loginState.value.copy(isLogged = true)
                    _loginState.value = loginState.value.copy(isLoading = false)
                }
                .addOnFailureListener {
                    Log.i(TAG, "ERRO ao LOGAR USUÁRIO.")
                    val errorMessage = try {
                        throw it
                    } catch (invalidUser: FirebaseAuthInvalidUserException) {
                        invalidUser.printStackTrace()
                        "E-mail não cadastrado."
                    } catch (invalidCredentials: FirebaseAuthInvalidCredentialsException) {
                        invalidCredentials.printStackTrace()
                        "E-mail ou senha estão incorretos."
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                        "Erro inesperado. Tente novamente mais tarde."
                    }

                    updateLoginState(
                        newState.copy(
                            emailErrorMessage = if(
                                it is FirebaseAuthInvalidCredentialsException ||
                                it is FirebaseAuthInvalidUserException
                            ) errorMessage else null,
                            isLoading = false
                        )
                    )
                }
        }
    }

    private fun validateUserEmail(email: String): Boolean {
        return if(email.isEmpty()) {
            updateLoginState(loginState.value.copy(emailErrorMessage = "Informe o seu e-mail."))
            false
        } else {
            true
        }
    }

    private fun validateUserPassword(password: String): Boolean {
        return if(password.isEmpty()) {
            updateLoginState(loginState.value.copy(passwordErrorMessage = "Informe a sua senha."))
            false
        } else {
            true
        }
    }

    private fun encrypt(text: String, secretKey: SecretKey): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = generateIv()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encryptedText = cipher.doFinal(text.toByteArray())
        return Pair(encryptedText, iv) // Retorna o texto criptografado e o IV usado
    }

    private fun decrypt(encryptedData: ByteArray, secretKey: SecretKey, iv: ByteArray): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        return String(cipher.doFinal(encryptedData))
    }

    private fun generateKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(128)
        return keyGen.generateKey()
    }

    private fun generateIv(): ByteArray {
        val iv = ByteArray(16) // Tamanho do bloco para AES é 16 bytes
        SecureRandom().nextBytes(iv)
        return iv
    }
}
