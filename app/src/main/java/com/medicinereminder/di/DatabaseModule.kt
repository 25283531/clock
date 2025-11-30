package com.medicinereminder.di

import android.content.Context
import com.medicinereminder.data.database.MedicineDatabase
import com.medicinereminder.data.dao.MedicineDao
import com.medicinereminder.data.dao.MedicineRecordDao
import com.medicinereminder.data.dao.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库模块
 * 用于配置数据库相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * 提供数据库实例
     * @param context 应用上下文
     * @return 数据库实例
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MedicineDatabase {
        return MedicineDatabase.getInstance(context)
    }

    /**
     * 提供药物DAO实例
     * @param database 数据库实例
     * @return 药物DAO实例
     */
    @Provides
    @Singleton
    fun provideMedicineDao(database: MedicineDatabase): MedicineDao {
        return database.medicineDao()
    }

    /**
     * 提供提醒DAO实例
     * @param database 数据库实例
     * @return 提醒DAO实例
     */
    @Provides
    @Singleton
    fun provideReminderDao(database: MedicineDatabase): ReminderDao {
        return database.reminderDao()
    }

    /**
     * 提供服药记录DAO实例
     * @param database 数据库实例
     * @return 服药记录DAO实例
     */
    @Provides
    @Singleton
    fun provideMedicineRecordDao(database: MedicineDatabase): MedicineRecordDao {
        return database.medicineRecordDao()
    }
}
