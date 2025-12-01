package com.medicinereminder.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.medicinereminder.data.dao.MedicineDao
import com.medicinereminder.data.dao.MedicineRecordDao
import com.medicinereminder.data.dao.ReminderDao
import com.medicinereminder.data.entity.Medicine
import com.medicinereminder.data.entity.MedicineRecord
import com.medicinereminder.data.entity.Reminder
import com.medicinereminder.data.database.DateTypeConverters

/**
 * 药物提醒数据库
 * 版本号：1
 */
@Database(
    entities = [
        Medicine::class,
        Reminder::class,
        MedicineRecord::class
    ],
    version = 1,
    exportSchema = false,
    typeConverters = [DateTypeConverters::class]
)
abstract class MedicineDatabase : RoomDatabase() {

    /**
     * 获取药物DAO
     */
    abstract fun medicineDao(): MedicineDao

    /**
     * 获取提醒DAO
     */
    abstract fun reminderDao(): ReminderDao

    /**
     * 获取服药记录DAO
     */
    abstract fun medicineRecordDao(): MedicineRecordDao

    companion object {
        // 数据库名称
        private const val DATABASE_NAME = "medicine_reminder.db"

        // 单例实例
        @Volatile
        private var INSTANCE: MedicineDatabase? = null

        /**
         * 获取数据库实例
         */
        fun getInstance(context: Context): MedicineDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        /**
         * 构建数据库
         */
        private fun buildDatabase(context: Context): MedicineDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MedicineDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration() // 版本升级时销毁旧数据
                .build()
        }
    }
}
