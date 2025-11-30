package com.medicinereminder.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicinereminder.domain.model.MedicineModel
import com.medicinereminder.domain.model.ReminderModel
import com.medicinereminder.domain.usecase.medicine.GetAllMedicinesUseCase
import com.medicinereminder.domain.usecase.reminder.AddReminderUseCase
import com.medicinereminder.domain.usecase.reminder.UpdateReminderUseCase
import com.medicinereminder.domain.usecase.reminder.GetReminderByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 添加/编辑提醒ViewModel
 * 用于处理添加和编辑提醒的业务逻辑和数据管理
 */
@HiltViewModel
class AddEditReminderViewModel @Inject constructor(
    private val getAllMedicinesUseCase: GetAllMedicinesUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val getReminderByIdUseCase: GetReminderByIdUseCase
) : ViewModel() {

    // 提醒信息
    private val _reminder = MutableStateFlow<ReminderModel?>(null)
    val reminder: StateFlow<ReminderModel?> = _reminder

    // 药物列表
    private val _medicines = MutableStateFlow(emptyList<MedicineModel>())
    val medicines: StateFlow<List<MedicineModel>> = _medicines

    init {
        // 加载药物列表
        loadMedicines()
    }

    /**
     * 加载药物列表
     */
    private fun loadMedicines() {
        getAllMedicinesUseCase().onEach {\ medicines ->
            _medicines.value = medicines
        }.launchIn(viewModelScope)
    }

    /**
     * 加载提醒信息
     * @param reminderId 提醒ID
     */
    fun loadReminder(reminderId: Long) {
        viewModelScope.launch {
            val loadedReminder = getReminderByIdUseCase(reminderId)
            _reminder.value = loadedReminder
        }
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
}
