package com.medicinereminder.domain.usecase.record

import com.medicinereminder.data.mapper.MedicineRecordMapper
import com.medicinereminder.data.repository.MedicineRecordRepository
import com.medicinereminder.domain.model.MedicineRecordModel

/**
 * 添加服药记录用例
 * 用于添加新的服药记录到数据库
 */
class AddMedicineRecordUseCase(private val medicineRecordRepository: MedicineRecordRepository) {

    /**
     * 执行添加服药记录操作
     * @param record 要添加的记录模型
     * @return 添加成功后的记录ID
     */
    suspend operator fun invoke(record: MedicineRecordModel): Long {
        val entity = MedicineRecordMapper.toEntity(record)
        return medicineRecordRepository.insertRecord(entity)
    }
}
