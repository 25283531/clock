package com.medicinereminder.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicinereminder.domain.model.MedicineModel
import com.medicinereminder.domain.usecase.medicine.GetAllMedicinesUseCase
import com.medicinereminder.domain.usecase.medicine.AddMedicineUseCase
import com.medicinereminder.domain.usecase.medicine.UpdateMedicineUseCase
import com.medicinereminder.domain.usecase.medicine.DeleteMedicineUseCase
import com.medicinereminder.domain.usecase.medicine.GetMedicineByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 药物ViewModel
 * 用于处理药物相关的业务逻辑和数据管理
 */
@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val getAllMedicinesUseCase: GetAllMedicinesUseCase,
    private val addMedicineUseCase: AddMedicineUseCase,
    private val updateMedicineUseCase: UpdateMedicineUseCase,
    private val deleteMedicineUseCase: DeleteMedicineUseCase,
    private val getMedicineByIdUseCase: GetMedicineByIdUseCase
) : ViewModel() {

    // 药物列表
    private val _medicines = MutableStateFlow(emptyList<MedicineModel>())
    val medicines: StateFlow<List<MedicineModel>> = _medicines

    // 删除对话框状态
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog

    // 要删除的药物
    private val _medicineToDelete = MutableStateFlow<MedicineModel?>(null)
    val medicineToDelete: StateFlow<MedicineModel?> = _medicineToDelete

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
     * 添加药物
     * @param medicine 药物模型
     */
    fun addMedicine(medicine: MedicineModel) {
        viewModelScope.launch {
            addMedicineUseCase(medicine)
        }
    }

    /**
     * 更新药物
     * @param medicine 药物模型
     */
    fun updateMedicine(medicine: MedicineModel) {
        viewModelScope.launch {
            updateMedicineUseCase(medicine)
        }
    }

    /**
     * 删除药物
     * @param medicine 药物模型
     */
    fun deleteMedicine(medicine: MedicineModel) {
        viewModelScope.launch {
            deleteMedicineUseCase(medicine)
        }
    }

    /**
     * 根据ID获取药物
     * @param id 药物ID
     * @return 药物模型，如果不存在则返回null
     */
    suspend fun getMedicineById(id: Long): MedicineModel? {
        return getMedicineByIdUseCase(id)
    }

    /**
     * 显示删除对话框
     * @param medicine 要删除的药物
     */
    fun showDeleteDialog(medicine: MedicineModel) {
        _medicineToDelete.value = medicine
        _showDeleteDialog.value = true
    }

    /**
     * 关闭删除对话框
     */
    fun dismissDeleteDialog() {
        _showDeleteDialog.value = false
        _medicineToDelete.value = null
    }
}
