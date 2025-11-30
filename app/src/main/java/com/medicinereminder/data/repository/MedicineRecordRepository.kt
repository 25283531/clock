package com.medicinereminder.data.repository

import com.medicinereminder.data.dao.MedicineRecordDao
import com.medicinereminder.data.entity.MedicineRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * 服药记录数据仓库
 * 封装服药记录相关的数据库操作，提供统一的数据访问接口
 */
class MedicineRecordRepository(private val medicineRecordDao: MedicineRecordDao) {

    /**
     * 获取所有服药记录，按服药时间倒序排列
     */
    fun getAllRecords(): Flow<List<MedicineRecord>> {
        return medicineRecordDao.getAllRecords()
    }

    /**
     * 根据提醒ID获取服药记录，按服药时间倒序排列
     */
    fun getRecordsByReminderId(reminderId: Long): Flow<List<MedicineRecord>> {
        return medicineRecordDao.getRecordsByReminderId(reminderId)
    }

    /**
     * 根据ID获取服药记录
     */
    suspend fun getRecordById(id: Long): MedicineRecord? {
        return medicineRecordDao.getRecordById(id)
    }

    /**
     * 获取指定日期范围内的服药记录
     */
    fun getRecordsByDateRange(startDate: Date, endDate: Date): Flow<List<MedicineRecord>> {
        return medicineRecordDao.getRecordsByDateRange(startDate, endDate)
    }

    /**
     * 获取今天的服药记录
     */
    fun getTodayRecords(): Flow<List<MedicineRecord>> {
        return medicineRecordDao.getTodayRecords()
    }

    /**
     * 插入服药记录
     */
    suspend fun insertRecord(record: MedicineRecord): Long {
        return medicineRecordDao.insertRecord(record)
    }

    /**
     * 更新服药记录
     */
    suspend fun updateRecord(record: MedicineRecord) {
        medicineRecordDao.updateRecord(record)
    }

    /**
     * 删除服药记录
     */
    suspend fun deleteRecord(record: MedicineRecord) {
        medicineRecordDao.deleteRecord(record)
    }

    /**
     * 根据ID删除服药记录
     */
    suspend fun deleteRecordById(id: Long) {
        medicineRecordDao.deleteRecordById(id)
    }

    /**
     * 清除所有服药记录
     */
    suspend fun clearAllRecords() {
        medicineRecordDao.clearAllRecords()
    }

    /**
     * 获取服药记录总数
     */
    suspend fun getRecordCount(): Int {
        return medicineRecordDao.getRecordCount()
    }

    /**
     * 获取今天已服药的记录数
     */
    suspend fun getTodayTakenCount(): Int {
        return medicineRecordDao.getTodayTakenCount()
    }

    /**
     * 获取今天漏服的记录数
     */
    suspend fun getTodayMissedCount(): Int {
        return medicineRecordDao.getTodayMissedCount()
    }

    /**
     * 删除指定日期之前的所有记录
     */
    suspend fun deleteRecordsBeforeDate(cutoffDate: Date): Int {
        return medicineRecordDao.deleteRecordsBeforeDate(cutoffDate)
    }
}
