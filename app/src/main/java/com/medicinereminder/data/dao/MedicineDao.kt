package com.medicinereminder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.medicinereminder.data.entity.Medicine
import kotlinx.coroutines.flow.Flow

/**
 * 药物数据访问对象
 */
@Dao
interface MedicineDao {
    /**
     * 获取所有药物，按创建时间倒序排列
     */
    @Query("SELECT * FROM medicines ORDER BY createdAt DESC")
    fun getAllMedicines(): Flow<List<Medicine>>

    /**
     * 根据ID获取药物
     */
    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicineById(id: Long): Medicine?

    /**
     * 根据ID列表获取药物
     */
    @Query("SELECT * FROM medicines WHERE id IN (:ids)")
    suspend fun getMedicinesByIds(ids: List<Long>): List<Medicine>

    /**
     * 插入药物
     */
    @Insert
    suspend fun insertMedicine(medicine: Medicine): Long

    /**
     * 更新药物
     */
    @Update
    suspend fun updateMedicine(medicine: Medicine)

    /**
     * 删除药物
     */
    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    /**
     * 根据ID删除药物
     */
    @Query("DELETE FROM medicines WHERE id = :id")
    suspend fun deleteMedicineById(id: Long)

    /**
     * 获取药物总数
     */
    @Query("SELECT COUNT(*) FROM medicines")
    suspend fun getMedicineCount(): Int
}
