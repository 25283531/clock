package com.medicinereminder.domain.usecase.record

import com.medicinereminder.data.mapper.MedicineRecordMapper
import com.medicinereminder.data.repository.MedicineRecordRepository
import com.medicinereminder.domain.model.MedicineRecordModel

/**
 * 更新服药记录用例
 * 用于更新数据库中的服药记录信息，特别是用于撤销服药操作
 */
class UpdateMedicineRecordUseCase(private val medicineRecordRepository: MedicineRecordRepository) {

    /**
     * 执行更新服药记录操作
     * @param record 要更新的记录模型
     */
    suspend operator fun invoke(record: MedicineRecordModel) {
        val entity = MedicineRecordMapper.toEntity(record)
        medicineRecordRepository.updateRecord(entity)
    }
}
