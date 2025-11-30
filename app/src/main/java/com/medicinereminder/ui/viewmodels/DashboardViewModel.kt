package com.medicinereminder.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicinereminder.domain.model.MedicineRecordModel
import com.medicinereminder.domain.model.NextDoseModel
import com.medicinereminder.domain.usecase.medicine.GetMedicineCountUseCase
import com.medicinereminder.domain.usecase.reminder.CalculateNextDoseUseCase
import com.medicinereminder.domain.usecase.reminder.GetActiveRemindersUseCase
import com.medicinereminder.domain.usecase.reminder.GetAllRemindersUseCase
import com.medicinereminder.domain.usecase.reminder.GetReminderCountUseCase
import com.medicinereminder.domain.usecase.record.AddMedicineRecordUseCase
import com.medicinereminder.domain.usecase.record.GetTodayRecordsUseCase
import com.medicinereminder.domain.usecase.record.GetTodayTakenCountUseCase
import com.medicinereminder.domain.usecase.record.GetTodayMissedCountUseCase
import com.medicinereminder.domain.usecase.record.UpdateMedicineRecordUseCase
import com.medicinereminder.manager.ReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * 首页ViewModel
 * 用于处理首页的业务逻辑和数据管理
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getActiveRemindersUseCase: GetActiveRemindersUseCase,
    private val getAllRemindersUseCase: GetAllRemindersUseCase,
    private val calculateNextDoseUseCase: CalculateNextDoseUseCase,
    private val getMedicineCountUseCase: GetMedicineCountUseCase,
    private val getReminderCountUseCase: GetReminderCountUseCase,
    private val getTodayRecordsUseCase: GetTodayRecordsUseCase,
    private val getTodayTakenCountUseCase: GetTodayTakenCountUseCase,
    private val getTodayMissedCountUseCase: GetTodayMissedCountUseCase,
    private val addMedicineRecordUseCase: AddMedicineRecordUseCase,
    private val updateMedicineRecordUseCase: UpdateMedicineRecordUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    // 提醒管理器
    private val reminderManager = ReminderManager(context)

    // 下次服药信息
    private val _nextDose = MutableStateFlow<NextDoseModel?>(null)
    val nextDose: StateFlow<NextDoseModel?> = _nextDose

    // 统计数据
    private val _statistics = MutableStateFlow(StatisticsState())
    val statistics: StateFlow<StatisticsState> = _statistics

    // 今日记录
    private val _todayRecords = MutableStateFlow(emptyList<MedicineRecordModel>())
    val todayRecords: StateFlow<List<MedicineRecordModel>> = _todayRecords

    init {
        // 初始化数据
        loadData()
    }

    /**
     * 加载数据
     */
    private fun loadData() {
        // 获取活跃提醒并计算下次服药时间
        getActiveRemindersUseCase().onEach { reminders ->
            if (reminders.isNotEmpty()) {
                // 这里简化处理，只取第一个提醒来计算下次服药时间
                // 实际应用中应该计算所有提醒的下次服药时间，然后取最近的一个
                val reminder = reminders.first()
                viewModelScope.launch {
                    val nextDose = calculateNextDoseUseCase(reminder)
                    _nextDose.value = nextDose
                }
            } else {
                _nextDose.value = null
            }
        }.launchIn(viewModelScope)

        // 获取统计数据
        viewModelScope.launch {
            val totalMedicines = getMedicineCountUseCase()
            val totalReminders = getReminderCountUseCase()
            val takenToday = getTodayTakenCountUseCase()
            val missedToday = getTodayMissedCountUseCase()

            _statistics.update {
                it.copy(
                    totalMedicines = totalMedicines,
                    totalReminders = totalReminders,
                    takenToday = takenToday,
                    missedToday = missedToday
                )
            }
        }

        // 获取今日记录
        getTodayRecordsUseCase().onEach {
            _todayRecords.value = it
        }.launchIn(viewModelScope)

        // 重新设置所有提醒
        getAllRemindersUseCase().onEach { reminders ->
            reminderManager.resetAllReminders(reminders)
        }.launchIn(viewModelScope)
    }

    /**
     * 标记药物已服用
     * @param reminderId 提醒ID
     */
    fun markMedicineTaken(reminderId: Long) {
        viewModelScope.launch {
            // 创建服药记录
            val record = MedicineRecordModel(
                reminderId = reminderId,
                medicineIds = emptyList(), // 实际应用中应该从reminder获取medicineIds
                takenTime = Date(),
                isTaken = true
            )
            addMedicineRecordUseCase(record)
            // 重新加载数据
            loadData()
        }
    }

    /**
     * 撤销服药记录
     * @param record 要撤销的服药记录
     */
    fun undoMedicineTaken(record: MedicineRecordModel) {
        viewModelScope.launch {
            // 将记录标记为未服药
            val updatedRecord = record.copy(isTaken = false)
            updateMedicineRecordUseCase(updatedRecord)
            // 重新加载数据
            loadData()
        }
    }

    /**
     * 统计数据状态
     * @param totalMedicines 药物总数
     * @param totalReminders 提醒总数
     * @param takenToday 今日已服
     * @param missedToday 今日漏服
     */
    data class StatisticsState(
        val totalMedicines: Int = 0,
        val totalReminders: Int = 0,
        val takenToday: Int = 0,
        val missedToday: Int = 0
    )
}
