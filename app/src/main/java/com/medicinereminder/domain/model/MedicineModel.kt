package com.medicinereminder.domain.model

import java.util.Date

/**
 * 药物领域模型
 * 用于UI层展示和交互
 */
data class MedicineModel(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val dosage: String,
    val photoPath: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
