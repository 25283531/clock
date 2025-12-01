package com.medicinereminder.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 设置数据存储
 * 用于保存应用的设置选项，如语音提醒开关
 */
class SettingsDataStore(private val dataStore: DataStore<Preferences>) {

    companion object {
        // 数据存储名称
        private const val DATA_STORE_NAME = "settings"
        
        // 扩展函数，用于获取SettingsDataStore实例
        fun Context.settingsDataStore() = preferencesDataStore(name = DATA_STORE_NAME)
        
        // 偏好设置键
        val VOICE_REMINDER_ENABLED = booleanPreferencesKey("voice_reminder_enabled")
    }

    /**
     * 获取语音提醒开关状态
     */
    val isVoiceReminderEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[VOICE_REMINDER_ENABLED] ?: true // 默认开启
        }

    /**
     * 设置语音提醒开关状态
     */
    suspend fun setVoiceReminderEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[VOICE_REMINDER_ENABLED] = enabled
        }
    }
}
