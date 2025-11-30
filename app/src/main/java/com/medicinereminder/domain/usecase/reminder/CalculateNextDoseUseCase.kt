package com.medicinereminder.domain.usecase.reminder

import com.medicinereminder.domain.model.NextDoseModel
import com.medicinereminder.domain.model.ReminderModel
import com.medicinereminder.domain.model.ScheduleType
import com.medicinereminder.domain.usecase.medicine.GetMedicinesByIdsUseCase
import java.util.Calendar
import java.util.Date

/**
 * 计算下次服药时间用例
 * 用于根据服药提醒配置和上次服药时间计算下次服药时间
 * @param getMedicinesByIdsUseCase 获取药物信息的用例
 */
class CalculateNextDoseUseCase(private val getMedicinesByIdsUseCase: GetMedicinesByIdsUseCase) {

    /**
     * 执行计算下次服药时间操作
     * @param reminder 服药提醒模型
     * @param lastTakenTime 上次服药时间，如果为null则使用当前时间作为基准
     * @return 下次服药信息模型
     */
    suspend operator fun invoke(reminder: ReminderModel, lastTakenTime: Date? = null): NextDoseModel {
        val now = Date()
        val baseTime = lastTakenTime ?: now
        val nextDoseTime = calculateNextTime(reminder, baseTime)
        val medicines = getMedicinesByIdsUseCase(reminder.medicineIds)
        
        return NextDoseModel(
            nextDoseTime = nextDoseTime,
            medicines = medicines,
            reminderId = reminder.id
        )
    }

    /**
     * 根据提醒配置计算下次服药时间
     * @param reminder 服药提醒模型
     * @param baseTime 基准时间
     * @return 下次服药时间
     */
    private fun calculateNextTime(reminder: ReminderModel, baseTime: Date): Date {
        val calendar = Calendar.getInstance().apply { time = baseTime }
        
        when (reminder.scheduleType) {
            ScheduleType.DAILY -> {
                // 每天多次服药，计算下次服药时间
                if (reminder.timesPerDay == 1) {
                    // 每天一次，下次服药时间为明天的同一时间
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                    adjustToStartTime(calendar, reminder.startTime)
                } else {
                    // 每天多次，根据间隔小时数计算下次服药时间
                    calendar.add(Calendar.HOUR_OF_DAY, reminder.intervalHours)
                    
                    // 检查是否超过当天的最后一次服药时间
                    val lastDoseTime = Calendar.getInstance().apply {
                        time = reminder.startTime
                        add(Calendar.HOUR_OF_DAY, (reminder.timesPerDay - 1) * reminder.intervalHours)
                    }
                    
                    val currentDayLastDose = Calendar.getInstance().apply {
                        time = baseTime
                        set(Calendar.HOUR_OF_DAY, lastDoseTime.get(Calendar.HOUR_OF_DAY))
                        set(Calendar.MINUTE, lastDoseTime.get(Calendar.MINUTE))
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    
                    if (calendar.before(currentDayLastDose) || calendar == currentDayLastDose) {
                        // 当天还有服药时间
                        return calendar.time
                    } else {
                        // 超过当天最后一次服药时间，下次服药时间为明天的第一次服药时间
                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                        adjustToStartTime(calendar, reminder.startTime)
                    }
                }
            }
            ScheduleType.INTERVAL_DAYS -> {
                // 间隔天数服药，下次服药时间为间隔天数后的同一时间
                calendar.add(Calendar.DAY_OF_YEAR, reminder.intervalDays)
                adjustToStartTime(calendar, reminder.startTime)
            }
        }
        
        return calendar.time
    }

    /**
     * 调整日历时间到提醒的开始时间
     * @param calendar 要调整的日历
     * @param startTime 提醒的开始时间
     */
    private fun adjustToStartTime(calendar: Calendar, startTime: Date) {
        val startTimeCalendar = Calendar.getInstance().apply { time = startTime }
        calendar.set(Calendar.HOUR_OF_DAY, startTimeCalendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, startTimeCalendar.get(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }
}
