package com.medicinereminder.domain.usecase.medicine

import com.medicinereminder.data.repository.MedicineRepository

/**
 * 获取药物总数的用例
 */
class GetMedicineCountUseCase(private val medicineRepository: MedicineRepository) {

    /**
     * 执行用例
     * @return 药物总数
     */
    suspend operator fun invoke(): Int {
        return medicineRepository.getMedicineCount()
    }
}