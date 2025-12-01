package com.medicinereminder.domain.usecase.record

import com.medicinereminder.data.repository.MedicineRecordRepository
import com.medicinereminder.data.entity.MedicineRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * 获取指定日期范围内服药记录的用例
 */
class GetRecordsByDateRangeUseCase(private val medicineRecordRepository: MedicineRecordRepository) {

    /**
     * 执行用例
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 指定日期范围内的服药记录流
     */
    operator fun invoke(startDate: Date, endDate: Date): Flow<List<MedicineRecord>> {
        return medicineRecordRepository.getRecordsByDateRange(startDate, endDate)
    }
}