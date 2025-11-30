package com.medicinereminder.domain.usecase.record

import com.medicinereminder.data.mapper.MedicineRecordMapper
import com.medicinereminder.data.repository.MedicineRecordRepository
import com.medicinereminder.domain.model.MedicineRecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 获取今天的服药记录用例
 * 用于从数据库获取今天的服药记录列表
 */
class GetTodayRecordsUseCase(private val medicineRecordRepository: MedicineRecordRepository) {

    /**
     * 执行获取今天的服药记录操作
     * @return 今天的服药记录模型列表的Flow
     */
    operator fun invoke(): Flow<List<MedicineRecordModel>> {
        return medicineRecordRepository.getTodayRecords()
            .map { MedicineRecordMapper.toModelList(it) }
    }
}
