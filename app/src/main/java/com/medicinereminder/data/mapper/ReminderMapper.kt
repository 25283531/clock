package com.medicinereminder.data.mapper

import com.medicinereminder.data.entity.Reminder
import com.medicinereminder.domain.model.ReminderModel
import com.medicinereminder.domain.model.ScheduleType

/**
 * 服药提醒映射器
 * 用于在提醒实体和领域模型之间进行转换
 */
object ReminderMapper {

    /**
     * 将提醒实体转换为领域模型
     */
    fun toModel(entity: Reminder): ReminderModel {
        return ReminderModel(
            id = entity.id,
            medicineIds = entity.medicineIds.split(",").mapNotNull { it.toLongOrNull() },
            scheduleType = when (entity.scheduleType) {
                1 -> ScheduleType.DAILY
                2 -> ScheduleType.INTERVAL_DAYS
                else -> ScheduleType.DAILY
            },
            timesPerDay = entity.timesPerDay,
            intervalHours = entity.intervalHours,
            intervalDays = entity.intervalDays,
            startTime = entity.startTime,
            isActive = entity.isActive,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    /**
     * 将提醒领域模型转换为实体
     */
    fun toEntity(model: ReminderModel): Reminder {
        return Reminder(
            id = model.id,
            medicineIds = model.medicineIds.joinToString(","),
            scheduleType = when (model.scheduleType) {
                ScheduleType.DAILY -> 1
                ScheduleType.INTERVAL_DAYS -> 2
            },
            timesPerDay = model.timesPerDay,
            intervalHours = model.intervalHours,
            intervalDays = model.intervalDays,
            startTime = model.startTime,
            isActive = model.isActive,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )
    }

    /**
     * 将提醒实体列表转换为领域模型列表
     */
    fun toModelList(entities: List<Reminder>): List<ReminderModel> {
        return entities.map { toModel(it) }
    }

    /**
     * 将提醒领域模型列表转换为实体列表
     */
    fun toEntityList(models: List<ReminderModel>): List<Reminder> {
        return models.map { toEntity(it) }
    }
}
