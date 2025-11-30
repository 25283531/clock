package com.medicinereminder.domain.usecase.medicine

import com.medicinereminder.data.mapper.MedicineMapper
import com.medicinereminder.data.repository.MedicineRepository
import com.medicinereminder.domain.model.MedicineModel

/**
 * 根据ID列表获取药物用例
 * 用于从数据库获取指定ID列表的药物
 */
class GetMedicinesByIdsUseCase(private val medicineRepository: MedicineRepository) {

    /**
     * 执行根据ID列表获取药物操作
     * @param ids 药物ID列表
     * @return 药物模型列表
     */
    suspend operator fun invoke(ids: List<Long>): List<MedicineModel> {
        val entities = medicineRepository.getMedicinesByIds(ids)
        return MedicineMapper.toModelList(entities)
    }
}
