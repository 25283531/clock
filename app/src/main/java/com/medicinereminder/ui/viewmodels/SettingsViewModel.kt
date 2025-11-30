package com.medicinereminder.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicinereminder.data.datastore.SettingsDataStore
import com.medicinereminder.domain.usecase.data.ExportDataUseCase
import com.medicinereminder.domain.usecase.data.ImportDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置ViewModel
 * 用于处理设置相关的业务逻辑和数据管理
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exportDataUseCase: ExportDataUseCase,
    private val importDataUseCase: ImportDataUseCase,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    // 导出状态
    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting

    // 导入状态
    private val _isImporting = MutableStateFlow(false)
    val isImporting: StateFlow<Boolean> = _isImporting

    // 导出结果
    private val _exportResult = MutableStateFlow<Boolean?>(null)
    val exportResult: StateFlow<Boolean?> = _exportResult

    // 导入结果
    private val _importResult = MutableStateFlow<Boolean?>(null)
    val importResult: StateFlow<Boolean?> = _importResult

    // 语音提醒开关状态
    val isVoiceReminderEnabled: Flow<Boolean> = settingsDataStore.isVoiceReminderEnabled

    /**
     * 导出数据
     * @param uri 文件URI
     */
    fun exportData(uri: Uri) {
        viewModelScope.launch {
            _isExporting.value = true
            _exportResult.value = null
            val success = exportDataUseCase(uri)
            _exportResult.value = success
            _isExporting.value = false
        }
    }

    /**
     * 导入数据
     * @param uri 文件URI
     * @param overwrite 是否覆盖现有数据
     */
    fun importData(uri: Uri, overwrite: Boolean = false) {
        viewModelScope.launch {
            _isImporting.value = true
            _importResult.value = null
            val success = importDataUseCase(uri, overwrite)
            _importResult.value = success
            _isImporting.value = false
        }
    }

    /**
     * 设置语音提醒开关状态
     * @param enabled 是否启用语音提醒
     */
    fun setVoiceReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setVoiceReminderEnabled(enabled)
        }
    }

    /**
     * 重置导出结果
     */
    fun resetExportResult() {
        _exportResult.value = null
    }

    /**
     * 重置导入结果
     */
    fun resetImportResult() {
        _importResult.value = null
    }
}
