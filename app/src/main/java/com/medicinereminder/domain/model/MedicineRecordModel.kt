package com.medicinereminder.domain.model

import java.util.Date

/**
 * 服药记录领域模型
 * 用于UI层展示和交互
 */
data class MedicineRecordModel(
    val id: Long = 0,
    val reminderId: Long,
    val medicineIds: List<Long>,
    val takenTime: Date,
    val isTaken: Boolean = true,
    val createdAt: Date = Date()
)
