package com.medicinereminder.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 服药记录实体类
 * @property id 记录ID
 * @property reminderId 关联的提醒ID
 * @property medicineIds 本次服用的药物ID列表，以逗号分隔
 * @property takenTime 实际服药时间
 * @property isTaken 是否已服药
 * @property createdAt 创建时间
 */
@Entity(tableName = "medicine_records")
data class MedicineRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reminderId: Long,
    val medicineIds: String, // 格式："1,2,3"
    val takenTime: Date,
    val isTaken: Boolean = true,
    val createdAt: Date = Date()
)
