package com.clickpost.app.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clickpost.app.social.data.AccountGroup
import com.clickpost.app.social.data.Platform
import com.clickpost.app.social.engine.PlatformAdapter
import com.clickpost.app.social.engine.ValidationResult
import com.clickpost.app.social.repository.AccountGroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountGroupViewModel @Inject constructor(
    private val groupRepo: AccountGroupRepository,
    private val adapters: Map<Platform, @JvmSuppressWildcards PlatformAdapter>
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _validationState = MutableStateFlow<ValidationState>(ValidationState.Idle)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<UiState> = combine(
        groupRepo.groupsFlow,
        _isLoading,
        _validationState,
        _error
    ) { groups, isLoading, validationState, error ->
        UiState(groups, isLoading, validationState, error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun createGroup(name: String) {
        viewModelScope.launch {
            try {
                groupRepo.createGroup(name)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun renameGroup(groupId: String, newName: String) {
        viewModelScope.launch {
            groupRepo.updateGroupName(groupId, newName)
        }
    }

    fun deleteGroup(groupId: String) {
        viewModelScope.launch {
            groupRepo.deleteGroup(groupId)
        }
    }

    fun validateAndAddCredential(groupId: String, platform: Platform, token: String) {
        viewModelScope.launch {
            _validationState.value = ValidationState.Validating
            val adapter = adapters[platform]
            if (adapter == null) {
                _validationState.value = ValidationState.Failure("No adapter for $platform")
                return@launch
            }

            val result = adapter.validateCredential(token)
            if (result.isValid) {
                groupRepo.addCredential(
                    groupId = groupId,
                    platform = platform,
                    accountDisplayName = result.accountDisplayName,
                    token = token,
                    scopes = result.grantedScopes
                )
                _validationState.value = ValidationState.Success(result)
            } else {
                _validationState.value = ValidationState.Failure(result.errorMessage ?: "Validation failed")
            }
        }
    }

    fun updateCredential(credId: String, newToken: String, displayName: String) {
        viewModelScope.launch {
            groupRepo.updateCredential(credId, newToken, displayName)
        }
    }

    fun deleteCredential(credId: String) {
        viewModelScope.launch {
            groupRepo.deleteCredential(credId)
        }
    }

    fun clearError() {
        _error.value = null
    }

    data class UiState(
        val groups: List<AccountGroup> = emptyList(),
        val isLoading: Boolean = false,
        val validationState: ValidationState = ValidationState.Idle,
        val error: String? = null
    )

    sealed class ValidationState {
        object Idle : ValidationState()
        object Validating : ValidationState()
        data class Success(val result: ValidationResult) : ValidationState()
        data class Failure(val message: String) : ValidationState()
    }
}
