package com.medicinereminder.domain.usecase.record

import com.medicinereminder.data.repository.MedicineRecordRepository

/**
 * 获取今天已服药记录数的用例
 */
class GetTodayTakenCountUseCase(private val medicineRecordRepository: MedicineRecordRepository) {

    /**
     * 执行用例
     * @return 今天已服药记录数
     */
    suspend operator fun invoke(): Int {
        return medicineRecordRepository.getTodayTakenCount()
    }
}