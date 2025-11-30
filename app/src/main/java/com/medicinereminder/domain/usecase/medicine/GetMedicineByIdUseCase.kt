package com.medicinereminder.domain.usecase.medicine

import com.medicinereminder.data.mapper.MedicineMapper
import com.medicinereminder.data.repository.MedicineRepository
import com.medicinereminder.domain.model.MedicineModel

/**
 * 根据ID获取药物用例
 * 用于从数据库获取指定ID的药物
 */
class GetMedicineByIdUseCase(private val medicineRepository: MedicineRepository) {

    /**
     * 执行根据ID获取药物操作
     * @param id 药物ID
     * @return 药物模型，如果不存在则返回null
     */
    suspend operator fun invoke(id: Long): MedicineModel? {
        val entity = medicineRepository.getMedicineById(id)
        return entity?.let { MedicineMapper.toModel(it) }
    }
}
