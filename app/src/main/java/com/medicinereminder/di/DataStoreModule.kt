package com.medicinereminder.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.medicinereminder.data.datastore.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DataStore模块
 * 用于配置DataStore相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    // 创建DataStore实例的扩展属性
    private val Context.dataStore by preferencesDataStore(name = "medicine_reminder_preferences")

    /**
     * 提供DataStore实例
     * @param context 上下文
     * @return DataStore<Preferences> 实例
     */
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    /**
     * 提供SettingsDataStore实例
     * @param dataStore DataStore实例
     * @return SettingsDataStore 实例
     */
    @Provides
    @Singleton
    fun provideSettingsDataStore(dataStore: DataStore<Preferences>): SettingsDataStore {
        return SettingsDataStore(dataStore)
    }
}
