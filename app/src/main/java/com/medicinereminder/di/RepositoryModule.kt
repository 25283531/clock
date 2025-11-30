package com.medicinereminder.di

import com.medicinereminder.data.repository.MedicineRepository
import com.medicinereminder.data.repository.MedicineRecordRepository
import com.medicinereminder.data.repository.ReminderRepository
import com.medicinereminder.data.dao.MedicineDao
import com.medicinereminder.data.dao.MedicineRecordDao
import com.medicinereminder.data.dao.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 仓库模块
 * 用于配置仓库相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * 提供药物仓库实例
     * @param medicineDao 药物DAO实例
     * @return 药物仓库实例
     */
    @Provides
    @Singleton
    fun provideMedicineRepository(medicineDao: MedicineDao): MedicineRepository {
        return MedicineRepository(medicineDao)
    }

    /**
     * 提供提醒仓库实例
     * @param reminderDao 提醒DAO实例
     * @return 提醒仓库实例
     */
    @Provides
    @Singleton
    fun provideReminderRepository(reminderDao: ReminderDao): ReminderRepository {
        return ReminderRepository(reminderDao)
    }

    /**
     * 提供服药记录仓库实例
     * @param medicineRecordDao 服药记录DAO实例
     * @return 服药记录仓库实例
     */
    @Provides
    @Singleton
    fun provideMedicineRecordRepository(medicineRecordDao: MedicineRecordDao): MedicineRecordRepository {
        return MedicineRecordRepository(medicineRecordDao)
    }
}
