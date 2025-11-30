package com.medicinereminder.domain.usecase.medicine

import com.medicinereminder.data.mapper.MedicineMapper
import com.medicinereminder.data.repository.MedicineRepository
import com.medicinereminder.domain.model.MedicineModel

/**
 * 删除药物用例
 * 用于从数据库删除药物
 */
class DeleteMedicineUseCase(private val medicineRepository: MedicineRepository) {

    /**
     * 执行删除药物操作
     * @param medicine 要删除的药物模型
     */
    suspend operator fun invoke(medicine: MedicineModel) {
        val entity = MedicineMapper.toEntity(medicine)
        medicineRepository.deleteMedicine(entity)
    }

    /**
     * 执行根据ID删除药物操作
     * @param id 要删除的药物ID
     */
    suspend operator fun invoke(id: Long) {
        medicineRepository.deleteMedicineById(id)
    }
}
