package com.medicinereminder.manager

import android.content.Context
import android.net.Uri
import android.util.Log
import com.medicinereminder.data.entity.Medicine
import com.medicinereminder.data.entity.MedicineRecord
import com.medicinereminder.data.entity.Reminder
import com.medicinereminder.data.repository.MedicineRecordRepository
import com.medicinereminder.data.repository.MedicineRepository
import com.medicinereminder.data.repository.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*

/**
 * 数据导出导入管理器
 * 用于处理数据的导出和导入，支持JSON格式
 */
class DataExportImportManager(
    private val context: Context,
    private val medicineRepository: MedicineRepository,
    private val reminderRepository: ReminderRepository,
    private val medicineRecordRepository: MedicineRecordRepository
) {

    /**
     * 导出数据到文件
     * @param uri 文件URI
     * @return 导出是否成功
     */
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun exportData(uri: Uri): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // 获取所有数据
                val medicines = medicineRepository.getAllMedicines()
                val reminders = reminderRepository.getAllReminders()
                val records = medicineRecordRepository.getAllRecords()

                // 创建导出数据模型
                val exportData = ExportData(
                    version = "1.0",
                    medicines = medicines,
                    reminders = reminders,
                    records = records
                )

                // 序列化数据
                val json = Json {
                    prettyPrint = true
                    encodeDefaults = true
                }
                val jsonString = json.encodeToString(exportData)

                // 写入文件
                context.contentResolver.openOutputStream(uri)?.use {\ outputStream ->
                    OutputStreamWriter(outputStream).use {\ writer ->
                        writer.write(jsonString)
                    }
                }

                Log.d("DataExportImportManager", "数据导出成功")
                return@withContext true
            } catch (e: Exception) {
                Log.e("DataExportImportManager", "数据导出失败: ${e.message}", e)
                return@withContext false
            }
        }
    }

    /**
     * 从文件导入数据
     * @param uri 文件URI
     * @param overwrite 是否覆盖现有数据
     * @return 导入是否成功
     */
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun importData(uri: Uri, overwrite: Boolean = false): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // 读取文件内容
                val jsonString = context.contentResolver.openInputStream(uri)?.use {\ inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        reader.readText()
                    }
                } ?: return@withContext false

                // 反序列化数据
                val json = Json {
                    ignoreUnknownKeys = true
                }
                val importData = json.decodeFromString<ExportData>(jsonString)

                // 验证数据版本
                if (importData.version != "1.0") {
                    Log.e("DataExportImportManager", "不支持的数据版本: ${importData.version}")
                    return@withContext false
                }

                // 根据overwrite参数决定是否清除现有数据
                if (overwrite) {
                    // 清除现有数据
                    medicineRecordRepository.clearAllRecords()
                    reminderRepository.deleteAllReminders()
                    medicineRepository.deleteAllMedicines()
                }

                // 导入数据
                // 先导入药物，因为提醒依赖药物
                importData.medicines.forEach { medicine ->
                    medicineRepository.insertMedicine(medicine)
                }

                // 导入提醒
                importData.reminders.forEach { reminder ->
                    reminderRepository.insertReminder(reminder)
                }

                // 导入服药记录
                importData.records.forEach { record ->
                    medicineRecordRepository.insertRecord(record)
                }

                Log.d("DataExportImportManager", "数据导入成功")
                return@withContext true
            } catch (e: Exception) {
                Log.e("DataExportImportManager", "数据导入失败: ${e.message}", e)
                return@withContext false
            }
        }
    }

    /**
     * 导出数据模型
     */
    @Serializable
    data class ExportData(
        val version: String,
        val medicines: List<Medicine>,
        val reminders: List<Reminder>,
        val records: List<MedicineRecord>
    )
}
