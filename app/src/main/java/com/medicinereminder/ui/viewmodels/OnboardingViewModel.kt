package com.medicinereminder.ui.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 引导页面ViewModel
 * 用于处理引导页面的状态管理
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    // 引导页面是否已完成
    private val _isOnboardingCompleted = MutableStateFlow(false)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted

    // 从DataStore获取引导页面状态
    init {
        viewModelScope.launch {
            getOnboardingCompleted().collect { completed ->
                _isOnboardingCompleted.value = completed
            }
        }
    }

    /**
     * 标记引导页面已完成
     */
    fun completeOnboarding() {
        viewModelScope.launch {
            setOnboardingCompleted(true)
        }
    }

    /**
     * 重置引导页面状态
     */
    fun resetOnboarding() {
        viewModelScope.launch {
            setOnboardingCompleted(false)
        }
    }

    /**
     * 从DataStore获取引导页面是否已完成
     */
    private fun getOnboardingCompleted(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] ?: false
        }
    }

    /**
     * 将引导页面状态保存到DataStore
     */
    private suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = completed
        }
    }

    companion object {
        // DataStore键
        private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
    }
}
