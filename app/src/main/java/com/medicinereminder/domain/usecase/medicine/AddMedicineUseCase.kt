package com.medicinereminder.domain.usecase.medicine

import com.medicinereminder.data.mapper.MedicineMapper
import com.medicinereminder.data.repository.MedicineRepository
import com.medicinereminder.domain.model.MedicineModel

/**
 * 添加药物用例
 * 用于添加新的药物到数据库
 */
class AddMedicineUseCase(private val medicineRepository: MedicineRepository) {

    /**
     * 执行添加药物操作
     * @param medicine 要添加的药物模型
     * @return 添加成功后的药物ID
     */
    suspend operator fun invoke(medicine: MedicineModel): Long {
        val entity = MedicineMapper.toEntity(medicine)
        return medicineRepository.insertMedicine(entity)
    }
}
