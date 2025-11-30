package com.medicinereminder.data.mapper

import com.medicinereminder.data.entity.Medicine
import com.medicinereminder.domain.model.MedicineModel

/**
 * 药物映射器
 * 用于在药物实体和领域模型之间进行转换
 */
object MedicineMapper {

    /**
     * 将药物实体转换为领域模型
     */
    fun toModel(entity: Medicine): MedicineModel {
        return MedicineModel(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            dosage = entity.dosage,
            photoPath = entity.photoPath,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    /**
     * 将药物领域模型转换为实体
     */
    fun toEntity(model: MedicineModel): Medicine {
        return Medicine(
            id = model.id,
            name = model.name,
            description = model.description,
            dosage = model.dosage,
            photoPath = model.photoPath,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )
    }

    /**
     * 将药物实体列表转换为领域模型列表
     */
    fun toModelList(entities: List<Medicine>): List<MedicineModel> {
        return entities.map { toModel(it) }
    }

    /**
     * 将药物领域模型列表转换为实体列表
     */
    fun toEntityList(models: List<MedicineModel>): List<Medicine> {
        return models.map { toEntity(it) }
    }
}
