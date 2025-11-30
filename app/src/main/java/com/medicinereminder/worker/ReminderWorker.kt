package com.medicinereminder.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.medicinereminder.data.datastore.SettingsDataStore
import com.medicinereminder.domain.model.ReminderModel
import com.medicinereminder.domain.usecase.medicine.GetMedicinesByIdsUseCase
import com.medicinereminder.domain.usecase.reminder.CalculateNextDoseUseCase
import com.medicinereminder.domain.usecase.reminder.GetReminderByIdUseCase
import com.medicinereminder.service.ReminderNotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * 提醒工作器
 * 用于处理定时提醒逻辑，显示通知并计算下一次提醒时间
 */
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getReminderByIdUseCase: GetReminderByIdUseCase,
    private val getMedicinesByIdsUseCase: GetMedicinesByIdsUseCase,
    private val calculateNextDoseUseCase: CalculateNextDoseUseCase,
    private val settingsDataStore: SettingsDataStore
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        // 工作名称
        const val WORK_NAME = "MedicineReminderWorker"
        // 参数键
        const val PARAM_REMINDER_ID = "reminder_id"
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                // 获取提醒ID
                val reminderId = inputData.getLong(PARAM_REMINDER_ID, 0)
                if (reminderId == 0L) {
                    return@withContext Result.failure()
                }

                // 获取提醒信息
                val reminder = getReminderByIdUseCase(reminderId)
                if (reminder == null || !reminder.isActive) {
                    return@withContext Result.failure()
                }

                // 获取关联的药物信息
                val medicines = getMedicinesByIdsUseCase(reminder.medicineIds)
                val medicineNames = medicines.map { it.name }

                // 检查语音提醒开关状态
                val isVoiceReminderEnabled = settingsDataStore.isVoiceReminderEnabled.first()
                
                // 显示通知
                val notificationService = ReminderNotificationService(applicationContext)
                notificationService.showReminderNotification(
                    reminderId = reminderId,
                    title = "服药提醒",
                    content = "到服药时间了！",
                    medicineNames = medicineNames,
                    isVoiceReminderEnabled = isVoiceReminderEnabled
                )

                // TODO: 计算下一次提醒时间并设置新的工作

                Result.success()
            } catch (e: Exception) {
                // 记录错误日志
                e.printStackTrace()
                Result.retry()
            }
        }
    }
}
