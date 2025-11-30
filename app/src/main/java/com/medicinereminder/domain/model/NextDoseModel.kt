package com.medicinereminder.domain.model

import java.util.Date

/**
 * 下次服药信息模型
 * 用于桌面小组件和应用内显示下次服药信息
 */
data class NextDoseModel(
    /**
     * 下次服药时间
     */
    val nextDoseTime: Date,
    /**
     * 下次服药的药物列表
     */
    val medicines: List<MedicineModel>,
    /**
     * 关联的提醒ID
     */
    val reminderId: Long
)
