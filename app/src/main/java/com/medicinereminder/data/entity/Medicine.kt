package com.medicinereminder.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 药物实体类
 * @property id 药物ID
 * @property name 药物名称
 * @property description 药物描述
 * @property dosage 服用剂量
 * @property photoPath 药物照片路径
 * @property createdAt 创建时间
 * @property updatedAt 更新时间
 */
@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val dosage: String,
    val photoPath: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
