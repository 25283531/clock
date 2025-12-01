package com.medicinereminder.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicinereminder.domain.model.ReminderModel
import com.medicinereminder.domain.usecase.reminder.GetAllRemindersUseCase
import com.medicinereminder.domain.usecase.reminder.AddReminderUseCase
import com.medicinereminder.domain.usecase.reminder.UpdateReminderUseCase
import com.medicinereminder.domain.usecase.reminder.DeleteReminderUseCase
import com.medicinereminder.domain.usecase.reminder.GetReminderByIdUseCase
import com.medicinereminder.manager.ReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 提醒ViewModel
 * 用于处理提醒相关的业务逻辑和数据管理
 */
@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val getAllRemindersUseCase: GetAllRemindersUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val getReminderByIdUseCase: GetReminderByIdUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    // 提醒管理器
    private val reminderManager = ReminderManager(context)

    // 提醒列表
    private val _reminders = MutableStateFlow(emptyList<ReminderModel>())
    val reminders: StateFlow<List<ReminderModel>> = _reminders

    // 删除对话框状态
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog

    // 要删除的提醒
    private val _reminderToDelete = MutableStateFlow<ReminderModel?>(null)
    val reminderToDelete: StateFlow<ReminderModel?> = _reminderToDelete

    init {
        // 加载提醒列表
        loadReminders()
    }

    /**
     * 加载提醒列表
     */
    private fun loadReminders() {
        getAllRemindersUseCase().onEach { reminders ->
            _reminders.value = reminders
        }.launchIn(viewModelScope)
    }

    /**
     * 添加提醒
     * @param reminder 提醒模型
     */
    fun addReminder(reminder: ReminderModel) {
        viewModelScope.launch {
            addReminderUseCase(reminder)
        }
    }

    /**
     * 更新提醒
     * @param reminder 提醒模型
     */
    fun updateReminder(reminder: ReminderModel) {
        viewModelScope.launch {
            updateReminderUseCase(reminder)
        }
    }

    /**
     * 删除提醒
     * @param reminder 提醒模型
     */
    fun deleteReminder(reminder: ReminderModel) {
        viewModelScope.launch {
            deleteReminderUseCase(reminder)
            // 取消提醒闹钟
            reminderManager.cancelReminder(reminder.id)
        }
    }

    /**
     * 根据ID获取提醒
     * @param id 提醒ID
     * @return 提醒模型，如果不存在则返回null
     */
    suspend fun getReminderById(id: Long): ReminderModel? {
        return getReminderByIdUseCase(id)
    }

    /**
     * 显示删除对话框
     * @param reminder 要删除的提醒
     */
    fun showDeleteDialog(reminder: ReminderModel) {
        _reminderToDelete.value = reminder
        _showDeleteDialog.value = true
    }

    /**
     * 关闭删除对话框
     */
    fun dismissDeleteDialog() {
        _showDeleteDialog.value = false
        _reminderToDelete.value = null
    }
}
