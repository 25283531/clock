package com.medicinereminder.data.repository

import com.medicinereminder.data.dao.MedicineDao
import com.medicinereminder.data.entity.Medicine
import kotlinx.coroutines.flow.Flow

/**
 * 药物数据仓库
 * 封装药物相关的数据库操作，提供统一的数据访问接口
 */
class MedicineRepository(private val medicineDao: MedicineDao) {

    /**
     * 获取所有药物，按创建时间倒序排列
     */
    fun getAllMedicines(): Flow<List<Medicine>> {
        return medicineDao.getAllMedicines()
    }

    /**
     * 根据ID获取药物
     */
    suspend fun getMedicineById(id: Long): Medicine? {
        return medicineDao.getMedicineById(id)
    }

    /**
     * 根据ID列表获取药物
     */
    suspend fun getMedicinesByIds(ids: List<Long>): List<Medicine> {
        return medicineDao.getMedicinesByIds(ids)
    }

    /**
     * 插入药物
     */
    suspend fun insertMedicine(medicine: Medicine): Long {
        return medicineDao.insertMedicine(medicine)
    }

    /**
     * 更新药物
     */
    suspend fun updateMedicine(medicine: Medicine) {
        medicineDao.updateMedicine(medicine)
    }

    /**
     * 删除药物
     */
    suspend fun deleteMedicine(medicine: Medicine) {
        medicineDao.deleteMedicine(medicine)
    }

    /**
     * 根据ID删除药物
     */
    suspend fun deleteMedicineById(id: Long) {
        medicineDao.deleteMedicineById(id)
    }

    /**
     * 获取药物总数
     */
    suspend fun getMedicineCount(): Int {
        return medicineDao.getMedicineCount()
    }
}
