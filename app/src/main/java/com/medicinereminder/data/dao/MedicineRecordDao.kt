package com.medicinereminder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.medicinereminder.data.entity.MedicineRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * 服药记录数据访问对象
 */
@Dao
interface MedicineRecordDao {
    /**
     * 获取所有服药记录，按服药时间倒序排列
     */
    @Query("SELECT * FROM medicine_records ORDER BY takenTime DESC")
    fun getAllRecords(): Flow<List<MedicineRecord>>

    /**
     * 根据提醒ID获取服药记录，按服药时间倒序排列
     */
    @Query("SELECT * FROM medicine_records WHERE reminderId = :reminderId ORDER BY takenTime DESC")
    fun getRecordsByReminderId(reminderId: Long): Flow<List<MedicineRecord>>

    /**
     * 根据ID获取服药记录
     */
    @Query("SELECT * FROM medicine_records WHERE id = :id")
    suspend fun getRecordById(id: Long): MedicineRecord?

    /**
     * 获取指定日期范围内的服药记录
     */
    @Query("SELECT * FROM medicine_records WHERE takenTime BETWEEN :startDate AND :endDate ORDER BY takenTime DESC")
    fun getRecordsByDateRange(startDate: Date, endDate: Date): Flow<List<MedicineRecord>>

    /**
     * 获取今天的服药记录
     */
    @Query("SELECT * FROM medicine_records WHERE date(takenTime) = date('now') ORDER BY takenTime DESC")
    fun getTodayRecords(): Flow<List<MedicineRecord>>

    /**
     * 插入服药记录
     */
    @Insert
    suspend fun insertRecord(record: MedicineRecord): Long

    /**
     * 更新服药记录
     */
    @Update
    suspend fun updateRecord(record: MedicineRecord)

    /**
     * 删除服药记录
     */
    @Delete
    suspend fun deleteRecord(record: MedicineRecord)

    /**
     * 根据ID删除服药记录
     */
    @Query("DELETE FROM medicine_records WHERE id = :id")
    suspend fun deleteRecordById(id: Long)

    /**
     * 清除所有服药记录
     */
    @Query("DELETE FROM medicine_records")
    suspend fun clearAllRecords()

    /**
     * 获取服药记录总数
     */
    @Query("SELECT COUNT(*) FROM medicine_records")
    suspend fun getRecordCount(): Int

    /**
     * 获取今天已服药的记录数
     */
    @Query("SELECT COUNT(*) FROM medicine_records WHERE date(takenTime) = date('now') AND isTaken = 1")
    suspend fun getTodayTakenCount(): Int

    /**
     * 获取今天漏服的记录数
     */
    @Query("SELECT COUNT(*) FROM medicine_records WHERE date(takenTime) = date('now') AND isTaken = 0")
    suspend fun getTodayMissedCount(): Int

    /**
     * 删除指定日期之前的所有记录
     */
    @Query("DELETE FROM medicine_records WHERE takenTime < :cutoffDate")
    suspend fun deleteRecordsBeforeDate(cutoffDate: Date): Int
}
