package com.medicinereminder.domain.usecase.record

import com.medicinereminder.data.repository.MedicineRecordRepository
import java.util.Date

/**
 * 删除指定日期之前的服药记录用例
 * 用于批量删除指定日期之前的所有服药记录
 * @param medicineRecordRepository 服药记录仓库
 */
class DeleteRecordsBeforeDateUseCase(private val medicineRecordRepository: MedicineRecordRepository) {
    /**
     * 执行删除指定日期之前的服药记录操作
     * @param cutoffDate 截止日期，删除该日期之前的所有记录
     * @return 删除的记录数量
     */
    suspend operator fun invoke(cutoffDate: Date): Int {
        return medicineRecordRepository.deleteRecordsBeforeDate(cutoffDate)
    }
}
