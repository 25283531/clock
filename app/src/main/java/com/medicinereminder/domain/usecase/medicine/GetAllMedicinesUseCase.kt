package com.medicinereminder.domain.usecase.medicine

import com.medicinereminder.data.mapper.MedicineMapper
import com.medicinereminder.data.repository.MedicineRepository
import com.medicinereminder.domain.model.MedicineModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 获取所有药物用例
 * 用于从数据库获取所有药物列表
 */
class GetAllMedicinesUseCase(private val medicineRepository: MedicineRepository) {

    /**
     * 执行获取所有药物操作
     * @return 药物模型列表的Flow
     */
    operator fun invoke(): Flow<List<MedicineModel>> {
        return medicineRepository.getAllMedicines()
            .map { MedicineMapper.toModelList(it) }
    }
}
