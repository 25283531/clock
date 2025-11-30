package com.medicinereminder.domain.usecase.reminder

import com.medicinereminder.data.mapper.ReminderMapper
import com.medicinereminder.data.repository.ReminderRepository
import com.medicinereminder.domain.model.ReminderModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 获取所有服药提醒用例
 * 用于从数据库获取所有服药提醒列表
 */
class GetAllRemindersUseCase(private val reminderRepository: ReminderRepository) {

    /**
     * 执行获取所有服药提醒操作
     * @return 提醒模型列表的Flow
     */
    operator fun invoke(): Flow<List<ReminderModel>> {
        return reminderRepository.getAllReminders()
            .map { ReminderMapper.toModelList(it) }
    }
}
