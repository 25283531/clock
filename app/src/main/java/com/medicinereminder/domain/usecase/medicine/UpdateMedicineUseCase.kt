package com.medicinereminder.domain.usecase.medicine

import com.medicinereminder.data.mapper.MedicineMapper
import com.medicinereminder.data.repository.MedicineRepository
import com.medicinereminder.domain.model.MedicineModel

/**
 * 更新药物用例
 * 用于更新数据库中的药物信息
 */
class UpdateMedicineUseCase(private val medicineRepository: MedicineRepository) {

    /**
     * 执行更新药物操作
     * @param medicine 要更新的药物模型
     */
    suspend operator fun invoke(medicine: MedicineModel) {
        val entity = MedicineMapper.toEntity(medicine)
        medicineRepository.updateMedicine(entity)
    }
}
