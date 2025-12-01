package com.medicinereminder.domain.usecase.record

import com.medicinereminder.data.repository.MedicineRecordRepository

/**
 * 删除服药记录的用例
 */
class DeleteMedicineRecordUseCase(private val medicineRecordRepository: MedicineRecordRepository) {

    /**
     * 执行用例
     * @param recordId 要删除的记录ID
     */
    suspend operator fun invoke(recordId: Long) {
        medicineRecordRepository.deleteRecordById(recordId)
    }
}