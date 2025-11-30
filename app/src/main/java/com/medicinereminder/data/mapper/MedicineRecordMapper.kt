package com.medicinereminder.data.mapper

import com.medicinereminder.data.entity.MedicineRecord
import com.medicinereminder.domain.model.MedicineRecordModel

/**
 * 服药记录映射器
 * 用于在服药记录实体和领域模型之间进行转换
 */
object MedicineRecordMapper {

    /**
     * 将服药记录实体转换为领域模型
     */
    fun toModel(entity: MedicineRecord): MedicineRecordModel {
        return MedicineRecordModel(
            id = entity.id,
            reminderId = entity.reminderId,
            medicineIds = entity.medicineIds.split(",").mapNotNull { it.toLongOrNull() },
            takenTime = entity.takenTime,
            isTaken = entity.isTaken,
            createdAt = entity.createdAt
        )
    }

    /**
     * 将服药记录领域模型转换为实体
     */
    fun toEntity(model: MedicineRecordModel): MedicineRecord {
        return MedicineRecord(
            id = model.id,
            reminderId = model.reminderId,
            medicineIds = model.medicineIds.joinToString(","),
            takenTime = model.takenTime,
            isTaken = model.isTaken,
            createdAt = model.createdAt
        )
    }

    /**
     * 将服药记录实体列表转换为领域模型列表
     */
    fun toModelList(entities: List<MedicineRecord>): List<MedicineRecordModel> {
        return entities.map { toModel(it) }
    }

    /**
     * 将服药记录领域模型列表转换为实体列表
     */
    fun toEntityList(models: List<MedicineRecordModel>): List<MedicineRecord> {
        return models.map { toEntity(it) }
    }
}
