package com.medicinereminder.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medicinereminder.domain.model.MedicineRecordModel
import com.medicinereminder.domain.usecase.record.DeleteMedicineRecordUseCase
import com.medicinereminder.domain.usecase.record.DeleteRecordsBeforeDateUseCase
import com.medicinereminder.domain.usecase.record.GetAllRecordsUseCase
import com.medicinereminder.domain.usecase.record.GetRecordsByDateRangeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Date

/**
 * 服药记录历史ViewModel
 * 用于处理服药记录的业务逻辑和数据管理
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllRecordsUseCase: GetAllRecordsUseCase,
    private val getRecordsByDateRangeUseCase: GetRecordsByDateRangeUseCase,
    private val deleteMedicineRecordUseCase: DeleteMedicineRecordUseCase,
    private val deleteRecordsBeforeDateUseCase: DeleteRecordsBeforeDateUseCase
) : ViewModel() {

    // 服药记录列表
    private val _records = MutableStateFlow(emptyList<MedicineRecordModel>())
    val records: StateFlow<List<MedicineRecordModel>> = _records

    // 日期范围
    private val _startDate = MutableStateFlow<Date?>(null)
    val startDate: StateFlow<Date?> = _startDate

    private val _endDate = MutableStateFlow<Date?>(null)
    val endDate: StateFlow<Date?> = _endDate

    // 删除对话框状态
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog

    // 要删除的记录
    private val _recordToDelete = MutableStateFlow<MedicineRecordModel?>(null)
    val recordToDelete: StateFlow<MedicineRecordModel?> = _recordToDelete

    // 批量删除确认对话框状态
    private val _showBatchDeleteDialog = MutableStateFlow(false)
    val showBatchDeleteDialog: StateFlow<Boolean> = _showBatchDeleteDialog

    // 批量删除选项
    private val _batchDeleteOption = MutableStateFlow(BatchDeleteOption.NONE)
    val batchDeleteOption: StateFlow<BatchDeleteOption> = _batchDeleteOption

    init {
        // 加载所有记录
        loadAllRecords()
    }

    /**
     * 加载所有记录
     */
    fun loadAllRecords() {
        getAllRecordsUseCase().onEach {\ records ->
            _records.value = records
        }.launchIn(viewModelScope)
    }

    /**
     * 按日期范围查询记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    fun getRecordsByDateRange(startDate: Date, endDate: Date) {
        _startDate.value = startDate
        _endDate.value = endDate
        getRecordsByDateRangeUseCase(startDate, endDate).onEach {\ records ->
            _records.value = records
        }.launchIn(viewModelScope)
    }

    /**
     * 删除记录
     * @param record 要删除的记录
     */
    fun deleteRecord(record: MedicineRecordModel) {
        viewModelScope.launch {
            deleteMedicineRecordUseCase(record)
        }
    }

    /**
     * 显示删除对话框
     * @param record 要删除的记录
     */
    fun showDeleteDialog(record: MedicineRecordModel) {
        _recordToDelete.value = record
        _showDeleteDialog.value = true
    }

    /**
     * 关闭删除对话框
     */
    fun dismissDeleteDialog() {
        _showDeleteDialog.value = false
        _recordToDelete.value = null
    }

    /**
     * 显示批量删除确认对话框
     * @param option 批量删除选项
     */
    fun showBatchDeleteDialog(option: BatchDeleteOption) {
        _batchDeleteOption.value = option
        _showBatchDeleteDialog.value = true
    }

    /**
     * 关闭批量删除确认对话框
     */
    fun dismissBatchDeleteDialog() {
        _showBatchDeleteDialog.value = false
        _batchDeleteOption.value = BatchDeleteOption.NONE
    }

    /**
     * 执行批量删除
     */
    fun executeBatchDelete() {
        viewModelScope.launch {
            val option = _batchDeleteOption.value
            val currentDate = Date()
            val cutoffDate = when (option) {
                BatchDeleteOption.LAST_7_DAYS -> Date(currentDate.time - 7 * 24 * 60 * 60 * 1000)
                BatchDeleteOption.LAST_30_DAYS -> Date(currentDate.time - 30 * 24 * 60 * 60 * 1000)
                BatchDeleteOption.LAST_90_DAYS -> Date(currentDate.time - 90 * 24 * 60 * 60 * 1000)
                BatchDeleteOption.LAST_6_MONTHS -> Date(currentDate.time - 6 * 30 * 24 * 60 * 60 * 1000)
                BatchDeleteOption.LAST_1_YEAR -> Date(currentDate.time - 365 * 24 * 60 * 60 * 1000)
                else -> return@launch
            }
            // 执行批量删除
            deleteRecordsBeforeDateUseCase(cutoffDate)
            // 重新加载记录列表
            loadAllRecords()
        }
    }

    /**
     * 批量删除选项枚举
     */
    enum class BatchDeleteOption {
        /**
         * 无选项
         */
        NONE,
        /**
         * 删除最近7天的记录
         */
        LAST_7_DAYS,
        /**
         * 删除最近30天的记录
         */
        LAST_30_DAYS,
        /**
         * 删除最近90天的记录
         */
        LAST_90_DAYS,
        /**
         * 删除最近6个月的记录
         */
        LAST_6_MONTHS,
        /**
         * 删除最近1年的记录
         */
        LAST_1_YEAR
    }
}
