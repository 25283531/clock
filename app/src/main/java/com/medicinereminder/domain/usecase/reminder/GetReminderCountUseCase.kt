package com.medicinereminder.domain.usecase.reminder

import com.medicinereminder.data.repository.ReminderRepository

/**
 * 获取提醒总数的用例
 */
class GetReminderCountUseCase(private val reminderRepository: ReminderRepository) {

    /**
     * 执行用例
     * @return 提醒总数
     */
    suspend operator fun invoke(): Int {
        return reminderRepository.getReminderCount()
    }
}