package com.medicinereminder.domain.usecase.reminder

import com.medicinereminder.data.mapper.ReminderMapper
import com.medicinereminder.data.repository.ReminderRepository
import com.medicinereminder.domain.model.ReminderModel

/**
 * 根据ID获取服药提醒用例
 * 用于从数据库获取指定ID的服药提醒
 */
class GetReminderByIdUseCase(private val reminderRepository: ReminderRepository) {

    /**
     * 执行根据ID获取服药提醒操作
     * @param id 提醒ID
     * @return 提醒模型，如果不存在则返回null
     */
    suspend operator fun invoke(id: Long): ReminderModel? {
        val entity = reminderRepository.getReminderById(id)
        return entity?.let { ReminderMapper.toModel(it) }
    }
}
